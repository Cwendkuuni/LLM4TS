import org.jsoup.Connection;
import org.jsoup.parser.Tag;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static org.junit.Assert.assertEquals;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());

        formElement.addElement(element);
        Elements elements = formElement.elements();

        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmitWithValidActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();

        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    // Additional test cases for formData() method can be added here

}