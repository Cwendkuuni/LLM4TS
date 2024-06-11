package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void addElement_shouldAddElementToFormElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertTrue(elements.contains(element));
    }
    
    @Test
    public void removeChild_shouldRemoveChildElementFromFormElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        Elements elements = formElement.elements();
        assertFalse(elements.contains(element));
    }
    
    @Test
    public void submit_shouldReturnConnectionWithCorrectUrlAndMethod() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "post");
        
        Connection connection = formElement.submit();
        
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void submit_shouldThrowExceptionWhenActionUrlCannotBeDetermined() {
        formElement.submit();
    }
    
    @Test
    public void formData_shouldReturnListOfKeyVals() {
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input.attr("name", "username");
        input.attr("type", "text");
        input.val("jsmith");
        formElement.addElement(input);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("jsmith", keyVal.value());
    }
    
    @Test
    public void formData_shouldNotIncludeDisabledFormInputs() {
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input.attr("name", "username");
        input.attr("type", "text");
        input.val("jsmith");
        input.attr("disabled", "");
        formElement.addElement(input);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(0, formData.size());
    }
    
    @Test
    public void formData_shouldHandleSelectElementWithSelectedOption() {
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("value", "1");
        option1.attr("selected", "");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.attr("value", "2");
        select.appendChild(option1);
        select.appendChild(option2);
        formElement.addElement(select);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("select", keyVal.key());
        assertEquals("1", keyVal.value());
    }
    
    @Test
    public void formData_shouldHandleSelectElementWithoutSelectedOption() {
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("value", "1");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.attr("value", "2");
        select.appendChild(option1);
        select.appendChild(option2);
        formElement.addElement(select);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("select", keyVal.key());
        assertEquals("1", keyVal.value());
    }
    
    @Test
    public void formData_shouldHandleCheckboxWithCheckedAttribute() {
        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("name", "agree");
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "");
        formElement.addElement(checkbox);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("agree", keyVal.key());
        assertEquals("on", keyVal.value());
    }
    
    @Test
    public void formData_shouldHandleCheckboxWithoutCheckedAttribute() {
        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("name", "agree");
        checkbox.attr("type", "checkbox");
        formElement.addElement(checkbox);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(0, formData.size());
    }
    
    @Test
    public void formData_shouldHandleRadioWithCheckedAttribute() {
        Element radio = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radio.attr("name", "gender");
        radio.attr("type", "radio");
        radio.attr("checked", "");
        formElement.addElement(radio);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("gender", keyVal.key());
        assertEquals("on", keyVal.value());
    }
    
    @Test
    public void formData_shouldHandleRadioWithoutCheckedAttribute() {
        Element radio = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radio.attr("name", "gender");
        radio.attr("type", "radio");
        formElement.addElement(radio);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(0, formData.size());
    }
}