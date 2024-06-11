
package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FormElementTest {
    
    private FormElement formElement;
    
    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());
        
        Element element = new Element(Tag.valueOf("input"), "");
        formElement.addElement(element);
        
        elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "");
        formElement.addElement(element);
        
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }
    
    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "");
        formElement.addElement(element);
        
        formElement.removeChild(element);
        
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testSubmit() {
        formElement.attr("action", "http://example.com/action");
        formElement.attr("method", "POST");
        
        Connection connection = formElement.submit();
        
        assertEquals("http://example.com/action", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "");
        element1.attr("name", "input1");
        element1.attr("value", "value1");
        
        Element element2 = new Element(Tag.valueOf("input"), "");
        element2.attr("name", "input2");
        element2.attr("value", "value2");
        
        Element element3 = new Element(Tag.valueOf("input"), "");
        element3.attr("name", "input3");
        element3.attr("value", "");
        
        Element element4 = new Element(Tag.valueOf("input"), "");
        element4.attr("name", "input4");
        element4.attr("type", "checkbox");
        element4.attr("checked", "");
        
        Element element5 = new Element(Tag.valueOf("input"), "");
        element5.attr("name", "input5");
        element5.attr("type", "checkbox");
        
        Element element6 = new Element(Tag.valueOf("input"), "");
        element6.attr("name", "input6");
        element6.attr("type", "radio");
        
        Element element7 = new Element(Tag.valueOf("input"), "");
        element7.attr("name", "input7");
        element7.attr("type", "radio");
        element7.attr("checked", "");
        
        Element element8 = new Element(Tag.valueOf("select"), "");
        element8.attr("name", "input8");
        Element option1 = new Element(Tag.valueOf("option"), "");
        option1.attr("selected", "");
        option1.val("option1");
        element8.appendChild(option1);
        Element option2 = new Element(Tag.valueOf("option"), "");
        option2.attr("value", "option2");
        element8.appendChild(option2);
        
        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);
        formElement.addElement(element4);
        formElement.addElement(element5);
        formElement.addElement(element6);
        formElement.addElement(element7);
        formElement.addElement(element8);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(6, formData.size());
        assertEquals(HttpConnection.KeyVal.create("input1", "value1"), formData.get(0));
        assertEquals(HttpConnection.KeyVal.create("input2", "value2"), formData.get(1));
        assertEquals(HttpConnection.KeyVal.create("input4", "on"), formData.get(2));
        assertEquals(HttpConnection.KeyVal.create("input7", "on"), formData.get(3));
        assertEquals(HttpConnection.KeyVal.create("input8", "option1"), formData.get(4));
        assertEquals(HttpConnection.KeyVal.create("input8", "option2"), formData.get(5));
    }
}

