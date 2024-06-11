package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "", attributes);
        assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        Tag inputTag2 = new Tag("input");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "", formAttributes);
        Elements elements = formElement.elements();

        elements.add(inputTag1);
        elements.add(inputTag2);

        assertEquals(2, elements.size());
    }

    @Test
    public void testAddElement() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        Tag inputTag2 = new Tag("input");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "", formAttributes);

        Element inputElement1 = formElement.addElement(inputTag1);
        Element inputElement2 = formElement.addElement(inputTag2);

        assertEquals(formElement, inputElement1);
        assertEquals(formElement, inputElement2);
    }

    @Test
    public void testSubmitWithGETMethod() throws Exception {
        String baseUri = "https://test.com";
        String formAction = "submit.html";
        Tag formTag = new Tag("form", "action", formAction);
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputTag = new Tag("input", "name", "inputName", "type", "text");
        formElement.addElement(inputTag).val("testValue");

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals(formAction, connection.url());
        List<Connection.KeyVal> formData = connection.data();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("testValue", keyVal.value());
    }

    @Test
    public void testSubmitWithPOSTMethod() throws Exception {
        String baseUri = "https://test.com";
        String formAction = "submit.html";
        Tag formTag = new Tag("form", "action", formAction);
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputTag = new Tag("input", "name", "inputName", "type", "text");
        formElement.addElement(inputTag).val("testValue");

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals(formAction, connection.url());
        List<Connection.KeyVal> formData = connection.data();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("testValue", keyVal.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyFormAction() {
        String baseUri = "https://test.com";
        Tag formTag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.submit();
    }

    @Test
    public void testFormDataEmptyForm() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithSingleTextInput() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input", "name", "inputName", "type", "text");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        formElement.addElement(inputTag).val("testValue");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("testValue", keyVal.value());
    }

    @Test
    public void testFormDataWithSingleSelect() {
        Tag formTag = new Tag("form");
        Tag selectTag = new Tag("select", "name", "inputName");
        Tag optionTag1 = new Tag("option", "value", "option1");
        Tag optionTag2 = new Tag("option", "value", "option2", "selected", "");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        Element select = formElement.addElement(selectTag);
        select.addElement(optionTag1);
        select.addElement(optionTag2);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("option1", keyVal.value());
    }

    @Test
    public void testFormDataWithSingleCheckbox() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input", "name", "inputName", "type", "checkbox");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        formElement.addElement(inputTag).attr("checked", "checked");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataWithMultipleInputs() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input", "name", "inputName1", "type", "text");
        Tag inputTag2 = new Tag("input", "name", "inputName2", "type", "text");
        Tag selectTag = new Tag("select", "name", "inputName3");
        Tag optionTag1 = new Tag("option", "value", "option1");
        Tag optionTag2 = new Tag("option", "value", "option2", "selected", "");
        FormElement formElement = new FormElement(formTag, "", new Attributes());

        formElement.addElement(inputTag1).val("testValue1");
        formElement.addElement(inputTag2).val("testValue2");
        formElement.addElement(selectTag);
        selectTag.addElement(optionTag1);
        selectTag.addElement(optionTag2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(4, formData.size());
        Connection.KeyVal inputName1KeyVal = formData.get(0);
        assertEquals("inputName1", inputName1KeyVal.key());
        assertEquals("testValue1", inputName1KeyVal.value());

        Connection.KeyVal inputName2KeyVal = formData.get(1);
        assertEquals("inputName2", inputName2KeyVal.key());
        assertEquals("testValue2", inputName2KeyVal.value());

        Connection.KeyVal inputName3Option1KeyVal = formData.get(2);
        assertEquals("inputName3", inputName3Option1KeyVal.key());
        assertEquals("option1", inputName3Option1KeyVal.value());

        Connection.KeyVal inputName3Option2KeyVal = formData.get(3);
        assertEquals("inputName3", inputName3Option2KeyVal.key());
        assertEquals("option2", inputName3Option2KeyVal.value());
    }
}