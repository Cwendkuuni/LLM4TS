package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    private static final String BASE_URI = "http://example.com";
    private static final String FORM_HTML = "<form action='/submit' method='post'>" +
            "<input type='text' name='field1' value='value1'>" +
            "<input type='text' name='field2' value='value2'>" +
            "<select name='select1'>" +
            "<option value='option1'>Option 1</option>" +
            "<option value='option2' selected>Option 2</option>" +
            "</select>" +
            "<input type='checkbox' name='check1' value='check1' checked>" +
            "</form>";
    private Element formElement;

    @Before
    public void setUp() {
        Document document = Jsoup.parse(FORM_HTML);
        formElement = document.selectFirst("form");
    }

    @Test
    public void testElements() {
        List<Element> expectedElements = Arrays.asList(
                formElement.select("input[type='text']"),
                formElement.select("select"),
                formElement.select("input[type='checkbox']")
        );

        Elements actualElements = formElement.elements();
        assertEquals(expectedElements.size(), actualElements.size());
        assertTrue(actualElements.containsAll(expectedElements));
    }

    @Test
    public void testAddElement() {
        Element newInput = new Element("input");
        newInput.attr("type", "text");
        newInput.attr("name", "field3");
        newInput.attr("value", "value3");

        formElement.addElement(newInput);

        Elements actualElements = formElement.elements();
        List<Element> expectedElements = Arrays.asList(
                formElement.select("input[type='text']"),
                formElement.select("select"),
                formElement.select("input[type='checkbox']"),
                newInput
        );

        assertTrue(actualElements.containsAll(expectedElements));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();

        assertEquals("/submit", connection.url().toString());
        assertEquals(Connection.Method.POST, connection.method());

        List<Connection.KeyVal> formData = formElement.formData();

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("field1", "value1"));
        expectedFormData.add(HttpConnection.KeyVal.create("field2", "value2"));
        expectedFormData.add(HttpConnection.KeyVal.create("select1", "option2"));
        expectedFormData.add(HttpConnection.KeyVal.create("check1", "check1"));

        assertEquals(expectedFormData.size(), formData.size());
        assertTrue(formData.containsAll(expectedFormData));
    }

    @Test
    public void testFormDataWhenNoInputs() {
        FormElement emptyFormElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
        List<Connection.KeyVal> formData = emptyFormElement.formData();
        assertEquals(0, formData.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() {
        FormElement formElementWithoutAction = new FormElement(new Tag("form"), BASE_URI, new Attributes());
        formElementWithoutAction.submit();
    }
}