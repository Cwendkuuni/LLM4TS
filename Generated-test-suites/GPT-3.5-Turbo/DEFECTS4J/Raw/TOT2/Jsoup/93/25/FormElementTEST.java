import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        assertNotNull(formElement);
    }

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());

        FormElement returnedFormElement = formElement.addElement(element);

        assertTrue(returnedFormElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        assertThrows(IllegalArgumentException.class, () -> {
            formElement.submit();
        });
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john_doe");
        formElement.addElement(inputElement);

        assertEquals(1, formElement.formData().size());
    }
}