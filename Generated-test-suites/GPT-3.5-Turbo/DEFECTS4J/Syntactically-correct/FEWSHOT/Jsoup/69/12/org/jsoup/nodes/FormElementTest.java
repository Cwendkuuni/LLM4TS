package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    
    private FormElement formElement;
    private Elements elements;
    
    @Before
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        elements = new Elements();
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        formElement.addElement(new Element(Tag.valueOf("textarea"), "", new Attributes()));
    }
    
    @Test
    public void testConstructor() {
        assertEquals(Tag.valueOf("form"), formElement.tag());
        assertEquals("http://example.com", formElement.baseUri());
        assertNotNull(formElement.attributes());
    }
    
    @Test
    public void testElements() {
        assertEquals(elements, formElement.elements());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("button"), "", new Attributes());
        formElement.addElement(element);
        
        assertTrue(formElement.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        formElement.attr("method", "POST");
        
        Connection connection = formElement.submit();
        
        assertEquals(Method.POST, connection.request().method());
        assertEquals("http://example.com", connection.request().url());
    }
    
    @Test
    public void testSubmitWithRelativeUrl() {
        Connection connection = formElement.submit();
        
        assertEquals(Method.GET, connection.request().method());
        assertEquals("http://example.com", connection.request().url());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formWithoutBaseUri = new FormElement(Tag.valueOf("form"), "", new Attributes());
        
        formWithoutBaseUri.submit();
    }
    
    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(3, formData.size());
    }
    
    @Test
    public void testFormDataWithDisabledInput() {
        Element disabledElement = new Element(Tag.valueOf("input"), "", new Attributes("disabled", "disabled"));
        formElement.addElement(disabledElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(3, formData.size());
    }
    
    @Test
    public void testFormDataWithEmptyName() {
        Element elementWithEmptyName = new Element(Tag.valueOf("input"), "", new Attributes("name", ""));
        formElement.addElement(elementWithEmptyName);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(3, formData.size());
    }
    
    @Test
    public void testFormDataWithSelect() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes("selected", "selected"));
        optionElement.val("option1");
        selectElement.addElement(optionElement);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(4, formData.size());
    }
    
    @Test
    public void testFormDataWithCheckbox() {
        Element checkBoxElement = new Element(Tag.valueOf("input"), "", new Attributes("type", "checkbox", "name", "checkbox", "checked", "checked"));
        formElement.addElement(checkBoxElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(4, formData.size());
    }
    
    @Test
    public void testFormDataWithCheckboxNotChecked() {
        Element checkBoxElement = new Element(Tag.valueOf("input"), "", new Attributes("type", "checkbox", "name", "checkbox"));
        formElement.addElement(checkBoxElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(3, formData.size());
    }
    
    @Test
    public void testFormDataWithRadio() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes("type", "radio", "name", "radio", "checked", "checked"));
        formElement.addElement(radioElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(4, formData.size());
    }
    
    @Test
    public void testFormDataWithRadioNotChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes("type", "radio", "name", "radio"));
        formElement.addElement(radioElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(3, formData.size());
    }
    
    @Test
    public void testFormDataWithValue() {
        Element elementWithValue = new Element(Tag.valueOf("input"), "", new Attributes("value", "value"));
        formElement.addElement(elementWithValue);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(4, formData.size());
    }
    
    @Test
    public void testFormDataWithoutValue() {
        Element elementWithoutValue = new Element(Tag.valueOf("input"), "", new Attributes("value", ""));
        formElement.addElement(elementWithoutValue);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(4, formData.size());
    }
    
}