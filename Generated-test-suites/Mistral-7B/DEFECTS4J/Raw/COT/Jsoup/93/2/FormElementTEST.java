package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for FormElement
 */
public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertNotNull(formElement);
        assertEquals(tag.tagName(), formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testGetFormControls() {
        String html = "<html><body><form><input type=\"text\" name=\"input1\"></form></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        Elements formControls = formElement.elements();
        assertEquals(1, formControls.size());
        assertEquals("input", formControls.get(0).tagName());
    }

    @Test
    public void testAddElement() {
        String html = "<html><body><form><input type=\"text\" name=\"input1\"></form></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "input2");
        formElement.addElement(inputElement);
        Elements formControls = formElement.elements();
        assertEquals(2, formControls.size());
        assertEquals("input", formControls.get(1).tagName());
    }

    @Test
    public void testRemoveElement() {
        String html = "<html><body><form><input type=\"text\" name=\"input1\"></form><input type=\"text\" name=\"input2\"></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        Element inputElementToRemove = (Element) doc.select("input:last-child").first();
        formElement.removeChild(inputElementToRemove);
        Elements formControls = formElement.elements();
        assertEquals(1, formControls.size());
        assertFalse(formElement.contains(inputElementToRemove));
    }

    @Test
    public void testSubmitNullAction() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        try {
            formElement.submit();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubmitEmptyFormData() {
        String html = "<html><body><form><input type=\"text\" name=\"input1\"></form></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testSubmitFormWithOneControl() {
        String html = "<html><body><form><input type=\"text\" name=\"input1\" value=\"value1\"></form></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("input1", keyVal.key());
        assertEquals("value1", keyVal.value());
    }

    @Test
    public void testSubmitFormWithMultipleControls() {
        String html = "<html><body><form><input type=\"text\" name=\"input1\" value=\"value1\"><input type=\"text\" name=\"input2\" value=\"value2\"></form></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        for (Connection.KeyVal keyVal : formData) {
            String key = keyVal.key();
            String value = keyVal.value();
            if ("input1".equals(key)) {
                assertEquals("input1", key);
                assertEquals("value1", value);
            } else if ("input2".equals(key)) {
                assertEquals("input2", key);
                assertEquals("value2", value);
            } else {
                fail("Unexpected key: " + key);
            }
        }
    }

    @Test
    public void testSubmitFormWithDisabledControl() {
        String html = "<html><body><form><input type=\"text\" name=\"input1\" value=\"value1\" disabled><input type=\"text\" name=\"input2\" value=\"value2\"></form></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        for (Connection.KeyVal keyVal : formData) {
            String key = keyVal.key();
            String value = keyVal.value();
            if ("input2".equals(key)) {
                assertEquals("input2", key);
                assertEquals("value2", value);
            } else {
                fail("Unexpected key: " + key);
            }
        }
    }

    @Test
    public void testSubmitFormWithSelectElement() {
        String html = "<html><body><form><select name=\"selectElement\"><option value=\"option1\">Option 1</option><option value=\"option2\" selected><option value=\"option3\">Option 3</option></select></form></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("selectElement", keyVal.key());
        assertEquals("option2", keyVal.value());
    }

    @Test
    public void testSubmitFormWithCheckboxOrRadioElement() {
        String html = "<html><body><form><input type=\"checkbox\" name=\"checkboxElement\" value=\"checkboxValue\"><input type=\"text\" name=\"input1\" value=\"inputValue1\"></form></body></html>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        html = "<html><body><form><input type=\"checkbox\" name=\"checkboxElement\" value=\"checkboxValue\" checked><input type=\"text\" name=\"input1\" value=\"inputValue1\"></form></body></html>";
        doc = Jsoup.parse(html, new ParseSetup().parser(new FormatterParser()));
        formElement = (FormElement) doc.select("form").first();
        formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("checkboxElement", keyVal.key());
        assertEquals("checkboxValue", keyVal.value());
    }
}