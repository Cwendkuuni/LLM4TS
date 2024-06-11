package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {
    
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        Element element = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        form.addElement(element);
        
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        Connection connection = form.submit();
        
        assertNotNull(connection);
        assertEquals("https://example.com", connection.request().url().toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        form.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        Element input1 = new Element(Tag.valueOf("input"), "https://example.com", new Attributes().put("name", "field1").put("value", "value1"));
        Element input2 = new Element(Tag.valueOf("input"), "https://example.com", new Attributes().put("name", "field2").put("value", "value2"));
        form.addElement(input1);
        form.addElement(input2);
        
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("field1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("field2", formData.get(1).key());
        assertEquals("value2", formData.get(1).value());
    }
    
    @Test
    public void testEmptyFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }
    
    @Test
    public void testFormDataWithDisabledElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        Element input = new Element(Tag.valueOf("input"), "https://example.com", new Attributes().put("name", "field1").put("disabled", ""));
        form.addElement(input);
        
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }
    
    // Add more test methods to comprehensively test all methods in the FormElement class
    
}