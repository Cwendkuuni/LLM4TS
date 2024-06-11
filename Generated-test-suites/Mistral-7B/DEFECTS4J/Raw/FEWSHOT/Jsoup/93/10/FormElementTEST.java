package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form action='/test' method='post'><input type='text' name='foo' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        assertNotNull(form);
        assertEquals("form", form.tagName());
        assertEquals("/test", form.absUrl("action"));
        assertEquals("post", form.attr("method"));
    }

    @Test
    public void testEmptyFormElement() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        assertNotNull(form);
        assertEquals("form", form.tagName());
        assertNull(form.absUrl("action"));
    }

    @Test
    public void testFormElementWithNoAction() {
        String html = "<form name='myForm'><input type='text' name='foo' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        assertNotNull(form);
        assertEquals("form", form.tagName());
        assertNull(form.absUrl("action"));
        assertEquals("myForm", form.attr("name"));
    }

    @Test
    public void testFormElementNoElements() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Elements elements = form.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testFormElementOneElement() {
        String html = "<form><input type='text' name='foo' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        Element input = elements.first();
        assertEquals("input", input.tagName());
        assertEquals("text", input.attr("type"));
        assertEquals("foo", input.attr("name"));
    }

    @Test
    public void testFormElementMultipleElements() {
        String html = "<form><input type='text' name='foo' /><input type='text' name='bar' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Elements elements = form.elements();
        assertEquals(2, elements.size());
        Element input1 = elements.get(0);
        Element input2 = elements.get(1);
        assertEquals("text", input1.attr("type"));
        assertEquals("foo", input1.attr("name"));
        assertEquals("text", input2.attr("type"));
        assertEquals("bar", input2.attr("name"));
    }

    @Test
    public void testFormElementWithDisabledInput() {
        String html = "<form><input type='text' name='foo' disabled /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        Element input = elements.first();
        assertTrue(input.hasAttr("disabled"));
    }

    @Test
    public void testFormElementWithSelectElement() {
        String html = "<form><select name='foo'><option value='option1'>Option 1</option><option value='option2'>Option 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        Element select = elements.first();
        assertEquals("select", select.tagName());
        assertEquals("foo", select.attr("name"));
        Elements options = select.select("option[selected]");
        assertEquals(1, options.size());
        Element option = options.first();
        assertEquals("option1", option.val());
    }

    @Test
    public void testFormElementWithCheckboxOrRadioElement() {
        String html = "<form><input type='checkbox' name='foo' checked /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        Element input = elements.first();
        assertEquals("checkbox", input.attr("type"));
        assertEquals("foo", input.attr("name"));
        assertTrue(input.hasAttr("checked"));

        html = "<form><input type='radio' name='foo' checked /></form>";
        doc = Jsoup.parse(html);
        form = (FormElement) doc.select("form").first();
        elements = form.elements();
        assertEquals(1, elements.size());
        input = elements.first();
        assertEquals("radio", input.attr("type"));
        assertEquals("foo", input.attr("name"));
        assertTrue(input.hasAttr("checked"));
    }

    @Test
    public void testFormElementWithMixedInputs() {
        String html = "<form><input type='text' name='foo' /><input type='checkbox' name='bar' checked /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Elements elements = form.elements();
        assertEquals(2, elements.size());
        Element input1 = elements.get(0);
        Element input2 = elements.get(1);
        assertEquals("text", input1.attr("type"));
        assertEquals("foo", input1.attr("name"));
        assertEquals("", input1.val());
        assertEquals("checkbox", input2.attr("type"));
        assertEquals("bar", input2.attr("name"));
        assertTrue(input2.hasAttr("checked"));
    }

    @Test
    public void testFormElementSubmitNullBaseUri() {
        String html = "<form action='/submit' method='post'><input type='text' name='foo' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Connection connection = form.submit();
        assertEquals("/submit", connection.url());
    }

    @Test
    public void testFormElementSubmitWithBaseUri() {
        String html = "<form action='/form' method='post' baseuri='http://test.com/'><input type='text' name='foo' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Connection connection = form.submit();
        assertEquals("http://test.com/form", connection.url());
    }

    @Test
    public void testFormElementSubmitWithPostMethod() {
        String html = "<form action='/form' method='post'><input type='text' name='foo' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Connection connection = form.submit();
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormElementSubmitWithGetMethod() {
        String html = "<form action='/form' method='get'><input type='text' name='foo' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        Connection connection = form.submit();
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testFormElementFormData() {
        String html = "<form action='/form' method='post'><input type='text' name='foo' value='test' /><input type='text' name='bar' value='test2' /></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> data = form.formData();
        assertEquals(2, data.size());
        Connection.KeyVal kv1 = data.get(0);
        Connection.KeyVal kv2 = data.get(1);
        assertEquals("foo", kv1.key());
        assertEquals("test", kv1.value());
        assertEquals("bar", kv2.key());
        assertEquals("test2", kv2.value());
    }
}