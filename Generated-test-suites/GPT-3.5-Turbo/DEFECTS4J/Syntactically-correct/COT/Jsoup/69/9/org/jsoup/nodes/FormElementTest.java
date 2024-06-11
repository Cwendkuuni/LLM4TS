import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        // Create a FormElement
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Add some elements to the form
        formElement.addElement(new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes()));
        formElement.addElement(new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes()));

        // Get the elements from the form
        Elements elements = formElement.elements();

        // Assert that the elements are not null
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        // Create a FormElement
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Create an element to add
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());

        // Add the element to the form
        formElement.addElement(element);

        // Assert that the element is added successfully
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        // Create a FormElement
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Set the method attribute
        formElement.attr("method", "POST");

        // Submit the form
        Connection connection = formElement.submit();

        // Assert that the connection is not null
        assertNotNull(connection);
        // Assert that the method is set correctly
        assertEquals(Connection.Method.POST, connection.method());
        // Assert that the URL is set correctly
        assertEquals("http://example.com", connection.url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() {
        // Create a FormElement
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Set the method attribute
        formElement.attr("method", "POST");

        // Submit the form
        formElement.submit();
    }

    @Test
    public void testFormData() {
        // Create a FormElement
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Add some elements to the form
        formElement.addElement(new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("name", "username").put("value", "test")));
        formElement.addElement(new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes().put("name", "country")));
        formElement.addElement(new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("name", "subscribe").put("type", "checkbox").put("checked", "")));

        // Get the form data
        List<Connection.KeyVal> formData = formElement.formData();

        // Assert that the form data is not empty
        assertFalse(formData.isEmpty());
    }
}