package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseException;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form><input type='text' name='textField' value='hello' /><select name='selectField'><option value='option1'>option1</option><option value='option2' selected>option2</option></select><input type='hidden' name='hiddenField' value='hiddenValue' /></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.select("form").first();
        assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        String html = "<form><input type='text' name='textField1' value='hello1' /><input type='text' name='textField2' value='hello2' /><select name='selectField'><option value='option1'>option1</option><option value='option2' selected>option2</option></select></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.select("form").first();
        Elements elements = formElement.elements();
        assertEquals(3, elements.size());
        assertEquals("textField1", elements.get(0).attr("name"));
        assertEquals("textField2", elements.get(1).attr("name"));
        assertEquals("selectField", elements.get(2).attr("name"));
    }

    @Test
    public void testAddElement() {
        String html = "<form></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.select("form").first();
        Element element = new Element("input");
        element.attr("type", "text");
        element.attr("name", "textField");
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals("textField", elements.get(0).attr("name"));
    }

    @Test
    public void testSubmit() {
        String html = "<form action='http://example.com' method='get'><input type='text' name='textField' value='hello' /></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("GET", connection.method().name());
        assertEquals("http://example.com", connection.url().toString());
    }

    @Test
    public void testFormData() {
        String html = "<form><input type='text' name='textField1' value='hello1' /><input type='text' name='textField2' value='hello2' /><select name='selectField'><option value='option1'>option1</option><option value='option2' selected>option2</option></select></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
        assertEquals("textField1", formData.get(0).key());
        assertEquals("hello1", formData.get(0).value());
        assertEquals("textField2", formData.get(1).key());
        assertEquals("hello2", formData.get(1).value());
        assertEquals("selectField", formData.get(2).key());
        assertEquals("option2", formData.get(2).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNullForm() {
        FormElement formElement = null;
        Connection connection = formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyFormAction() {
        String html = "<form action='' method='get'><input type='text' name='textField' value='hello' /></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyMethod() {
        String html = "<form action='http://example.com' method=''><input type='text' name='textField' value='hello' /></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNullFormData() {
        String html = "<form action='http://example.com' method='get'><input type='text' name='textField' value='hello' /></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = null;
        Connection connection = formElement.submit();
    }
}