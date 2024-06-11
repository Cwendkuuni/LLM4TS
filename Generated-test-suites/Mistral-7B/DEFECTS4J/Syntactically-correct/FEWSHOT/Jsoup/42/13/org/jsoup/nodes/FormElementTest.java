package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "", attributes);
        assertEquals("form", formElement.tagName());
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testFormElementWithControls() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        inputTag1.attr("name", "name1");
        inputTag1.attr("type", "text");
        Tag inputTag2 = new Tag("input");
        inputTag2.attr("name", "name2");
        inputTag2.attr("type", "checkbox");
        Tag selectTag = new Tag("select");
        Elements options = new Elements();
        options.add(new Tag("option").attr("value", "value1").text("Option 1"));
        options.add(new Tag("option").attr("value", "value2").text("Option 2"));
        selectTag.children().addAll(options);

        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com");
        attributes.put("method", "post");

        Element formElement = new FormElement(formTag, "", attributes);
        Element inputElement1 = formElement.addElement(inputTag1);
        Element inputElement2 = formElement.addElement(inputTag2);
        Element selectElement = formElement.addElement(selectTag);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
        assertEquals("name1", formData.get(0).key);
        assertEquals("", formData.get(0).val); // input tag without value
        assertEquals("name2", formData.get(1).key);
        assertNull(formData.get(1).val); // unchecked checkbox
        assertEquals("name2", formData.get(2).key);
        assertEquals("value1", formData.get(2).val); // first selected option
    }

    @Test
    public void testFormElementSubmission() throws Exception {
        String baseUrl = "http://example.com";
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        inputTag1.attr("name", "name1");
        inputTag1.attr("type", "text");
        inputTag1.attr("value", "value1");
        Tag inputTag2 = new Tag("input");
        inputTag2.attr("name", "name2");
        inputTag2.attr("type", "text");
        inputTag2.attr("value", "value2");
        Tag textAreaTag = new Tag("textarea");
        textAreaTag.attr("name", "name3");
        textAreaTag.text("value3");
        Tag selectTag = new Tag("select");
        Elements options = new Elements();
        options.add(new Tag("option").attr("value", "value1").text("Option 1"));
        options.add(new Tag("option").attr("value", "value2").text("Option 2"));
        selectTag.children().addAll(options);
        Attribute attr = new Attribute("action", "http://example.com/submit");
        FormElement formElement = new FormElement(formTag, baseUrl.toString(), new Attributes().put(attr));
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);
        formElement.addElement(textAreaTag);
        formElement.addElement(selectTag);

        Connection connection = formElement.submit();
        assertEquals("POST", connection.method());
        assertEquals("http://example.com/submit", connection.url());
        List<Connection.KeyVal> formData = connection.data();
        assertEquals(5, formData.size());
        assertEquals("name1", formData.get(0).key);
        assertEquals("value1", formData.get(0).val);
        assertEquals("name2", formData.get(1).key);
        assertEquals("value2", formData.get(1).val);
        assertEquals("name3", formData.get(2).key);
        assertTrue(formData.get(2).val.contains("value3"));
        assertEquals("name3", formData.get(3).key);
        assertEquals("", formData.get(3).val); // textarea with no name attribute is treated as "name" by default
        assertEquals("name2", formData.get(4).key);
        assertEquals("value1", formData.get(4).val); // first option selected
    }

    @Test
    public void testFormElementWithoutAction() {
        Tag formTag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "", attributes);
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormElementWithoutName() {
        Tag formTag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "", attributes);
        Element inputTag = new Tag("input");
        formElement.addElement(inputTag);
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Form element has no name attribute.", e.getMessage());
        }
    }
}