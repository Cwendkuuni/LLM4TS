import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Mock
    private Element mockElement;

    private FormElement formElement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        formElement.addElement(mockElement);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(mockElement));
    }

    @Test
    public void testRemoveChild() {
        formElement.addElement(mockElement);
        formElement.removeChild(mockElement);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
        assertFalse(elements.contains(mockElement));
    }

    @Test
    public void testSubmitWithActionAttribute() {
        formElement.attr("action", "/submit-url");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com/submit-url", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionAttribute() {
        formElement.submit();
    }

    @Test
    public void testSubmitWithPostMethod() {
        formElement.attr("action", "/submit-url");
        formElement.attr("method", "post");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitWithGetMethod() {
        formElement.attr("action", "/submit-url");
        formElement.attr("method", "get");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormDataWithSelectElement() {
        Element selectElement = mock(Element.class);
        when(selectElement.tag()).thenReturn(Tag.valueOf("select"));
        when(selectElement.normalName()).thenReturn("select");
        when(selectElement.attr("name")).thenReturn("selectField");
        when(selectElement.select("option[selected]")).thenReturn(new Elements());
        when(selectElement.select("option")).thenReturn(new Elements());
        formElement.addElement(selectElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }

    @Test
    public void testFormDataWithCheckboxElement() {
        Element checkboxElement = mock(Element.class);
        when(checkboxElement.tag()).thenReturn(Tag.valueOf("input"));
        when(checkboxElement.normalName()).thenReturn("input");
        when(checkboxElement.attr("type")).thenReturn("checkbox");
        when(checkboxElement.attr("name")).thenReturn("checkboxField");
        when(checkboxElement.hasAttr("checked")).thenReturn(true);
        when(checkboxElement.val()).thenReturn("checkboxValue");
        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }

    @Test
    public void testFormDataWithRadioElement() {
        Element radioElement = mock(Element.class);
        when(radioElement.tag()).thenReturn(Tag.valueOf("input"));
        when(radioElement.normalName()).thenReturn("input");
        when(radioElement.attr("type")).thenReturn("radio");
        when(radioElement.attr("name")).thenReturn("radioField");
        when(radioElement.hasAttr("checked")).thenReturn(true);
        when(radioElement.val()).thenReturn("radioValue");
        formElement.addElement(radioElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }

    @Test
    public void testFormDataWithOtherElement() {
        Element otherElement = mock(Element.class);
        when(otherElement.tag()).thenReturn(Tag.valueOf("input"));
        when(otherElement.normalName()).thenReturn("input");
        when(otherElement.attr("type")).thenReturn("other");
        when(otherElement.attr("name")).thenReturn("otherField");
        when(otherElement.val()).thenReturn("otherValue");
        formElement.addElement(otherElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }
}