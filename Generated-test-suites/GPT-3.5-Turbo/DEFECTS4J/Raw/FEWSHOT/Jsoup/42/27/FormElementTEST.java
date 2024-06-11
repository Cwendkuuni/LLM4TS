package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements emptyElements = formElement.elements();

        assertEquals(0, emptyElements.size());
        
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);
        
        Elements elements = formElement.elements();

        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        
        FormElement returnedFormElement = formElement.addElement(element);
        
        assertTrue(returnedFormElement == formElement);
        assertTrue(formElement.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");
        
        Connection connection = formElement.submit();
        
        assertEquals("http://www.example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        formElement.attr("method", "POST");
        
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Element inputElement1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement1.attr("name", "input1");
        inputElement1.attr("type", "text");
        inputElement1.val("value1");
        formElement.addElement(inputElement1);
        
        Element inputElement2 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement2.attr("name", "input2");
        inputElement2.attr("type", "checkbox");
        inputElement2.attr("checked", "checked");
        inputElement2.val("value2");
        formElement.addElement(inputElement2);
        
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        selectElement.attr("name", "select1");
        
        Element optionElement1 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        optionElement1.attr("selected", "");
        optionElement1.val("option1");
        selectElement.appendChild(optionElement1);
        
        Element optionElement2 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        optionElement2.val("option2");
        selectElement.appendChild(optionElement2);
        
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(3, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("input1", "value1")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("input2", "value2")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("select1", "option1")));
    }
    
    @Test
    public void testEquals() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        
        FormElement formElement1 = new FormElement(tag, baseUri, attributes);
        FormElement formElement2 = new FormElement(tag, baseUri, attributes);
        
        assertEquals(formElement1, formElement2);

        formElement1.attr("action", "/submit");
        assertFalse(formElement1.equals(formElement2));

        formElement1.attr("action", "");
        formElement1.attr("method", "POST");
        assertFalse(formElement1.equals(formElement2));

        formElement2.attr("action", "/submit");
        assertFalse(formElement1.equals(formElement2));

        formElement1.attr("method", "GET");
        assertFalse(formElement1.equals(formElement2));
    }    
}