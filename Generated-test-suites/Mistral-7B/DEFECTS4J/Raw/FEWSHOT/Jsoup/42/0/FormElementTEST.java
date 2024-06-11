package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
    }

    @Test
    public void testGetElements() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html = "<form><input name=\"name1\" value=\"value1\"><input name=\"name2\" value=\"value2\"></form>";
        Document doc = parser.parse(html);
        Element formElement = doc.select("form").first();
        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        assertEquals("input", elements.get(0).tagName());
        assertEquals("input", elements.get(1).tagName());
    }

    @Test
    public void testAddElement() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html = "<form></form>";
        Document doc = parser.parse(html);
        Element formElement = doc.select("form").first();
        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "name1");
        inputElement.attr("value", "value1");
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals("input", elements.get(0).tagName());
        assertEquals("text", elements.get(0).attr("type"));
        assertEquals("name1", elements.get(0).attr("name"));
        assertEquals("value1", elements.get(0).attr("value"));
    }

    @Test
    public void testSubmitNullFormAction() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html = "<form></form>";
        Document doc = parser.parse(html);
        Element formElement = doc.select("form").first();
        try {
            formElement.submit();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubmitFormWithAction() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html = "<form action=\"http://example.com\"></form>";
        Document doc = parser.parse(html);
        Element formElement = doc.select("form").first();
        Connection connection = formElement.submit();
        assertEquals("http://example.com", connection.url());
    }

    @Test
    public void testFormDataEmptyForm() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html = "<form></form>";
        Document doc = parser.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataSelectElement() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html = "<form><select name=\"name1\"><option value=\"option1\">Option 1</option><option selected value=\"option2\">Option 2</option></select></form>";
        Document doc = parser.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("option2", formData.get(0).value());
        assertEquals("name1", formData.get(0).key());
    }

    @Test
    public void testFormDataCheckboxElement() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html = "<form><input type=\"checkbox\" name=\"name1\" value=\"value1\"></form>";
        Document doc = parser.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertFalse(formData.isEmpty());
        // checkbox is not checked by default
        assertEquals(0, formData.size());

        formElement.getElementsByTag("input").first().attr("checked", "checked");
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("value1", formData.get(0).value());
        assertEquals("name1", formData.get(0).key());
    }

    @Test
    public void testFormDataRadioElement() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html = "<form><input type=\"radio\" name=\"name1\" value=\"option1\"> <input type=\"radio\" name=\"name1\" value=\"option2\"> <input type=\"radio\" name=\"name1\" value=\"option3\" checked></form>";
        Document doc = parser.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("option3", formData.get(0).value());
        assertEquals("name1", formData.get(0).key());
    }

    @Test
    public void testFormDataMultipleCheckboxes() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html = "<form><input type=\"checkbox\" name=\"name1\" value=\"option1\"> <input type=\"checkbox\" name=\"name1\" value=\"option2\"> <input type=\"checkbox\" name=\"name1\" value=\"option3\" checked></form>";
        Document doc = parser.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
        assertTrue(Arrays.asList(formData.toArray()).contains(HttpConnection.KeyVal.create("option1", "name1")));
        assertTrue(Arrays.asList(formData.toArray()).contains(HttpConnection.KeyVal.create("option3", "name1")));
        assertFalse(Arrays.asList(formData.toArray()).contains(HttpConnection.KeyVal.create("option2", "name1")));
    }

    @Test
    public void testFormEquals() {
        Parser parser = new Parser.MalformedHtmlParser();
        String html1 = "<form action=\"http://example.com\"><input name=\"name1\" value=\"value1\"></form>";
        String html2 = "<form action=\"http://example.com\"><input name=\"name2\" value=\"value2\"></form>";
        Document doc1 = parser.parse(html1);
        Document doc2 = parser.parse(html2);
        Element formElement1 = doc1.select("form").first();
        Element formElement2 = doc2.select("form").first();
        assertFalse(formElement1.equals(formElement2));

        html1 = "<form action=\"http://example.com\"><input name=\"name1\" value=\"value1\"><input name=\"name2\" value=\"value2\"></form>";
        doc1 = parser.parse(html1);
        formElement1 = doc1.select("form").first();

        html2 = "<form action=\"http://example.com\"><input name=\"name1\" value=\"value1\"></form>";
        doc2 = parser.parse(html2);
        formElement2 = doc2.select("form").first();

        assertFalse(formElement1.equals(formElement2));

        html2 = "<form action=\"http://example.com\"><input name=\"name1\" value=\"value1\"><input name=\"name2\" value=\"value2\"></form>";
        doc2 = parser.parse(html2);
        formElement2 = doc2.select("form").first();

        assertTrue(formElement1.equals(formElement2));
    }
}