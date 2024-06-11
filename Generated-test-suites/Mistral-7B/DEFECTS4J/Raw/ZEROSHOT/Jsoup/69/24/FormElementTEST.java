package org.jsoup.nodes.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertNotNull(formElement);
    }

    @Test
    public void testGetFormControls() {
        // Given
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tagForm, baseUri, attributes);

        Tag tagInput1 = new Tag("input");
        Tag tagInput2 = new Tag("input");
        Elements elements = new Elements();
        elements.add(tagInput1);
        elements.add(tagInput2);

        formElement.elements = elements;

        // When
        Elements formControls = formElement.elements();

        // Then
        Assert.assertEquals(2, formControls.size());
    }

    @Test
    public void testAddElement() {
        // Given
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tagForm, baseUri, attributes);

        Tag tagInput = new Tag("input");

        // When
        FormElement newFormElement = formElement.addElement(tagInput);

        // Then
        Elements formControls = formElement.elements();
        Assert.assertEquals(1, formControls.size());
        Assert.assertEquals(tagInput, formControls.first());
        Assert.assertEquals(formElement, newFormElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        // Given
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tagForm, baseUri, attributes);

        // When & Then
        formElement.submit();
        Validate.notReached();
    }

    @Test
    public void testSubmit() throws Exception {
        // Given
        String baseUri = "https://example.com";
        Tag tagForm = new Tag("form", new Attributes().put("action", "/submit"));
        FormElement formElement = new FormElement(tagForm, baseUri, new Attributes());

        Tag tagInput = new Tag("input", new Attributes().put("name", "inputName").put("value", "inputValue"));
        formElement.addElement(tagInput);

        // When
        Connection connection = formElement.submit();

        // Then
        Connection.Response response = connection.execute();
        Assert.assertNotNull(response);
    }

    @Test
    public void testFormData() {
        // Given
        Tag tagForm = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tagForm, baseUri, attributes);

        Tag tagInput1 = new Tag("input");
        tagInput1.attr("name", "input1");
        tagInput1.attr("value", "value1");
        formElement.addElement(tagInput1);

        Tag tagSelect = new Tag("select");
        Elements options = new Elements();
        options.add(new Tag("option").attr("value", "option1").attr("selected", "selected"));
        options.add(new Tag("option").attr("value", "option2"));
        tagSelect.select("option").addAll(options);
        formElement.addElement(tagSelect);

        Tag tagCheckbox = new Tag("input").attr("type", "checkbox").attr("name", "checkbox1");
        formElement.addElement(tagCheckbox);

        // When
        List<Connection.KeyVal> formData = formElement.formData();

        // Then
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("input1", "value1"));
        expectedFormData.add(new Connection.KeyVal("checkbox1", "on"));
        expectedFormData.add(new Connection.KeyVal("select", "option1"));

        Assert.assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Assert.assertEquals(expectedFormData.get(i).getName(), formData.get(i).getName());
            Assert.assertEquals(expectedFormData.get(i).getValue(), formData.get(i).getValue());
        }
    }
}