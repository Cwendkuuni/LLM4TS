
package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        
        Elements elements = formElement.elements();
        
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        
        Element element = new Element(Tag.valueOf("input"), "");
        FormElement updatedForm = formElement.addElement(element);
        
        assertEquals(1, updatedForm.elements().size());
        assertTrue(updatedForm.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        
        formElement.attr("action", "http://example.com");
        formElement.attr("method", "POST");
        
        Connection connection = formElement.submit();
        
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionURL() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        
        Element element1 = new Element(Tag.valueOf("input"), "");
        element1.attr("name", "name");
        element1.val("John");
        
        Element element2 = new Element(Tag.valueOf("input"), "");
        element2.attr("name", "age");
        element2.val("30");
        
        formElement.addElement(element1);
        formElement.addElement(element2);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
        assertEquals("age", formData.get(1).key());
        assertEquals("30", formData.get(1).value());
    }
    
    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        
        assertTrue(formElement1.equals(formElement1));
        assertTrue(formElement1.equals(formElement2));
    }
}
