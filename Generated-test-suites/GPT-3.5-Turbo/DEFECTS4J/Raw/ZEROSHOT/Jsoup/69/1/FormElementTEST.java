package org.jsoup.nodes.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com/";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertEquals(0, elements.size());

        // Add elements to the form
        formElement.addElement(new Element(Tag.valueOf("input"), "http://www.example.com/", new Attributes()));
        formElement.addElement(new Element(Tag.valueOf("textarea"), "http://www.example.com/", new Attributes()));

        elements = formElement.elements();
        Assert.assertEquals(2, elements.size());
        Assert.assertEquals("input", elements.get(0).tagName());
        Assert.assertEquals("textarea", elements.get(1).tagName());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com/", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        Assert.assertEquals(1, updatedFormElement.elements().size());
        Assert.assertEquals(element, updatedFormElement.elements().get(0));

        // Add another element
        Element element2 = new Element(Tag.valueOf("select"), "http://www.example.com/", new Attributes());
        updatedFormElement = updatedFormElement.addElement(element2);
        Assert.assertEquals(2, updatedFormElement.elements().size());
        Assert.assertEquals(element2, updatedFormElement.elements().get(1));
    }

    @Test
    public void testSubmit() {
        // Test when form has no action
        formElement.addElement(new Element(Tag.valueOf("input"), "http://www.example.com/", new Attributes()));
        Connection connection = formElement.submit();
        Assert.assertEquals("http://www.example.com/", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());

        // Test when form has action
        formElement.attr("action", "http://www.example.com/submit");
        connection = formElement.submit();
        Assert.assertEquals("http://www.example.com/submit", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        Element el1 = new Element(Tag.valueOf("input"), "http://www.example.com/", new Attributes());
        el1.attr("name", "input1");
        el1.attr("value", "value1");
        formElement.addElement(el1);

        Element el2 = new Element(Tag.valueOf("textarea"), "http://www.example.com/", new Attributes());
        el2.attr("name", "textarea1");
        el2.text("text1");
        formElement.addElement(el2);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());

        Connection.KeyVal kv1 = formData.get(0);
        Assert.assertEquals("input1", kv1.key());
        Assert.assertEquals("value1", kv1.value());

        Connection.KeyVal kv2 = formData.get(1);
        Assert.assertEquals("textarea1", kv2.key());
        Assert.assertEquals("text1", kv2.value());
    }
}