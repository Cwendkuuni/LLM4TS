package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testCreateFormElement() {
        String html = "<form><input type=\"text\" name=\"foo\"></form>";
        Parser parser = new Parser.Builder().addSetting(ParseSetting.OMIT_TAG_COMMENTS, true).build();
        Document doc = Jsoup.parse(html, parser);
        Element formElement = doc.selectFirst("form");
        FormElement form = (FormElement) formElement;
        assertNotNull(form);
        assertNotNull(form.elements());
    }

    @Test
    public void testSubmitFormWithGETMethod() throws Exception {
        String html = "<form action=\"http://test.com\" method=\"get\"><input type=\"text\" name=\"foo\" value=\"bar\"></form>";
        Parser parser = new Parser.Builder().build();
        Document doc = Jsoup.parse(html, parser);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        assertEquals(Connection.Method.GET, connection.method());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("foo", keyVal.key());
        assertEquals("bar", keyVal.value());
    }

    @Test
    public void testSubmitFormWithPOSTMethod() throws Exception {
        String html = "<form action=\"http://test.com\" method=\"post\"><input type=\"text\" name=\"foo\" value=\"bar\"></form>";
        Parser parser = new Parser.Builder().build();
        Document doc = Jsoup.parse(html, parser);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        assertEquals(Connection.Method.POST, connection.method());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("foo", keyVal.key());
        assertEquals("bar", keyVal.value());
    }

    @Test
    public void testFormWithEmptyFormData() throws Exception {
        String html = "<form><input type=\"text\" name=\"foo\"></form>";
        Parser parser = new Parser.Builder().build();
        Document doc = Jsoup.parse(html, parser);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormWithMultipleControls() throws Exception {
        String html = "<form><input type=\"text\" name=\"foo\" value=\"fooValue\"></form>" +
                "<form><input type=\"text\" name=\"bar\" value=\"barValue\"><input type=\"hidden\" name=\"hiddenValue\" value=\"hiddenValue\"></form>";
        Parser parser = new Parser.Builder().build();
        Document doc = Jsoup.parse(html, parser);
        FormElement firstForm = (FormElement) doc.selectFirst("form");
        FormElement secondForm = (FormElement) doc.selectFirst("form:last-child");
        List<Connection.KeyVal> firstFormData = firstForm.formData();
        List<Connection.KeyVal> secondFormData = secondForm.formData();
        assertEquals(1, firstFormData.size());
        Connection.KeyVal firstFormKeyVal = firstFormData.get(0);
        assertEquals("foo", firstFormKeyVal.key());
        assertEquals("fooValue", firstFormKeyVal.value());
        assertEquals(3, secondFormData.size());
        Connection.KeyVal firstSecondFormKeyVal = secondFormData.get(0);
        assertEquals("bar", firstSecondFormKeyVal.key());
        assertEquals("barValue", firstSecondFormKeyVal.value());
        Connection.KeyVal secondSecondFormKeyVal = secondFormData.get(1);
        assertEquals("hiddenValue", secondSecondFormKeyVal.key());
        assertEquals("hiddenValue", secondSecondFormKeyVal.value());
    }

    @Test
    public void testFormWithSelectElement() throws Exception {
        String html = "<form><label for=\"foo\"><input type=\"hidden\" name=\"foo\" id=\"foo\"><select name=\"bar\"><option selected value=\"barValue1\">Option 1</option><option value=\"barValue2\">Option 2</option></select></label></form>";
        Parser parser = new Parser.Builder().build();
        Document doc = Jsoup.parse(html, parser);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal formKeyVal = formData.get(0);
        assertEquals("bar", formKeyVal.key());
        assertEquals("barValue1", formKeyVal.value());
    }

    @Test
    public void testFormWithCheckboxOrRadioButtons() throws Exception {
        String html = "<form><input type=\"checkbox\" name=\"foo\" value=\"fooValue\"><label for=\"foo\">Checkbox</label></form>" +
                "<form><input type=\"radio\" name=\"bar\" value=\"barValue1\" id=\"bar1\"><label for=\"bar1\">Option 1</label>" +
                "<input type=\"radio\" name=\"bar\" value=\"barValue2\" id=\"bar2\"><label for=\"bar2\">Option 2</label></form>";
        Parser parser = new Parser.Builder().build();
        Document doc = Jsoup.parse(html, parser);
        FormElement firstForm = (FormElement) doc.selectFirst("form");
        FormElement secondForm = (FormElement) doc.selectFirst("form:last-child");
        List<Connection.KeyVal> firstFormData = firstForm.formData();
        List<Connection.KeyVal> secondFormData = secondForm.formData();
        assertEquals(0, firstFormData.size());
        assertEquals(0, secondFormData.size());

        // Test with checked checkbox/radio
        html = "<form><input type=\"checkbox\" name=\"foo\" value=\"fooValue\" checked><label for=\"foo\">Checkbox</label></form>";
        doc = Jsoup.parse(html, parser);
        firstForm = (FormElement) doc.selectFirst("form");
        formData = firstForm.formData();
        assertEquals(1, formData.size());
        formKeyVal = formData.get(0);
        assertEquals("foo", formKeyVal.key());
        assertEquals("fooValue", formKeyVal.value());

        // Test with checked radio
        html = "<form><input type=\"radio\" name=\"bar\" value=\"barValue1\" checked id=\"bar1\"><label for=\"bar1\">Option 1</label>" +
                "<input type=\"radio\" name=\"bar\" value=\"barValue2\" id=\"bar2\"><label for=\"bar2\">Option 2</label></form>";
        doc = Jsoup.parse(html, parser);
        secondForm = (FormElement) doc.selectFirst("form:last-child");
        formData = secondForm.formData();
        assertEquals(1, formData.size());
        formKeyVal = formData.get(0);
        assertEquals("bar", formKeyVal.key());
        assertEquals("barValue1", formKeyVal.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormWithNoActionURL() throws Exception {
        String html = "<form><input type=\"text\" name=\"foo\"></form>";
        Parser parser = new Parser.Builder().build();
        Document doc = Jsoup.parse(html, parser);
        FormElement form = (FormElement) doc.selectFirst("form");
        form.submit();
    }
}