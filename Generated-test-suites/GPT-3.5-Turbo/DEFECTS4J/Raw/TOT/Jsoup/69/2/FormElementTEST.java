import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        // Typical use case: There are elements associated with the form
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();

        assertEquals(1, elements.size());
        assertTrue(elements.contains(inputElement));

        // Edge case: There are no elements associated with the form
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        elements = formElement.elements();

        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        // Typical use case: Adding an element to the form
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();
        
        assertTrue(elements.contains(inputElement));

        // Error scenario: Adding a null element to the form
        inputElement = null;
        formElement.addElement(inputElement);

        elements = formElement.elements();
        
        assertFalse(elements.contains(inputElement));
    }

    @Test
    public void testSubmit() {
        // Typical use case: Form has an absolute action URL and method is POST
        formElement.attr("action", "http://example.com");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());

        // Typical use case: Form has no action attribute and method is GET
        formElement.removeAttr("action");
        formElement.attr("method", "GET");

        connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());

        // Error scenario: Form has no absolute action URL and method is neither GET nor POST
        formElement.removeAttr("action");
        formElement.removeAttr("method");

        boolean exceptionThrown = false;
        try {
            connection = formElement.submit();
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    @Test
    public void testFormData() {
        // Typical use case: Form has multiple form control elements and different types
        Element inputElement1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement1.attr("name", "username");
        inputElement1.attr("value", "john");

        Element inputElement2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement2.attr("name", "age");
        inputElement2.attr("value", "30");

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("value", "male");
        option1.attr("selected", "");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.attr("value", "female");
        
        selectElement.appendChild(option1);
        selectElement.appendChild(option2);

        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(3, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
        assertEquals("age", formData.get(1).key());
        assertEquals("30", formData.get(1).value());
        assertEquals("select", formData.get(2).key());
        assertEquals("male", formData.get(2).value());

        // Edge case: Form has no form control elements
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        formData = formElement.formData();

        assertEquals(0, formData.size());
    }
}