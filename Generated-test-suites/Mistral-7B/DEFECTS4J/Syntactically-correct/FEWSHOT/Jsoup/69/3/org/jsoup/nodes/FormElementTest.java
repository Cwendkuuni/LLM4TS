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
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "submit.php");
        attributes.put("method", "post");
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals("submit.php", formElement.attr("action"));
        assertEquals("post", formElement.attr("method").toUpperCase());
        assertNull(formElement.val());
    }

    @Test
    public void testElements() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Tag formTag = new Tag("form");
        Element inputElement1 = new Element("input", Arrays.asList("type", "text", "name", "username"));
        Element inputElement2 = new Element("input", Arrays.asList("type", "password", "name", "password"));
        Element textAreaElement = new Element("textarea", Arrays.asList("name", "message"));
        formTag.children().add(inputElement1);
        formTag.children().add(inputElement2);
        formTag.children().add(textAreaElement);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        assertEquals(3, formElement.elements().size());
        assertTrue(formElement.elements().get(0) instanceof InputElement);
        assertTrue(formElement.elements().get(1) instanceof InputElement);
        assertTrue(formElement.elements().get(2) instanceof TextareaElement);
    }

    @Test
    public void testAddElement() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element("input", Arrays.asList("type", "text", "name", "username"));
        formElement.addElement(inputElement);
        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().get(0));
    }

    @Test
    public void testSubmitNullForm() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        try {
            formElement.submit();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubmitWithBaseUri() {
        String baseUri = "http://example.com";
        String action = "submit.php";
        Attributes attributes = new Attributes();
        attributes.put("action", action);
        Tag formTag = new Tag("form", Arrays.asList("method", "post"));
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Connection connection = formElement.submit();
        assertEquals("http://example.com/submit.php", connection.url().toString());
    }

    @Test
    public void testSubmitWithoutBaseUri() {
        String action = "submit.php";
        Attributes attributes = new Attributes();
        attributes.put("action", action);
        Tag formTag = new Tag("form", Arrays.asList("method", "post"));
        FormElement formElement = new FormElement(formTag, null, attributes);
        try {
            formElement.submit();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormDataEmptyForm() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(0, data.size());
    }

    @Test
    public void testFormDataSimpleForm() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "submit.php");
        attributes.put("method", "post");
        Tag formTag = new Tag("form");
        Element inputElement1 = new Element("input", Arrays.asList("type", "text", "name", "username"));
        Element inputElement2 = new Element("input", Arrays.asList("type", "password", "name", "password"));
        formTag.children().add(inputElement1);
        formTag.children().add(inputElement2);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("", data.get(0).value());
        assertEquals("password", data.get(1).key());
        assertEquals("", data.get(1).value());
    }

    @Test
    public void testFormDataWithValues() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "submit.php");
        attributes.put("method", "post");
        Tag formTag = new Tag("form");
        Element inputElement1 = new Element("input", Arrays.asList("type", "text", "name", "username"));
        Element inputElement1Value = new Element("value", Arrays.asList("value", "Alice"));
        inputElement1.children().add(inputElement1Value);
        Element inputElement2 = new Element("input", Arrays.asList("type", "password", "name", "password"));
        Element inputElement2Value = new Element("value", Arrays.asList("value", "password"));
        inputElement2.children().add(inputElement2Value);
        formTag.children().add(inputElement1);
        formTag.children().add(inputElement2);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("Alice", data.get(0).value());
        assertEquals("password", data.get(1).key());
        assertEquals("password", data.get(1).value());
    }

    @Test
    public void testFormDataWithSelect() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "submit.php");
        attributes.put("method", "post");
        Tag formTag = new Tag("form");
        Element inputElement1 = new Element("input", Arrays.asList("type", "hidden", "name", "_csrf"));
        Element inputElement2 = new Element("input", Arrays.asList("type", "hidden", "name", "csrfToken"));
        Element selectElement = new Element("select", Arrays.asList("name", "color"));
        Element optionElement1 = new Element("option", Arrays.asList("selected", "value", "red"));
        Element optionElement2 = new Element("option", Arrays.asList("value", "blue"));
        selectElement.children().add(optionElement1);
        selectElement.children().add(optionElement2);
        formTag.children().add(inputElement1);
        formTag.children().add(inputElement2);
        formTag.children().add(selectElement);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(4, data.size());
        assertEquals("_csrf", data.get(0).key());
        assertEquals("", data.get(0).value());
        assertEquals("csrfToken", data.get(1).key());
        assertEquals("", data.get(1).value());
        assertEquals("color", data.get(2).key());
        assertEquals("red", data.get(2).value());
        assertEquals("", data.get(3).value());
    }
}