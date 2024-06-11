package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private static final String BASE_URI = "http://www.example.com";
    private static final String FORM_TAG = "form";
    private static final String INPUT_TAG = "input";
    private static final String SELECT_TAG = "select";
    private static final String CHECKBOX_TYPE = "checkbox";
    private static final String RADIO_TYPE = "radio";

    private ParseSetup parseSetup;
    private Element formElement;
    private Connection connection;

    @Before
    public void setup() {
        parseSetup = Jsoup.parseBodyFragment(
                "<html><head></head><body>" +
                "<" + FORM_TAG + " name=\"myForm\" action=\"" + BASE_URI + "/submit\" method=\"post\">" +
                "<" + INPUT_TAG + " type=\"text\" name=\"input1\" value=\"value1\">" +
                "<" + INPUT_TAG + " type=\"text\" name=\"input2\" disabled value=\"value2\">" +
                "<" + SELECT_TAG + " name=\"select1\">" +
                "<option value=\"option1\">Option 1</option>" +
                "<option value=\"option2\" selected>Option 2</option>" +
                "<option value=\"option3\">Option 3</option>" +
                "<" + INPUT_TAG + " type=\"" + CHECKBOX_TYPE + "\" name=\"checkbox1\" checked>" +
                "<" + INPUT_Tag + " type=\"" + CHECKBOX_TYPE + "\" name=\"checkbox2\">" +
                "<" + INPUT_TAG + " type=\"" + RADIO_TYPE + "\" name=\"radio1\" checked>" +
                "<" + INPUT_TAG + " type=\"" + RADIO_TYPE + "\" name=\"radio2\">" +
                "</" + FORM_TAG + "></body></html>");

        formElement = formElement(parseSetup);
        connection = mock(Connection.class);
    }

    @Test
    public void testFormElementConstruction() {
        Tag tag = formElement.tag();
        assertEquals(FORM_TAG, tag.name());
        assertEquals(BASE_URI, formElement.baseUri());
        Attributes attributes = formElement.attributes();
        assertTrue(attributes.hasKey("name"));
        assertEquals("myForm", attributes.get("name"));
        assertEquals(1, formElement.children().size());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(5, elements.size());
        for (Element element : elements) {
            assertEquals(formElement, element.parent());
        }
    }

    @Test
    public void testAddElement() {
        Element newElement = element(parseSetup, "input", "addElementTest");
        formElement.addElement(newElement);
        Elements elements = formElement.elements();
        assertEquals(6, elements.size());
        assertEquals(newElement, elements.last());
    }

    @Test
    public void testSubmitWithNoAction() {
        try {
            formElement.submit();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubmitWithEmptyAction() {
        formElement.setAttribute("action", "");
        Connection connection = formElement.submit();
        assertEquals(BASE_URI, connection.url().toString());
    }

    @Test
    public void testSubmitWithValidAction() {
        formElement.setAttribute("action", "/submit");
        Connection connection = formElement.submit();
        assertEquals("/submit", connection.url().toString());
    }

    @Test
    public void testFormDataWithTextInputs() {
        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("input1", "value1"),
                new Connection.KeyVal("input2", "value2"));
        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataWithDisabledInput() {
        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("input1", "value1"));
        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataWithSelectTag() {
        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("select1", "option2"));
        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataWithCheckbox() {
        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("checkbox1", "on"));
        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataWithRadio() {
        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("radio1", "on"));
        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataWithMixedInputs() {
        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("input1", "value1"),
                new Connection.KeyVal("select1", "option2"),
                new Connection.KeyVal("checkbox1", "on"),
                new Connection.KeyVal("checkbox2", ""));
        assertEquals(expectedData, formData);
    }

    @Test
    public void testConnectionPreparation() {
        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals("/submit", connection.url().toString());
        assertEquals(formData(), connection.data());

        verify(connection).data(argThat(data -> Arrays.deepEquals(formData().toArray(), data.toArray())));
    }

    @Test
    public void testFormSubmission() {
        when(connection.execute()).thenReturn("Response Body");
        String response = formElement.submit().execute().body();
        assertEquals("Response Body", response);
    }

    private FormElement formElement(ParseSetup parseSetup) {
        if (formElement == null) {
            formElement = parseSetup.parseBodyFragment("<" + FORM_TAG + " name=\"myForm\">").get(0);
        }
        return (FormElement) formElement;
    }

    private Element element(ParseSetup parseSetup, String tagName, String tagId) {
        return parseSetup.parseBodyFragment("<" + tagName + " id=\"" + tagId + "\">").get(0);
    }

    private List<Connection.KeyVal> formData() {
        return formElement.formData();
    }
}