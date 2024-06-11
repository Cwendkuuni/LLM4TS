import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = formElement.addElement(element);
        
        assertTrue(result.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        // Test when action attribute is not set
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            formElement.submit();
        });
        assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", exception.getMessage());
        
        // Test with valid action attribute
        formElement.attr("action", "http://submit-form.com");
        assertNotNull(formElement.submit());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        // Add form control elements for testing
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "john_doe");
        formElement.addElement(inputElement);
        
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("value", "1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john_doe", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("1", formData.get(1).value());
    }
}