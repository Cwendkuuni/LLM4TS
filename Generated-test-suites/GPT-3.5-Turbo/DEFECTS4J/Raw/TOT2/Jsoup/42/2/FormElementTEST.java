import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    
    private FormElement formElement;
    
    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }
    
    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
        assertEquals(0, formElement.elements().size());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        // Test for valid action URL
        Connection connection = formElement.submit();
        assertNotNull(connection);
        
        // Test for invalid action URL
        formElement.removeAttr("action");
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFormData() {
        // Test for form data generation
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("testuser");
        formElement.addElement(inputElement);
        
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("value", "1");
        optionElement.attr("selected", "selected");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testuser", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("1", formData.get(1).value());
        
        // Test for form data with checkbox
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "subscription");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("value", "true");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);
        
        formData = formElement.formData();
        assertEquals(3, formData.size());
        assertEquals("subscription", formData.get(2).key());
        assertEquals("true", formData.get(2).value());
    }
}