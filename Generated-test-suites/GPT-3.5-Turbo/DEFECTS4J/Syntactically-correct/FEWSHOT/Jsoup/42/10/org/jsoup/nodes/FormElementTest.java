package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    
    private FormElement formElement;
    
    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "test.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "test.com", new Attributes());
        formElement.addElement(element);
        
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }
    
    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("test.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
        assertFalse(connection.data().isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "test.com", new Attributes());
        element1.attr("name", "input1");
        element1.attr("type", "text");
        element1.val("value1");
        
        Element element2 = new Element(Tag.valueOf("select"), "test.com", new Attributes());
        element2.attr("name", "select1");
        Element option1 = new Element(Tag.valueOf("option"), "test.com", new Attributes());
        option1.attr("selected");
        option1.val("option1");
        element2.appendChild(option1);
        
        Element element3 = new Element(Tag.valueOf("checkbox"), "test.com", new Attributes());
        element3.attr("name", "checkbox1");
        element3.attr("checked");
        element3.val("checked");
        
        Element element4 = new Element(Tag.valueOf("radio"), "test.com", new Attributes());
        element4.attr("name", "radio1");
        element4.attr("checked");
        element4.val("checked");
        
        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);
        formElement.addElement(element4);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(4, formData.size());
        
        Connection.KeyVal keyVal1 = HttpConnection.KeyVal.create("input1", "value1");
        Connection.KeyVal keyVal2 = HttpConnection.KeyVal.create("select1", "option1");
        Connection.KeyVal keyVal3 = HttpConnection.KeyVal.create("checkbox1", "checked");
        Connection.KeyVal keyVal4 = HttpConnection.KeyVal.create("radio1", "checked");
        
        assertTrue(formData.contains(keyVal1));
        assertTrue(formData.contains(keyVal2));
        assertTrue(formData.contains(keyVal3));
        assertTrue(formData.contains(keyVal4));
    }
    
    @Test
    public void testEquals() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "test.com";
        Attributes attributes = new Attributes();
        FormElement formElement2 = new FormElement(tag, baseUri, attributes);
        
        assertTrue(formElement.equals(formElement2));
        assertFalse(formElement.equals(null));
    }
}