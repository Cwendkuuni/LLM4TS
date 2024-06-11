package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FormElementTest {
    @Test
    public void testConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }
    
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com");
        FormElement updatedFormElement = formElement.addElement(element);
        assertEquals(1, updatedFormElement.elements().size());
        assertEquals(element, updatedFormElement.elements().get(0));
    }
    
    @Test
    public void testRemoveChild() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com");
        formElement.addElement(element);
        formElement.removeChild(element);
        assertTrue(formElement.elements().isEmpty());
    }
    
    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "/submit");
        Connection connection = formElement.submit();
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "http://example.com");
        input.attr("name", "username");
        input.val("John Doe");
        formElement.addElement(input);
        
        Element select = new Element(Tag.valueOf("select"), "http://example.come");
        select.attr("name", "gender");
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com");
        option1.attr("value", "male");
        option1.attr("selected", "");
        select.appendChild(option1);
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com");
        option2.attr("value", "female");
        select.appendChild(option2);
        formElement.addElement(select);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("John Doe", formData.get(0).value());
        assertEquals("gender", formData.get(1).key());
        assertEquals("male", formData.get(1).value());
    }
}