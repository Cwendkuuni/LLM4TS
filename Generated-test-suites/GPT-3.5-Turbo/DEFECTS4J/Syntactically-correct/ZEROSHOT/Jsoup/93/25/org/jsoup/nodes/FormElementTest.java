import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {
    
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Elements elements = formElement.elements();
        
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        
        FormElement updatedFormElement = formElement.addElement(element);
        
        assertNotNull(updatedFormElement);
        assertEquals(formElement, updatedFormElement);
        assertEquals(1, updatedFormElement.elements().size());
        assertEquals(element, updatedFormElement.elements().get(0));
    }
    
    @Test
    public void testRemoveChild() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        
        formElement.removeChild(element);
        
        assertTrue(formElement.elements().isEmpty());
    }
    
    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Connection connection = formElement.submit();
        
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john");
        formElement.addElement(inputElement);
        
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "city");
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("selected", "");
        optionElement.val("New York");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
        assertEquals("city", formData.get(1).key());
        assertEquals("New York", formData.get(1).value());
    }
}