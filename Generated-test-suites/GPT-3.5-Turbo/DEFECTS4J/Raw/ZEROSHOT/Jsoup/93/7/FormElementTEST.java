package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement returnedElement = formElement.addElement(element);
        assertEquals(formElement, returnedElement);
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);

        formElement.removeChild(element);

        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() {
        FormElement formElementWithEmptyAction = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElementWithEmptyAction.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "username").put("value", "test"));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("test", formData.get(0).value());
    }
}