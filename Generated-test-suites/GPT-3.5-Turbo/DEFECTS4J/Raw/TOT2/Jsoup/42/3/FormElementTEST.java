import org.jsoup.nodes.*;
import org.jsoup.Connection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertNotNull(formElement);
        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);

        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john_doe");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertTrue(formData.get(0).key().equals("username"));
        assertTrue(formData.get(0).value().equals("john_doe"));
    }
}