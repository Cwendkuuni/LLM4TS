import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
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
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        assertEquals(1, updatedFormElement.elements().size());
        assertEquals(element, updatedFormElement.elements().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.submit();
    }

    @Test
    public void testSubmitWithPostMethod() {
        formElement.attr("method", "POST");
        formElement.attr("action", "http://example.com");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmitWithGetMethod() {
        formElement.attr("method", "GET");
        formElement.attr("action", "http://example.com");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("john", keyVal.value());
    }
}