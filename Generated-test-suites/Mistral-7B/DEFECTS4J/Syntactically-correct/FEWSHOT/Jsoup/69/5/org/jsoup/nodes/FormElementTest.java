package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
        attributes.put("id", "myForm");

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals("myForm", formElement.attr("id"));
        assertEquals(baseUri, formElement.baseUri());
    }

    @Test
    public void testEmptyFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "https://example.com";

        FormElement formElement = new FormElement(tag, baseUri, new Attributes());
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testAddFormControlElement() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes formAttributes = new Attributes();
        formAttributes.put("id", "myForm");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag = new Tag("input");
        inputTag.put("type", "text");
        inputTag.put("name", "username");
        inputTag.put("value", "testuser");

        FormElement inputElement = new FormElement(inputTag, baseUri, new Attributes());

        formElement.addElement(inputElement);

        List<Element> formControls = formElement.elements();
        assertEquals(1, formControls.size());
        assertEquals("input", formControls.get(0).tagName());
        assertEquals("username", formControls.get(0).attr("name"));
        assertEquals("testuser", formControls.get(0).attr("value"));
    }

    @Test
    public void testSubmitForm() {
        String baseUri = "https://example.com";

        Tag formTag = new Tag("form");
        formTag.put("action", "/submit");
        formTag.put("method", "POST");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag1 = new Tag("input");
        inputTag1.put("type", "text");
        inputTag1.put("name", "username");
        inputTag1.put("value", "testuser1");
        FormElement inputElement1 = new FormElement(inputTag1, baseUri, new Attributes());

        Tag inputTag2 = new Tag("input");
        inputTag2.put("type", "text");
        inputTag2.put("name", "password");
        inputTag2.put("value", "testpass1");
        FormElement inputElement2 = new FormElement(inputTag2, baseUri, new Attributes());

        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("/submit", connection.url());
        assertEquals(HttpConnection.ConnectionMethod.POST, connection.method());

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testuser1", formData.get(0).value());
        assertEquals("password", formData.get(1).key());
        assertEquals("testpass1", formData.get(1).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitFormWithoutBaseUri() {
        Tag formTag = new Tag("form");
        formTag.put("action", "/submit");
        formTag.put("method", "POST");
        Attributes formAttributes = new Attributes();

        FormElement formElement = new FormElement(formTag, null, formAttributes);
        formElement.submit();
    }

    @Test
    public void testSubmitFormWithDisabledFormControlElement() {
        String baseUri = "https://example.com";

        Tag formTag = new Tag("form");
        formTag.put("action", "/submit");
        formTag.put("method", "POST");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag1 = new Tag("input");
        inputTag1.put("type", "text");
        inputTag1.put("name", "username");
        inputTag1.put("value", "testuser1");
        FormElement inputElement1 = new FormElement(inputTag1, baseUri, new Attributes().put("disabled", "disabled"));

        Tag inputTag2 = new Tag("input");
        inputTag2.put("type", "text");
        inputTag2.put("name", "password");
        inputTag2.put("value", "testpass1");
        FormElement inputElement2 = new FormElement(inputTag2, baseUri, new Attributes());

        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);

        Connection connection = formElement.submit();
        assertTrue(connection == null);
    }

    @Test
    public void testFormWithSelectElement() {
        String baseUri = "https://example.com";

        Tag formTag = new Tag("form");
        formTag.put("action", "/submit");
        formTag.put("method", "POST");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag selectTag = new Tag("select");
        selectTag.put("name", "country");
        FormElement selectElement = new FormElement(selectTag, baseUri, new Attributes());

        Elements options = new Elements();
        options.add(new Tag("option").put("value", "USA").put("text", "United States"));
        options.add(new Tag("option").put("value", "CAN").put("text", "Canada"));
        selectElement.elements().addAll(options);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        selectElement.select("option[selected]").first().attr("selected", "selected");

        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("country", formData.get(0).key());
        assertEquals("USA", formData.get(0).value());

        selectElement.select("option[selected]").first().remove();

        formData = formElement.formData();
        assertEquals(0, formData.size());

        selectElement.select("option").first().attr("selected", "selected");

        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("country", formData.get(0).key());
        assertEquals("option:0", formData.get(0).value());
    }
}