package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form><input name=\"foo\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        assertNotNull(form);
    }

    @Test
    public void testGetElements() {
        String html = "<form><input name=\"foo\" type=\"text\"><input name=\"bar\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Elements elements = form.elements();
        assertEquals(2, elements.size());
    }

    @Test
    public void testAddElement() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Element input = doc.createElement("input");
        input.attr("name", "foo");
        FormElement addedElement = form.addElement(input);
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertEquals(addedElement, elements.get(0));
    }

    @Test
    public void testSubmit() throws Exception {
        String html = "<form action=\"http://example.com\" method=\"post\">" +
                "<input name=\"foo\" type=\"text\" value=\"bar\">" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Connection connection = form.submit();
        assertEquals("http://example.com", connection.url());
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormDataEmptyForm() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        List<Connection.KeyVal> formData = form.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataTextInput() {
        String html = "<form><input name=\"foo\" type=\"text\" value=\"bar\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("foo", keyVal.key());
        assertEquals("bar", keyVal.value());
    }

    @Test
    public void testFormDataSelectInput() {
        String html = "<form><select name=\"foo\"><option value=\"bar\">Option 1</option>" +
                "<option value=\"baz\">Option 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("foo", keyVal.key());
        assertEquals("bar", keyVal.value());
    }

    @Test
    public void testFormDataCheckboxInput() {
        String html = "<form><input type=\"checkbox\" name=\"foo\" checked></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("foo", keyVal.key());
        assertTrue(keyVal.value().contains("on"));
    }

    @Test
    public void testFormDataMultipleInputs() {
        String html = "<form><input type=\"text\" name=\"foo\" value=\"bar\"><input type=\"text\" name=\"foo\" value=\"baz\"><input type=\"checkbox\" name=\"foo\" checked></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        List<Connection.KeyVal> formData = form.formData();
        Map<String, List<String>> expectedFormData = new HashMap<>();
        expectedFormData.put("foo", Arrays.asList("bar", "baz", "on"));
        assertEquals(expectedFormData, formDataToMap(formData));
    }

    @Test
    public void testFormDataMultipleInputsWithEmptyValues() {
        String html = "<form><input type=\"text\" name=\"foo\"><input type=\"text\" name=\"foo\"><input type=\"checkbox\" name=\"foo\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        List<Connection.KeyVal> formData = form.formData();
        Map<String, List<String>> expectedFormData = new HashMap<>();
        expectedFormData.put("foo", Arrays.asList("", "", ""));
        assertEquals(expectedFormData, formDataToMap(formData));
    }

    private Map<String, List<String>> formDataToMap(List<Connection.KeyVal> formData) {
        Map<String, List<String>> map = new HashMap<>();
        for (Connection.KeyVal keyVal : formData) {
            if (!map.containsKey(keyVal.key())) {
                map.put(keyVal.key(), new ArrayList<>());
            }
            map.get(keyVal.key()).add(keyVal.value());
        }
        return map;
    }
}