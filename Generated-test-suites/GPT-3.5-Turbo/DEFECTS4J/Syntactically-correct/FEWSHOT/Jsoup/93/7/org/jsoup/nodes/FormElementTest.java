
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElementCreation() {
        assertNotNull(formElement);
        assertEquals("form", formElement.tagName());
        assertEquals("http://example.com", formElement.baseUri());
        assertNotNull(formElement.attributes());
        assertNotNull(formElement.elements());
        assertTrue(formElement.elements().isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        formElement.addElement(element);
        
        assertFalse(formElement.elements().isEmpty());
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().first());
    }
    
    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        formElement.addElement(element);
        
        assertFalse(formElement.elements().isEmpty());
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().first());
        
        formElement.removeChild(element);
        
        assertTrue(formElement.elements().isEmpty());
    }
    
    @Test
    public void testSubmit() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri());
        input.attr("name", "query");
        input.attr("value", "test");
        
        formElement.addElement(input);
        
        Connection connection = formElement.submit();
        
        assertNotNull(connection);
        assertEquals("GET", connection.request().method().name());
        assertEquals("http://example.com", connection.request().url().toExternalForm());
        assertNotNull(connection.request().data());
        assertEquals(1, connection.request().data().size());
        assertEquals(HttpConnection.KeyVal.create("query", "test"), connection.request().data().iterator().next());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyActionUrl() {
        formElement.submit();
    }
    
    @Test
    public void testSubmitWithMethodPost() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");
        
        Connection connection = formElement.submit();
        
        assertNotNull(connection);
        assertEquals("POST", connection.request().method().name());
        assertEquals("http://example.com/submit", connection.request().url().toExternalForm());
        assertNotNull(connection.request().data());
        assertTrue(connection.request().data().isEmpty());
    }
    
    @Test
    public void testFormData() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri());
        input.attr("name", "query");
        input.attr("value", "test");
        
        formElement.addElement(input);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals(HttpConnection.KeyVal.create("query", "test"), formData.get(0));
    }
    
    @Test
    public void testFormDataWithSelectElement() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri());
        select.attr("name", "select");
        formElement.addElement(select);
        
        Element option = new Element(Tag.valueOf("option"), formElement.baseUri());
        option.attr("value", "1");
        select.appendChild(option);
        
        option = new Element(Tag.valueOf("option"), formElement.baseUri());
        option.attr("value", "2");
        option.attr("selected", "selected");
        select.appendChild(option);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals(HttpConnection.KeyVal.create("select", "2"), formData.get(0));
    }
    
    @Test
    public void testFormDataWithCheckboxElement() {
        Element checkbox = new Element(Tag.valueOf("input"), formElement.baseUri());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "checkbox");
        checkbox.attr("checked", "checked");
        checkbox.attr("value", "true");
        
        formElement.addElement(checkbox);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals(HttpConnection.KeyVal.create("checkbox", "true"), formData.get(0));
    }
    
    @Test
    public void testFormDataWithDisabledElement() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri());
        input.attr("name", "query");
        input.attr("value", "test");
        input.attr("disabled", "disabled");
        
        formElement.addElement(input);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataWithElementWithoutNameAttribute() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri());
        input.attr("value", "test");
        
        formElement.addElement(input);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataWithEmptyElement() {
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
}
