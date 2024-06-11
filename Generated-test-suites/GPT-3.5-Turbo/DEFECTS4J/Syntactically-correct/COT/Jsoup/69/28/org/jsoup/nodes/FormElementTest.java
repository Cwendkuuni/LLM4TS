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
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }
    
    @Test
    public void testAddElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element element = new Element("input");
        formElement.addElement(element);
        
        Elements elements = formElement.elements();
        assertTrue(elements.contains(element));
    }
    
    @Test
    public void testSubmit() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        formElement.attr("action", "");
        
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element inputElement = new Element("input");
        inputElement.attr("name", "username");
        inputElement.val("user");
        formElement.addElement(inputElement);
        
        Element selectElement = new Element("select");
        Element optionElement = new Element("option");
        optionElement.attr("selected", "");
        optionElement.val("1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("user", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("1", formData.get(1).value());
    }
    
    @Test
    public void testFormDataNoElements() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataDisabledElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element inputElement = new Element("input");
        inputElement.attr("name", "username");
        inputElement.attr("disabled", "");
        formElement.addElement(inputElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataEmptyName() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element inputElement = new Element("input");
        inputElement.attr("name", "");
        inputElement.val("user");
        formElement.addElement(inputElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataSelectNoSelectedOption() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element selectElement = new Element("select");
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataCheckboxChecked() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element checkboxElement = new Element("input");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("checkbox", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }
    
    @Test
    public void testFormDataCheckboxUnchecked() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element checkboxElement = new Element("input");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox");
        formElement.addElement(checkboxElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataRadioChecked() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element radioElement = new Element("input");
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio");
        radioElement.attr("checked", "");
        formElement.addElement(radioElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("radio", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }
    
    @Test
    public void testFormDataRadioUnchecked() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element radioElement = new Element("input");
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio");
        formElement.addElement(radioElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
    
}