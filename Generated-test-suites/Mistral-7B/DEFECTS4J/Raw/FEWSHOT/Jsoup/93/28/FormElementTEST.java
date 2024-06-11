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
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit");

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tag().name());
        assertEquals("/submit", formElement.attr("action"));
        assertNull(formElement.attr("method"));
        assertSame(baseUri, formElement.baseUri());
    }

    @Test
    public void testEmptyFormElements() {
        Tag tag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddAndRemoveElements() {
        Tag tag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element input1 = new InputElement("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new InputElement("input", new Attributes().put("type", "text").put("name", "input2"));

        formElement.addElement(input1);
        formElement.addElement(input2);

        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(input1));
        assertTrue(elements.contains(input2));

        formElement.removeChild(input1);
        elements = formElement.elements();
        assertEquals(1, elements.size());
        assertFalse(elements.contains(input1));
        assertTrue(elements.contains(input2));
    }

    @Test
    public void testSubmitForm() {
        String baseUri = "https://example.com";
        String actionUrl = "https://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", actionUrl));
        Element input1 = new InputElement("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new InputElement("input", new Attributes().put("type", "text").put("name", "input2"));

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.addElement(input1);
        formElement.addElement(input2);

        Connection connection = formElement.submit();
        assertEquals(actionUrl, connection.url());
        List<Connection.KeyVal> formData = connection.requestBody();
        assertEquals(2, formData.size());

        assertEquals("input1", formData.get(0).key());
        assertEquals("", formData.get(0).value());

        assertEquals("input2", formData.get(1).key());
        assertEquals("", formData.get(1).value());
    }

    @Test
    public void testSubmitFormWithValues() {
        String baseUri = "https://example.com";
        String actionUrl = "https://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", actionUrl));
        Element input1 = new InputElement("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new InputElement("input", new Attributes().put("type", "text").put("name", "input2").put("value", "value1"));

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.addElement(input1);
        formElement.addElement(input2);

        Connection connection = formElement.submit();
        assertEquals(actionUrl, connection.url());
        List<Connection.KeyVal> formData = connection.requestBody();
        assertEquals(2, formData.size());

        assertEquals("input1", formData.get(0).key());
        assertEquals("", formData.get(0).value());

        assertEquals("input2", formData.get(1).key());
        assertEquals("value1", formData.get(1).value());
    }

    @Test
    public void testSubmitFormWithSelectElement() {
        String baseUri = "https://example.com";
        String actionUrl = "https://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", actionUrl));
        Element selectElement = new SelectElement("select", new Attributes().put("name", "selectElement"));
        Element option1 = new OptionElement("option", new Attributes().put("value", "option1").put("selected", "selected"));
        Element option2 = new OptionElement("option", new Attributes().put("value", "option2"));

        selectElement.addElement(option1);
        selectElement.addElement(option2);
        formElement.addElement(selectElement);

        Connection connection = formElement.submit();
        assertEquals(actionUrl, connection.url());
        List<Connection.KeyVal> formData = connection.requestBody();
        assertEquals(1, formData.size());

        assertEquals("selectElement", formData.get(0).key());
        assertEquals("option1", formData.get(0).value());
    }

    @Test
    public void testSubmitFormWithCheckedCheckboxOrRadio() {
        String baseUri = "https://example.com";
        String actionUrl = "https://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", actionUrl));
        Element checkboxElement = new InputElement("input", new Attributes().put("type", "checkbox").put("name", "checkboxElement").put("value", "value1"));
        Element radioElement1 = new InputElement("input", new Attributes().put("type", "radio").put("name", "radioElement").put("value", "value1").put("checked", "checked"));
        Element radioElement2 = new InputElement("input", new Attributes().put("type", "radio").put("name", "radioElement").put("value", "value2"));

        formElement.addElement(checkboxElement);
        formElement.addElement(radioElement1);
        formElement.addElement(radioElement2);

        Connection connection = formElement.submit();
        assertEquals(actionUrl, connection.url());
        List<Connection.KeyVal> formData = connection.requestBody();
        assertEquals(2, formData.size());

        assertEquals("checkboxElement", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());

        assertEquals("radioElement", formData.get(1).key());
        assertEquals("value1", formData.get(1).value());
    }
}