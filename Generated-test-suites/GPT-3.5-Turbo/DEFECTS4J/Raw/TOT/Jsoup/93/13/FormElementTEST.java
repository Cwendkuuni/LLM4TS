package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.jsoup.Jsoup;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setup() {
        // Initialize a FormElement object for testing
        formElement = new FormElement(Tag.form, "http://example.com/", new Attributes());
    }

    @After
    public void cleanup() {
        // Clean up after each test
        formElement = null;
    }

    @Test
    public void testElements_emptyFormElement_returnsEmptyList() {
        // Test when the form element is empty, elements() method should return an empty list
        List<Element> elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement_validElement_addedSuccessfully() {
        // Test adding a valid form control element to the form element
        Element inputElement = new Element(Tag.input, "", new Attributes());
        formElement.addElement(inputElement);
        List<Element> elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testRemoveChild_validChild_removedSuccessfully() {
        // Test removing a valid child element from the form element
        Element inputElement = new Element(Tag.input, "", new Attributes());
        formElement.addElement(inputElement);
        formElement.removeChild(inputElement);
        List<Element> elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_missingActionUrl_throwsException() {
        // Test submitting a form without a valid action URL should throw an IllegalArgumentException
        formElement.submit();
    }

    @Test
    public void testSubmit_validForm_submittedSuccessfully() {
        // Test submitting a valid form with GET method
        Element inputElement = new Element(Tag.input, "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "John");
        formElement.addElement(inputElement);
        Connection connection = formElement.submit();
        assertEquals("http://example.com/", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("username=John", connection.request().data());
    }

    @Test
    public void testFormData_selectElementWithSelectedOption_returnsSelectedOption() {
        // Test getting form data for a select element with selected option
        Element selectElement = new Element(Tag.select, "", new Attributes());
        selectElement.attr("name", "fruit");
        Element option1 = new Element(Tag.option, "", new Attributes());
        option1.attr("value", "apple");
        option1.attr("selected", "");
        Element option2 = new Element(Tag.option, "", new Attributes());
        option2.attr("value", "banana");
        selectElement.addElement(option1);
        selectElement.addElement(option2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("fruit=apple", formData.get(0).toString());
    }

    @Test
    public void testFormData_selectElementWithoutSelectedOption_returnsFirstOption() {
        // Test getting form data for a select element without selected option
        Element selectElement = new Element(Tag.select, "", new Attributes());
        selectElement.attr("name", "fruit");
        Element option1 = new Element(Tag.option, "", new Attributes());
        option1.attr("value", "apple");
        selectElement.addElement(option1);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("fruit=apple", formData.get(0).toString());
    }

    @Test
    public void testFormData_checkboxElementChecked_returnsValue() {
        // Test getting form data for a checked checkbox element
        Element checkboxElement = new Element(Tag.input, "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("checked", "");
        checkboxElement.attr("value", "yes");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("agree=yes", formData.get(0).toString());
    }

    @Test
    public void testFormData_checkboxElementUnchecked_returnsDefaultValue() {
        // Test getting form data for an unchecked checkbox element
        Element checkboxElement = new Element(Tag.input, "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("value", "yes");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_radioElementChecked_returnsValue() {
        // Test getting form data for a checked radio element
        Element radioElement1 = new Element(Tag.input, "", new Attributes());
        radioElement1.attr("type", "radio");
        radioElement1.attr("name", "gender");
        radioElement1.attr("checked", "");
        radioElement1.attr("value", "male");
        formElement.addElement(radioElement1);

        Element radioElement2 = new Element(Tag.input, "", new Attributes());
        radioElement2.attr("type", "radio");
        radioElement2.attr("name", "gender");
        radioElement2.attr("value", "female");
        formElement.addElement(radioElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("gender=male", formData.get(0).toString());
    }

    @Test
    public void testFormData_radioElementUnchecked_returnsDefaultValue() {
        // Test getting form data for an unchecked radio element
        Element radioElement1 = new Element(Tag.input, "", new Attributes());
        radioElement1.attr("type", "radio");
        radioElement1.attr("name", "gender");
        radioElement1.attr("value", "male");
        formElement.addElement(radioElement1);

        Element radioElement2 = new Element(Tag.input, "", new Attributes());
        radioElement2.attr("type", "radio");
        radioElement2.attr("name", "gender");
        radioElement2.attr("value", "female");
        formElement.addElement(radioElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_defaultInputElement_returnsValue() {
        // Test getting form data for a default input element
        Element inputElement = new Element(Tag.input, "", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.attr("value", "John");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username=John", formData.get(0).toString());
    }

    @Test
    public void testFormData_disabledInputElement_ignored() {
        // Test getting form data for a disabled input element should be ignored
        Element inputElement = new Element(Tag.input, "", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.attr("value", "John");
        inputElement.attr("disabled", "");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }
}