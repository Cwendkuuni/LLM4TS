package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);
        Assert.assertSame(formElement, result);
        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(element, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmitWithGetMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("method", "get");
        formElement.attr("action", "https://www.example.com");
        Connection connection = formElement.submit();
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
        Assert.assertEquals("https://www.example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmitWithPostMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("method", "post");
        formElement.attr("action", "https://www.example.com");
        Connection connection = formElement.submit();
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
        Assert.assertEquals("https://www.example.com", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("jsmith");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("username", keyVal.key());
        Assert.assertEquals("jsmith", keyVal.value());
    }
}