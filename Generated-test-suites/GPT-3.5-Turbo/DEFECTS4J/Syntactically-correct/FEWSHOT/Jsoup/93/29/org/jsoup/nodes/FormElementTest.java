
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement form;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        this.form = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        
        form.addElement(element);
        Elements elements = form.elements();
        
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }
    
    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        
        form.addElement(element);
        
        form.removeChild(element);
        
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testSubmit_NoActionAttribute() {
        Connection connection = form.submit();
        
        assertNotNull(connection);
        assertEquals(form.baseUri(), connection.url().toString());
        assertEquals(Connection.Method.GET, connection.method());
    }
    
    @Test
    public void testSubmit_ActionAttribute() {
        form.attr("action", "http://www.example.com/submit");
        form.attr("method", "POST");
        
        Connection connection = form.submit();
        
        assertNotNull(connection);
        assertEquals("http://www.example.com/submit", connection.url().toString());
        assertEquals(Connection.Method.POST, connection.method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_EmptyActionAttribute() {
        form.attr("action", "");
        form.submit();
    }
    
    @Test
    public void testFormData() {
        Element input = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        Element select = new Element(Tag.valueOf("select"), form.baseUri(), new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), form.baseUri(), new Attributes());
        option1.attr("selected", "selected");
        option1.val("option1");
        Element option2 = new Element(Tag.valueOf("option"), form.baseUri(), new Attributes());
        option2.val("option2");
        
        form.addElement(input);
        form.addElement(select);
        select.addElement(option1);
        select.addElement(option2);
        
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(2, data.size());
        
        Connection.KeyVal keyVal1 = data.get(0);
        assertNotNull(keyVal1);
        assertEquals("name", keyVal1.key());
        assertEquals("", keyVal1.value());
        
        Connection.KeyVal keyVal2 = data.get(1);
        assertNotNull(keyVal2);
        assertEquals("name", keyVal2.key());
        assertEquals("option1", keyVal2.value());
    }
    
    @Test
    public void testFormData_CheckboxChecked() {
        Element checkbox = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "name");
        checkbox.attr("value", "value");
        checkbox.attr("checked", "checked");
        
        form.addElement(checkbox);
        
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        
        assertEquals(1, data.size());
        
        Connection.KeyVal keyVal = data.get(0);
        assertNotNull(keyVal);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }
    
    @Test
    public void testFormData_CheckboxNotChecked() {
        Element checkbox = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "name");
        checkbox.attr("value", "value");
        
        form.addElement(checkbox);
        
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(0, data.size());
    }
    
    @Test
    public void testFormData_RadioButtonChecked() {
        Element radioButton = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        radioButton.attr("type", "radio");
        radioButton.attr("name", "name");
        radioButton.attr("value", "value");
        radioButton.attr("checked", "checked");
        
        form.addElement(radioButton);
        
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        
        assertEquals(1, data.size());
        
        Connection.KeyVal keyVal = data.get(0);
        assertNotNull(keyVal);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }
    
    @Test
    public void testFormData_RadioButtonNotChecked() {
        Element radioButton = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        radioButton.attr("type", "radio");
        radioButton.attr("name", "name");
        radioButton.attr("value", "value");
        
        form.addElement(radioButton);
        
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(0, data.size());
    }
    
    @Test
    public void testFormData_Select() {
        Element select = new Element(Tag.valueOf("select"), form.baseUri(), new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), form.baseUri(), new Attributes());
        option1.attr("selected", "selected");
        option1.val("option1");
        Element option2 = new Element(Tag.valueOf("option"), form.baseUri(), new Attributes());
        option2.val("option2");
        
        select.addElement(option1);
        select.addElement(option2);
        
        form.addElement(select);
        
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        
        Connection.KeyVal keyVal = data.get(0);
        assertNotNull(keyVal);
        assertEquals("name", keyVal.key());
        assertEquals("option1", keyVal.value());
    }
    
    @Test
    public void testFormData_SelectNoSelectedOption() {
        Element select = new Element(Tag.valueOf("select"), form.baseUri(), new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), form.baseUri(), new Attributes());
        option1.val("option1");
        Element option2 = new Element(Tag.valueOf("option"), form.baseUri(), new Attributes());
        option2.val("option2");
        
        select.addElement(option1);
        select.addElement(option2);
        
        form.addElement(select);
        
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        
        Connection.KeyVal keyVal = data.get(0);
        assertNotNull(keyVal);
        assertEquals("name", keyVal.key());
        assertEquals("option1", keyVal.value());
    }
    
    @Test
    public void testFormData_EmptyElements() {
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(0, data.size());
    }
}
