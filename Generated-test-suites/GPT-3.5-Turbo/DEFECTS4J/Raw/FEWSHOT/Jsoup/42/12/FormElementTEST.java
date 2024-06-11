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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        assertEquals(0, form.elements().size());

        Element firstElement = new Element(null, "", new Attributes());
        Element secondElement = new Element(null, "", new Attributes());

        form.addElement(firstElement);
        form.addElement(secondElement);

        assertEquals(2, form.elements().size());
        assertTrue(form.elements().contains(firstElement));
        assertTrue(form.elements().contains(secondElement));
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        assertEquals(0, form.elements().size());

        Element element = new Element(null, "", new Attributes());

        form.addElement(element);

        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        form.attr("action", "/submit");
        form.attr("method", "POST");

        Connection connection = form.submit();

        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoActionUrl() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());

        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());

        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        input.attr("name", "username");
        input.attr("value", "john");

        Element select = new Element(Tag.valueOf("select"), "", new Attributes());
        select.attr("name", "country");

        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "USA");

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "Canada");
        option2.attr("selected", "");

        select.appendChild(option1);
        select.appendChild(option2);

        form.addElement(input);
        form.addElement(select);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
        assertEquals("country", formData.get(1).key());
        assertEquals("Canada", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "", new Attributes());

        assertTrue(form1.equals(form1));
        assertTrue(form1.equals(form2));
        assertTrue(form2.equals(form1));

        form1.attr("action", "/submit");
        assertFalse(form1.equals(form2));
        assertFalse(form2.equals(form1));

        form2.attr("action", "/submit");
        assertTrue(form1.equals(form2));
        assertTrue(form2.equals(form1));
    }
}