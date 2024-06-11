package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        assertEquals(0, formElement.elements().size());

        Element element = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();
        assertEquals("https://www.example.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "https://www.example.com", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "https://www.example.com", new Attributes());
        optionElement1.val("option1");
        optionElement1.attr("selected", "");
        selectElement.addElement(optionElement1);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("option1", formData.get(1).value());
    }
}