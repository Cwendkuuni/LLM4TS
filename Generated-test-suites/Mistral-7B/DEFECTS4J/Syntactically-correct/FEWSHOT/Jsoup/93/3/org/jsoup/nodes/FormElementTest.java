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
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        assertEquals("form", formElement.tagName());
        assertEquals("http://example.com", formElement.baseUri());
        assertNull(formElement.text());
    }

    @Test
    public void testFormElementElements() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "http://example.com", new Attributes());

        Element inputTag1 = new Tag("input");
        inputTag1.putAttribute("type", "text");
        inputTag1.putAttribute("name", "name1");
        Element inputElement1 = new Element("input", inputTag1);
        formElement.addElement(inputElement1);

        Element inputTag2 = new Tag("input");
        inputTag2.putAttribute("type", "text");
        inputTag2.putAttribute("name", "name2");
        Element inputElement2 = new Element("input", inputTag2);
        formElement.addElement(inputElement2);

        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        assertSame(inputElement1, elements.get(0));
        assertSame(inputElement2, elements.get(1));
    }

    @Test
    public void testFormElementSubmit() {
        String baseUri = "http://example.com";
        String actionUri = "http://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", actionUri));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element inputTag = new Tag("input");
        inputTag.putAttribute("type", "text");
        inputTag.putAttribute("name", "name");
        inputTag.putAttribute("value", "value");
        Element inputElement = new Element("input", inputTag);
        formElement.addElement(inputElement);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals(actionUri, connection.url());
        List<Connection.KeyVal> formData = connection.data();
        assertEquals(1, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("value", formData.get(0).value());
    }

    @Test
    public void testFormElementSubmitPost() {
        String baseUri = "http://example.com";
        String actionUri = "http://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", actionUri).put("method", "post"));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element inputTag = new Tag("input");
        inputTag.putAttribute("type", "text");
        inputTag.putAttribute("name", "name");
        inputTag.putAttribute("value", "value");
        Element inputElement = new Element("input", inputTag);
        formElement.addElement(inputElement);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals(actionUri, connection.url());
        List<Connection.KeyVal> formData = connection.data();
        assertEquals(1, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("value", formData.get(0).value());
    }

    @Test
    public void testFormElementNoAction() {
        String baseUri = "http://example.com";

        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element inputTag = new Tag("input");
        inputTag.putAttribute("type", "text");
        inputTag.putAttribute("name", "name");
        inputTag.putAttribute("value", "value");
        Element inputElement = new Element("input", inputTag);
        formElement.addElement(inputElement);

        try {
            formElement.submit();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormElementSelectElement() {
        String baseUri = "http://example.com";
        String actionUri = "http://example.com/submit";

        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes().put("action", actionUri));

        Tag selectTag = new Tag("select");
        Element selectElement = new Element("select", selectTag);
        formElement.addElement(selectElement);

        Tag optionTag1 = new Tag("option");
        optionTag1.putAttribute("value", "option1");
        Element optionElement1 = new Element("option", optionTag1);
        selectElement.addElement(optionElement1);

        Tag optionTag2 = new Tag("option");
        optionTag2.putAttribute("value", "option2");
        optionTag2.putAttribute("selected", "");
        Element optionElement2 = new Element("option", optionTag2);
        selectElement.addElement(optionElement2);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals(actionUri, connection.url());
        List<Connection.KeyVal> formData = connection.data();
        assertEquals(1, formData.size());
        assertEquals("select", formData.get(0).key());
        assertEquals("option2", formData.get(0).value());
    }

    @Test
    public void testFormElementCheckboxOrRadio() {
        String baseUri = "http://example.com";
        String actionUri = "http://example.com/submit";

        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes().put("action", actionUri));

        Tag inputTag = new Tag("input");
        inputTag.putAttribute("type", "checkbox");
        inputTag.putAttribute("name", "name");
        Element checkboxElement = new Element("input", inputTag);
        formElement.addElement(checkboxElement);

        checkboxElement.putAttribute("checked", "");

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals(actionUri, connection.url());
        List<Connection.KeyVal> formData = connection.data();
        assertEquals(1, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("on", formData.get(0).value());

        checkboxElement.removeAttribute("checked");

        connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals(actionUri, connection.url());
        List<Connection.KeyVal> formData2 = connection.data();
        assertEquals(0, formData2.size());
    }

    @Test
    public void testFormElementRemoveChild() {
        String baseUri = "http://example.com";
        String actionUri = "http://example.com/submit";

        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes().put("action", actionUri));

        Element inputTag1 = new Tag("input");
        inputTag1.putAttribute("type", "text");
        inputTag1.putAttribute("name", "name1");
        Element inputElement1 = new Element("input", inputTag1);
        formElement.addElement(inputElement1);

        Element inputTag2 = new Tag("input");
        inputTag2.putAttribute("type", "text");
        inputTag2.putAttribute("name", "name2");
        Element inputElement2 = new Element("input", inputTag2);
        formElement.addElement(inputElement2);

        formElement.removeChild(inputElement1);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertSame(inputElement2, elements.get(0));
    }

    @Test
    public void testFormElementEmptyFormData() {
        String baseUri = "http://example.com";
        String actionUri = "http://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", actionUri));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementFormDataWithElements() {
        String baseUri = "http://example.com";
        String actionUri = "http://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", actionUri));

        Tag inputTag1 = new Tag("input");
        inputTag1.putAttribute("type", "text");
        inputTag1.putAttribute("name", "name1");
        inputTag1.putAttribute("value", "value1");
        Element inputElement1 = new Element("input", inputTag1);
        formTag.addElement(inputElement1);

        Tag inputTag2 = new Tag("input");
        inputTag2.putAttribute("type", "text");
        inputTag2.putAttribute("name", "name2");
        inputTag2.putAttribute("value", "value2");
        Element inputElement2 = new Element("input", inputTag2);
        formTag.addElement(inputElement2);

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("name1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("name2", formData.get(1).key());
        assertEquals("value2", formData.get(1).value());
    }
}