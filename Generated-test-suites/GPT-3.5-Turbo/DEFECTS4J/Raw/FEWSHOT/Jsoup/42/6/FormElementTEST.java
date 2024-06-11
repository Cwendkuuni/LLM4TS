package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement form;
    
    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        FormElement returnedForm = form.addElement(element);
        assertSame(form, returnedForm);
        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        form.attr("action", "/submit");
        form.attr("method", "POST");
        
        final Connection con = form.submit();
        assertNotNull(con);
        
        assertEquals("http://www.example.com/submit", con.request().url().toString());
        assertEquals(Connection.Method.POST, con.request().method());
        
        assertNull(form.submit().request().header("User-Agent"));
        assertNull(form.submit().request().header("Timeout"));
        assertNull(form.submit().request().header("Cookie"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_illegalAction() {
        Validate.exclusiveAttributes(form.attributes, "action");
        form.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("type", "text");
        inputElement.val("jsmith");

        Element checkboxElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        checkboxElement.attr("name", "subscribe");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");

        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        optionElement.attr("selected", "");
        optionElement.val("option1");
        selectElement.appendChild(optionElement);

        form.addElement(inputElement);
        form.addElement(checkboxElement);
        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(3, formData.size());

        Connection.KeyVal username = HttpConnection.KeyVal.create("username", "jsmith");
        assertTrue(formData.contains(username));

        Connection.KeyVal subscribe = HttpConnection.KeyVal.create("subscribe", "on");
        assertTrue(formData.contains(subscribe));

        Connection.KeyVal option = HttpConnection.KeyVal.create("select", "option1");
        assertTrue(formData.contains(option));
    }

}