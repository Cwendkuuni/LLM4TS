
package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
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
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com");
        FormElement updatedForm = form.addElement(element);
        Elements elements = updatedForm.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testRemoveChild() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com");
        form.addElement(element);
        form.removeChild(element);
        Elements elements = form.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection.Method expectedMethod = Connection.Method.POST;

        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(expectedMethod, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_invalidActionUrl() {
        FormElement form = new FormElement(Tag.valueOf("form"), null, new Attributes());
        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com");
        inputElement.attr("name", "username");
        inputElement.val("John Doe");
        form.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com");
        selectElement.attr("name", "gender");
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com").val("male");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com").val("female");
        selectElement.appendChild(option1);
        selectElement.appendChild(option2);
        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("John Doe", formData.get(0).value());
        assertEquals("gender", formData.get(1).key());
        assertEquals("male", formData.get(1).value());
    }
}
