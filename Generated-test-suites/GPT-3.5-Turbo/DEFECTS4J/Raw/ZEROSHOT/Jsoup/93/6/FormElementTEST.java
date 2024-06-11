import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        assertEquals(formElement, formElement.addElement(element));
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        formElement.addElement(element);
        formElement.removeChild(element);
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmit() {
        connection = formElement.submit();
        assertNotNull(connection);
        // TODO: Add more assertions for Connection object
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }
}