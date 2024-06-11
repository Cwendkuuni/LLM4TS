package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

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
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();

        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());

        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();

        assertEquals("", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals(new ArrayList<>(), connection.request().data());
    }

    @Test
    public void testSubmitWithAction() {
        formElement.attr("action", "https://example.com");

        Connection connection = formElement.submit();

        assertEquals("https://example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmitWithMethod() {
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("JohnDoe");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("JohnDoe", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckbox() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "agreement");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("agreement", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormDataWithRadio() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "gender");
        radioElement.attr("checked", "checked");
        radioElement.attr("value", "male");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("gender", formData.get(0).key());
        assertEquals("male", formData.get(0).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "", new Attributes());

        assertFalse(formElement.equals(formElement2));
    }
}