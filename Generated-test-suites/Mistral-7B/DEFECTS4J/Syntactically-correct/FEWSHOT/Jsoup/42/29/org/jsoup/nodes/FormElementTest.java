package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testNewFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertSame(tag, formElement.tag());
        assertSame(baseUri, formElement.baseUri());
        assertSame(attributes, formElement.attributes());
    }

    @Test
    public void testGetElements() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element inputElement = new Element("input", Arrays.asList("type", "text"), null);
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertSame(inputElement, elements.first());
    }

    @Test
    public void testSubmitWithEmptyForm() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Connection connection = formElement.submit();
        assertNull(connection);
    }

    @Test
    public void testSubmitWithFormData() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input", Arrays.asList("type", "text", "name", "username"));
        Tag inputTag2 = new Tag("input", Arrays.asList("type", "password", "name", "password"));
        Tag selectTag = new Tag("select", null, null);
        Tag optionTag1 = new Tag("option", Arrays.asList("value", "option1", "selected", "true"), null);
        Tag optionTag2 = new Tag("option", Arrays.asList("value", "option2"), null);
        selectTag.add(optionTag1);
        selectTag.add(optionTag2);

        FormElement formElement = new FormElement(formTag, null, null);
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);
        formElement.addElement(selectTag);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        List<Connection.KeyVal> formData = connection.data();
        assertEquals(3, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("", formData.get(0).val());
        assertEquals("password", formData.get(1).key());
        assertEquals("", formData.get(1).val());
        assertEquals("option1", formData.get(2).key());
        assertEquals("option1", formData.get(2).val());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNullForm() {
        FormElement formElement = null;
        formElement.submit();
    }

    @Test
    public void testFormDataWithEmptyForm() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, null);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithSingleInput() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input", Arrays.asList("type", "text", "name", "username"), null);
        FormElement formElement = new FormElement(formTag, null, null);
        formElement.addElement(inputTag);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("", formData.get(0).val());
    }

    @Test
    public void testFormDataWithSelect() {
        Tag formTag = new Tag("form");
        Tag selectTag = new Tag("select", null, null);
        Tag optionTag1 = new Tag("option", Arrays.asList("value", "option1", "selected", "true"), null);
        Tag optionTag2 = new Tag("option", Arrays.asList("value", "option2"), null);
        selectTag.add(optionTag1);
        selectTag.add(optionTag2);

        FormElement formElement = new FormElement(formTag, null, null);
        formElement.addElement(selectTag);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("", formData.get(0).key());
        assertEquals("option1", formData.get(0).val());
    }

    @Test
    public void testFormDataWithCheckbox() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input", Arrays.asList("type", "checkbox", "name", "checkbox"), null);
        FormElement formElement = new FormElement(formTag, null, null);
        formElement.addElement(inputTag);
        inputTag.hasAttr("checked", "checked");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("checkbox", formData.get(0).key());
        assertEquals("", formData.get(0).val());
    }

    @Test
    public void testFormDataWithRadio() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input", Arrays.asList("type", "radio", "name", "radio", "value", "value1"), null);
        Tag inputTag2 = new Tag("input", Arrays.asList("type", "radio", "name", "radio", "value", "value2"), null);
        FormElement formElement = new FormElement(formTag, null, null);
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);
        inputTag1.hasAttr("checked", "checked");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("radio", formData.get(0).key());
        assertEquals("value1", formData.get(0).val());
    }
}