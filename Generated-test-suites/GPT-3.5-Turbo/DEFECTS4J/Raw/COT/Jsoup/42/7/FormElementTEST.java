
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testElements() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Elements elements = formElement.elements();

        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = new Element(Tag.valueOf("input"), baseUri);
        FormElement result = formElement.addElement(element);

        assertSame(formElement, result);
        assertTrue(formElement.elements().contains(element));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitEmptyActionUrl() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        formElement.submit();
    }

    @Test
    public void testSubmit() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = new Element(Tag.valueOf("input"), baseUri);
        element.attr("name", "username");
        element.val("testuser");
        formElement.addElement(element);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("username=testuser", connection.request().requestBody());
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = new Element(Tag.valueOf("input"), baseUri);
        element.attr("name", "username");
        element.val("testuser");
        formElement.addElement(element);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("username=testuser", data.get(0).toString());
    }

    @Test
    public void testEquals() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement1 = new FormElement(tag, baseUri, attributes);
        FormElement formElement2 = new FormElement(tag, baseUri, attributes);

        boolean result = formElement1.equals(formElement2);

        assertTrue(result);
    }
}
