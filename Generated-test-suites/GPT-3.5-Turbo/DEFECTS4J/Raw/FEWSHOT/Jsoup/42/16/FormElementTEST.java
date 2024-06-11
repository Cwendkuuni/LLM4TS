package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        assertNotNull(form.elements());
        assertEquals(0, form.elements().size());
        
        Element input = new Element(Tag.valueOf("input"), "http://www.example.com");
        form.addElement(input);
        assertEquals(1, form.elements().size());
        assertEquals(input, form.elements().get(0));
    }
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        
        Element input = new Element(Tag.valueOf("input"), "http://www.example.com");
        form.addElement(input);
        
        assertEquals(1, form.elements().size());
        assertEquals(input, form.elements().get(0));
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        
        Connection con = form.submit();
        assertNotNull(con);
        assertEquals("http://www.example.com", con.request().url().toString());
        assertEquals(Connection.Method.GET, con.request().method());
        
        form.attr("action", "http://www.example.com/action");
        form.attr("method", "POST");
        
        con = form.submit();
        assertNotNull(con);
        assertEquals("http://www.example.com/action", con.request().url().toString());
        assertEquals(Connection.Method.POST, con.request().method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        
        form.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        
        // Test empty form data
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(0, data.size());
        
        // Test form data with one input element
        Element input = new Element(Tag.valueOf("input"), "http://www.example.com");
        input.attr("name", "username");
        input.attr("type", "text");
        input.val("admin");
        form.addElement(input);
        
        data = form.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("admin", data.get(0).value());
        
        // Test form data with multiple input elements including select, checkbox and radio
        Element select = new Element(Tag.valueOf("select"), "http://www.example.com");
        select.attr("name", "country");
        form.addElement(select);
        
        Element option1 = new Element(Tag.valueOf("option"), "http://www.example.com");
        option1.attr("value", "USA");
        option1.attr("selected", "");
        select.appendChild(option1);
        
        Element option2 = new Element(Tag.valueOf("option"), "http://www.example.com");
        option2.attr("value", "Canada");
        select.appendChild(option2);
        
        Element checkbox1 = new Element(Tag.valueOf("input"), "http://www.example.com");
        checkbox1.attr("name", "hobby");
        checkbox1.attr("type", "checkbox");
        checkbox1.attr("value", "singing");
        checkbox1.attr("checked", "");
        form.addElement(checkbox1);
        
        Element checkbox2 = new Element(Tag.valueOf("input"), "http://www.example.com");
        checkbox2.attr("name", "hobby");
        checkbox2.attr("type", "checkbox");
        checkbox2.attr("value", "dancing");
        form.addElement(checkbox2);
        
        Element radio1 = new Element(Tag.valueOf("input"), "http://www.example.com");
        radio1.attr("name", "gender");
        radio1.attr("type", "radio");
        radio1.attr("value", "male");
        radio1.attr("checked", "");
        form.addElement(radio1);
        
        Element radio2 = new Element(Tag.valueOf("input"), "http://www.example.com");
        radio2.attr("name", "gender");
        radio2.attr("type", "radio");
        radio2.attr("value", "female");
        form.addElement(radio2);
        
        data = form.formData();
        assertNotNull(data);
        assertEquals(5, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("admin", data.get(0).value());
        assertEquals("country", data.get(1).key());
        assertEquals("USA", data.get(1).value());
        assertEquals("hobby", data.get(2).key());
        assertEquals("singing", data.get(2).value());
        assertEquals("gender", data.get(3).key());
        assertEquals("male", data.get(3).value());
        assertEquals("gender", data.get(4).key());
        assertEquals("female", data.get(4).value());
    }
    
    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        FormElement form3 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        
        assertEquals(form1, form1);
        assertEquals(form1, form2);
        assertEquals(form2, form1);
        assertEquals(form1, form3);
        assertEquals(form3, form2);
        
        form1.attr("action", "http://www.example.com/action");
        form2.attr("action", "http://www.example.com/action");
        form3.attr("action", "http://www.example.com/action");
        
        assertEquals(form1, form1);
        assertEquals(form1, form2);
        assertEquals(form2, form1);
        assertEquals(form1, form3);
        assertEquals(form3, form2);
    }
}