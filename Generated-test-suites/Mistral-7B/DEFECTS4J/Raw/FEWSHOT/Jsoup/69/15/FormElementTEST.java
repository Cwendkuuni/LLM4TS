package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testGetFormControls() {
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Tag tagInputText1 = new Tag("input");
        attributes = new Attributes().put("type", "text").put("name", "textInput1").put("value", "value1");
        Element inputText1 = new Element(tagInputText1, baseUri, attributes);

        Tag tagInputText2 = new Tag("input");
        attributes = new Attributes().put("type", "text").put("name", "textInput2").put("value", "value2");
        Element inputText2 = new Element(tagInputText1, baseUri, attributes);

        Tag tagSelect = new Tag("select");
        Elements options = new Elements();
        options.add(new Element(new Tag("option").put("value", "option1").put("selected", true), baseUri));
        options.add(new Element(new Tag("option").put("value", "option2"), baseUri));
        Element select = new Element(tagSelect, baseUri, new Attributes().put("name", "selectElement"));
        select.select("option").add(options);

        Element formElement = new FormElement(tagForm, baseUri, new Attributes());
        formElement.addElement(inputText1);
        formElement.addElement(inputText2);
        formElement.addElement(select);

        Elements formControls = formElement.elements();
        assertEquals(3, formControls.size());
        assertTrue(formControls.contains(inputText1));
        assertTrue(formControls.contains(inputText2));
        assertTrue(formControls.contains(select));
    }

    @Test
    public void testSubmitForm() {
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit");

        Tag tagInputText = new Tag("input");
        attributes = new Attributes().put("type", "text").put("name", "textInput").put("value", "textValue");
        Element inputText = new Element(tagInputText, baseUri, attributes);

        Element formElement = new FormElement(tagForm, baseUri, attributes);
        formElement.addElement(inputText);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("/submit", connection.url());
        assertEquals(Connection.Method.GET, connection.method());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("textInput", keyVal.key());
        assertEquals("textValue", keyVal.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitFormWithoutAction() {
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Element formElement = new FormElement(tagForm, baseUri, attributes);
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Tag tagInputText1 = new Tag("input");
        attributes = new Attributes().put("type", "text").put("name", "textInput1").put("value", "value1");
        Element inputText1 = new Element(tagInputText1, baseUri, attributes);

        Tag tagInputHidden1 = new Tag("input");
        attributes = new Attributes().put("type", "hidden").put("name", "hiddenInput1").put("value", "hiddenValue1");
        Element hiddenInput1 = new Element(tagInputHidden1, baseUri, attributes);

        Tag tagInputText2 = new Tag("input");
        attributes = new Attributes().put("type", "text").put("name", "textInput2").put("value", "value2");
        Element inputText2 = new Element(tagInputText1, baseUri, attributes);

        Tag tagSelect = new Tag("select");
        Elements options = new Elements();
        options.add(new Element(new Tag("option").put("value", "option1").put("selected", true), baseUri));
        options.add(new Element(new Tag("option").put("value", "option2"), baseUri));
        Element select = new Element(tagSelect, baseUri, new Attributes().put("name", "selectElement"));
        select.select("option").add(options);

        Element formElement = new FormElement(tagForm, baseUri, new Attributes());
        formElement.addElement(inputText1);
        formElement.addElement(hiddenInput1);
        formElement.addElement(inputText2);
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(5, formData.size());
        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("textInput1", keyVal1.key());
        assertEquals("value1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("hiddenInput1", keyVal2.key());
        assertEquals("hiddenValue1", keyVal2.value());

        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("textInput2", keyVal3.key());
        assertEquals("value2", keyVal3.value());

        Connection.KeyVal keyVal4 = formData.get(3);
        assertEquals("selectElement", keyVal4.key());
        assertEquals("option1", keyVal4.value());

        Connection.KeyVal keyVal5 = formData.get(4);
        assertEquals("selectElement", keyVal5.key());
        assertEquals("option2", keyVal5.value());
    }

    @Test
    public void testSubmitFormWithEmptyInput() {
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Element formElement = new FormElement(tagForm, baseUri, attributes);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(baseUri, connection.url());
        assertEquals(Connection.Method.GET, connection.method());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testSubmitFormWithDisabledElement() {
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Tag tagInputText = new Tag("input");
        attributes = new Attributes().put("type", "text").put("name", "textInput").put("value", "value1").put("disabled", "disabled");
        Element inputText = new Element(tagInputText, baseUri, attributes);

        Element formElement = new FormElement(tagForm, baseUri, new Attributes());
        formElement.addElement(inputText);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(baseUri, connection.url());
        assertEquals(Connection.Method.GET, connection.method());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }
}