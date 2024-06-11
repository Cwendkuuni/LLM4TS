package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        String html = "<form><input name=\"name1\" type=\"text\"><input name=\"name2\" type=\"text\" checked><select name=\"name3\"><option value=\"option1\" selected></option><option value=\"option2\"></option></select></form>";
        FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
        Document doc = Jsoup.parse(html);
        formElement.mergeInto(doc);

        assertEquals("form", formElement.tagName());
        assertNotNull(formElement.elements());
        assertEquals(3, formElement.elements().size());

        Element name1 = formElement.elements().get(0);
        assertEquals("input", name1.tagName());
        assertEquals("name1", name1.attr("name"));
        assertEquals("text", name1.attr("type"));

        Element name2 = formElement.elements().get(1);
        assertEquals("input", name2.tagName());
        assertEquals("name2", name2.attr("name"));
        assertEquals("text", name2.attr("type"));
        assertTrue(name2.hasAttr("checked"));

        Element name3 = formElement.elements().get(2);
        assertEquals("select", name3.tagName());
        assertEquals("name3", name3.attr("name"));

        Elements options = name3.elements();
        assertEquals(2, options.size());
        assertEquals("option1", options.get(0).attr("value"));
        assertTrue(options.get(0).hasAttr("selected"));
        assertEquals("option2", options.get(1).attr("value"));
    }

    @Test
    public void testFormElementSubmit() {
        String html = "<form action=\"http://example.com\" method=\"post\"><input name=\"name1\" type=\"text\"><input name=\"name2\" type=\"text\" checked><select name=\"name3\"><option value=\"option1\" selected></option><option value=\"option2\"></option></select></form>";
        FormElement formElement = new FormElement(new Tag("form"), null, new Attributes().put("action", "http://example.com").put("method", "post"));
        Document doc = Jsoup.parse(html);
        formElement.mergeInto(doc);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());

        Connection connection = formElement.submit();
        assertEquals("http://example.com", connection.url().toString());
        assertEquals(Connection.Method.POST, connection.method());

        List<Connection.KeyVal> submittedData = connection.data();
        assertEquals(3, submittedData.size());

        assertEquals("name1", submittedData.get(0).key);
        assertEquals("", submittedData.get(0).val); // empty input fields submit as empty strings

        assertEquals("name2", submittedData.get(1).key);
        assertEquals("", submittedData.get(1).val);

        assertEquals("name3", submittedData.get(2).key);
        assertEquals("option1", submittedData.get(2).val); // selected option is submitted
    }

    @Test
    public void testFormElementSubmitEmptyForm() {
        String html = "<form action=\"http://example.com\" method=\"post\"></form>";
        FormElement formElement = new FormElement(new Tag("form"), null, new Attributes().put("action", "http://example.com").put("method", "post"));
        Document doc = Jsoup.parse(html);
        formElement.mergeInto(doc);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        Connection connection = formElement.submit();
        assertEquals("http://example.com", connection.url().toString());
        assertEquals(Connection.Method.POST, connection.method());

        List<Connection.KeyVal> submittedData = connection.data();
        assertEquals(0, submittedData.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testFormElementSubmitNoAction() {
        String html = "<form><input name=\"name1\" type=\"text\"><input name=\"name2\" type=\"text\" checked><select name=\"name3\"><option value=\"option1\" selected></option><option value=\"option2\"></option></select></form>";
        FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
        Document doc = Jsoup.parse(html);
        formElement.mergeInto(doc);

        List<Connection.KeyVal> formData = formElement.formData();
        // formData test is in another test method

        Connection connection = formElement.submit();
        fail("IllegalStateException expected but not thrown");
    }
}