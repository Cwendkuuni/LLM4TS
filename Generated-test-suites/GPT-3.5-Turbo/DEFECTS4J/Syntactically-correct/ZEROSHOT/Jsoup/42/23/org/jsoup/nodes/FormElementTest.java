package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());

        Elements elements = form.elements();

        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());

        Element element = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        FormElement updatedForm = form.addElement(element);

        assertNotNull(updatedForm);
        assertEquals(1, updatedForm.elements().size());
        assertTrue(updatedForm.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());

        Connection connection = form.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());

        List<Connection.KeyVal> formData = form.formData();

        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        FormElement form3 = new FormElement(Tag.valueOf("form"), "https://example.org", new Attributes());

        assertTrue(form1.equals(form2));
        assertFalse(form1.equals(form3));
    }
}