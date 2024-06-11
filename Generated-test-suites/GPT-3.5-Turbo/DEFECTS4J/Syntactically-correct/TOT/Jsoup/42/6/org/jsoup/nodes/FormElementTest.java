import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;
    private Element element;
    private Connection connection;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "https://example.com/submit");
        attributes.put("method", "POST");
        formElement = new FormElement(tag, "https://example.com", attributes);

        Tag inputTag = Tag.valueOf("input");
        Attributes inputAttributes = new Attributes();
        inputAttributes.put("name", "username");
        element = new Element(inputTag, "https://example.com", inputAttributes);

        formElement.addElement(element);

        connection = Jsoup.connect("https://example.com/submit");
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testAddElement() {
        FormElement updatedFormElement = formElement.addElement(element);
        Elements elements = updatedFormElement.elements();
        assertNotNull(elements);
        assertEquals(2, elements.size());
        assertEquals(element, elements.get(1));
    }

    @Test
    public void testSubmit() {
        Connection formConnection = formElement.submit();
        assertNotNull(formConnection);
        assertEquals(connection.request().url().toString(), formConnection.request().url().toString());
        assertEquals(connection.request().method(), formConnection.request().method());
        assertEquals(connection.request().data(), formConnection.request().data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionUrl() {
        FormElement invalidFormElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        invalidFormElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertNull(keyVal.value());
    }

    @Test
    public void testEquals() {
        assertTrue(formElement.equals(formElement));
        assertFalse(formElement.equals(null));
        assertFalse(formElement.equals("form"));
    }
}