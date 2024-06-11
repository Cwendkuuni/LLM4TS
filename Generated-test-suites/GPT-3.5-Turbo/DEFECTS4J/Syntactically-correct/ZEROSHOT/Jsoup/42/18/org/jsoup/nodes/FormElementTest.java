package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        FormElement returnedFormElement = formElement.addElement(element);
        assertEquals(formElement, returnedFormElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertSame(element, elements.first());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://www.example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.attr("value", "testuser");

        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        selectElement.attr("name", "country");
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        optionElement1.attr("value", "US");
        optionElement1.attr("selected", "");
        selectElement.appendChild(optionElement1);
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        optionElement2.attr("value", "UK");
        selectElement.appendChild(optionElement2);

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("username", keyVal1.key());
        assertEquals("testuser", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("country", keyVal2.key());
        assertEquals("US", keyVal2.value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        assertTrue(formElement.equals(formElement1));

        FormElement formElement2 = new FormElement(Tag.valueOf("div"), "http://www.example.com", new Attributes());
        assertFalse(formElement.equals(formElement2));

        FormElement formElement3 = new FormElement(Tag.valueOf("form"), "http://www.example.org", new Attributes());
        assertFalse(formElement.equals(formElement3));
    }
}