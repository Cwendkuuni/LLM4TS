package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form action='/' method='post'><input type='text' name='username' value='test'><input type='text' name='password' value='password'><select name='country'><option value='US'>United States</option><option value='CA'>Canada</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        Elements elements = form.elements();

        assertEquals("form", form.tagName());
        assertEquals(3, elements.size());

        Element usernameInput = elements.get(0);
        assertEquals("input", usernameInput.tagName());
        assertEquals("username", usernameInput.attr("name"));
        assertEquals("test", usernameInput.val());

        Element passwordInput = elements.get(1);
        assertEquals("input", passwordInput.tagName());
        assertEquals("password", passwordInput.attr("name"));
        assertTrue(passwordInput.attr("type").contains("password"));
        assertNull(passwordInput.val());

        Element countrySelect = elements.get(2);
        assertEquals("select", countrySelect.tagName());
        assertEquals("country", countrySelect.attr("name"));
        Elements options = countrySelect.select("option");
        assertEquals(2, options.size());
        assertSame(options.get(0), countrySelect.select("option[value='US']").first());
    }

    @Test
    public void testFormElementAddElement() {
        String html = "<form action='/' method='post'><input type='text' name='username' value='test'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        Element newInput = new Elements(Tag.INPUT).attr("type", "password").attr("name", "password").attr("value", "password").html();

        form.addElement(newInput);

        Elements elements = form.elements();
        assertEquals(2, elements.size());
        Element passwordInput = elements.get(1);
        assertEquals("password", passwordInput.attr("name"));
        assertTrue(passwordInput.attr("type").contains("password"));
        assertEquals("password", passwordInput.val());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitNoBaseUri() {
        String html = "<form action='/' method='post'>...</form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        form.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitInvalidAction() {
        String html = "<form action='' method='post'>...</form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        form.submit();
    }

    @Test
    public void testFormElementSubmit() {
        String html = "<form action='http://example.com/' method='post'><input type='text' name='username' value='test'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        Connection connection = form.submit();

        assertEquals("http://example.com/", connection.url().toString());
        assertEquals("POST", connection.method().name());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("test", keyVal.value());
    }

    @Test
    public void testFormElementSubmitWithEmptyForm() {
        String html = "<form action='http://example.com/' method='post'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        Connection connection = form.submit();

        assertEquals("http://example.com/", connection.url().toString());
        assertEquals("POST", connection.method().name());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementSubmitWithDisabledInput() {
        String html = "<form action='http://example.com/' method='post'><input type='text' name='username' value='test' disabled><input type='text' name='password' value='password'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        Connection connection = form.submit();

        assertEquals("http://example.com/", connection.url().toString());
        assertEquals("POST", connection.method().name());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("password", keyVal.key());
        assertEquals("password", keyVal.value());
    }

    @Test
    public void testFormElementSubmitWithSelectInput() {
        String html = "<form action='http://example.com/' method='post'><input type='text' name='username' value='test'><select name='country'><option value='US'>United States</option><option value='CA'>Canada</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        Connection connection = form.submit();

        assertEquals("http://example.com/", connection.url().toString());
        assertEquals("POST", connection.method().name());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("country", keyVal.key());
        assertEquals("US", keyVal.value());
    }

    @Test
    public void testFormElementSubmitWithCheckboxInput() {
        String html = "<form action='http://example.com/' method='post'><input type='text' name='username' value='test'><input type='checkbox' name='agree' value='agree'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        form.addElement(new Elements(Tag.INPUT).attr("type", "checkbox").attr("name", "agree").attr("value", "disagree").html());

        Connection connection = form.submit();

        assertEquals("http://example.com/", connection.url().toString());
        assertEquals("POST", connection.method().name());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("agree", keyVal.key());
        assertEquals("disagree", keyVal.value());
    }

    @Test
    public void testFormElementSubmitWithMultipleCheckboxInput() {
        String html = "<form action='http://example.com/' method='post'><input type='text' name='username' value='test'><input type='checkbox' name='color' value='red'>Red<input type='checkbox' name='color' value='green'>Green</form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        form.addElement(new Elements(Tag.INPUT).attr("type", "checkbox").attr("name", "color").attr("value", "blue").html());

        Connection connection = form.submit();

        assertEquals("http://example.com/", connection.url().toString());
        assertEquals("POST", connection.method().name());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(3, formData.size());
        Connection.KeyVal redKeyVal = null;
        Connection.KeyVal greenKeyVal = null;
        Connection.KeyVal blueKeyVal = null;
        for (Connection.KeyVal keyVal : formData) {
            if ("color".equals(keyVal.key())) {
                switch (keyVal.value()) {
                    case "red":
                        redKeyVal = keyVal;
                        break;
                    case "green":
                        greenKeyVal = keyVal;
                        break;
                    case "blue":
                        blueKeyVal = keyVal;
                        break;
                    default:
                        break;
                }
            }
        }
        assertNotNull(redKeyVal);
        assertNull(greenKeyVal); // assumes the first checkbox is checked by default
        assertNull(blueKeyVal); // newly added checkbox not checked
    }
}