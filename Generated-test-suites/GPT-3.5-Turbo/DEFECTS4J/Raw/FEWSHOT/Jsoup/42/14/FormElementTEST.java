package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        
        assertNotNull(formElement.elements());
    }
    
    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes().put("type", "text").put("name", "username").put("value", "john"));
        
        formElement.addElement(element);
        
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes().put("action", "/submit").put("method", "POST"));
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes().put("type", "text").put("name", "username").put("value", "john"));
        
        formElement.addElement(element);
        
        Connection connection = formElement.submit();
        assertEquals("http://www.example.com/submit", connection.request().url());
        assertEquals(Connection.Method.POST, connection.request().method());
        assertNotNull(connection.request().data());
    }
    
    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes().put("type", "text").put("name", "username").put("value", "john"));
        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes().put("name", "country"));
        Element option1 = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes().put("value", "USA").put("selected", ""));
        Element option2 = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes().put("value", "UK"));
        
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        selectElement.addElement(option1);
        selectElement.addElement(option2);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
        assertEquals("country", formData.get(1).key());
        assertEquals("USA", formData.get(1).value());
    }
    
    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        
        assertTrue(formElement1.equals(formElement2));
    }
}