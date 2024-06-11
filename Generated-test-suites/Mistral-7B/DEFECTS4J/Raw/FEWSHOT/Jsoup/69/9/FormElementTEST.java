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
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "myForm");

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals("form", formElement.tagName());
        assertEquals("myForm", formElement.attr("id"));
        assertEquals(baseUri, formElement.baseUri());
    }

    @Test
    public void testGetElements() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element input1 = new Element("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new Element("input", new Attributes().put("type", "text").put("name", "input2"));

        formElement.addElement(input1);
        formElement.addElement(input2);

        List<Element> elements = formElement.elements().list();

        assertEquals(2, elements.size());
        assertSame(input1, elements.get(0));
        assertSame(input2, elements.get(1));
    }

    @Test
    public void testSubmitWithNoAction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        try {
            formElement.submit();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubmitWithAction() {
        Tag formTag = new Tag("form");
        formTag.putAttr("action", "http://example.com/submit");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Connection connection = formElement.submit();

        assertEquals("http://example.com/submit", connection.url());
    }

    @Test
    public void testFormData() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element input1 = new Element("input", new Attributes().put("type", "text").put("name", "input1").put("value", "value1"));
        Element input2 = new Element("input", new Attributes().put("type", "text").put("name", "input2").put("value", "value2"));
        Element select = new Element("select", new Attributes().put("name", "select"));
        Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", true));
        Element option2 = new Element("option", new Attributes().put("value", "option2"));
        select.addElement(option1);
        select.addElement(option2);

        formElement.addElement(input1);
        formElement.addElement(input2);
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(4, formData.size());

        assertEquals("input1", formData.get(0).key);
        assertEquals("value1", formData.get(0).val);
        assertEquals("input2", formData.get(1).key);
        assertEquals("value2", formData.get(1).val);
        assertEquals("select", formData.get(2).key);
        assertEquals("option1", formData.get(3).key);
    }

    @Test
    public void testFormDataWithDisabledInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element input = new Element("input", new Attributes().put("type", "text").put("name", "input").put("disabled", "disabled"));

        formElement.addElement(input);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithSelectNoOptionSelected() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element select = new Element("select", new Attributes().put("name", "select"));

        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithCheckboxOrRadioNotChecked() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element input = new Element("input", new Attributes().put("type", "checkbox").put("name", "input"));

        formElement.addElement(input);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithCheckboxOrRadioChecked() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element input = new Element("input", new Attributes().put("type", "checkbox").put("name", "input").put("checked", "checked"));

        formElement.addElement(input);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("input", formData.get(0).key);
        assertEquals("on", formData.get(0).val);
    }
}