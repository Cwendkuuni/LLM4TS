import org.jsoup.Connection;
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
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("input");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Element element = new Element(tag, "http://example.com", new Attributes());

        FormElement updatedFormElement = formElement.addElement(element);

        assertNotNull(updatedFormElement.elements());
        assertEquals(1, updatedFormElement.elements().size());
    }

    @Test
    public void testSubmit() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());

        Connection connection = formElement.submit();

        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());

        assertNotNull(formElement.formData());
    }
}