import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.Connection;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testAddElement() {
        // Creating FormElement instance
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Adding form control element
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);

        // Asserting that element is added successfully
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
        assertEquals(formElement, result);
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Testing for IllegalArgumentException when action URL is empty
        try {
            Connection con = formElement.submit();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected Exception
        }
    }

    @Test
    public void testFormData() {
        // Create FormElement instance
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Add select input to form
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("selected", "selected");
        optionElement.val("Value");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        // Add checkbox input to form
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        checkboxElement.attr("name", "checkbox");
        checkboxElement.val("Checkbox Value");
        formElement.addElement(checkboxElement);

        // Add text input to form
        Element textElement = new Element(Tag.valueOf("input"), "", new Attributes());
        textElement.attr("type", "text");
        textElement.attr("name", "text");
        textElement.val("Text Value");
        formElement.addElement(textElement);

        // Test formData method
        List<Connection.KeyVal> formData = formElement.formData();

        // Asserting that correct key-value pairs are returned
        assertEquals(3, formData.size());
        assertEquals("Value", formData.get(0).value());
        assertEquals("Checkbox Value", formData.get(1).value());
        assertEquals("Text Value", formData.get(2).value());
    }
}