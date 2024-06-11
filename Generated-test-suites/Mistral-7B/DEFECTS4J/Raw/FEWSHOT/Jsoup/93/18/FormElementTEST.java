package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        String html = "<form><input name=\"name1\" type=\"text\"><input name=\"name2\" type=\"text\"><input type=\"submit\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");
        assertEquals("form", formElement.tag().name());

        List<String> attributeNames = Arrays.asList("action", "method");
        for (String attributeName : attributeNames) {
            assertNull(formElement.attr(attributeName));
        }
    }

    @Test
    public void testElements() {
        String html = "<form><input name=\"name1\" type=\"text\"><input name=\"name2\" type=\"text\"><input type=\"submit\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");

        Element input1 = formElement.selectFirst("input[name=name1]");
        Element input2 = formElement.selectFirst("input[name=name2]");
        Element submitButton = formElement.selectFirst("input[type=submit]");

        List<Element> elements = formElement.elements();
        assertEquals(3, elements.size());
        assertTrue(elements.contains(input1));
        assertTrue(elements.contains(input2));
        assertTrue(elements.contains(submitButton));
    }

    @Test
    public void testAddElement() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");

        Element inputElement = new Element("input");
        inputElement.attr("name", "name1");
        inputElement.attr("type", "text");
        formElement.addElement(inputElement);

        List<Element> elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testRemoveChild() {
        String html = "<form><input name=\"name1\" type=\"text\"><input name=\"name2\" type=\"text\"><input type=\"submit\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");

        Element input1 = formElement.selectFirst("input[name=name1]");
        formElement.removeChild(input1);

        List<Element> elements = formElement.elements();
        assertEquals(2, elements.size());
        assertFalse(elements.contains(input1));
    }

    @Test
    public void testSubmit() {
        String html = "<form action=\"http://example.com/submit\" method=\"post\"><input name=\"name\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");

        Connection connection = formElement.submit();
        assertEquals("http://example.com/submit", connection.url().toString());
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        String html = "<form action=\"\" method=\"post\"><input name=\"name\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");
        formElement.submit();
    }

    @Test
    public void testFormDataEmptyForm() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSimpleForm() {
        String html = "<form action=\"http://example.com/submit\" method=\"post\"><input name=\"name1\" type=\"text\" value=\"value1\"><input name=\"name2\" type=\"text\" value=\"value2\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("name1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("name2", formData.get(1).key());
        assertEquals("value2", formData.get(1).value());
    }

    @Test
    public void testFormDataSelectElement() {
        String html = "<form action=\"http://example.com/submit\" method=\"post\"><select name=\"name\"><option value=\"value1\">Option 1</option><option selected value=\"value2\">Option 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("value2", formData.get(0).value());
    }

    @Test
    public void testFormDataCheckboxAndRadioElements() {
        String html = "<form action=\"http://example.com/submit\" method=\"post\"><input name=\"name\" type=\"checkbox\" checked><input name=\"name\" type=\"radio\" checked></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }
}