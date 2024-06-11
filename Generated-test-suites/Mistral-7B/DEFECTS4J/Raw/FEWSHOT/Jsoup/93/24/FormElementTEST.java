package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ElementFactory;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        String html = "<form><input name=\"foo\" type=\"text\" value=\"bar\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals("form", formElement.tag().name());
        assertNull(formElement.attr("action"));
        assertEquals(1, formElement.elements().size());
        assertEquals("input", formElement.elements().first().tag().name());
        assertEquals("foo", formElement.elements().first().attr("name"));
        assertEquals("text", formElement.elements().first().attr("type"));
        assertEquals("bar", formElement.elements().first().val());
    }

    @Test
    public void testFormElementWithAction() {
        String html = "<form action=\"http://example.com\"><input name=\"foo\" type=\"text\" value=\"bar\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals("http://example.com", formElement.attr("action"));
    }

    @Test
    public void testFormElementSubmission() throws Exception {
        String html = "<form action=\"http://example.com\"><input name=\"foo\" type=\"text\" value=\"bar\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("foo", formData.get(0).key());
        assertEquals("bar", formData.get(0).value());
        assertEquals("POST", connection.method().name());
        assertEquals("http://example.com", connection.url());
    }

    @Test
    public void testFormElementWithDisabledInput() {
        String html = "<form><input name=\"foo\" type=\"text\" value=\"bar\" disabled></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(1, formElement.elements().size());
        assertEquals("input", formElement.elements().first().tag().name());
        assertFalse(formElement.elements().first().hasAttr("checked"));
        assertFalse(formElement.elements().first().hasAttr("selected"));
        assertFalse(formElement.elements().first().hasAttr("disabled"));
    }

    @Test
    public void testFormElementWithMultipleInputs() {
        String html = "<form><input name=\"foo\" type=\"text\" value=\"foo\"><input name=\"bar\" type=\"text\" value=\"bar\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(2, formElement.elements().size());
        assertEquals("input", formElement.elements().get(0).tag().name());
        assertEquals("foo", formElement.elements().get(0).attr("name"));
        assertEquals("foo", formElement.elements().get(0).val());
        assertEquals("input", formElement.elements().get(1).tag().name());
        assertEquals("bar", formElement.elements().get(1).attr("name"));
        assertEquals("bar", formElement.elements().get(1).val());
    }

    @Test
    public void testFormElementWithSelect() {
        String html = "<form><select name=\"foo\"><option value=\"option1\">Option 1</option><option value=\"option2\" selected>Option 2</option></select></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(1, formElement.elements().size());
        assertEquals("select", formElement.elements().first().tag().name());
        assertEquals("foo", formElement.elements().first().attr("name"));
        Elements options = formElement.elements().first().select("option[selected]");
        assertEquals(1, options.size());
        assertEquals("option2", options.get(0).tag().name());
        assertEquals("Option 2", options.get(0).text());
        assertEquals("option1", formElement.elements().first().select("option:not([selected])").get(0).val());
    }

    @Test
    public void testFormElementWithCheckbox() {
        String html = "<form><input name=\"foo\" type=\"checkbox\" value=\"bar\" checked></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(1, formElement.elements().size());
        assertEquals("input", formElement.elements().first().tag().name());
        assertEquals("checkbox", formElement.elements().first().attr("type"));
        assertTrue(formElement.elements().first().hasAttr("checked"));
    }

    @Test
    public void testFormElementWithRadioButton() {
        String html = "<form><input name=\"foo\" type=\"radio\" value=\"bar\" checked><input name=\"foo\" type=\"radio\" value=\"baz\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(2, formElement.elements().size());
        assertEquals("input", formElement.elements().get(0).tag().name());
        assertEquals("radio", formElement.elements().get(0).attr("type"));
        assertTrue(formElement.elements().get(0).hasAttr("checked"));
        assertEquals("input", formElement.elements().get(1).tag().name());
        assertEquals("radio", formElement.elements().get(1).attr("type"));
        assertFalse(formElement.elements().get(1).hasAttr("checked"));
    }

    @Test
    public void testFormElementWithMultipleSelects() {
        String html = "<form><select name=\"foo\">" +
                "<option value=\"option1\">Option 1</option>" +
                "<option value=\"option2\" selected>Option 2</option>" +
                "</select>" +
                "<select name=\"bar\">" +
                "<option value=\"option3\">Option 3</option>" +
                "<option value=\"option4\" selected>Option 4</option>" +
                "</select></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(2, formElement.elements().size());
        assertEquals(2, formElement.elements().get(0).select("option[selected]").size());
        assertEquals(2, formElement.elements().get(1).select("option[selected]").size());
    }

    @Test
    public void testFormElementWithEmptyInputs() {
        String html = "<form><input name=\"foo\" type=\"text\" value=\"\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(1, formElement.elements().size());
        assertEquals("input", formElement.elements().first().tag().name());
        assertEquals("text", formElement.elements().first().attr("type"));
        assertEquals("", formElement.elements().first().val());
    }

    @Test
    public void testFormElementWithEmptySelect() {
        String html = "<form><select name=\"foo\"></select></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(1, formElement.elements().size());
        assertEquals("select", formElement.elements().first().tag().name());
        assertEquals("foo", formElement.elements().first().attr("name"));
        assertTrue(formElement.elements().first().select("option:not([selected])").isEmpty());
    }

    @Test
    public void testFormElementWithEmptyCheckbox() {
        String html = "<form><input name=\"foo\" type=\"checkbox\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(1, formElement.elements().size());
        assertEquals("input", formElement.elements().first().tag().name());
        assertEquals("checkbox", formElement.elements().first().attr("type"));
        assertFalse(formElement.elements().first().hasAttr("checked"));
    }

    @Test
    public void testFormElementWithEmptyRadioButton() {
        String html = "<form><input name=\"foo\" type=\"radio\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(1, formElement.elements().size());
        assertEquals("input", formElement.elements().first().tag().name());
        assertEquals("radio", formElement.elements().first().attr("type"));
        assertFalse(formElement.elements().first().hasAttr("checked"));
    }

    @Test
    public void testFormElementWithFormSubmittableInputs() {
        String html = "<form><input name=\"foo\" type=\"text\" value=\"foo\"><input name=\"bar\" type=\"text\" value=\"bar\"><input name=\"baz\" type=\"submit\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(3, formElement.elements().size());
        assertEquals("input", formElement.elements().get(0).tag().name());
        assertEquals("text", formElement.elements().get(0).attr("type"));
        assertEquals("foo", formElement.elements().get(0).attr("name"));
        assertEquals("foo", formElement.elements().get(0).val());
        assertEquals("input", formElement.elements().get(1).tag().name());
        assertEquals("text", formElement.elements().get(1).attr("type"));
        assertEquals("bar", formElement.elements().get(1).attr("name"));
        assertEquals("bar", formElement.elements().get(1).val());
        assertEquals("input", formElement.elements().get(2).tag().name());
        assertEquals("submit", formElement.elements().get(2).attr("type"));
        assertEquals("", formElement.elements().get(2).val());
    }

    @Test
    public void testFormElementWithFormNonSubmittableInputs() {
        String html = "<form><script type=\"text/javascript\">document.forms[0].elements[0].disabled = true;</script><input name=\"foo\" type=\"text\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(1, formElement.elements().size());
        assertEquals("input", formElement.elements().first().tag().name());
        assertEquals("text", formElement.elements().first().attr("type"));
        assertEquals("foo", formElement.elements().first().attr("name"));
        assertFalse(formElement.elements().first().hasAttr("checked"));
        assertFalse(formElement.elements().first().hasAttr("disabled"));
    }

    @Test
    public void testFormElementWithFormDisabled() {
        String html = "<form disabled><input name=\"foo\" type=\"text\" value=\"foo\"></form>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        assertEquals(1, formElement.elements().size());
        assertEquals("input", formElement.elements().first().tag().name());
        assertEquals("text", formElement.elements().first().attr("type"));
        assertEquals("foo", formElement.elements().first().attr("name"));
        assertEquals("foo", formElement.elements().first().val());
        assertTrue(formElement.hasAttr("disabled"));
    }

    @Test
    public void testFormElementWithFormNestedInAnotherForm() {
        String html = "<form><form><input name=\"foo\" type=\"text\"></form></form>";
        Document document = Jsoup.parse(html);
        FormElement outerFormElement = document.selectFirst("form");
        FormElement innerFormElement = outerFormElement.selectFirst("form");

        assertEquals(1, outerFormElement.elements().size());
        assertEquals(1, innerFormElement.elements().size());
        assertEquals("input", innerFormElement.elements().first().tag().name());
        assertEquals("text", innerFormElement.elements().first().attr("type"));
        assertEquals("foo", innerFormElement.elements().first().attr("name"));
        assertEquals("", innerFormElement.elements().first().val());
    }

    @Test
    public void testFormElementWithFormNestedInAnotherFormAndSubmission() throws Exception {
        String html = "<form><form name=\"innerForm\"><input name=\"foo\" type=\"text\"></form></form>";
        Document document = Jsoup.parse(html);
        FormElement outerFormElement = document.selectFirst("form");
        FormElement innerFormElement = outerFormElement.selectFirst("form");

        Connection connection = outerFormElement.submit();
        List<Connection.KeyVal> formData = outerFormElement.formData();

        assertEquals(1, formData.size());
        assertEquals("", formData.get(0).value());
        assertEquals("POST", connection.method().name());
        assertEquals("", connection.url());
    }
}