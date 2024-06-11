package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        form.addElement(element);
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        form.addElement(element);
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        form.removeChild(element);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmit() {
        // Test when "action" attribute is present
        form.attr("action", "/submit");
        Connection connection = form.submit();
        assertEquals("http://www.example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());

        // Test when "action" attribute is not present
        form.removeAttr("action");
        Connection connection2 = form.submit();
        assertEquals("http://www.example.com", connection2.request().url().toString());
        assertEquals(Connection.Method.GET, connection2.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidActionUrl() {
        form.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "inputField");
        inputElement.val("inputValue");
        form.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        form.addElement(selectElement);
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        optionElement1.attr("selected", "");
        optionElement1.val("option1");
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        optionElement2.val("option2");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);

        List<Connection.KeyVal> formData = form.formData();
        assertEquals(2, formData.size());
        assertEquals("inputField", formData.get(0).key());
        assertEquals("inputValue", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("option1", formData.get(1).value());
    }
}