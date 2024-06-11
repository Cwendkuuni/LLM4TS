package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements_ReturnsElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement_AddsElementToFormElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement newFormElement = formElement.addElement(element);
        assertEquals(formElement, newFormElement);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit_WithActionNoMethod_ThrowsIllegalArgumentException() {
        try {
            formElement.attr("action", "/submit");
            Connection connection = formElement.submit();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testSubmit_WithActionAndMethod_ReturnsConnectionWithCorrectUrlAndMethod() {
        FormElement mockedFormElement = new FormElement(Tag.valueOf("form"), formElement.baseUri(), new Attributes());
        mockedFormElement.attr("action", "/submit");
        mockedFormElement.attr("method", "POST");

        Connection connection = mockedFormElement.submit();

        assertEquals("https://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData_WithNoFormControlElements_ReturnsEmptyList() {
        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_WithFormControlElements_ReturnsCorrectList() {
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        selectElement.attr("name", "color");
        Element optionElement = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        optionElement.attr("value", "red");
        optionElement.attr("selected", "selected");
        selectElement.appendChild(optionElement);
        Element secondOptionElement = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        secondOptionElement.attr("value", "blue");
        selectElement.appendChild(secondOptionElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "fruit");
        checkboxElement.attr("value", "banana");
        checkboxElement.attr("checked", "checked");

        Element secondCheckboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        secondCheckboxElement.attr("type", "checkbox");
        secondCheckboxElement.attr("name", "fruit");
        secondCheckboxElement.attr("value", "apple");

        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "gender");
        radioElement.attr("value", "male");
        radioElement.attr("checked", "checked");

        Element secondRadioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        secondRadioElement.attr("type", "radio");
        secondRadioElement.attr("name", "gender");
        secondRadioElement.attr("value", "female");

        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "name");
        inputElement.attr("value", "John");

        formElement.addElement(selectElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(secondCheckboxElement);
        formElement.addElement(radioElement);
        formElement.addElement(secondRadioElement);
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(6, formData.size());
        assertEquals("color", formData.get(0).key());
        assertEquals("red", formData.get(0).value());
        assertEquals("fruit", formData.get(1).key());
        assertEquals("banana", formData.get(1).value());
        assertEquals("gender", formData.get(2).key());
        assertEquals("male", formData.get(2).value());
        assertEquals("name", formData.get(3).key());
        assertEquals("John", formData.get(3).value());
        assertEquals("fruit", formData.get(4).key());
        assertEquals("apple", formData.get(4).value());
        assertEquals("gender", formData.get(5).key());
        assertEquals("female", formData.get(5).value());
    }

    @Test
    public void testEquals_WithSameObject_ReturnsTrue() {
        assertTrue(formElement.equals(formElement));
    }

    @Test
    public void testEquals_WithDifferentObjectType_ReturnsFalse() {
        assertFalse(formElement.equals(new Object()));
    }

    @Test
    public void testEquals_WithDifferentBaseUri_ReturnsFalse() {
        FormElement differentFormElement = new FormElement(Tag.valueOf("form"), "https://example.org", new Attributes());
        assertFalse(formElement.equals(differentFormElement));
    }

    @Test
    public void testEquals_WithDifferentTag_ReturnsFalse() {
        FormElement differentFormElement = new FormElement(Tag.valueOf("div"), formElement.baseUri(), new Attributes());
        assertFalse(formElement.equals(differentFormElement));
    }

    @Test
    public void testEquals_WithSameBaseUriAndTag_ReturnsTrue() {
        FormElement sameFormElement = new FormElement(Tag.valueOf("form"), formElement.baseUri(), new Attributes());
        assertTrue(formElement.equals(sameFormElement));
    }

}