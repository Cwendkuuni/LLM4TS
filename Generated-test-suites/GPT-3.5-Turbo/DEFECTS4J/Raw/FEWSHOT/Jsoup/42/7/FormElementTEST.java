package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

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

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());

        Element input1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element input2 = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(input1);
        formElement.addElement(input2);

        elements = formElement.elements();
        assertEquals(2, elements.size());
        assertEquals(input1, elements.get(0));
        assertEquals(input2, elements.get(1));
    }

    @Test
    public void testAddElement() {
        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(input);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(input, elements.get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());

        formElement.attr("action", "/submit");
        formElement.attr("method", "post");
        connection = formElement.submit();
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        Element input1 = new Element(Tag.valueOf("input"), "", new Attributes());
        input1.attr("name", "name1");
        input1.attr("type", "text");
        input1.val("value1");
        Element input2 = new Element(Tag.valueOf("input"), "", new Attributes());
        input2.attr("name", "name2");
        input2.attr("type", "checkbox");
        input2.attr("checked", "checked");
        input2.val("value2");
        formElement.addElement(input1);
        formElement.addElement(input2);

        formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals(HttpConnection.KeyVal.create("name1", "value1"), formData.get(0));
        assertEquals(HttpConnection.KeyVal.create("name2", "value2"), formData.get(1));
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(formElement, formElement1);

        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(input);
        formElement1.addElement(input);
        assertEquals(formElement, formElement1);
    }
}