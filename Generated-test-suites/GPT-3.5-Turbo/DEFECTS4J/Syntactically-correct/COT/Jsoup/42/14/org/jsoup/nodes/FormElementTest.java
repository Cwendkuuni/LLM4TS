
package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        form = new FormElement(tag, "", attributes);
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement newForm = form.addElement(element);
        assertTrue(newForm.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        try {
            Connection connection = form.submit();
            assertNotNull(connection);
        } catch (IllegalArgumentException e) {
            fail("Failed to submit form: " + e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
    }

    @Test
    public void testEquals() {
        FormElement otherForm = new FormElement(Tag.valueOf("form"), "", new Attributes());
        boolean result = form.equals(otherForm);
        assertTrue(result);
    }

}
