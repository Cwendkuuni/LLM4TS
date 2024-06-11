package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("method", "POST");
        attributes.put("action", "/submit");

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals("POST", formElement.attr("method"));
        assertEquals("/submit", formElement.attr("action"));
    }

    @Test
    public void testGetElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("method", "POST");
        attributes.put("action", "/submit");

        Element inputTextElement = new Element("input", Arrays.asList("type", "text"), new Attributes().put("name", "name1").put("value", "value1"));
        Element inputTextElement2 = new Element("input", Arrays.asList("type", "text"), new Attributes().put("name", "name2").put("value", "value2"));
        Element textAreaElement = new Element("textarea", Arrays.asList("name", "name3"), new Attributes().put("value", "value3"));
        Element selectElement = new Element("select", Arrays.asList(), new Attributes());
        Element option1Element = new Element("option", Arrays.asList("value", "value1"), new Attributes().put("selected", "true"));
        Element option2Element = new Element("option", Arrays.asList("value", "value2"));
        selectElement.add(option1Element);
        selectElement.add(option2Element);
        Elements elements = new Elements();
        elements.add(inputTextElement);
        elements.add(inputTextElement2);
        elements.add(textAreaElement);
        elements.add(selectElement);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.elements = elements;

        Elements formControls = formElement.elements();
        assertEquals(4, formControls.size());
        assertTrue(formControls.contains(inputTextElement));
        assertTrue(formControls.contains(inputTextElement2));
        assertTrue(formControls.contains(textAreaElement));
        assertTrue(formControls.contains(selectElement));
    }

    @Test
    public void testAddElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("method", "POST");
        attributes.put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element inputTextElement = new Element("input", Arrays.asList("type", "text"), new Attributes().put("name", "name1").put("value", "value1"));
        FormElement formElement2 = new FormElement(formTag, baseUri, attributes);

        formElement.addElement(inputTextElement);
        formElement2.addElement(inputTextElement);

        assertEquals(1, formElement.elements().size());
        assertEquals(1, formElement2.elements().size());
        assertTrue(formElement.elements().contains(inputTextElement));
        assertTrue(formElement2.elements().contains(inputTextElement));
    }

    @Test
    public void testSubmit() throws Exception {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST").put("action", "/submit");

        Tag formTag = new Tag("form", Arrays.asList("method", "POST"), attributes);
        Element inputTextElement = new Element("input", Arrays.asList("type", "text"), new Attributes().put("name", "name1").put("value", "value1"));
        Element textAreaElement = new Element("textarea", Arrays.asList("name", "name3"), new Attributes().put("value", "value3"));
        Element selectElement = new Element("select", Arrays.asList(), new Attributes());
        Element option1Element = new Element("option", Arrays.asList("value", "value1"), new Attributes().put("selected", "true"));
        Element option2Element = new Element("option", Arrays.asList("value", "value2"));
        selectElement.add(option1Element);
        selectElement.add(option2Element);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.elements.add(inputTextElement);
        formElement.elements.add(textAreaElement);
        formElement.elements.add(selectElement);

        Connection connection = formElement.submit();
        String expectedFormData = "name1=value1&name3=value3&name=value1";
        List<Connection.KeyVal> formData = connection.requestBody();
        assertEquals(expectedFormData, formData.toString());
    }

    @Test
    public void testFormDataEmptyForm() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST").put("action", "/submit");

        Tag formTag = new Tag("form", Arrays.asList("method", "POST"), attributes);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataSelectElementWithSingleSelectedOption() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST").put("action", "/submit");

        Tag formTag = new Tag("form", Arrays.asList("method", "POST"), attributes);
        Element selectElement = new Element("select", Arrays.asList(), new Attributes());
        Element option1Element = new Element("option", Arrays.asList("value", "value1"), new Attributes().put("selected", "true"));
        selectElement.add(option1Element);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.elements.add(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals("value1", formData.get(0).getValue());
        assertEquals("value1", formData.get(0).getName());
    }

    @Test
    public void testFormDataSelectElementWithMultipleSelectedOptions() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST").put("action", "/submit");

        Tag formTag = new Tag("form", Arrays.asList("method", "POST"), attributes);
        Element selectElement = new Element("select", Arrays.asList(), new Attributes());
        Element option1Element = new Element("option", Arrays.asList("value", "value1"), new Attributes().put("selected", "true"));
        Element option2Element = new Element("option", Arrays.asList("value", "value2"), new Attributes().put("selected", "true"));
        selectElement.add(option1Element);
        selectElement.add(option2Element);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.elements.add(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("value1", formData.get(0).getValue());
        assertEquals("value1", formData.get(0).getName());
        assertEquals("value2", formData.get(1).getValue());
        assertEquals("value2", formData.get(1).getName());
    }

    @Test
    public void testFormDataCheckboxOrRadioElementWithChecked() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST").put("action", "/submit");

        Tag formTag = new Tag("form", Arrays.asList("method", "POST"), attributes);
        Element checkboxElement = new Element("input", Arrays.asList("type", "checkbox"), new Attributes().put("name", "name1").put("value", "value1"));
        checkboxElement.hasAttr("checked");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.elements.add(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("value1", formData.get(0).getValue());
        assertEquals("name1", formData.get(0).getName());
    }

    @Test
    public void testFormDataCheckboxOrRadioElementWithoutChecked() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST").put("action", "/submit");

        Tag formTag = new Tag("form", Arrays.asList("method", "POST"), attributes);
        Element checkboxElement = new Element("input", Arrays.asList("type", "checkbox"), new Attributes().put("name", "name1").put("value", "value1"));

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.elements.add(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataElementWithEmptyName() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST").put("action", "/submit");

        Tag formTag = new Tag("form", Arrays.asList("method", "POST"), attributes);
        Element element = new Element("input", Arrays.asList("type", "text"), new Attributes().put("name", ""));

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.elements.add(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }
}