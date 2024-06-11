package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form action='/submit' method='post'><input name='foo' type='text' value='bar'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        assertEquals("form", form.tagName());
        assertEquals("/submit", form.absUrl("action"));
        assertEquals("POST", form.attr("method"));
        Element input = form.elements().first();
        assertEquals("input", input.tagName());
        assertEquals("text", input.attr("type"));
        assertEquals("bar", input.val());
    }

    @Test
    public void testFormElementMethods() {
        String html = "<form action='/submit' method='post'><input name='foo' type='text' value='bar'><input name='baz' type='text' value='qux'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        assertEquals(2, form.elements().size());
        Element input1 = form.elements().get(0);
        Element input2 = form.elements().get(1);

        assertSame(form, form.addElement(input1));
        assertSame(form, form.addElement(input2));

        assertEquals(2, form.elements().size());

        form.removeChild(input1);
        assertEquals(1, form.elements().size());
        assertFalse(form.elements().contains(input1));
        assertTrue(form.elements().contains(input2));

        List<Connection.KeyVal> formData = form.formData();
        assertEquals(2, formData.size());
        assertEquals("foo", formData.get(0).key);
        assertEquals("bar", formData.get(0).value);
        assertEquals("baz", formData.get(1).key);
        assertEquals("qux", formData.get(1).value);
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        String html = "<form action='/submit' method='post'><input name='foo' type='text' value='bar'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");

        Connection connection = form.submit();
        assertEquals("/submit", connection.url());
        assertEquals("POST", connection.method());

        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("foo", formData.get(0).key);
        assertEquals("bar", formData.get(0).value);

        Connection response = connection.execute();
        // Perform assertions on the response here
    }

    @Test
    public void testFormElementEmptyFormData() {
        String html = "<form action='/submit' method='post'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementDisabledInput() {
        String html = "<form action='/submit' method='post'><input name='foo' type='text' value='bar' disabled></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementSelectElement() {
        String html = "<form action='/submit' method='post'><select name='foo'><option value='bar'>Option 1</option><option value='qux' selected>Option 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("foo", formData.get(0).key);
        assertEquals("qux", formData.get(0).value);
    }

    @Test
    public void testFormElementSelectElementNoOptionSelected() {
        String html = "<form action='/submit' method='post'><select name='foo'><option value='bar'>Option 1</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementCheckboxRadioElement() {
        String html = "<form action='/submit' method='post'><input name='foo' type='checkbox' checked><input name='bar' type='radio' checked></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(2, formData.size());
        assertEquals("foo", formData.get(0).key);
        assertEquals("on", formData.get(0).value);
        assertEquals("bar", formData.get(1).key);
        assertEquals("on", formData.get(1).value);
    }

    @Test
    public void testFormElementCheckboxRadioElementUnchecked() {
        String html = "<form action='/submit' method='post'><input name='foo' type='checkbox'><input name='bar' type='radio'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementFormWithEmptyName() {
        String html = "<form action='/submit' method='post'><input name='' type='text' value='bar'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementFormWithEmptyValue() {
        String html = "<form action='/submit' method='post'><input name='foo' type='text' value=''></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }
}