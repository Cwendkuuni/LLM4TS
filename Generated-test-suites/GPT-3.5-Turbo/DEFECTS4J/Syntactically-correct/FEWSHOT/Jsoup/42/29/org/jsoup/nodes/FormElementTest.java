package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
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
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();

        String expectedAction = "https://www.example.com";
        assertEquals(expectedAction, connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("testuser");

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement1.attr("value", "option1");
        optionElement1.attr("selected", "selected");
        Element optionElement2 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement2.attr("value", "option2");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("username", "testuser")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("select", "option1")));
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        assertTrue(formElement.equals(formElement2));

        formElement2.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        assertFalse(formElement.equals(formElement2));
    }
}