package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertNotNull(formElement);
        assertEquals("form", formElement.tagName());
    }

    @Test
    public void testGetFormControls() {
        Tag formTag = new Tag("form");
        Elements controls = new Elements();
        controls.add(new TextNode("Text Input"));
        controls.add(new InputTag("input", "text", "name", "value"));
        controls.add(new SelectTag("select", Arrays.asList("Option 1", "Option 2", "Option 3")));
        controls.add(new InputTag("input", "checkbox", "name", "value"));

        FormElement formElement = new FormElement(formTag, "", new Attributes().put("action", ""));
        formElement.elements().addAll(controls);

        List<Element> expectedControls = Arrays.asList(controls.get(0), controls.get(1), controls.get(2), controls.get(3));
        List<Element> actualControls = formElement.elements();

        assertEquals(expectedControls.size(), actualControls.size());
        for (int i = 0; i < expectedControls.size(); i++) {
            assertEquals(expectedControls.get(i), actualControls.get(i));
        }
    }

    @Test
    public void testSubmitForm() {
        String baseUri = "https://example.com";
        Tag formTag = new Tag("form", new Attributes().put("action", baseUri + "/submit"));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        formElement.addElement(new InputTag("input", "text", "name1", "value1"));
        formElement.addElement(new InputTag("input", "hidden", "name2", "value2"));
        formElement.addElement(new SelectTag("select", Arrays.asList("Option 1", "Option 2", "Option 3")));
        formElement.addElement(new InputTag("input", "checkbox", "name3", "value3"));
        formElement.addElement(new InputTag("input", "radio", "name4", "value4"));

        List<Connection.KeyVal> formData = formElement.formData();

        Connection connection = formElement.submit();
        connection.data(formData);

        String expectedFormData = "name1=value1&name2=value2&name3=value3&name4=value4";
        String actualFormData = connection.requestBody();

        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testSubmitFormWithEmptyInput() {
        String baseUri = "https://example.com";
        Tag formTag = new Tag("form", new Attributes().put("action", baseUri + "/submit"));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        formElement.addElement(new InputTag("input", "text", "name1", ""));

        List<Connection.KeyVal> formData = formElement.formData();

        Connection connection = formElement.submit();
        connection.data(formData);

        String expectedFormData = "name1=";
        String actualFormData = connection.requestBody();

        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testSubmitFormWithDisabledInput() {
        String baseUri = "https://example.com";
        Tag formTag = new Tag("form", new Attributes().put("action", baseUri + "/submit"));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        formElement.addElement(new InputTag("input", "text", "name1", "").attr("disabled", "disabled"));

        List<Connection.KeyVal> formData = formElement.formData();

        Connection connection = formElement.submit();
        connection.data(formData);

        // An empty list should be returned since the disabled input is not considered in formData.
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        String actualFormData = connection.requestBody();

        assertEquals(expectedFormData, Arrays.asList(new Connection.KeyVal[]{}));
    }
}