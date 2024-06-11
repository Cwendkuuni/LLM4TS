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
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<html><body><form><input type='text' name='foo'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first().formElement();
        assertEquals("form", form.tagName());
        assertNull(form.baseUri());
        Attributes attributes = form.attributes();
        assertEquals(0, attributes.size());
    }

    @Test
    public void testFormElementWithAttributes() {
        String html = "<html><body><form action='/search' method='post'><input type='text' name='foo'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first().formElement();
        assertEquals("form", form.tagName());
        assertEquals("/search", form.attr("action"));
        assertEquals("post", form.attr("method"));
        Attributes attributes = form.attributes();
        assertEquals(2, attributes.size());
    }

    @Test
    public void testFormElementEmpty() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first().formElement();
        assertEquals("form", form.tagName());
        assertNull(form.baseUri());
        Attributes attributes = form.attributes();
        assertEquals(0, attributes.size());
        assertEquals(0, form.elements().size());
    }

    @Test
    public void testFormElementWithElements() {
        String html = "<html><body><form><input type='text' name='foo'><input type='text' name='bar'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first().formElement();
        assertEquals("form", form.tagName());
        assertNull(form.baseUri());
        Attributes attributes = form.attributes();
        assertEquals(0, attributes.size());
        Elements elements = form.elements();
        assertEquals(2, elements.size());
        for (Element el : elements) {
            assertEquals("input", el.tagName());
            assertTrue(el.hasAttr("name"));
        }
    }

    @Test
    public void testFormElementWithDisabledElement() {
        String html = "<html><body><form><input type='text' name='foo' disabled><input type='text' name='bar'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first().formElement();
        assertEquals("form", form.tagName());
        assertNull(form.baseUri());
        Attributes attributes = form.attributes();
        assertEquals(0, attributes.size());
        Elements elements = form.elements();
        assertEquals(2, elements.size());
        Element disabled = elements.get(0);
        assertTrue(disabled.hasAttr("disabled"));
    }

    @Test
    public void testFormElementWithSelectElement() {
        String html = "<html><body><form><select name='foo'><option value='value1'>Option 1</option><option value='value2'>Option 2</option><option selected value='value3'>Option 3</option></select></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first().formElement();
        assertEquals("form", form.tagName());
        assertNull(form.baseUri());
        Attributes attributes = form.attributes();
        assertEquals(0, attributes.size());
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        Element select = elements.get(0);
        assertEquals("select", select.tagName());
        Elements options = select.select("option[selected]");
        assertEquals(1, options.size());
        Element option = options.get(0);
        assertEquals("option", option.tagName());
        assertEquals("Option 3", option.text());
        assertEquals("value3", option.val());
    }

    @Test
    public void testFormElementWithCheckboxOrRadioElement() {
        String html = "<html><body><form><input type='checkbox' name='foo'><input type='radio' name='bar' checked></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first().formElement();
        assertEquals("form", form.tagName());
        assertNull(form.baseUri());
        Attributes attributes = form.attributes();
        assertEquals(0, attributes.size());
        Elements elements = form.elements();
        assertEquals(2, elements.size());
        Element checkbox = elements.get(0);
        assertEquals("input", checkbox.tagName());
        assertEquals("checkbox", checkbox.attr("type"));
        assertFalse(checkbox.hasAttr("checked"));
        Element radio = elements.get(1);
        assertEquals("input", radio.tagName());
        assertEquals("radio", radio.attr("type"));
        assertTrue(radio.hasAttr("checked"));
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        String html = "<html><body><form action='/submit' method='get'><input type='text' name='foo' value='bar'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first().formElement();
        Connection connection = form.submit();
        assertEquals("/submit", connection.url());
        List<Connection.KeyVal> data = form.formData();
        assertEquals(1, data.size());
        Connection.KeyVal keyVal = data.get(0);
        assertEquals("foo", keyVal.key());
        assertEquals("bar", keyVal.value());
    }
}