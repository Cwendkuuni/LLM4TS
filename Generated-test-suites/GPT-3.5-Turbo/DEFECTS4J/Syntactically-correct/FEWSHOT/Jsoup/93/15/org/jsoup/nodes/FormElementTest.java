package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {
    
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element);
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }
    
    @Test
    public void testRemoveChild() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element);
        form.removeChild(element);
        Elements elements = form.elements();
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Connection connection = form.submit();
        assertNotNull(connection);
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element input1 = new Element(Tag.valueOf("input"), "", new Attributes());
        input1.attr("name", "name");
        input1.val("John");
        form.addElement(input1);
        
        Element input2 = new Element(Tag.valueOf("input"), "", new Attributes());
        input2.attr("name", "email");
        input2.val("john@example.com");
        form.addElement(input2);
        
        Element select = new Element(Tag.valueOf("select"), "", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("selected", "");
        option1.val("option1");
        select.addElement(option1);
        form.addElement(select);
        
        List<Connection.KeyVal> data = form.formData();
        assertEquals(3, data.size());
    }
}