package org.jsoup.nodes;

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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

    private static final String HTML_CONTENT = "<html><form action=\"https://example.com\" method=\"post\"><input name=\"username\" type=\"text\"/><input name=\"password\" type=\"password\"/><select name=\"country\"><option value=\"US\">United States</option><option value=\"CA\" selected>Canada</option></select><input type=\"submit\" name=\"submit\"/></form></html>";
    private Element formElement;

    @Before
    public void setUp() {
        Document document = Jsoup.parseBodyFragment(HTML_CONTENT);
        formElement = document.selectFirst("form");
    }

    @Test
    public void testFormElementConstructor() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        assertNotNull(formElement);
    }

    @Test
    public void testElementsMethod() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(4, elements.size());
    }

    @Test
    public void testAddElementMethod() {
        Element input = new Element("input");
        input.attr("name", "newElement");
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.addElement(input);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(input, elements.first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithoutBaseUri() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithoutActionAttribute() {
        formElement.setAttribute("action", null);
        formElement.submit();
    }

    @Test
    public void testFormDataMethodWhenFormHasNoControls() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataMethodWhenFormHasTextInput() {
        Element input = formElement.selectFirst("input[name=username]");
        Elements elements = formElement.elements();
        assertEquals(4, elements.size());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals(input.val(), keyVal.value());
    }

    @Test
    public void testFormDataMethodWhenFormHasSelectInput() {
        Element select = formElement.selectFirst("select[name=country]");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("country", keyVal.key());
        assertEquals("CA", keyVal.value());
    }

    @Test
    public void testFormDataMethodWhenFormHasCheckboxInput() {
        Element checkbox = formElement.selectFirst("input[name=password]");
        checkbox.attr("checked", "checked");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("password", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithInvalidMethod() {
        formElement.setAttribute("method", "INVALID");
        formElement.submit();
    }
}