package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for FormElement.
 */
public class FormElementTest {

    private static final String HTML_FORM_SAMPLE = "<form action='/submit' method='post'>\n" +
            "   <input type='text' name='username'>\n" +
            "   <input type='password' name='password'>\n" +
            "   <textarea name='message'></textarea>\n" +
            "   <select name='gender'>\n" +
            "      <option value='male' selected>Male</option>\n" +
            "      <option value='female'>Female</option>\n" +
            "   </select>\n" +
            "   <input type='checkbox' name='agree' checked>\n" +
            "   <input type='submit' value='Submit'>\n" +
            "</form>";

    private Element formElement;

    @Before
    public void setUp() {
        Document doc = Jsoup.parseBodyFragment(HTML_FORM_SAMPLE);
        formElement = doc.select("form").first();
    }

    @Test
    public void testFormElementCreation() {
        FormElement formElement = new FormElement(new Tag("form"), "/", new Attributes());
        assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        List<Element> expectedElements = new ArrayList<>();
        expectedElements.add(formElement.select("input[type='text']").first());
        expectedElements.add(formElement.select("input[type='password']").first());
        expectedElements.add(formElement.select("textarea").first());
        expectedElements.add(formElement.select("select").first());
        expectedElements.add(formElement.select("input[type='checkbox']").first());
        expectedElements.add(formElement.select("input[type='submit']").first());

        List<Element> actualElements = formElement.elements().stream().collect(Collectors.toList());
        assertEquals("Elements list size mismatch", expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            assertEquals("Elements list element " + i + " mismatch", expectedElements.get(i), actualElements.get(i));
        }
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element("input");
        inputElement.attr("type", "hidden");
        inputElement.attr("name", "hiddenField");

        FormElement formElement = new FormElement(new Tag("form"), "/", new Attributes());
        formElement.addElement(inputElement);

        List<Element> actualElements = formElement.elements().stream().collect(Collectors.toList());
        List<Element> expectedElements = new ArrayList<>();
        expectedElements.add(inputElement);
        assertEquals("Elements list size mismatch", expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            assertEquals("Elements list element " + i + " mismatch", expectedElements.get(i), actualElements.get(i));
        }
    }

    @Test
    public void testSubmit() throws Exception {
        Connection connection = formElement.submit();
        String expectedUrl = "http://%2Fsubmit";
        assertEquals("Unexpected submit URL", expectedUrl, connection.url());

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("username", ""));
        expectedFormData.add(HttpConnection.KeyVal.create("password", ""));
        expectedFormData.add(HttpConnection.KeyVal.create("message", ""));
        expectedFormData.add(HttpConnection.KeyVal.create("gender", "male"));
        expectedFormData.add(HttpConnection.KeyVal.create("agree", ""));

        List<Connection.KeyVal> actualFormData = connection.requestBody().asFormData();
        assertEquals("Form data size mismatch", expectedFormData.size(), actualFormData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            assertEquals("Form data pair " + i + " mismatch", expectedFormData.get(i).getName(), actualFormData.get(i).getName());
            assertEquals("Form data pair " + i + " mismatch", expectedFormData.get(i).getValue(), actualFormData.get(i).getValue());
        }
    }

    @Test
    public void testFormDataEmptyForm() {
        FormElement emptyFormElement = new FormElement(new Tag("form"), "/", new Attributes());
        List<Connection.KeyVal> actualFormData = emptyFormElement.formData();
        assertEquals("Unexpected form data list size", 0, actualFormData.size());
    }
}