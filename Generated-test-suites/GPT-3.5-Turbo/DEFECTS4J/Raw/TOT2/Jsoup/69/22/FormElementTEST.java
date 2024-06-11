import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("input");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Element element = new Element(tag, "http://example.com", new Attributes());
        FormElement returnedElement = formElement.addElement(element);
        assertNotNull(returnedElement);
    }

    @Test
    public void testSubmit() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        assertThrows(IllegalArgumentException.class, formElement::submit);
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        assertFalse(formElement.formData().isEmpty());
    }
}