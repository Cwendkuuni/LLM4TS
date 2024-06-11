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
        FormElement formElement = new FormElement(tag, null, null);
        assertEquals("form", formElement.tagName());
        assertNull(formElement.baseUri());
        assertNull(formElement.attributes());
        assertEquals(0, formElement.children().size());
    }

    @Test
    public void testGetElements() {
        Tag formTag = new Tag("form");
        Element input1Tag = new Tag("input");
        Element input2Tag = new Tag("input");
        FormElement formElement = new FormElement(formTag, null, null);
        formElement.addElement(input1Tag);
        formElement.addElement(input2Tag);

        List<Element> elements = formElement.elements().stream().collect(Collectors.toList());
        assertEquals(2, elements.size());
        assertTrue(elements.contains(input1Tag));
        assertTrue(elements.contains(input2Tag));
    }

    @Test
    public void testAddElement() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, null);
        Element inputElement = new Element("input");

        formElement.addElement(inputElement);
        assertEquals(1, formElement.elements().size());
        assertSame(inputElement, formElement.elements().get(0));
    }

    @Test
    public void testSubmitWithNoActionAttribute() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, formElement::submit);
        assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", exception.getMessage());
    }

    @Test
    public void testSubmitWithActionAttribute() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form", null, new Attributes().put("action", "/path/to/submit"));
        FormElement formElement = new FormElement(formTag, baseUri, null);

        Connection connection = formElement.submit();
        assertEquals("/path/to/submit", connection.url());
    }

    @Test
    public void testSubmitWithGetMethod() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form", null, new Attributes().put("action", "/path/to/submit").put("method", "get"));
        FormElement formElement = new FormElement(formTag, baseUri, null);

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testSubmitWithPostMethod() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form", null, new Attributes().put("action", "/path/to/submit").put("method", "post"));
        FormElement formElement = new FormElement(formTag, baseUri, null);

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormDataEmptyForm() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, null);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithInputElements() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form");
        Element input1Tag = new Element("input", null, new Attributes().put("name", "input1").put("type", "text").put("value", "value1"));
        Element input2Tag = new Element("input", null, new Attributes().put("name", "input2").put("type", "text").put("value", "value2"));
        FormElement formElement = new FormElement(formTag, baseUri, null);
        formElement.addElement(input1Tag);
        formElement.addElement(input2Tag);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("input2", formData.get(1).key());
        assertEquals("value2", formData.get(1).value());
    }

    @Test
    public void testFormDataWithSelectElement() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form");
        Element selectTag = new Element("select", null, new Attributes().put("name", "select"));
        Element option1Tag = new Element("option", null, new Attributes().put("value", "option1").put("selected", "selected"));
        Element option2Tag = new Element("option", null, new Attributes().put("value", "option2"));
        formElement.addElement(selectTag);
        selectTag.addElement(option1Tag);
        selectTag.addElement(option2Tag);
        FormElement formElement = new FormElement(formTag, baseUri, null);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("select", formData.get(0).key());
        assertEquals("option1", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxOrRadioButtons() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form");
        Element checkboxTag = new Element("input", null, new Attributes().put("name", "checkbox").put("type", "checkbox").put("checked", "checked"));
        FormElement formElement = new FormElement(formTag, baseUri, null);
        formElement.addElement(checkboxTag);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("checkbox", formData.get(0).key());
        assertEquals("on", formData.get(0).value());

        // test radio buttons
        Tag radioFormTag = new Tag("form");
        Element radioButton1Tag = new Element("input", null, new Attributes().put("name", "radio").put("type", "radio").put("checked", "checked"));
        Element radioButton2Tag = new Element("input", null, new Attributes().put("name", "radio").put("type", "radio"));
        FormElement radioFormElement = new FormElement(radioFormTag, baseUri, null);
        radioFormElement.addElement(radioButton1Tag);
        radioFormElement.addElement(radioButton2Tag);

        List<Connection.KeyVal> radioFormData = radioFormElement.formData();
        assertEquals(1, radioFormData.size());
        assertEquals("radio", radioFormData.get(0).key());
        assertEquals("on", radioFormData.get(0).value());
    }
}