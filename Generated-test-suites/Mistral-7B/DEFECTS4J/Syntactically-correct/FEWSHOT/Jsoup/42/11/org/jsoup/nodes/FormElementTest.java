package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ElementParser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        // Test valid creation
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        assertEquals("form", formElement.tagName());
        assertNull(formElement.elements());

        // Test creation with attributes
        Attributes attributes = new Attributes().put("action", "/test").put("method", "POST");
        Tag formTagWithAttributes = new Tag("form", attributes);
        FormElement formElementWithAttributes = new FormElement(formTagWithAttributes, "", attributes);
        assertEquals("/test", formElementWithAttributes.attr("action"));
        assertEquals("POST", formElementWithAttributes.attr("method"));
        assertSame(attributes, formElementWithAttributes.attributes());
    }

    @Test
    public void testFormElementElements() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "inputName"));
        formElement.addElement(inputElement);
        Elements expectedElements = new Elements(inputElement);
        Elements actualElements = formElement.elements();
        assertEquals(expectedElements, actualElements);
        assertEquals(1, actualElements.size());
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        String html = "<html><body><form action=\"/test\" method=\"post\">" +
                "<input type=\"text\" name=\"inputName\">" +
                "<input type=\"text\" name=\"inputName2\" value=\"testValue\">" +
                "</form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        Connection connection = formElement.submit();

        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedFormData = new ArrayList<>(Arrays.asList(
                new Connection.KeyVal("inputName", ""),
                new Connection.KeyVal("inputName2", "testValue")
        ));

        assertEquals(expectedFormData, formData);
        assertEquals("/test", connection.url().toString());
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormElementSubmitWithEmptyForm() throws Exception {
        String html = "<html><body><form action=\"/test\" method=\"post\"></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        Connection connection = formElement.submit();

        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
        assertEquals("/test", connection.url().toString());
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormElementSubmitWithNoActionAttribute() throws Exception {
        String html = "<html><body><form method=\"post\">" +
                "<input type=\"text\" name=\"inputName\">" +
                "</form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        try {
            formElement.submit();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormElementFormDataForSelectElement() {
        String html = "<html><body><form>" +
                "<select name=\"selectName\">" +
                "<option value=\"option1\">Option 1</option>" +
                "<option value=\"option2\" selected>" +
                "Option 2</option>" +
                "</select>" +
                "</form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedFormData = new ArrayList<>(Arrays.asList(
                new Connection.KeyVal("selectName", "option2")
        ));

        assertEquals(expectedFormData, formData);
    }

    @Test
    public void testFormElementFormDataForCheckboxOrRadioElement() {
        String html = "<html><body><form>" +
                "<input type=\"checkbox\" name=\"checkboxName\" checked>" +
                "</form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedFormData = new ArrayList<>(Arrays.asList(
                new Connection.KeyVal("checkboxName", "")
        ));

        assertEquals(expectedFormData, formData);

        html = "<html><body><form>" +
                "<input type=\"radio\" name=\"radioName\" checked>" +
                "</form></body></html>";
        doc = Jsoup.parse(html);
        formElement = doc.selectFirst("form");

        expectedFormData = new ArrayList<>(Arrays.asList(
                new Connection.KeyVal("radioName", "")
        ));

        assertEquals(expectedFormData, formElement.formData());
    }

    @Test
    public void testFormElementFormDataForEmptyForm() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormElementEquals() {
        Tag formTag1 = new Tag("form");
        FormElement formElement1 = new FormElement(formTag1, "", new Attributes());

        Tag formTag2 = new Tag("form");
        FormElement formElement2 = new FormElement(formTag2, "", new Attributes());

        assertEquals(formElement1, formElement1);
        assertEquals(formElement1, formElement1);

        assertFalse(formElement1.equals(formElement2));
        assertFalse(formElement2.equals(formElement1));

        FormElement formElement3 = new FormElement(formTag1, "", new Attributes());
        assertEquals(formElement1, formElement3);
        assertEquals(formElement3, formElement1);
    }
}