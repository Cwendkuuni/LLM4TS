
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
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

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
    public void testAddElement() {
        assertEquals(0, formElement.elements().size());
        
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        formElement.addElement(element);
        
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }
    
    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        formElement.addElement(element);
        
        assertEquals(1, formElement.elements().size());
        
        formElement.removeChild(element);
        
        assertEquals(0, formElement.elements().size());
        assertFalse(formElement.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        
        assertEquals(formElement.absUrl("action"), connection.request().url().toString());
        assertEquals("POST", connection.request().method());
    }
    
    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri());
        inputElement.attr("name", "inputName");
        inputElement.attr("value", "inputValue");
        
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri());
        Element optionElement = new Element(Tag.valueOf("option"), formElement.baseUri());
        optionElement.attr("value", "optionValue");
        optionElement.attr("selected", "");
        selectElement.appendChild(optionElement);
        
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(2, formData.size());
        
        Connection.KeyVal inputKeyVal = formData.get(0);
        assertEquals("inputName", inputKeyVal.key());
        assertEquals("inputValue", inputKeyVal.value());
        
        Connection.KeyVal selectKeyVal = formData.get(1);
        assertEquals("inputName", selectKeyVal.key());
        assertEquals("optionValue", selectKeyVal.value());
    }
}
