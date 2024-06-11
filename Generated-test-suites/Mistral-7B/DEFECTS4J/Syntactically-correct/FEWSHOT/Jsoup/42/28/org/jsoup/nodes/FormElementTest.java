package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
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
        String baseUri = "http://example.com";
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "/path/to/submit");

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals("/path/to/submit", formElement.absUrl("action"));
        assertEquals(baseUri, formElement.baseUri());
        assertNull(formElement.elements());
    }

    @Test
    public void testFormElementGetElements() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/path/to/submit");

        Tag inputTag1 = new Tag("input");
        inputTag1.classes().add("input1");
        inputTag1.attr("type", "text");
        inputTag1.attr("name", "input1");
        Element element1 = new Element(inputTag1);

        Tag inputTag2 = new Tag("input");
        inputTag2.classes().add("input2");
        inputTag2.attr("type", "checkbox");
        inputTag2.attr("name", "input2");
        inputTag2.attr("checked", "true");
        Element element2 = new Element(inputTag2);

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Element> expectedElements = Arrays.asList(element1, element2);
        List<Element> actualElements = formElement.elements();
        assertEquals(expectedElements.size(), actualElements.size());
        assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testFormElementSubmit() {
        String baseUri = "http://example.com";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", formAction);

        Tag inputTag1 = new Tag("input");
        inputTag1.classes().add("input1");
        inputTag1.attr("type", "text");
        inputTag1.attr("name", "input1");
        Element inputElement1 = new Element(inputTag1);
        inputElement1.val("value1");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.addElement(inputElement1);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("input1", "value1"));
        List<Connection.KeyVal> actualFormData = formElement.formData();
        assertEquals(expectedFormData.size(), actualFormData.size());
        assertEquals(expectedFormData, actualFormData);

        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithEmptyAction() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.submit();
    }

    @Test
    public void testFormElementFormDataWithSelect() {
        String baseUri = "http://example.com";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", formAction);

        Tag selectTag = new Tag("select");
        Element selectElement = new Element(selectTag);

        Tag optionTag1 = new Tag("option");
        optionTag1.attr("value", "option1");
        optionTag1.attr("selected", "selected");
        Element optionElement1 = new Element(optionTag1);
        optionElement1.val("Option 1");
        selectElement.add(optionElement1);

        Tag optionTag2 = new Tag("option");
        optionTag2.attr("value", "option2");
        Element optionElement2 = new Element(optionTag2);
        optionElement2.val("Option 2");
        selectElement.add(optionElement2);

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("select", "Option 1"));
        List<Connection.KeyVal> actualFormData = formElement.formData();
        assertEquals(expectedFormData.size(), actualFormData.size());
        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormElementFormDataWithCheckbox() {
        String baseUri = "http://example.com";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", formAction);

        Tag inputTag = new Tag("input");
        inputTag.classes().add("checkbox");
        inputTag.attr("type", "checkbox");
        inputTag.attr("name", "checkbox");
        Element inputElement = new Element(inputTag);
        inputElement.attr("checked", "true");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.addElement(inputElement);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("checkbox", "true"));
        List<Connection.KeyVal> actualFormData = formElement.formData();
        assertEquals(expectedFormData.size(), actualFormData.size());
        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormElementFormDataWithMultipleControls() {
        String baseUri = "http://example.com";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", formAction);

        Tag inputTag1 = new Tag("input");
        inputTag1.classes().add("input1");
        inputTag1.attr("type", "text");
        inputTag1.attr("name", "input1");
        Element inputElement1 = new Element(inputTag1);
        inputElement1.val("value1");

        Tag inputTag2 = new Tag("input");
        inputTag2.classes().add("checkbox");
        inputTag2.attr("type", "checkbox");
        inputTag2.attr("name", "checkbox");
        Element inputElement2 = new Element(inputTag2);
        inputElement2.attr("checked", "true");

        Tag selectTag = new Tag("select");
        Element selectElement = new Element(selectTag);

        Tag optionTag1 = new Tag("option");
        optionTag1.attr("value", "option1");
        optionTag1.attr("selected", "selected");
        Element optionElement1 = new Element(optionTag1);
        optionElement1.val("Option 1");
        selectElement.add(optionElement1);

        Tag optionTag2 = new Tag("option");
        optionTag2.attr("value", "option2");
        Element optionElement2 = new Element(optionTag2);
        optionElement2.val("Option 2");
        selectElement.add(optionElement2);

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("input1", "value1"),
                new Connection.KeyVal("checkbox", "true"),
                new Connection.KeyVal("select", "Option 1"));
        List<Connection.KeyVal> actualFormData = formElement.formData();
        assertEquals(expectedFormData.size(), actualFormData.size());
        assertEquals(expectedFormData, actualFormData);
    }
}