package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertNotNull(formElement);
        assertEquals("form", formElement.tagName());
    }

    @Test
    public void testGetElements() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Tag tagForm = new Tag("form");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());

        Tag tagInput = new Tag("input");
        tagInput.attr("type", "text");
        tagInput.attr("name", "username");

        formElement.addElement(tagInput);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.get(0) instanceof Element);
        assertEquals("input", elements.get(0).tagName());
    }

    @Test
    public void testSubmit() {
        Tag tagForm = new Tag("form");
        tagForm.attr("action", "/submit");

        FormElement formElement = new FormElement(tagForm, "", new Attributes());

        Element tagInput = new Tag("input");
        tagInput.attr("type", "text");
        tagInput.attr("name", "username");
        tagInput.attr("value", "test");
        formElement.addElement(tagInput);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertTrue(connection instanceof Connection);
        assertEquals("/submit", connection.url());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        Tag tagForm = new Tag("form");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormDataEmptyForm() {
        Tag tagForm = new Tag("form");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataTextInput() {
        Tag tagForm = new Tag("form");
        tagForm.attr("action", "/submit");

        FormElement formElement = new FormElement(tagForm, "", new Attributes());

        Element tagInput1 = new Tag("input");
        tagInput1.attr("type", "text");
        tagInput1.attr("name", "username");
        tagInput1.attr("value", "test1");
        formElement.addElement(tagInput1);

        Element tagInput2 = new Tag("input");
        tagInput2.attr("type", "text");
        tagInput2.attr("name", "password");
        tagInput2.attr("value", "test2");
        formElement.addElement(tagInput2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyValUsername = formData.get(0);
        assertEquals("username", keyValUsername.key());
        assertEquals("test1", keyValUsername.value());

        Connection.KeyVal keyValPassword = formData.get(1);
        assertEquals("password", keyValPassword.key());
        assertEquals("test2", keyValPassword.value());
    }

    @Test
    public void testFormDataSelect() {
        Tag tagForm = new Tag("form");
        tagForm.attr("action", "/submit");

        FormElement formElement = new FormElement(tagForm, "", new Attributes());

        Element tagSelect = new Tag("select");
        tagSelect.attr("name", "color");
        Element tagOptionRed = new Tag("option");
        tagOptionRed.attr("value", "red");
        tagOptionRed.text("Red");
        tagSelect.add(tagOptionRed);

        Element tagOptionGreen = new Tag("option");
        tagOptionGreen.attr("value", "green");
        tagOptionGreen.text("Green");
        tagSelect.add(tagOptionGreen);

        Element tagInput = new Tag("input");
        tagInput.attr("type", "submit");
        formElement.addElement(tagSelect);
        formElement.addElement(tagInput);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        tagSelect. select("option[selected]").first().attr("selected", "selected");

        formData = formElement.formData();
        assertEquals(1, formData.size());

        Connection.KeyVal keyValColor = formData.get(0);
        assertEquals("color", keyValColor.key());
        assertEquals("red", keyValColor.value());
    }

    @Test
    public void testFormDataCheckbox() {
        Tag tagForm = new Tag("form");
        tagForm.attr("action", "/submit");

        FormElement formElement = new FormElement(tagForm, "", new Attributes());

        Element tagInput1 = new Tag("input");
        tagInput1.attr("type", "checkbox");
        tagInput1.attr("name", "option1");
        tagInput1.attr("value", "value1");
        formElement.addElement(tagInput1);

        Element tagInput2 = new Tag("input");
        tagInput2.attr("type", "checkbox");
        tagInput2.attr("name", "option1");
        tagInput2.attr("value", "value2");
        formElement.addElement(tagInput2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        tagInput1.attr("checked", "checked");

        formData = formElement.formData();
        assertEquals(1, formData.size());

        Connection.KeyVal keyValOption1 = formData.get(0);
        assertEquals("option1", keyValOption1.key());
        assertEquals("value1", keyValOption1.value());

        tagInput2.attr("checked", "checked");

        formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyValOption1Value1 = formData.get(0);
        Connection.KeyVal keyValOption1Value2 = formData.get(1);
        assertEquals("option1", keyValOption1Value1.key());
        assertEquals("value1", keyValOption1Value1.value());
        assertEquals("option1", keyValOption1Value2.key());
        assertEquals("value2", keyValOption1Value2.value());
    }

    @Test
    public void testFormDataRadio() {
        Tag tagForm = new Tag("form");
        tagForm.attr("action", "/submit");

        FormElement formElement = new FormElement(tagForm, "", new Attributes());

        Element tagInput1 = new Tag("input");
        tagInput1.attr("type", "radio");
        tagInput1.attr("name", "gender");
        tagInput1.attr("value", "male");
        formElement.addElement(tagInput1);

        Element tagLabelMale = new Tag("label");
        tagLabelMale.attr("for", tagInput1.attr("id"));
        tagLabelMale.text("Male");
        formElement.addElement(tagLabelMale);

        Element tagInput2 = new Tag("input");
        tagInput2.attr("type", "radio");
        tagInput2.attr("name", "gender");
        tagInput2.attr("value", "female");
        formElement.addElement(tagInput2);

        Element tagLabelFemale = new Tag("label");
        tagLabelFemale.attr("for", tagInput2.attr("id"));
        tagLabelFemale.text("Female");
        formElement.addElement(tagLabelFemale);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        tagInput1.attr("checked", "checked");

        formData = formElement.formData();
        assertEquals(1, formData.size());

        Connection.KeyVal keyValGenderMale = formData.get(0);
        assertEquals("gender", keyValGenderMale.key());
        assertEquals("male", keyValGenderMale.value());

        tagInput2.attr("checked", "checked");

        formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyValGenderMale = formData.get(0);
        Connection.KeyVal keyValGenderFemale = formData.get(1);
        assertEquals("gender", keyValGenderMale.key());
        assertEquals("male", keyValGenderMale.value());
        assertEquals("gender", keyValGenderFemale.key());
        assertEquals("female", keyValGenderFemale.value());
    }
}