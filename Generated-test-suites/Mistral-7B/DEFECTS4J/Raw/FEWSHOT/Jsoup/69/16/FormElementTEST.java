package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ElementFactory;
import org.jsoup.nodes.Selectable;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = mock(Tag.class);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testFormElementGetElements() {
        ElementFactory factory = Jsoup.parseBodyFragments("").factory();
        Tag formTag = factory.tag("form");
        Element formElement = new FormElement(formTag, "http://example.com", new Attributes());

        Element input1 = factory.tag("input", new Attributes().put("name", "input1").put("type", "text"));
        FormElement formElementWithInput = formElement.addElement(input1);

        Element input2 = factory.tag("input", new Attributes().put("name", "input2").put("type", "text"));
        formElementWithInput.addElement(input2);

        Elements expectedElements = Arrays.asList(input1, input2);

        Elements actualElements = formElementWithInput.elements();

        assertEquals(expectedElements.size(), actualElements.size());
        for (Element element : expectedElements) {
            assertTrue(actualElements.contains(element));
        }
    }

    @Test
    public void testFormElementSubmitNullBaseUri() {
        String baseUri = null;
        ElementFactory factory = Jsoup.parseBodyFragments("").factory();
        Tag formTag = factory.tag("form");
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        assertThrows(IllegalArgumentException.class, formElement::submit);
    }

    @Test
    public void testFormElementSubmitWithBaseUri() {
        String baseUri = "http://example.com";
        ElementFactory factory = Jsoup.parseBodyFragments("").factory();
        Tag formTag = factory.tag("form");
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Connection connection = formElement.submit();

        String action = formElement.attr("action");
        assertEquals(baseUri + action, connection.url());
    }

    @Test
    public void testFormElementSubmitWithActionAttribute() {
        String baseUri = "http://example.com";
        String action = "/submit";
        ElementFactory factory = Jsoup.parseBodyFragments("").factory();
        Tag formTag = factory.tag("form", new Attributes().put("action", action));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Connection connection = formElement.submit();

        assertEquals(baseUri + action, connection.url());
    }

    @Test
    public void testFormElementFormDataEmptyForm() {
        ElementFactory factory = Jsoup.parseBodyFragments("").factory();
        Tag formTag = factory.tag("form");
        FormElement formElement = new FormElement(formTag, "http://example.com", new Attributes());

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData.size(), actualFormData.size());
        assertTrue(actualFormData.isEmpty());
    }

    @Test
    public void testFormElementFormDataSingleInput() {
        ElementFactory factory = Jsoup.parseBodyFragments("").factory();
        Tag formTag = factory.tag("form");
        Element input = factory.tag("input", new Attributes().put("name", "input1").put("type", "text").put("value", "value1"));
        FormElement formElement = new FormElement(formTag, "http://example.com", new Attributes());
        formElement.addElement(input);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(new Connection.KeyVal("input1", "value1"));

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData.size(), actualFormData.size());
        for (Connection.KeyVal keyVal : expectedFormData) {
            Connection.KeyVal actualKeyVal = findKeyVal(actualFormData, keyVal.key());
            assertEquals(keyVal.value(), actualKeyVal.value());
        }
    }

    @Test
    public void testFormElementFormDataMultipleInputs() {
        ElementFactory factory = Jsoup.parseBodyFragments("").factory();
        Tag formTag = factory.tag("form");
        Element input1 = factory.tag("input", new Attributes().put("name", "input1").put("type", "text").put("value", "value1"));
        Element input2 = factory.tag("input", new Attributes().put("name", "input2").put("type", "text").put("value", "value2"));
        FormElement formElement = new FormElement(formTag, "http://example.com", new Attributes());
        formElement.addElement(input1);
        formElement.addElement(input2);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("input1", "value1"),
                new Connection.KeyVal("input2", "value2")
        );

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData.size(), actualFormData.size());
        for (Connection.KeyVal keyVal : expectedFormData) {
            Connection.KeyVal actualKeyVal = findKeyVal(actualFormData, keyVal.key());
            assertEquals(keyVal.value(), actualKeyVal.value());
        }
    }

    @Test
    public void testFormElementFormDataSelectElement() {
        ElementFactory factory = Jsoup.parseBodyFragments("").factory();
        Tag formTag = factory.tag("form");
        Element select = factory.tag("select", new Attributes().put("name", "select1"));
        Element option1 = factory.tag("option", new Attributes().put("value", "value1").put("selected", "selected"));
        Element option2 = factory.tag("option", new Attributes().put("value", "value2"));
        select.add(option1);
        select.add(option2);
        FormElement formElement = new FormElement(formTag, "http://example.com", new Attributes());
        formElement.addElement(select);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("select1", "value1")
        );

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData.size(), actualFormData.size());
        for (Connection.KeyVal keyVal : expectedFormData) {
            Connection.KeyVal actualKeyVal = findKeyVal(actualFormData, keyVal.key());
            assertEquals(keyVal.value(), actualKeyVal.value());
        }
    }

    @Test
    public void testFormElementFormDataCheckboxAndRadioElements() {
        ElementFactory factory = Jsoup.parseBodyFragments("").factory();
        Tag formTag = factory.tag("form");
        Element checkbox1 = factory.tag("input", new Attributes().put("name", "checkbox1").put("type", "checkbox"));
        Element checkbox2 = factory.tag("input", new Attributes().put("name", "checkbox1").put("type", "checkbox").put("checked", "checked"));
        Element radio1 = factory.tag("input", new Attributes().put("name", "radio1").put("type", "radio"));
        Element radio2 = factory.tag("input", new Attributes().put("name", "radio1").put("type", "radio").put("checked", "checked"));
        FormElement formElement = new FormElement(formTag, "http://example.com", new Attributes());
        formElement.addElement(checkbox1);
        formElement.addElement(checkbox2);
        formElement.addElement(radio1);
        formElement.addElement(radio2);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("checkbox1", "on"),
                new Connection.KeyVal("radio1", "on")
        );

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData.size(), actualFormData.size());
        for (Connection.KeyVal keyVal : expectedFormData) {
            Connection.KeyVal actualKeyVal = findKeyVal(actualFormData, keyVal.key());
            assertEquals(keyVal.value(), actualKeyVal.value());
        }
    }

    private Connection.KeyVal findKeyVal(List<Connection.KeyVal> list, String key) {
        for (Connection.KeyVal keyVal : list) {
            if (keyVal.key().equals(key)) {
                return keyVal;
            }
        }
        return null;
    }
}