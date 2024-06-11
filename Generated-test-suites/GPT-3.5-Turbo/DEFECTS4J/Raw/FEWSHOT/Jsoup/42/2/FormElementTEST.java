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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        
        assertTrue(elements.isEmpty());
        
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element1);
        
        elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element1));
        
        Element element2 = new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes());
        formElement.addElement(element2);
        
        elements = formElement.elements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
    }
    
    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element1);
        
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element1));
        
        Element element2 = new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes());
        formElement.addElement(element2);
        
        elements = formElement.elements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
    }
    
    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
        
        formElement.attr("action", "/submit");
        formElement.attr("method", "post");
        
        connection = formElement.submit();
        
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "name");
        inputElement.val("John Doe");
        formElement.addElement(inputElement);
        
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement1.attr("value", "option1");
        optionElement1.attr("selected", "");
        selectElement.addElement(optionElement1);
        
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement2.attr("value", "option2");
        selectElement.addElement(optionElement2);
        
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(2, formData.size());
        
        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("name", keyVal1.key());
        assertEquals("John Doe", keyVal1.value());
        
        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("name", keyVal2.key());
        assertEquals("option1", keyVal2.value());
    }
    
}