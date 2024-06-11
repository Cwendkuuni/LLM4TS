package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testCreateFormElement() {
        String baseUri = "http://example.com";
        String html = "<form action=\"/submit\" method=\"post\">" +
                "<input name=\"name1\" type=\"text\" value=\"value1\">" +
                "<select name=\"name2\">" +
                "<option value=\"option1\">Option 1</option>" +
                "<option value=\"option2\" selected>Option 2</option>" +
                "</select>" +
                "<input name=\"name3\" type=\"hidden\" value=\"value3\">" +
                "</form>";

        Document doc = Jsoup.parse(html, baseUri);
        FormElement form = doc.select("form").first();
        assertEquals("form", form.tagName());
        assertEquals(baseUri, form.baseUri());

        List<Attribute> attributes = form.attributes();
        assertEquals("action", attributes.get(0).getName());
        assertEquals("http://example.com/submit", attributes.get(0).getValue());
        assertEquals("method", attributes.get(1).getName());
        assertEquals("post", attributes.get(1).getValue());
    }

    @Test
    public void testFormElementEmpty() {
        String baseUri = "http://example.com";
        String html = "<form></form>";

        Document doc = Jsoup.parse(html, baseUri);
        FormElement form = doc.select("form").first();
        assertEquals("form", form.tagName());
        assertNull(form.baseUri());
        assertTrue(form.elements().isEmpty());
    }

    @Test
    public void testFormElementWithElements() {
        String baseUri = "http://example.com";
        String html = "<form action=\"/submit\" method=\"post\">" +
                "<input name=\"name1\" type=\"text\" value=\"value1\">" +
                "<select name=\"name2\">" +
                "<option value=\"option1\">Option 1</option>" +
                "<option value=\"option2\" selected>Option 2</option>" +
                "</select>" +
                "</form>";

        Document doc = Jsoup.parse(html, baseUri);
        FormElement form = doc.select("form").first();
        assertEquals("form", form.tagName());

        List<Element> formElements = form.elements();
        assertEquals(2, formElements.size());

        Element name1 = formElements.get(0);
        assertEquals("input", name1.tagName());
        assertEquals("name1", name1.attr("name"));
        assertEquals("text", name1.attr("type"));
        assertEquals("value1", name1.val());

        Element name2 = formElements.get(1);
        assertEquals("select", name2.tagName());
        assertEquals("name2", name2.attr("name"));

        List<Element> options = name2.select("option");
        assertEquals(2, options.size());

        Element option1 = options.get(0);
        assertEquals("option", option1.tagName());
        assertEquals("option1", option1.val());
        assertFalse(option1.hasAttr("selected"));

        Element option2 = options.get(1);
        assertEquals("option", option2.tagName());
        assertEquals("option2", option2.val());
        assertTrue(option2.hasAttr("selected"));
    }

    @Test
    public void testFormElementSubmit() {
        String baseUri = "http://example.com";
        String html = "<form action=\"/submit\" method=\"post\">" +
                "<input name=\"name1\" type=\"text\" value=\"value1\">" +
                "<input name=\"name2\" type=\"hidden\" value=\"value2\">" +
                "</form>";

        Document doc = Jsoup.parse(html, baseUri);
        FormElement form = doc.select("form").first();

        Connection connection = form.submit();
        assertEquals("/submit", connection.url().toString());
        assertEquals(Connection.Method.POST, connection.method());

        List<Connection.KeyVal> formData = form.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("name1", keyVal1.key());
        assertEquals("value1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("name2", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithoutBaseUri() {
        String html = "<form action=\"/submit\" method=\"post\">" +
                "<input name=\"name1\" type=\"text\" value=\"value1\">" +
                "</form>";

        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        try {
            form.submit();
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
            throw e;
        }
    }
}