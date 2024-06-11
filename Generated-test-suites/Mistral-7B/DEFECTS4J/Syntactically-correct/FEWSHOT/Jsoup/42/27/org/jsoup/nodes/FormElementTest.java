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
    public void testCreateFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("method", "post");
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testGetElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();

        Element input1 = new Element("input");
        Element input2 = new Element("input");
        Element textArea = new Element("textarea");

        formTag.children().add(input1);
        formTag.children().add(input2);
        formTag.children().add(textArea);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Elements elements = formElement.elements();

        assertEquals(3, elements.size());
        assertTrue(elements.contains(input1));
        assertTrue(elements.contains(input2));
        assertTrue(elements.contains(textArea));
    }

    @Test
    public void testAddElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element input = new Element("input");
        FormElement formElementWithInput = formElement.addElement(input);

        assertEquals(formElement, formElementWithInput);
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(input));
    }

    @Test
    public void testSubmitWithEmptyForm() {
        Tag formTag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("method", "post");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> expectedData = new ArrayList<>();

        Connection connection = formElement.submit();
        List<Connection.KeyVal> actualData = connection.data();

        assertEquals(expectedData, actualData);
    }

    @Test
    public void testSubmitWithInputElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("method", "post");

        Element input1 = new Element("input", "input1");
        input1.attr("name", "username");
        input1.attr("type", "text");
        input1.attr("value", "John Doe");

        Element input2 = new Element("input", "input2");
        input2.attr("name", "password");
        input2.attr("type", "password");
        input2.attr("value", "password123");

        formTag.children().add(input1);
        formTag.children().add(input2);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("username", "John Doe"));
        expectedData.add(HttpConnection.KeyVal.create("password", "password123"));

        Connection connection = formElement.submit();
        List<Connection.KeyVal> actualData = connection.data();

        assertEquals(expectedData, actualData);
    }

    @Test
    public void testSubmitWithSelectElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("method", "post");

        Element select = new Element("select");
        select.attr("name", "color");

        Element optionRed = new Element("option", "optionRed");
        optionRed.attr("value", "red");
        optionRed.attr("selected", "selected");

        Element optionGreen = new Element("option", "optionGreen");
        optionGreen.attr("value", "green");

        Element optionBlue = new Element("option", "optionBlue");

        select.children().add(optionRed);
        select.children().add(optionGreen);
        select.children().add(optionBlue);

        formTag.children().add(select);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("color", "red"));

        Connection connection = formElement.submit();
        List<Connection.KeyVal> actualData = connection.data();

        assertEquals(expectedData, actualData);
    }

    @Test
    public void testSubmitWithCheckboxAndRadioElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("method", "post");

        Element checkbox = new Element("input");
        checkbox.attr("name", "fruit");
        checkbox.attr("type", "checkbox");
        checkbox.attr("value", "apple");

        Element radioGroup = new Element("input");
        radioGroup.attr("name", "color");
        radioGroup.attr("type", "radio");

        Element radioRed = new Element("input", "radioRed");
        radioRed.attr("type", "radio");
        radioRed.attr("name", "color");
        radioRed.attr("value", "red");

        Element radioGreen = new Element("input", "radioGreen");
        radioGreen.attr("type", "radio");
        radioGreen.attr("name", "color");
        radioGreen.attr("value", "green");

        formTag.children().add(checkbox);
        formTag.children().add(radioGroup);
        formTag.children().add(radioRed);
        formTag.children().add(radioGreen);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> expectedData = new ArrayList<>();

        // checkbox not checked
        List<Connection.KeyVal> actualData = formElement.submit().data();
        assertEquals(expectedData, actualData);

        checkbox.attr("checked", "checked");
        formElement = new FormElement(formTag, baseUri, attributes);

        // checkbox checked
        expectedData.add(HttpConnection.KeyVal.create("fruit", "apple"));
        actualData = formElement.submit().data();
        assertEquals(expectedData, actualData);

        radioRed.attr("checked", "checked");
        formElement = new FormElement(formTag, baseUri, attributes);

        // radio button checked
        expectedData.clear();
        expectedData.add(HttpConnection.KeyVal.create("color", "red"));
        actualData = formElement.submit().data();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testSubmitWithEmptyFormAndNullBaseUri() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());

        try {
            formElement.submit();
            fail("IllegalArgumentException should be thrown when the form's absolute action URL cannot be determined.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormElementHashCodeAndEquals() {
        Tag formTag1 = new Tag("form");
        String baseUri1 = "http://www.example.com";
        Attributes attributes1 = new Attributes();

        FormElement formElement1 = new FormElement(formTag1, baseUri1, attributes1);

        Tag formTag2 = new Tag("form");
        String baseUri2 = "http://www.example.com";
        Attributes attributes2 = new Attributes();

        FormElement formElement2 = new FormElement(formTag2, baseUri2, attributes2);

        Tag formTag3 = new Tag("form");
        String baseUri3 = "http://www.example.com";
        Attributes attributes3 = new Attributes();
        attributes3.put("method", "get");

        FormElement formElement3 = new FormElement(formTag3, baseUri3, attributes3);

        FormElement formElement4 = new FormElement(formTag1, baseUri1, attributes1);

        assertEquals(formElement1, formElement4);
        assertEquals(formElement1.hashCode(), formElement4.hashCode());

        assertFalse(formElement1.equals(formElement2));
        assertFalse(formElement1.equals(formElement3));
        assertFalse(formElement1.equals(null));
        assertFalse(formElement1.equals(new Object()));
    }
}