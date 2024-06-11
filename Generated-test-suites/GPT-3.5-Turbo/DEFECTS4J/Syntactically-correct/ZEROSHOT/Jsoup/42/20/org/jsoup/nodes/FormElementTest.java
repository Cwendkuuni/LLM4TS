package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FormElementTest {
  
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        formElement.addElement(new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes().put("type", "text")));
        formElement.addElement(new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes().put("type", "password")));
        
        Elements elements = formElement.elements();
        
        assertEquals(2, elements.size());
        assertEquals("input", elements.get(0).tagName());
        assertEquals("input", elements.get(1).tagName());
        assertEquals("text", elements.get(0).attr("type"));
        assertEquals("password", elements.get(1).attr("type"));
    }
    
    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes().put("type", "text"));
        
        FormElement updatedFormElement = formElement.addElement(inputElement);
        
        assertSame(formElement, updatedFormElement);
        assertEquals(1, updatedFormElement.elements.size());
        assertEquals(inputElement, updatedFormElement.elements.get(0));
    }
    
    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes().put("action", "/submit-url").put("method", "POST"));
        formElement.addElement(new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes().put("name", "username").put("type", "text").put("value", "testUser")));
        formElement.addElement(new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes().put("name", "password").put("type", "password").put("value", "secret")));
        
        Connection connection = formElement.submit();
        
        assertNotNull(connection);
        assertEquals("/submit-url", connection.request().url().getPath());
        assertEquals(Connection.Method.POST, connection.request().method());
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testUser", formData.get(0).value());
        assertEquals("password", formData.get(1).key());
        assertEquals("secret", formData.get(1).value());
    }
    
    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        formElement.addElement(new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes().put("name", "username").put("type", "text").put("value", "testUser")));
        formElement.addElement(new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes().put("name", "password").put("type", "password").put("value", "secret")));
        formElement.addElement(new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes().put("name", "rememberMe").put("type", "checkbox").put("checked", "")));
        formElement.addElement(new Element(Tag.valueOf("select"), "https://www.example.com", new Attributes().put("name", "language")));
        formElement.addElement(new Element(Tag.valueOf("option"), "https://www.example.com", new Attributes().put("value", "en").put("selected", "")));
        formElement.addElement(new Element(Tag.valueOf("option"), "https://www.example.com", new Attributes().put("value", "fr")));
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(4, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testUser", formData.get(0).value());
        assertEquals("password", formData.get(1).key());
        assertEquals("secret", formData.get(1).value());
        assertEquals("rememberMe", formData.get(2).key());
        assertEquals("", formData.get(2).value());
        assertEquals("language", formData.get(3).key());
        assertEquals("en", formData.get(3).value());
    }
    
    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        
        assertEquals(formElement1, formElement2);
    }
  
}