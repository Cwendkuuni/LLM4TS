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
        assertEquals("http://example.com", formElement.baseUri());
    }

    @Test
    public void testElements() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Elements elements = formElement.elements();

        assertEquals(0, elements.size());

        Element inputElement = new Element("input", Arrays.asList("type", "text"));
        formElement.addElement(inputElement);

        assertEquals(1, elements.size());
        assertSame(inputElement, elements.get(0));

        formElement.removeChild(inputElement);
        assertEquals(0, elements.size());
    }

    @Test
    public void testSubmit() {
        String baseUri = "http://example.com";
        String action = "http://example.com/submit";

        Tag formTag = new Tag("form", Arrays.asList("action", action));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        Element inputElement = new Element("input", Arrays.asList("name", "username", "type", "text"));
        formElement.addElement(inputElement);
        formElement.addElement(new Element("input", Arrays.asList("name", "password", "type", "password")));

        formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("", formData.get(0).value()); // empty by default
        assertEquals("password", formData.get(1).key());
        assertEquals("", formData.get(1).value()); // empty by default

        inputElement.attr("value", "testUsername");
        formElement.removeChild(formElement.elements().get(1));

        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testUsername", formData.get(0).value());

        Connection connection = formElement.submit();
        assertNull(connection); // not implemented in this test
    }

    @Test
    public void testFormDataSelect() {
        String baseUri = "http://example.com";
        String action = "http://example.com/submit";

        Tag formTag = new Tag("form", Arrays.asList("action", action));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element selectElement = new Element("select", Arrays.asList("name", "options"));
        formElement.addElement(selectElement);

        Element option1 = new Element("option", Arrays.asList("selected", "value", "My Option 1"));
        Element option2 = new Element("option", Arrays.asList("value", "My Option 2"));
        selectElement.addElement(option1);
        selectElement.addElement(option2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        formElement.removeChild(option1);

        formData = formElement.formData();
        assertEquals(0, formData.size());

        formData = formElement.formData(); // should be empty since no option is selected
        assertEquals(0, formData.size());

        formElement.addElement(option1);

        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("options", formData.get(0).key());
        assertEquals("My Option 1", formData.get(0).value());

        formElement.removeChild(option1);

        formData = formElement.formData();
        assertEquals(0, formData.size());

        formElement.addElement(option2);

        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("options", formData.get(0).key());
        assertEquals("My Option 2", formData.get(0).value());
    }

    @Test
    public void testFormDataCheckboxAndRadio() {
        String baseUri = "http://example.com";
        String action = "http://example.com/submit";

        Tag formTag = new Tag("form", Arrays.asList("action", action));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element checkboxElement = new Element("input", Arrays.asList("name", "checkBox", "type", "checkbox"));
        formElement.addElement(checkboxElement);

        Element radioGroup = new Element("div", new Attributes().put("name", "radioGroup"));
        Element radio1 = new Element("input", Arrays.asList("name", "radioGroup", "type", "radio"));
        Element label1 = new Element("label", Arrays.asList("for", "radio1"));
        label1.text("Option 1");
        radioGroup.addElement(radio1);
        radioGroup.addElement(label1);
        formElement.addElement(radioGroup);

        Element radio2 = new Element("input", Arrays.asList("name", "radioGroup", "type", "radio"));
        Element label2 = new Element("label", Arrays.asList("for", "radio2"));
        label2.text("Option 2");
        radioGroup.addElement(radio2);
        radioGroup.addElement(label2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        checkboxElement.attr("checked", "checked");

        formData = formElement.formData();
        assertEquals(0, formData.size());

        checkboxElement.attr("checked", null); // remove checked attribute

        formData = formElement.formData();
        assertEquals(0, formData.size());

        radio1.attr("checked", "checked");

        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("radioGroup", formData.get(0).key());
        assertEquals("My Option 1", formData.get(0).value());

        radio1.attr("checked", null); // remove checked attribute

        formData = formElement.formData();
        assertEquals(0, formData.size());

        radio2.attr("checked", "checked");

        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("radioGroup", formData.get(0).key());
        assertEquals("My Option 2", formData.get(0).value());
    }
}