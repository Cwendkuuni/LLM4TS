package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://example.com/";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "");
        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("https://example.com/", connection.request().url().toString());

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(formData, connection.request().data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "");
        element1.attr("name", "input1");
        element1.attr("value", "value1");

        Element element2 = new Element(Tag.valueOf("select"), "");
        element2.attr("name", "select1");
        element2.append("<option selected value='option1'>Option 1</option>");

        Element element3 = new Element(Tag.valueOf("input"), "");
        element3.attr("name", "input2");
        element3.attr("type", "checkbox");
        element3.attr("checked", "");

        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());

        assertEquals("input1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());

        assertEquals("select1", formData.get(1).key());
        assertEquals("option1", formData.get(1).value());

        assertEquals("input2", formData.get(2).key());
        assertEquals("", formData.get(2).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "https://example.com/", new Attributes());
        assertEquals(formElement, formElement2);

        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        FormElement formElement3 = new FormElement(Tag.valueOf("form"), "https://example.com/", attributes);
        assertNotEquals(formElement, formElement3);
    }
}