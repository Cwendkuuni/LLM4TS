package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

    private Element formElement;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        String html = "<html><head></head><body>" +
                "<form action='/submit' method='post'>" +
                "<input type='text' name='username'>" +
                "<select name='status'>" +
                "<option value='single'>Single</option>" +
                "<option value='married' selected>Married</option>" +
                "</select>" +
                "<input type='checkbox' name='agree' checked>" +
                "<input type='submit' value='Submit'>" +
                "</form>" +
                "</body></html>";

        Document document = Jsoup.parse(html);
        formElement = document.select("form").first();
    }

    @After
    public void tearDown() throws Exception {
        if (formElement != null) {
            formElement.remove();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testFormElementCreation() {
        assertEquals("form", formElement.tagName());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(4, elements.size());
        assertEquals("input", elements.get(0).tagName());
        assertEquals("select", elements.get(1).tagName());
        assertEquals("input", elements.get(2).tagName());
        assertEquals("input", elements.get(3).tagName());
    }

    @Test
    public void testAddElement() throws Exception {
        Element inputElement = new Element("input");
        inputElement.setAttribute("type", "hidden");
        inputElement.setAttribute("name", "newElement");

        FormElement formElement = (FormElement) Jsoup.parse("<form></form>", "").first();
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals("input", elements.get(0).tagName());
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveChildWithoutElement() {
        formElement.removeChild(new Element("div"));
    }

    @Test
    public void testSubmit() throws Exception {
        connection = formElement.submit();
        assertEquals("/submit", connection.url());
        assertEquals(Connection.Method.POST, connection.method());

        List<Connection.KeyVal> keyValues = connection.data();
        assertEquals(3, keyValues.size());

        Connection.KeyVal keyVal = keyValues.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("", keyVal.value());

        keyVal = keyValues.get(1);
        assertEquals("status", keyVal.key());
        assertEquals("married", keyVal.value());

        keyVal = keyValues.get(2);
        assertEquals("agree", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataWhenInputIsDisabled() throws Exception {
        formElement.getElementsByTag("input").first().setAttribute("disabled", "disabled");
        List<Connection.KeyVal> keyValues = formElement.submit().data();
        assertEquals(2, keyValues.size());
    }

    @Test
    public void testFormDataForSelectElement() throws Exception {
        List<Connection.KeyVal> keyValues = formElement.submit().data();
        assertEquals(3, keyValues.size());

        Connection.KeyVal keyVal = keyValues.get(1);
        assertEquals("status", keyVal.key());
        assertEquals("married", keyVal.value());
    }

    @Test
    public void testFormDataForCheckboxOrRadioElement() throws Exception {
        Element checkboxElement = formElement.getElementsByTag("input").get(2);
        checkboxElement.setAttribute("checked", "checked");

        List<Connection.KeyVal> keyValues = formElement.submit().data();
        assertEquals(4, keyValues.size());

        Connection.KeyVal keyVal = keyValues.get(3);
        assertEquals("agree", keyVal.key());
        assertEquals("on", keyVal.value());
    }
}