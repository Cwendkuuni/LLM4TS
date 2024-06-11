
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement formElement;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        assertTrue(formElement.elements().isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        assertTrue(formElement.elements().contains(element));
    }
    
    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        
        formElement.removeChild(element);
        assertFalse(formElement.elements().contains(element));
    }
    
    @Test
    public void testSubmitWithAction() {
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "name");
        inputElement.attr("value", "John");
        formElement.addElement(inputElement);
        
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("selected", "selected");
        optionElement.attr("value", "1");
        selectElement.addElement(optionElement);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(2, data.size());
        assertEquals("name", data.get(0).key());
        assertEquals("John", data.get(0).value());
        assertEquals("name", data.get(1).key());
        assertEquals("1", data.get(1).value());
    }
}
