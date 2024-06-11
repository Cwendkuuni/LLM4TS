package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        // Test case for elements() method
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        Elements elements = formElement.elements();
        
        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        // Test case for addElement() method
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        
        FormElement result = formElement.addElement(element);
        
        Assert.assertEquals(formElement, result);
        Assert.assertEquals(1, formElement.elements().size());
        Assert.assertEquals(element, formElement.elements().get(0));
    }
    
    @Test
    public void testSubmit() {
        // Test case for submit() method
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        formElement.attr("action", "/submit");
        formElement.attr("method", "post");
        
        Connection connection = formElement.submit();
        
        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test
    public void testFormData() {
        // Test case for formData() method
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("admin");
        formElement.addElement(inputElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("username", formData.get(0).key());
        Assert.assertEquals("admin", formData.get(0).value());
    }
}