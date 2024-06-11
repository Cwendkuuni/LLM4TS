import org.jsoup.nodes.*;
import org.jsoup.Connection;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testConstructor() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        
        assertEquals(tag, formElement.tag());
        assertEquals("http://example.com", formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }
    
    @Test
    public void testElementsMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        
        assertNotNull(formElement.elements());
        assertEquals(0, formElement.elements().size());
    }
    
    @Test
    public void testAddElementMethod() {
        Tag tag = Tag.valueOf("input");
        Element element = new Element(tag, "http://example.com", new Attributes());
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        assertNotNull(formElement.addElement(element));
        assertEquals(1, formElement.elements().size());
    }
    
    @Test
    public void testSubmitMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Connection con = formElement.submit();
        
        assertNotNull(con);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodException() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        formElement.submit();
    }
    
    @Test
    public void testFormDataMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        
        assertNotNull(formElement.formData());
        assertEquals(0, formElement.formData().size());
    }

    // Add more test cases for edge cases and other methods if needed

}