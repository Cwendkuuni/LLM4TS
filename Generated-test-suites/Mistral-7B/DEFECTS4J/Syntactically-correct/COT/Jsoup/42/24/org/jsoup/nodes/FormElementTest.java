package org.jsoup.nodes.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form><input type=\"text\" name=\"name\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        assertEquals("form", formElement.tagName());
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testElementsMethod() {
        String html = "<form><input type=\"text\" name=\"name1\"><input type=\"text\" name=\"name2\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        assertEquals("input", elements.get(0).tagName());
        assertEquals("input", elements.get(1).tagName());
    }

    @Test
    public void testAddElementMethod() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        Element inputElement = new Parser().parseInput("input", null).tag();
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals("input", elements.get(0).tagName());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddElementMethodWithNegativeIndex() {
        String html = "<form><input type=\"text\" name=\"name\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        formElement.addElement(-1, new Parser().parseInput("input", null).tag());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodNoAction() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        formElement.submit();
    }

    @Test
    public void testSubmitMethodValid() {
        String html = "<form action=\"http://example.com\" method=\"get\"><input type=\"text\" name=\"name\" value=\"John\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        Connection connection = formElement.submit();
        assertEquals("GET", connection.method());
        assertEquals("http://example.com", connection.url().toString());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("John", keyVal.value());
    }

    @Test
    public void testSubmitMethodPost() {
        String html = "<form action=\"http://example.com\" method=\"post\"><input type=\"text\" name=\"name\" value=\"John\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        Connection connection = formElement.submit();
        assertEquals("POST", connection.method());
        assertEquals("http://example.com", connection.url().toString());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("John", keyVal.value());
    }

    @Test
    public void testFormDataEmptyForm() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithSelectElement() {
        String html = "<form><select name=\"name\"><option value=\"John\">John</option><option value=\"Jane\">Jane</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("John", keyVal.value());
    }

    @Test
    public void testFormDataWithCheckboxElement() {
        String html = "<form><input type=\"checkbox\" name=\"name\" value=\"John\">John</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        formElement.getElementsByTag("input").first().attr("checked", "checked");
        formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("John", keyVal.value());
    }

    @Test
    public void testFormDataWithMultipleElements() {
        String html = "<form><input type=\"text\" name=\"name1\" value=\"John\"><input type=\"text\" name=\"name2\" value=\"Jane\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("name1", keyVal1.key());
        assertEquals("John", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("name2", keyVal2.key());
        assertEquals("Jane", keyVal2.value());
    }

    @Test(expected = IllegalStateException.class)
    public void testSubmitMethodAfterSubmitted() {
        String html = "<form action=\"http://example.com\"><input type=\"text\" name=\"name\" value=\"John\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().tagName("form").first();
        formElement.submit();
        formElement.submit();
    }

    @Test
    public void testEqualsMethod() {
        String html1 = "<form action=\"http://example.com\"><input type=\"text\" name=\"name1\" value=\"John\"></form>";
        String html2 = "<form action=\"http://example.com\"><input type=\"text\" name=\"name2\" value=\"John\"></form>";
        Document doc1 = Jsoup.parse(html1);
        Document doc2 = Jsoup.parse(html2);
        FormElement formElement1 = (FormElement) doc1.body().tagName("form").first();
        FormElement formElement2 = (FormElement) doc2.body().tagName("form").first();
        assertFalse(formElement1.equals(formElement2));

        FormElement formElement3 = (FormElement) doc1.body().tagName("form").first();
        assertTrue(formElement1.equals(formElement3));
    }
}