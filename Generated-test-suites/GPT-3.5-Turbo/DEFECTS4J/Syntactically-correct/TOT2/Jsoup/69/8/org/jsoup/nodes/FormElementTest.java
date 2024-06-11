import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.helper.HttpConnection;
import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
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
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertTrue(elements.contains(element));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionUrl() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input.attr("name", "username");
        input.val("testuser");

        formElement.addElement(input);
        List<Connection.KeyVal> formData = formElement.formData();
        assertFalse(formData.isEmpty());
        assertEquals("username=testuser", formData.get(0).toString());
    }
}