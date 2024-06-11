
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes()); // Create a new FormElement object
        Elements elements = formElement.elements(); // Get the elements from the form
        assertNotNull(elements); // Assert that the elements is not null
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes()); // Create a new FormElement object
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes()); // Create a new Element object
        formElement.addElement(element); // Add the element to the form
        Elements elements = formElement.elements(); // Get the elements from the form
        assertTrue(elements.contains(element)); // Assert that the elements contains the added element
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes()); // Create a new FormElement object
        Connection connection = formElement.submit(); // Submit the form and get the connection
        assertNotNull(connection); // Assert that the connection is not null
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes()); // Create a new FormElement object
        Connection connection = formElement.submit(); // Submit the form and get the connection
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes()); // Create a new FormElement object
        List<Connection.KeyVal> formData = formElement.formData(); // Get the form data
        assertNotNull(formData); // Assert that the form data is not null
    }

    @Test
    public void testEquals() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes()); // Create a new FormElement object
        boolean equals = formElement.equals(formElement); // Compare the form element to itself
        assertTrue(equals); // Assert that the form element is equal to itself
    }
}
