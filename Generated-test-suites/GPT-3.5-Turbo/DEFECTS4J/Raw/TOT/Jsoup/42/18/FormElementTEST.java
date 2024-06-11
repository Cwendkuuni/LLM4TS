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
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes());
        form.addElement(element1);
        form.addElement(element2);
        Elements formElements = form.elements();
        assertEquals(element1, formElements.get(0));
        assertEquals(element2, formElements.get(1));
    }
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element);
        Elements formElements = form.elements();
        assertEquals(element, formElements.get(0));
    }
    
    @Test
    public void testSubmit() {
        FormElement formWithoutAction = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connectionWithoutAction = formWithoutAction.submit();
        assertNull(connectionWithoutAction.request().url().toString());
        
        FormElement formWithAction = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formWithAction.attr("action", "/submit");
        Connection connection = formWithAction.submit();
        assertEquals("http://example.com/submit", connection.request().url().toString());
        
        FormElement formWithEmptyAction = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formWithEmptyAction.attr("action", "");
        Connection emptyActionConnection = formWithEmptyAction.submit();
        assertEquals("http://example.com", emptyActionConnection.request().url().toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        FormElement formWithInvalidAction = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formWithInvalidAction.attr("action", "invalid");
        formWithInvalidAction.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Element inputText = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputText.attr("name", "inputText");
        inputText.val("ValueExample");
        form.addElement(inputText);
        
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        select.attr("name", "select");
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("selected", "");
        option1.val("Option1");
        select.addElement(option1);
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.val("Option2");
        select.addElement(option2);
        Element option3 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option3.attr("selected", "");
        option3.val("Option3");
        select.addElement(option3);
        form.addElement(select);
        
        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("name", "checkbox");
        checkbox.attr("checked", "");
        checkbox.val("Checkbox");
        form.addElement(checkbox);
        
        List<Connection.KeyVal> formData = form.formData();
        assertEquals("inputText", formData.get(0).key());
        assertEquals("ValueExample", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("Option1", formData.get(1).value());
        assertEquals("select", formData.get(2).key());
        assertEquals("Option3", formData.get(2).value());
        assertEquals("checkbox", formData.get(3).key());
        assertEquals("Checkbox", formData.get(3).value());
    }
    
    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form3 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form3.attr("action", "/submit");
        
        assertTrue(form1.equals(form1));
        assertTrue(form1.equals(form2));
        assertFalse(form1.equals(form3));
    }
}