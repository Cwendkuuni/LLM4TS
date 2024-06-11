import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Mockito.mock(Tag.class);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = Mockito.mock(Element.class);
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = Mockito.mock(Element.class);
        formElement.addElement(element);
        formElement.removeChild(element);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
        assertFalse(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = Mockito.mock(Connection.class);
        Connection.Response response = Mockito.mock(Connection.Response.class);
        when(connection.response()).thenReturn(response);
        when(response.statusCode()).thenReturn(200);

        String action = "http://example.com/submit";
        formElement.attr("action", action);
        formElement.attr("method", "POST");

        List<Connection.KeyVal> formData = new ArrayList<>();
        when(formElement.formData()).thenReturn(formData);

        Jsoup jsoup = Mockito.mock(Jsoup.class);
        when(Jsoup.connect(action)).thenReturn(connection);
        Validate.notEmpty(action, "Could not determine a form action URL for submit. Ensure you set a base URI when parsing.");

        Connection.Method method = Connection.Method.POST;
        when(connection.method()).thenReturn(method);
        when(connection.data(formData)).thenReturn(connection);

        Connection submitConnection = formElement.submit();
        assertNotNull(submitConnection);
        assertEquals(connection, submitConnection);
        assertEquals(method, submitConnection.method());
    }

    @Test
    public void testFormData() {
        Element element = Mockito.mock(Element.class);
        when(element.tag()).thenReturn(Tag.valueOf("input"));
        when(element.attr(anyString())).thenReturn("");
        when(element.hasAttr(anyString())).thenReturn(false);
        when(element.attr("name")).thenReturn("inputName");
        when(element.val()).thenReturn("inputValue");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertNotNull(keyVal);
        assertEquals("inputName", keyVal.key());
        assertEquals("inputValue", keyVal.value());
    }
}