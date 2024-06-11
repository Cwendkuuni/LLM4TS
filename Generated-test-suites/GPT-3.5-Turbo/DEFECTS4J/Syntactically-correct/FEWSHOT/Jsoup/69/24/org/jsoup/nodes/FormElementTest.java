package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(0, form.elements().size());
        
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element1);
        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element1));
        
        Element element2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element2);
        assertEquals(2, form.elements().size());
        assertTrue(form.elements().contains(element2));
    }
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element1);
        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element1));
        
        Element element2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element2);
        assertEquals(2, form.elements().size());
        assertTrue(form.elements().contains(element2));
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = form.submit();
        
        assertEquals("http://example.com", connection.request().url());
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals(0, connection.request().data().size());
        
        form.attr("action", "http://example.com/login");
        form.attr("method", "GET");
        connection = form.submit();
        
        assertEquals("http://example.com/login", connection.request().url());
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals(0, connection.request().data().size());
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> data = form.formData();
        
        assertEquals(0, data.size());
        
        Element inputText = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputText.attr("name", "username");
        inputText.val("john");
        form.addElement(inputText);
        data = form.formData();
        
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john", data.get(0).value());
        
        Element inputPassword = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputPassword.attr("name", "password");
        inputPassword.attr("type", "password");
        inputPassword.val("pass123");
        form.addElement(inputPassword);
        data = form.formData();
        
        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john", data.get(0).value());
        assertEquals("password", data.get(1).key());
        assertEquals("pass123", data.get(1).value());
    }
}