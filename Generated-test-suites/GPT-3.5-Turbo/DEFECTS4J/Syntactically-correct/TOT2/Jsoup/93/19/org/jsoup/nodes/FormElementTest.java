package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com/form", new Attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        
        Elements elements = formElement.elements();
        assertEquals(1, elements.size()); // check if element is added successfully
        assertEquals(element, elements.get(0)); // check if correct element is added
    }

    @Test
    public void testSubmitWithNoActionAttribute() {
        Connection connection = formElement.submit();
        assertEquals("http://example.com/form", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithMalformedActionAttribute() {
        formElement.attr("action", "malformed-url");
        formElement.submit(); // should throw IllegalArgumentException
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "jsmith");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size()); // check if key value pair is populated
        assertEquals("username", formData.get(0).key());
        assertEquals("jsmith", formData.get(0).value());
    }
}