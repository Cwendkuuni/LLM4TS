
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FormElementTest {
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input1").put("type", "text"));
        
        FormElement result = form.addElement(element);
        
        assertTrue(result.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes().put("action", "/submit"));
        
        Connection connection = form.submit();
        
        assertEquals("http://www.example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input").put("type", "text").put("value", "value"));
        
        form.addElement(inputElement);
        List<Connection.KeyVal> formData = form.formData();
        
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("input", keyVal.key());
        assertEquals("value", keyVal.value());
    }
}
