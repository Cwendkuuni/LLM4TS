
package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        assertNotNull(form.elements());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        form.addElement(element);
        assertEquals(1, form.elements().size());
        assertEquals(element, form.elements().get(0));
    }

    @Test
    public void testSubmit() {
        try {
            Connection connection = form.submit();
            assertNotNull(connection);
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
    }

    @Test
    public void testEquals() {
        FormElement sameForm = new FormElement(form.tag(), form.baseUri(), form.attributes());
        assertEquals(form, sameForm);
    }
}
