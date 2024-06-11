package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(formElement, result);
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testSubmitWithEmptyAction() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(formElement.baseUri(), connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testSubmitWithPostMethod() {
        formElement.attr("action", "https://www.example.com/form-handler");
        formElement.attr("method", "post");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("https://www.example.com/form-handler", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitWithGetMethod() {
        formElement.attr("action", "https://www.example.com/form-handler");
        formElement.attr("method", "get");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("https://www.example.com/form-handler", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormDataWithSelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement1.attr("selected", "");
        optionElement1.attr("value", "option1");
        Element optionElement2 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement1.attr("value", "option2");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("option1", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxElement() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormDataWithRadioElement() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio");
        radioElement.attr("checked", "");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormDataWithTextInput() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "name");
        inputElement.val("John Doe");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("John Doe", formData.get(0).value());
    }
}