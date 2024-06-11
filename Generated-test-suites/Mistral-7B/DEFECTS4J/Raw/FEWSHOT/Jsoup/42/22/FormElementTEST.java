package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for FormElement.
 */
public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        // Test empty form element creation
        String html = "<form></form>";
        Document doc = Jsoup.parse(html, Parse.htmlParser());
        FormElement formElement = (FormElement) doc.select("form").first();
        assertEquals("form", formElement.tagName());
        assertNull(formElement.attributes().get("action"));

        // Test form element creation with attributes
        html = "<form action='/submit' method='post'></form>";
        doc = Jsoup.parse(html, Parse.htmlParser());
        formElement = (FormElement) doc.select("form").first();
        assertEquals("/submit", formElement.attributes().get("action"));
        assertEquals("post", formElement.attributes().get("method"));
    }

    @Test
    public void testFormElementElements() {
        String html = "<form><input type='text' name='username'><input type='password' name='password'></form>";
        Document doc = Jsoup.parse(html, Parse.htmlParser());
        FormElement formElement = (FormElement) doc.select("form").first();

        Elements elements = formElement.elements();
        assertEquals(2, elements.size());

        Element usernameInput = elements.get(0);
        assertEquals("input", usernameInput.tagName());
        assertEquals("text", usernameInput.attr("type"));
        assertEquals("username", usernameInput.attr("name"));

        Element passwordInput = elements.get(1);
        assertEquals("input", passwordInput.tagName());
        assertEquals("password", passwordInput.attr("type"));
        assertEquals("password", passwordInput.attr("name"));
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        String html = "<form action='/submit' method='post'><input type='text' name='username' value='test'><input type='password' name='password' value='password'></form>";
        Document doc = Jsoup.parse(html, Parse.htmlParser());
        FormElement formElement = (FormElement) doc.select("form").first();

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());

        Connection.KeyVal usernameKeyVal = formData.get(0);
        assertEquals("username", usernameKeyVal.key);
        assertEquals("test", usernameKeyVal.value);

        Connection.KeyVal passwordKeyVal = formData.get(1);
        assertEquals("password", passwordKeyVal.key);
        assertEquals("password", passwordKeyVal.value);

        assertEquals("/submit", connection.url());
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormElementWithSelect() throws Exception {
        String html = "<form><select name='fruit'><option value='apple'>Apple</option><option value='banana' selected>Banana</option></select></form>";
        Document doc = Jsoup.parse(html, Parse.htmlParser());
        FormElement formElement = (FormElement) doc.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());

        Connection.KeyVal fruitKeyVal = formData.get(0);
        assertEquals("fruit", fruitKeyVal.key);
        assertEquals("banana", fruitKeyVal.value);
    }

    @Test
    public void testFormElementWithCheckboxes() throws Exception {
        String html = "<form><input type='checkbox' name='fruit' value='apple'>Apple<input type='checkbox' name='fruit' value='banana'>Banana</form>";
        Document doc = Jsoup.parse(html, Parse.htmlParser());
        FormElement formElement = (FormElement) doc.select("form").first();

        // No checkboxes are checked by default
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        // Check the first checkbox
        Element appleCheckbox = formElement.select("input[value='apple']").first();
        appleCheckbox.attr("checked", "checked");

        formData = formElement.formData();
        assertEquals(1, formData.size());

        Connection.KeyVal fruitKeyVal = formData.get(0);
        assertEquals("fruit", fruitKeyVal.key);
        assertEquals("apple", fruitKeyVal.value);

        // Check the second checkbox
        Element bananaCheckbox = formElement.select("input[value='banana']").first();
        bananaCheckbox.attr("checked", "checked");

        formData = formElement.formData();
        assertEquals(2, formData.size());

        fruitKeyVal = formData.get(1);
        assertEquals("fruit", fruitKeyVal.key);
        assertEquals("banana", fruitKeyVal.value);
    }
}