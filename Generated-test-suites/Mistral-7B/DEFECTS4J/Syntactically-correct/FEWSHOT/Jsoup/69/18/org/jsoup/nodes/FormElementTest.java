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
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "myForm");

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals("myForm", formElement.attr("id"));
        assertEquals(baseUri, formElement.baseUri());
    }

    @Test
    public void testEmptyFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";

        FormElement formElement = new FormElement(tag, baseUri, new Attributes());
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testFormElementWithElements() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        inputTag1.put("type", "text");
        inputTag1.put("name", "username");

        Tag inputTag2 = new Tag("input");
        inputTag2.put("type", "password");
        inputTag2.put("name", "password");

        Tag selectTag = new Tag("select");
        Tag optionTag1 = new Tag("option").setText("Option 1");
        optionTag1.put("selected", "true");
        Tag optionTag2 = new Tag("option").setText("Option 2");

        formTag.add(inputTag1);
        formTag.add(inputTag2);
        formTag.add(selectTag);
        selectTag.add(optionTag1);
        selectTag.add(optionTag2);

        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        assertEquals(3, formElement.elements().size());

        Element firstElement = formElement.elements().get(0);
        assertEquals("input", firstElement.tagName());
        assertEquals("username", firstElement.attr("name"));

        Element secondElement = formElement.elements().get(1);
        assertEquals("input", secondElement.tagName());
        assertEquals("password", secondElement.attr("name"));

        Element thirdElement = formElement.elements().get(2);
        assertEquals("select", thirdElement.tagName());
    }

    @Test
    public void testFormDataEmptyForm() {
        String baseUri = "http://example.com";
        FormElement formElement = new FormElement(new Tag("form"), baseUri, new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithTextInput() {
        String baseUri = "http://example.com";

        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input");
        inputTag.put("type", "text");
        inputTag.put("name", "username");
        inputTag.put("value", "John Doe");

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.add(inputTag);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key);
        assertEquals("John Doe", keyVal.value);
    }

    @Test
    public void testFormDataWithCheckboxInput() {
        String baseUri = "http://example.com";

        Tag formTag = new Tag("form");
        Tag checkboxTag = new Tag("input");
        checkboxTag.put("type", "checkbox");
        checkboxTag.put("name", "agreeTerms");
        checkboxTag.put("checked", "checked");

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.add(checkboxTag);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("agreeTerms", keyVal.key);
        assertEquals("on", keyVal.value);
    }

    @Test
    public void testFormDataWithRadioInput() {
        String baseUri = "http://example.com";

        Tag formTag = new Tag("form");
        Tag radioGroupTag = new Tag("div");
        radioGroupTag.put("name", "gender");

        Tag maleRadioTag = new Tag("input");
        maleRadioTag.put("type", "radio");
        maleRadioTag.put("id", "male");
        maleRadioTag.put("name", "gender");
        maleRadioTag.put("value", "male");

        Tag femaleRadioTag = new Tag("input");
        femaleRadioTag.put("type", "radio");
        femaleRadioTag.put("id", "female");
        femaleRadioTag.put("name", "gender");
        femaleRadioTag.put("value", "female");

        radioGroupTag.add(maleRadioTag);
        radioGroupTag.add(femaleRadioTag);

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.add(radioGroupTag);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        maleRadioTag.put("checked", "checked");

        formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("gender", keyVal.key);
        assertEquals("male", keyVal.value);
    }

    @Test
    public void testFormDataWithSelectInput() {
        String baseUri = "http://example.com";

        Tag formTag = new Tag("form");
        Tag selectTag = new Tag("select");
        selectTag.put("name", "size");

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.add(selectTag);

        Element optionTag1 = new Tag("option").setText("Small");
        selectTag.add(optionTag1);
        Element optionTag2 = new Tag("option").setText("Medium");
        selectTag.add(optionTag2);
        Element optionTag3 = new Tag("option").setText("Large").put("selected", "true");
        selectTag.add(optionTag3);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("size", keyVal.key);
        assertEquals("Large", keyVal.value);
    }

    @Test
    public void testSubmitForm() {
        String baseUri = "http://example.com";

        Tag formTag = new Tag("form");
        formTag.put("action", "/submit");
        formTag.put("method", "POST");

        Element inputTag1 = new Tag("input");
        inputTag1.put("type", "text");
        inputTag1.put("name", "username");
        inputTag1.put("value", "John Doe");

        Element inputTag2 = new Tag("input");
        inputTag2.put("type", "password");
        inputTag2.put("name", "password");
        inputTag2.put("value", "password123");

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.add(inputTag1);
        formElement.add(inputTag2);

        Connection connection = formElement.submit();

        List<NameValuePair> requestData = connection.requestBody();
        assertEquals(2, requestData.size());

        NameValuePair nameValuePair1 = requestData.get(0);
        assertEquals("username", nameValuePair1.getName());
        assertEquals("John Doe", nameValuePair1.getValue());

        NameValuePair nameValuePair2 = requestData.get(1);
        assertEquals("password", nameValuePair2.getName());
        assertEquals("password123", nameValuePair2.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitFormWithoutBaseUri() {
        Tag formTag = new Tag("form");
        formTag.put("action", "/submit");
        formTag.put("method", "POST");

        FormElement formElement = new FormElement(formTag, null, new Attributes());
        formElement.submit();
    }
}