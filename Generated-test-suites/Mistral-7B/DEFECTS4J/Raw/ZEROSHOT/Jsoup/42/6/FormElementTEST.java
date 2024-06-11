import org.jsoup.helper.ElementUtils;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormElementTest {

    private static final String TAG_NAME = "form";
    private static final String BASE_URI = "https://www.example.com";
    private static final String ATTRIBUTE_ACTION = "action";
    private static final String ATTRIBUTE_METHOD = "method";

    private FormElement formElement;
    private Element formControlElement1;
    private Element formControlElement2;
    private Connection connection;
    private Attributes attributes;

    @Before
    public void setup() {
        formControlElement1 = mock(Element.class);
        formControlElement2 = mock(Element.class);
        attributes = mock(Attributes.class);
        when(formControlElement1.tagName()).thenReturn(new Tag("input"));
        when(formControlElement1.tag()).thenReturn(new Tag("input"));
        when(formControlElement1.attr("name")).thenReturn("name1");
        when(formControlElement1.attr("type")).thenReturn("text");
        when(formControlElement1.val()).thenReturn("value1");
        when(formControlElement2.tagName()).thenReturn(new Tag("select"));
        when(formControlElement2.tag()).thenReturn(new Tag("select"));
        when(formControlElement2.attr("name")).thenReturn("name2");
        when(formControlElement2.attr("type")).thenReturn("select-one");
        when(formControlElement2.val()).thenReturn("value3");
        when(formControlElement2.select("option[selected]")).thenReturn(Arrays.asList(formControlElement2).subList(0, 1));

        formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, attributes);
        formElement.addElement(formControlElement1);
        formElement.addElement(formControlElement2);
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testFormElementCreation() {
        assertNotNull(formElement);
        assertSame(TAG_NAME, formElement.tagName());
        assertEquals(BASE_URI, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());

        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testFormElementAddElement() {
        formElement.addElement(formControlElement1);
        formElement.addElement(formControlElement2);

        assertEquals(2, formElement.elements().size());
        assertSame(formControlElement1, formElement.elements().get(0));
        assertSame(formControlElement2, formElement.elements().get(1));
    }

    @Test
    public void testFormElementGetListOfFormControlElements() {
        List<Element> expectedList = Arrays.asList(formControlElement1, formControlElement2);

        List<Element> actualList = formElement.elements();

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFormElementSubmit() {
        when(formElement.hasAttr("action")).thenReturn(true);
        when(formElement.absUrl("action")).thenReturn("https://www.example.com/submit");
        when(formElement.attr("method")).thenReturn("post");

        Connection actualConnection = formElement.submit();

        assertEquals(Connection.Method.POST, actualConnection.method());
        assertEquals("https://www.example.com/submit", actualConnection.url());

        String[] expectedFormData = new String[]{"name1=value1", "name2=value3"};
        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertFalse(actualFormData.isEmpty());
        assertEquals(expectedFormData.length, actualFormData.size());

        for (int i = 0; i < expectedFormData.length; i++) {
            String expectedKeyVal = expectedFormData[i];
            String[] keyVal = actualFormData.get(i).toString().split("=");

            assertEquals(keyVal[0], expectedKeyVal.split("=")[0]);
            assertEquals(keyVal[1], expectedKeyVal.split("=")[1]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWhenFormActionCannotBeDetermined() {
        when(formElement.hasAttr("action")).thenReturn(false);

        formElement.submit();
    }

    @Test
    public void testFormElementFormDataEmptyForm() {
        FormElement emptyFormElement = new FormElement(new Tag(TAG_NAME), BASE_URI, attributes);

        List<Connection.KeyVal> actualFormData = emptyFormElement.formData();

        assertTrue(actualFormData.isEmpty());
    }
}