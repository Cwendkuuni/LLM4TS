package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.ElementsSelectors;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    @Test
    public void testCreateFormElement() {
        String html = "<form action=\"https://www.example.com/\"><input type=\"text\" name=\"username\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        assertNotNull(formElement);
        assertEquals("form", formElement.tagName());
    }

    @Test
    public void testGetFormControls() {
        String html = "<form action=\"https://www.example.com/\"><input type=\"text\" name=\"username\"><input type=\"password\" name=\"password\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        Elements formControls = formElement.elements();
        assertNotNull(formControls);
        assertTrue(formControls.size() > 0);
        Element textInput = formControls.get(0);
        Element passwordInput = formControls.get(1);
        assertEquals("input", textInput.tagName());
        assertEquals("input", passwordInput.tagName());
        assertEquals("username", textInput.attr("name"));
        assertEquals("password", passwordInput.attr("name"));
    }

    @Test
    public void testAddFormControl() {
        String html = "<form action=\"https://www.example.com/\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        Element textInput = new Parser().parseFromString("<input type=\"text\" name=\"username\">").body();
        formElement.addElement(textInput);
        Elements formControls = formElement.elements();
        assertNotNull(formControls);
        assertTrue(formControls.size() > 0);
        Element addedInput = formControls.get(formControls.size() - 1);
        assertEquals("input", addedInput.tagName());
        assertEquals("username", addedInput.attr("name"));
    }

    @Test
    public void testSubmitForm() throws Exception {
        String html = "<form action=\"https://www.example.com/\"><input type=\"text\" name=\"username\" value=\"test\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.getKey());
        assertEquals("test", keyVal.getValue());
        assertEquals("https://www.example.com/", connection.url().toString());
    }

    @Test
    public void testEmptyFormData() {
        String html = "<form action=\"https://www.example.com/\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormWithNoNameAttribute() {
        String html = "<form action=\"https://www.example.com/\"><input type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormWithMultipleInputsWithSameName() {
        String html = "<form action=\"https://www.example.com/\"><input type=\"text\" name=\"username\"><input type=\"text\" name=\"username\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        Connection.KeyVal keyVal1 = formData.get(0);
        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("username", keyVal1.getKey());
        assertEquals("", keyVal1.getValue());
        assertEquals("username", keyVal2.getKey());
        assertEquals("", keyVal2.getValue());
    }

    @Test
    public void testFormWithDisabledInput() {
        String html = "<form action=\"https://www.example.com/\"><input type=\"text\" name=\"username\" disabled></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitFormWithNullBaseUri() {
        String html = "<form action=\"https://www.example.com/\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitFormWithEmptyAction() {
        String html = "<form action=\"\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        formElement.submit();
    }
}