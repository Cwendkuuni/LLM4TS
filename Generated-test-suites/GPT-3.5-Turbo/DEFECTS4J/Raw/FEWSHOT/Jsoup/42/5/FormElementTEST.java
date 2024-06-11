package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(0, form.elements().size());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element1);
        assertEquals(1, form.elements().size());

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element2);
        assertEquals(2, form.elements().size());
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element1);
        assertTrue(form.elements().contains(element1));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("action", "http://example.com");
        form.attr("method", "POST");

        Connection con = form.submit();

        assertEquals("http://example.com", con.request().url().toString());
        assertEquals(Connection.Method.POST, con.request().method());
        assertEquals(HttpConnection.KeyVal.create("name", "value"), con.request().data().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionURL() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "name");
        inputElement.attr("type", "text");
        inputElement.attr("value", "John Doe");
        form.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "gender");
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "male");
        option1.attr("selected", "");
        selectElement.appendChild(option1);
        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "female");
        selectElement.appendChild(option2);
        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();
        List<Connection.KeyVal> expectedData = Arrays.asList(
                HttpConnection.KeyVal.create("name", "John Doe"),
                HttpConnection.KeyVal.create("gender", "male")
        );

        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataEmptyName() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("value", "John Doe");

        form.addElement(inputElement);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(0, formData.size());
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(form1.equals(form2));
    }

    @Test
    public void testNotEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form2.attr("action", "http://example2.com");
        assertFalse(form1.equals(form2));
    }
}