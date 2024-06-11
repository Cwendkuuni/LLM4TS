import org.jsoup.Connection;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element testElement = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(testElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(testElement, elements.get(0));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john_doe");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john_doe", formData.get(0).value());
    }
}