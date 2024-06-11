package org.jsoup.nodes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertEquals("form", formElement.tagName());
        assertTrue(formElement instanceof FormElement);
    }

    @Test
    public void testElementsEmpty() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testAddElement() {
        Tag tagForm = new Tag("form");
        Tag tagInput = new Tag("input");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        formElement.addElement(tagInput);
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testRemoveChild() {
        Tag tagForm = new Tag("form");
        Tag tagInput1 = new Tag("input");
        Tag tagInput2 = new Tag("input");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        formElement.addElement(tagInput1);
        formElement.addElement(tagInput2);
        formElement.removeChild(tagInput1);
        assertEquals(1, formElement.elements().size());
        assertEquals(tagInput2, formElement.elements().get(0));
    }

    @Test
    public void testSubmitNullBaseUri() {
        Tag tagForm = new Tag("form");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        assertThrows(IllegalArgumentException.class, formElement::submit);
    }

    @Test
    public void testSubmitEmptyFormAction() {
        Tag tagForm = new Tag("form", "action", "");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        assertThrows(IllegalArgumentException.class, formElement::submit);
    }

    @Test
    public void testSubmitFormWithMethodGet() {
        String baseUri = "http://example.com";
        Tag tagForm = new Tag("form", "action", "");
        FormElement formElement = new FormElement(tagForm, baseUri, new Attributes().put("method", "GET"));
        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testSubmitFormWithMethodPost() {
        String baseUri = "http://example.com";
        Tag tagForm = new Tag("form", "action", "");
        FormElement formElement = new FormElement(tagForm, baseUri, new Attributes().put("method", "POST"));
        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormDataEmptyForm() {
        Tag tagForm = new Tag("form");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataFormWithSelectTag() {
        String baseUri = "http://example.com";
        Tag tagForm = new Tag("form");
        Tag tagSelect = new Tag("select");
        Tag tagOption1 = new Tag("option", "value", "Option1");
        Tag tagOption2 = new Tag("option", "value", "Option2");
        tagSelect.add(tagOption1);
        tagSelect.add(tagOption2);
        tagForm.add(tagSelect);
        FormElement formElement = new FormElement(tagForm, baseUri, new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("Option1", formData.get(0).key());
        assertEquals("Option1", formData.get(0).value());
    }

    @Test
    public void testFormDataFormWithCheckboxOrRadioTag() {
        String baseUri = "http://example.com";
        Tag tagForm = new Tag("form");
        Tag tagInput1 = new Tag("input", null, "type", "checkbox", "name", "checkboxName");
        tagInput1.attr("checked", "checked");
        Tag tagInput2 = new Tag("input", null, "type", "radio", "name", "radioName");
        tagInput2.attr("checked", "checked");
        tagForm.add(tagInput1);
        tagForm.add(tagInput2);
        FormElement formElement = new FormElement(tagForm, baseUri, new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("checkboxName", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
        assertEquals("radioName", formData.get(1).key());
        assertEquals("on", formData.get(1).value());
    }

    @Test
    public void testFormDataFormWithTextField() {
        String baseUri = "http://example.com";
        Tag tagForm = new Tag("form");
        Tag tagInput = new Tag("input", null, "type", "text", "name", "textFieldName");
        tagInput.attr("value", "testValue");
        tagForm.add(tagInput);
        FormElement formElement = new FormElement(tagForm, baseUri, new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("textFieldName", formData.get(0).key());
        assertEquals("testValue", formData.get(0).value());
    }
}