package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        String html = "<form><input name=\"input1\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        assertNotNull(formElement);
    }

    @Test
    public void testEmptyFormElement() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testFormElementWithSingleInput() {
        String html = "<form><input name=\"input1\" type=\"text\" value=\"value1\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        assertEquals(1, formElement.elements().size());
        Element element = formElement.elements().first();
        assertEquals("input", element.tagName());
        assertEquals("input1", element.attr("name"));
        assertEquals("value1", element.val());
    }

    @Test
    public void testFormElementWithMultipleInputs() {
        String html = "<form><input name=\"input1\" type=\"text\" value=\"value1\"><input name=\"input2\" type=\"text\" value=\"value2\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        assertEquals(2, formElement.elements().size());
    }

    @Test
    public void testFormElementSubmission() throws Exception {
        String html = "<form action=\"http://example.com\" method=\"post\">" +
                "<input name=\"input1\" type=\"text\" value=\"value1\">" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("input1", formData.get(0).key);
        assertEquals("value1", formData.get(0).value);

        Connection con = formElement.submit();
        assertEquals("http://example.com", con.url());
        assertEquals(Connection.Method.POST, con.method());
    }

    @Test
    public void testFormElementSubmissionWithMultipleInputs() throws Exception {
        String html = "<form action=\"http://example.com\" method=\"post\">" +
                "<input name=\"input1\" type=\"text\" value=\"value1\"><input name=\"input2\" type=\"text\" value=\"value2\">" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("input1", formData.get(0).key);
        assertEquals("value1", formData.get(0).value);
        assertEquals("input2", formData.get(1).key);
        assertEquals("value2", formData.get(1).value);

        Connection con = formElement.submit();
        assertEquals("http://example.com", con.url());
        assertEquals(Connection.Method.POST, con.method());
    }

    @Test
    public void testFormElementSubmissionWithSelectElement() throws Exception {
        String html = "<form action=\"http://example.com\" method=\"post\">" +
                "<select name=\"selectElement\">" +
                "<option value=\"option1\">Option 1</option>" +
                "<option value=\"option2\" selected>Option 2</option>" +
                "<option value=\"option3\">Option 3</option>" +
                "</select>" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("selectElement", formData.get(0).key);
        assertEquals("option2", formData.get(0).value);

        Connection con = formElement.submit();
        assertEquals("http://example.com", con.url());
        assertEquals(Connection.Method.POST, con.method());
    }

    @Test
    public void testFormElementSubmissionWithCheckboxOrRadioButton() throws Exception {
        String html = "<form action=\"http://example.com\" method=\"post\">" +
                "<input type=\"checkbox\" name=\"checkbox\" value=\"checkboxValue\">" +
                "<input type=\"text\" name=\"textInput\" value=\"textValue\">" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        doc = Jsoup.parse(html + "<input type=\"checkbox\" name=\"checkbox\" checked value=\"checkboxValue\">");
        formElement = doc.body().selectFirst("form");
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("checkbox", formData.get(0).key);
        assertEquals("checkboxValue", formData.get(0).value);

        doc = Jsoup.parse(html + "<input type=\"radio\" name=\"radio\" checked value=\"radioValue\">");
        formElement = doc.body().selectFirst("form");
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("radio", formData.get(0).key);
        assertEquals("radioValue", formData.get(0).value);
    }

    @Test
    public void testFormElementSubmissionWithNoInputs() throws Exception {
        String html = "<form action=\"http://example.com\" method=\"post\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        Connection con = formElement.submit();
        assertEquals("http://example.com", con.url());
        assertEquals(Connection.Method.POST, con.method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmissionWithoutAction() throws Exception {
        String html = "<form method=\"post\">" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.body().selectFirst("form");
        formElement.submit();
    }
}