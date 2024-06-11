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
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ElementParser;
import org.jsoup.select.Elements;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form action=\"/form\" method=\"post\"><input name=\"name\" type=\"text\" /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        assertEquals("form", form.tagName());
        assertEquals("/form", form.absUrl("action"));
        assertEquals("POST", form.attr("method"));
        assertNull(form.attr("action"));
        assertEquals("name", form.elements().first().attr("name"));
    }

    @Test
    public void testFormElementWithActionAttr() {
        String html = "<form action=\"/form\" method=\"post\"><input name=\"name\" type=\"text\" /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        assertEquals("/form", form.attr("action"));
        assertEquals("/form", form.absUrl("action"));
    }

    @Test
    public void testFormElementWithMethodAttr() {
        String html = "<form action=\"/form\"><input name=\"name\" type=\"text\" /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        assertNull(form.attr("method"));
        assertEquals("post", form.attr("method").toUpperCase());
    }

    @Test
    public void testFormElementWithNoActionOrMethod() {
        String html = "<form><input name=\"name\" type=\"text\" /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        assertNull(form.attr("action"));
        assertNull(form.attr("method"));
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        String html = "<form action=\"/form\" method=\"post\"><input name=\"name\" type=\"text\" value=\"John\" /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();
        assertEquals(1, data.size());
        assertEquals("name", data.get(0).key);
        assertEquals("John", data.get(0).value);
        assertEquals("John\r\n", connection.requestBody());
    }

    @Test
    public void testFormElementSubmitWithNoName() throws Exception {
        String html = "<form action=\"/form\" method=\"post\"><input type=\"text\" /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();
        assertTrue(data.isEmpty());
        assertNull(connection.requestBody());
    }

    @Test
    public void testFormElementSubmitWithMultipleInputs() throws Exception {
        String html = "<form action=\"/form\" method=\"post\"><input name=\"name1\" type=\"text\" value=\"John\" /><input name=\"name2\" type=\"text\" value=\"Doe\" /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();
        assertEquals(2, data.size());
        assertEquals("name1", data.get(0).key);
        assertEquals("John", data.get(0).value);
        assertEquals("name2", data.get(1).key);
        assertEquals("Doe", data.get(1).value);
        assertEquals("name1=John&name2=Doe\r\n", connection.requestBody());
    }

    @Test
    public void testFormElementSubmitWithSelect() throws Exception {
        String html = "<form action=\"/form\" method=\"post\"><select name=\"name\"><option value=\"John\">John</option><option value=\"Doe\">Doe</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();
        assertEquals(1, data.size());
        assertEquals("name", data.get(0).key);
        // The value depends on which option is selected
    }

    @Test
    public void testFormElementSubmitWithCheckbox() throws Exception {
        String html = "<form action=\"/form\" method=\"post\"><input type=\"checkbox\" name=\"name\" value=\"John\" checked /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();
        assertEquals(1, data.size());
        assertEquals("name", data.get(0).key);
        assertEquals("John", data.get(0).value);
    }

    @Test
    public void testFormElementSubmitWithRadio() throws Exception {
        String html = "<form action=\"/form\" method=\"post\"><input type=\"radio\" name=\"name\" value=\"John\" checked /><input type=\"radio\" name=\"name\" value=\"Doe\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();
        assertEquals(1, data.size());
        assertEquals("name", data.get(0).key);
        assertEquals("John", data.get(0).value);
    }

    @Test
    public void testFormElementSubmitWithDisabledInput() throws Exception {
        String html = "<form action=\"/form\" method=\"post\"><input name=\"name\" type=\"text\" value=\"John\" disabled /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();
        assertTrue(data.isEmpty());
        assertNull(connection.requestBody());
    }

    @Test
    public void testFormElementAddElement() {
        String html = "<form action=\"/form\" method=\"post\"><input name=\"name1\" type=\"text\" /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Element input = new ElementParser().parseInput("input", new Attributes().put("type", "text").put("name", "name2"));
        form.addElement(input);
        assertEquals(2, form.elements().size());
        assertEquals("name2", form.elements().get(1).attr("name"));
    }

    @Test
    public void testFormElementRemoveChild() {
        String html = "<form action=\"/form\" method=\"post\"><input name=\"name1\" type=\"text\" /><input name=\"name2\" type=\"text\" /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Element input2 = form.elements().get(1);
        form.removeChild(input2);
        assertEquals(1, form.elements().size());
        assertFalse(form.elements().contains(input2));
    }
}