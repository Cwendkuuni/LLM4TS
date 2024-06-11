import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        // Test adding elements to form element and checking if elements are returned correctly
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);

        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().get(0));
    }

    @Test
    public void testSubmit() {
        // Test submitting a form element and checking if Connection object is created
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();

        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        // Test getting form data from form element and checking if KeyVal list is returned
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "inputName").put("value", "inputValue"));
        formElement.addElement(inputElement);

        assertEquals(1, formElement.formData().size());
        assertEquals("inputName", formElement.formData().get(0).key());
        assertEquals("inputValue", formElement.formData().get(0).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionUrl() {
        // Test submitting a form element without action URL and expect an IllegalArgumentException
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testEquals() {
        // Test equals method of FormElement class
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        assertTrue(formElement1.equals(formElement2));
    }
}