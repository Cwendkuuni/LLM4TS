package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ElementFactory;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testCreateFormElement() {
        String html = "<form action=\"http://example.com\"><input name=\"foo\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        assertNotNull(formElement);
    }

    @Test
    public void testFormElementIsSubmittable() {
        String html = "<form action=\"http://example.com\"><input name=\"foo\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        assertTrue(formElement.isFormSubmittable());
    }

    @Test
    public void testFormElementNotSubmittable() {
        String html = "<div><input name=\"foo\"></div>";
        Document doc = Jsoup.parse(html);
        Element element = doc.selectFirst("input");
        assertFalse(element.isFormSubmittable());
    }

    @Test
    public void testFormElementEmptyElements() {
        String html = "<form action=\"http://example.com\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testFormElementWithElements() {
        String html = "<form action=\"http://example.com\"><input name=\"foo\"><input name=\"bar\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        assertEquals(2, formElement.elements().size());
    }

    @Test
    public void testFormElementAddElement() {
        String html = "<form action=\"http://example.com\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        Element input = ElementFactory.parse("<input name=\"foo\">");
        formElement.addElement(input);

        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        String html = "<form action=\"http://example.com\"><input name=\"foo\" value=\"bar\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();
        assertEquals(1, formData.size());
        assertEquals("foo", formData.get(0).key());
        assertEquals("bar", formData.get(0).value());
    }

    @Test
    public void testFormElementSubmitWithMultipleInputsWithSameName() throws Exception {
        String html = "<form action=\"http://example.com\"><input name=\"foo\" value=\"bar1\"><input name=\"foo\" value=\"bar2\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();
        assertEquals(2, formData.size());
        assertEquals("foo", formData.get(0).key());
        assertEquals("bar1", formData.get(0).value());
        assertEquals("foo", formData.get(1).key());
        assertEquals("bar2", formData.get(1).value());
    }

    @Test
    public void testFormElementSubmitWithMultipleSelects() throws Exception {
        String html = "<form action=\"http://example.com\"><select name=\"foo\"><option value=\"bar1\">Option 1</option><option value=\"bar2\">Option 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();
        assertEquals(1, formData.size());
        assertEquals("foo", formData.get(0).key());
        assertTrue(formData.get(0).value().contains("bar1") || formData.get(0).value().contains("bar2"));
    }

    @Test
    public void testFormElementSubmitWithCheckboxes() throws Exception {
        String html = "<form action=\"http://example.com\"><input type=\"checkbox\" name=\"foo\" value=\"bar1\"><input type=\"checkbox\" name=\"foo\" value=\"bar2\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        // submit form with both checkboxes checked
        Connection connection1 = formElement.submit();
        List<Connection.KeyVal> formData1 = connection1.requestBody();
        assertEquals(1, formData1.size());
        assertTrue(formData1.get(0).value().contains("bar1") && formData1.get(0).value().contains("bar2"));

        // submit form with only the first checkbox checked
        Element checkbox1 = doc.selectFirst("input[name=\"foo\"][value=\"bar1\"]");
        checkbox1.attr("checked", "checked");
        Connection connection2 = formElement.submit();
        List<Connection.KeyVal> formData2 = connection2.requestBody();
        assertEquals(1, formData2.size());
        assertEquals("foo", formData2.get(0).key());
        assertEquals("bar1", formData2.get(0).value());
    }

    @Test
    public void testFormElementSubmitWithEmptyForm() throws Exception {
        String html = "<form action=\"http://example.com\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();
        assertEquals(0, formData.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithInvalidFormAction() throws Exception {
        String html = "<form action=\"\"><input name=\"foo\" value=\"bar\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.submit();
    }

}