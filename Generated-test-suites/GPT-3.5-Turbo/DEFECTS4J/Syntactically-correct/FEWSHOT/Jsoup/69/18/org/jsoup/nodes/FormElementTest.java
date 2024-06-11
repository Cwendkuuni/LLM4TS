import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());

        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        assertEquals(1, elements.size());
        assertEquals(inputElement, elements.first());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(inputElement, elements.first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), null, new Attributes());
        formElement.addFormElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));
        formElement.submit();
    }

    @Test
    public void testSubmitGetMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "get");
        formElement.addFormElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testSubmitPostMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "post");
        formElement.addFormElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.addFormElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        // Add more assertions here to test the content of the returned data list.
    }
}