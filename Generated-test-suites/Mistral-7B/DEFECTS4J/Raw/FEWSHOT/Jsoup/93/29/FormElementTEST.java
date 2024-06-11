package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "formId");

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals("form", formElement.tag().name());
        assertEquals("formId", formElement.tag().attr("id"));
        assertEquals("http://example.com/", formElement.baseUri());
        assertNull(formElement.attr("action"));
    }

    @Test
    public void testFormElementAddElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com/";
        Attributes formAttributes = new Attributes();
        formAttributes.put("id", "formId");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag = new Tag("input");
        inputTag.attr("type", "text");
        inputTag.attr("name", "username");
        Element inputElement = new Element(inputTag);

        formElement.addElement(inputElement);

        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().get(0));
    }

    @Test
    public void testFormElementRemoveChild() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com/";
        Attributes formAttributes = new Attributes();
        formAttributes.put("id", "formId");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag = new Tag("input");
        inputTag.attr("type", "text");
        inputTag.attr("name", "username");
        Element inputElement = new Element(inputTag);

        formElement.addElement(inputElement);
        formElement.removeChild(inputElement);

        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testFormElementSubmit() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com/";
        Attributes formAttributes = new Attributes();
        formAttributes.put("id", "formId");
        formAttributes.put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag1 = new Tag("input");
        inputTag1.attr("type", "text");
        inputTag1.attr("name", "username");
        Element inputElement1 = new Element(inputTag1);
        inputElement1.attr("value", "testUser");

        Tag inputTag2 = new Tag("input");
        inputTag2.attr("type", "password");
        inputTag2.attr("name", "password");
        Element inputElement2 = new Element(inputTag2);
        inputElement2.attr("value", "testPassword");

        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);

        Connection connection = formElement.submit();

        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("username", "testUser"),
                new Connection.KeyVal("password", "testPassword")
        );

        assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Connection.KeyVal expectedData = expectedFormData.get(i);
            Connection.KeyVal actualData = formData.get(i);

            assertEquals(expectedData.key, actualData.key);
            assertEquals(expectedData.value, actualData.value);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithoutBaseUri() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/submit");

        FormElement formElement = new FormElement(formTag, null, formAttributes);

        formElement.submit();
    }

    @Test
    public void testFormElementFormDataEmptyForm() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com/";
        Attributes formAttributes = new Attributes();
        formAttributes.put("id", "formId");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementFormDataSelectElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com/";
        Attributes formAttributes = new Attributes();
        formAttributes.put("id", "formId");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag selectTag = new Tag("select");
        selectTag.attr("name", "color");
        Element selectElement = new Element(selectTag);

        Tag optionTag1 = new Tag("option");
        optionTag1.attr("value", "red");
        Element optionElement1 = new Element(optionTag1);
        optionElement1.attr("selected", "selected");

        selectElement.add(optionElement1);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        Connection.KeyVal formDataEntry = formData.get(0);

        assertEquals("color", formDataEntry.key);
        assertEquals("red", formDataEntry.value);
    }

    @Test
    public void testFormElementFormDataCheckboxOrRadioElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com/";
        Attributes formAttributes = new Attributes();
        formAttributes.put("id", "formId");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag = new Tag("input");
        inputTag.attr("type", "checkbox");
        inputTag.attr("name", "option1");
        Element inputElement1 = new Element(inputTag);
        inputElement1.attr("checked", "checked");

        Tag inputTag2 = new Tag("input");
        inputTag2.attr("type", "radio");
        inputTag2.attr("name", "option1");
        inputTag2.attr("id", "option1_value1");
        Element inputElement2 = new Element(inputTag2);
        inputElement2.attr("checked", "checked");

        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());

        Connection.KeyVal formDataEntry1 = formData.get(0);
        Connection.KeyVal formDataEntry2 = formData.get(1);

        assertEquals("option1", formDataEntry1.key);
        assertTrue(formDataEntry1.value.equals("on") || formDataEntry1.value.equals(""));

        assertEquals("option1", formDataEntry2.key);
        assertTrue(formDataEntry2.value.equals("on") || formDataEntry2.value.equals("option1_value1"));
    }

    @Test
    public void testFormElementFormDataTextInputElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com/";
        Attributes formAttributes = new Attributes();
        formAttributes.put("id", "formId");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag = new Tag("input");
        inputTag.attr("type", "text");
        inputTag.attr("name", "username");
        Element inputElement = new Element(inputTag);
        inputElement.attr("value", "testUser");

        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        Connection.KeyVal formDataEntry = formData.get(0);

        assertEquals("username", formDataEntry.key);
        assertEquals("testUser", formDataEntry.value);
    }
}