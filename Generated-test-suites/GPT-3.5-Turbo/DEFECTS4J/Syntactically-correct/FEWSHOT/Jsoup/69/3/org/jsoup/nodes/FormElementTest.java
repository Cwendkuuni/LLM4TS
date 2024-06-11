
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testAddAndGetElements() {
        Element element1 = new Element(Tag.valueOf("input"), "");
        Element element2 = new Element(Tag.valueOf("textarea"), "");

        formElement.addElement(element1);
        formElement.addElement(element2);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
    }

    @Test
    public void testSubmitWithActionAttribute() {
        Element element1 = new Element(Tag.valueOf("input"), "");
        element1.attr("name", "username");
        element1.attr("value", "john");

        Element element2 = new Element(Tag.valueOf("input"), "");
        element2.attr("name", "password");
        element2.attr("value", "pass123");

        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.attr("action", "/login");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://www.example.com/login", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> requestParams = connection.request().data();
        assertEquals(formData.size(), requestParams.size());
        for (int i = 0; i < formData.size(); i++) {
            assertEquals(formData.get(i).key(), requestParams.get(i).key());
            assertEquals(formData.get(i).value(), requestParams.get(i).value());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionAttribute() {
        formElement.attr("method", "POST");
        try {
            formElement.submit();
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFormDataWithNoFormSubmittableElement() {
        Element element1 = new Element(Tag.valueOf("div"), "");
        Element element2 = new Element(Tag.valueOf("span"), "");

        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithDisabledFormInput() {
        Element element1 = new Element(Tag.valueOf("input"), "");
        element1.attr("disabled", "");

        formElement.addElement(element1);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithSelectElement() {
        Element element = new Element(Tag.valueOf("select"), "");
        Element option1 = new Element(Tag.valueOf("option"), "");
        option1.attr("selected", "");
        option1.attr("value", "option1");
        Element option2 = new Element(Tag.valueOf("option"), "");
        option2.attr("value", "option2");

        element.appendChild(option1);
        element.appendChild(option2);

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("option1", formData.get(0).value());
    }

    @Test
    public void testFormDataWithEmptySelectedOption() {
        Element element = new Element(Tag.valueOf("select"), "");
        Element option1 = new Element(Tag.valueOf("option"), "");
        Element option2 = new Element(Tag.valueOf("option"), "");
        option2.attr("value", "option2");

        element.appendChild(option1);
        element.appendChild(option2);

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("option2", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxInput() {
        Element element = new Element(Tag.valueOf("input"), "");
        element.attr("type", "checkbox");
        element.attr("name", "checkbox");
        element.attr("value", "value");
        element.attr("checked", "");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("value", formData.get(0).value());
    }

    @Test
    public void testFormDataWithUncheckedCheckboxInput() {
        Element element = new Element(Tag.valueOf("input"), "");
        element.attr("type", "checkbox");
        element.attr("name", "checkbox");
        element.attr("value", "value");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithRadioInput() {
        Element element = new Element(Tag.valueOf("input"), "");
        element.attr("type", "radio");
        element.attr("name", "radio");
        element.attr("value", "value");
        element.attr("checked", "");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("value", formData.get(0).value());
    }

    @Test
    public void testFormDataWithUncheckedRadioInput() {
        Element element = new Element(Tag.valueOf("input"), "");
        element.attr("type", "radio");
        element.attr("name", "radio");
        element.attr("value", "value");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithValueAttribute() {
        Element element = new Element(Tag.valueOf("input"), "");
        element.attr("name", "name");
        element.attr("value", "John");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("John", formData.get(0).value());
    }

    @Test
    public void testFormDataWithoutValueAttribute() {
        Element element = new Element(Tag.valueOf("input"), "");
        element.attr("name", "name");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("", formData.get(0).value());
    }
}
