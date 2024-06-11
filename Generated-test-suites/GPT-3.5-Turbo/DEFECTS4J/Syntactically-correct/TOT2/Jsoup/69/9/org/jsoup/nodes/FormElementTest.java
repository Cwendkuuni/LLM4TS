import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("input");
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Element element = new Element(tag, baseUri, attributes);

        Tag formTag = Tag.valueOf("form");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_noActionUrl() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        formElement.submit();
    }

    @Test
    public void testSubmit_withActionUrl() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("action", "http://submit.com/");
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }
}