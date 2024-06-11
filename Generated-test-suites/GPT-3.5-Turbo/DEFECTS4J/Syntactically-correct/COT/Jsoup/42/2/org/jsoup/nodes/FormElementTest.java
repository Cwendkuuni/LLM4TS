
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        // Verify that the elements method returns the same elements object stored in the FormElement class
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertEquals(formElement.elements(), elements);
    }

    @Test
    public void testAddElement() {
        // Verify that the addElement method adds the provided element to the elements object in the FormElement class
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        assertTrue(formElement.elements().contains(element));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        // Verify that an IllegalArgumentException is thrown when calling submit method without setting the form action
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmitPost() {
        // Verify that the submit method returns a Connection object with the POST request set up
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitGet() {
        // Verify that the submit method returns a Connection object with the GET request set up
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "GET");
        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        // Verify that the formData method returns a list of key-value pairs representing the form data to be submitted
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes().put("name", "username").put("value", "john")));
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes().put("name", "password").put("value", "pass123")));
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
        assertEquals("password", formData.get(1).key());
        assertEquals("pass123", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        // Verify that the equals method compares the FormElement object to an equal object and returns true
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example1.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example1.com", new Attributes());
        assertTrue(formElement1.equals(formElement2));
    }
}

