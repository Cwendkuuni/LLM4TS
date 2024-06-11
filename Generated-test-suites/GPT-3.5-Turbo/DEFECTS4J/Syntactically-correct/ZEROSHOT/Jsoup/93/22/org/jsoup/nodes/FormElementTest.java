import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

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
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertSame(element, elements.get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertSame(element, elements.get(0));
        formElement.removeChild(element);
        elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmit() {
        // TODO: Implement test for submit() method
    }

    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri());
        element1.attr("name", "name1");
        element1.val("value1");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), formElement.baseUri());
        element2.attr("name", "name2");
        element2.attr("type", "checkbox");
        element2.attr("checked", "");
        element2.val("value2");
        formElement.addElement(element2);

        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(2, data.size());
        assertEquals("name1", data.get(0).key());
        assertEquals("value1", data.get(0).value());
        assertEquals("name2", data.get(1).key());
        assertEquals("value2", data.get(1).value());
    }
}