package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @org.junit.Test
    public void testFormElementCreation() {
        String html = "<form action='/' method='get'><input type='text' name='username' /></form>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.selectFirst("form");
        FormElement formElementObject = (FormElement) formElement;
        assertNotNull(formElementObject);
    }

    @org.junit.Test
    public void testGetElements() {
        String html = "<form action='/' method='get'><input type='text' name='username' />" +
                "<input type='text' name='password' /></form>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.selectFirst("form");
        FormElement formElementObject = (FormElement) formElement;
        Elements elements = formElementObject.elements();
        assertEquals(2, elements.size());
    }

    @org.junit.Test
    public void testAddElement() {
        String html = "<form action='/' method='get'></form>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.selectFirst("form");
        FormElement formElementObject = (FormElement) formElement;
        Element inputElement = new Parser().parseInput("input", "<input type='text' name='username' />").first();
        formElementObject.addElement(inputElement);
        Elements elements = formElementObject.elements();
        assertEquals(1, elements.size());
        assertEquals("input", elements.get(0).tagName());
    }

    @org.junit.Test
    public void testRemoveChild() {
        String html = "<form action='/' method='get'><input type='text' name='username' /><input type='text' name='password' /></form>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.selectFirst("form");
        FormElement formElementObject = (FormElement) formElement;
        Element inputElementToRemove = formElementObject.elements().get(1);
        formElementObject.removeChild(inputElementToRemove);
        Elements elements = formElementObject.elements();
        assertEquals(1, elements.size());
        assertFalse(formElementObject.elements().contains(inputElementToRemove));
    }

    @org.junit.Test
    public void testSubmit() {
        String html = "<form action='/' method='post'><input type='text' name='username' value='test' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElementObject = (FormElement) doc.selectFirst("form");
        Connection connection = formElementObject.submit();
        List<Connection.KeyVal> formData = formElementObject.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("test", formData.get(0).value());
        assertEquals("POST", connection.method().name());
        assertEquals("/", connection.url().path());
    }

    @org.junit.Test
    public void testFormDataSelectElement() {
        String html = "<form action='/' method='post'><select name='colors'><option value='red' selected>Red</option>" +
                "<option value='green'>Green</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElementObject = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElementObject.formData();
        assertEquals(1, formData.size());
        assertEquals("colors", formData.get(0).key());
        assertEquals("red", formData.get(0).value());
    }

    @org.junit.Test
    public void testFormDataCheckboxOrRadioElement() {
        String html = "<form action='/' method='post'><input type='checkbox' name='agree' checked /></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElementObject = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElementObject.formData();
        assertEquals(1, formData.size());
        assertEquals("agree", formData.get(0).key());
        assertEquals("on", formData.get(0).value());

        html = "<form action='/' method='post'><input type='radio' name='gender' checked /></form>";
        doc = Jsoup.parse(html);
        formElementObject = (FormElement) doc.selectFirst("form");
        formData = formElementObject.formData();
        assertEquals(1, formData.size());
        assertEquals("gender", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }
}