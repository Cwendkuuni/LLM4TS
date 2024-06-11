package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testGetElements() {
        String html = "<html><body><form><input type=\"text\" name=\"name\"></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.select("form").first();

        Elements expectedElements = doc.select("input[name=name]");
        Elements actualElements = formElement.elements();

        assertEquals(expectedElements.size(), actualElements.size());
        for (Element expectedElement : expectedElements) {
            Element actualElement = actualElements.get(expectedElements.indexOf(expectedElement));
            assertSame(expectedElement, actualElement);
        }
    }

    @Test
    public void testAddElement() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.select("form").first();

        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "name");

        formElement.addElement(inputElement);

        Elements actualElements = formElement.elements();
        Element expectedElement = inputElement;

        assertEquals(1, actualElements.size());
        assertSame(expectedElement, actualElements.get(0));
    }

    @Test
    public void testSubmitWithEmptyForm() {
        String html = "<html><body><form action=\"/submit\"></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.select("form").first();

        try {
            Connection connection = formElement.submit();
            assertNull(connection);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSubmitWithFormData() {
        String html = "<html><body><form action=\"/submit\"><input type=\"text\" name=\"name\" value=\"John\"></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.select("form").first();

        Connection connection = formElement.submit();

        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("name", "John"));

        List<Connection.KeyVal> actualData = connection.requestBody();

        assertEquals(expectedData.size(), actualData.size());
        for (Connection.KeyVal expectedDataEntry : expectedData) {
            Connection.KeyVal actualDataEntry = actualData.get(actualData.indexOf(expectedDataEntry));
            assertEquals(expectedDataEntry.key, actualDataEntry.key);
            assertEquals(expectedDataEntry.value, actualDataEntry.value);
        }
    }

    @Test
    public void testSubmitWithSelectElement() {
        String html = "<html><body><form action=\"/submit\"><select name=\"select\"><option value=\"Option1\">Option1</option>" +
                "<option value=\"Option2\" selected>Option2</option></select></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.select("form").first();

        Connection connection = formElement.submit();

        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("select", "Option2"));

        List<Connection.KeyVal> actualData = connection.requestBody();

        assertEquals(expectedData.size(), actualData.size());
        for (Connection.KeyVal expectedDataEntry : expectedData) {
            Connection.KeyVal actualDataEntry = actualData.get(actualData.indexOf(expectedDataEntry));
            assertEquals(expectedDataEntry.key, actualDataEntry.key);
            assertEquals(expectedDataEntry.value, actualDataEntry.value);
        }
    }

    @Test
    public void testSubmitWithCheckboxOrRadioElement() {
        String html = "<html><body><form action=\"/submit\"><input type=\"checkbox\" name=\"checkbox\" value=\"Value\"></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.select("form").first();
        formElement.select("input[name=checkbox]").first().attr("checked", "checked");

        Connection connection = formElement.submit();

        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("checkbox", "Value"));

        List<Connection.KeyVal> actualData = connection.requestBody();

        assertEquals(expectedData.size(), actualData.size());
        for (Connection.KeyVal expectedDataEntry : expectedData) {
            Connection.KeyVal actualDataEntry = actualData.get(actualData.indexOf(expectedDataEntry));
            assertEquals(expectedDataEntry.key, actualDataEntry.key);
            assertEquals(expectedDataEntry.value, actualDataEntry.value);
        }
    }
}