import org.jsoup.nodes.FormElement;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        FormElement newFormElement = formElement.addElement(new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes()));
        Elements elements = formElement.elements();
        assertTrue(elements.contains(newFormElement));
    }

    @Test
    public void testRemoveChild() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Element childElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        formElement.addElement(childElement);
        formElement.removeChild(childElement);
        Elements elements = formElement.elements();
        assertFalse(elements.contains(childElement));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }
}