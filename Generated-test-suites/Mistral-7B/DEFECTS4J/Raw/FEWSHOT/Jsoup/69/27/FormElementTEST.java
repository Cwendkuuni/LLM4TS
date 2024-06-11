package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private Document doc;

    @Before
    public void setup() {
        ParseSetup parseSetup = new ParseSetup();
        doc = Jsoup.parse("<html><body>" +
                "<form action='/' method='POST'>" +
                "<input type='text' name='input1' value='value1'>" +
                "<select name='select1'>" +
                "<option value='opt1'>Option 1</option>" +
                "<option value='opt2' selected>Option 2</option>" +
                "</select>" +
                "<input type='checkbox' name='checkbox1' value='check1' checked>" +
                "<input type='checkbox' name='checkbox2' value='check2'>" +
                "<input type='submit' name='submit' value='Submit'>" +
                "</form>" +
                "</body></html>", parseSetup);
    }

    @Test
    public void testFormElementCreation() {
        FormElement formElement = (FormElement) doc.select("form").first();
        assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        FormElement formElement = (FormElement) doc.select("form").first();
        Elements elements = formElement.elements();
        assertEquals(5, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = (FormElement) doc.select("form").first();
        Element newElement = new Element("input");
        newElement.attr("type", "hidden");
        newElement.attr("name", "hiddenInput");
        formElement.addElement(newElement);

        Elements elements = formElement.elements();
        assertEquals(6, elements.size());
    }

    @Test
    public void testSubmit() throws Exception {
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(5, formData.size());
        assertEquals("input1", formData.get(0).getKey());
        assertEquals("value1", formData.get(0).getValue());
        assertEquals("select1", formData.get(1).getKey());
        assertEquals("opt2", formData.get(1).getValue());
        assertEquals("checkbox1", formData.get(2).getKey());
        assertEquals("check1", formData.get(2).getValue());
        assertNull(formData.get(3).getValue()); // checkbox2 is not checked
        assertEquals("submit", formData.get(4).getKey());
        assertEquals("Submit", formData.get(4).getValue());

        String result = connection.execute().body();
        // add assertions for expected result here
    }

    @Test
    public void testFormDataEmptyForm() {
        FormElement formElement = (FormElement) doc.select("form[action='']").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }
}