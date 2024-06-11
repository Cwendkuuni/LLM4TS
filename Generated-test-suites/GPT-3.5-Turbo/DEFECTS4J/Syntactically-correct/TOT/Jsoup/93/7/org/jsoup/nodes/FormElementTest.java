import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.nodes.Attribute;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

    @Mock
    private Element mockElement;

    private Tag tag = Tag.valueOf("form");
    private String baseUri = "http://example.com";
    private Attributes attributes = new Attributes();

    @Test
    public void testConstructor() {
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Elements elements = formElement.elements();
        
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        formElement.addElement(mockElement);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(mockElement));
    }

    @Test
    public void testRemoveChild() {
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        formElement.addElement(mockElement);
        formElement.removeChild(mockElement);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmitWithAction() {
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        formElement.attr("action", "/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals((baseUri + "/submit"), connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        formElement.submit();
    }

    @Test
    public void testFormData() {
        // Mocking Elements for form control elements
        Element element1 = mock(Element.class);
        when(element1.hasAttr("disabled")).thenReturn(false);
        when(element1.attr("name")).thenReturn("username");
        when(element1.attr("type")).thenReturn("text");
        when(element1.tag()).thenReturn(Tag.valueOf("input"));
        when(element1.val()).thenReturn("John Doe");

        Element element2 = mock(Element.class);
        when(element2.hasAttr("disabled")).thenReturn(true);

        Element element3 = mock(Element.class);
        when(element3.attr("name")).thenReturn("");
        when(element3.tag()).thenReturn(Tag.valueOf("input"));

        Elements elements = new Elements();
        elements.add(element1);
        elements.add(element2);
        elements.add(element3);

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("John Doe", keyVal.value());
    }
}