package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSettings;
import org.jsoup.select.Elements;

/**
 * Test class for FormElement.java
 */
public class FormElementTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String HTML_CODE = "<html>\n" +
            "<head>\n" +
            "<title>Test Form</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<form action='/submit' method='post'>\n" +
            "<input type='text' name='textField' value='test'>\n" +
            "<select name='selectField'>\n" +
            "<option value='option1'>Option 1</option>\n" +
            "<option value='option2' selected>Option 2</option>\n" +
            "</select>\n" +
            "<input type='checkbox' name='checkboxField' checked>\n" +
            "<input type='submit'>\n" +
            "</form>\n" +
            "</body>\n" +
            "</html>";

    private Element formElement;

    @Before
    public void setup() {
        ParseSettings settings = new ParseSettings.Builder().build();
        Document document = Jsoup.parse(HTML_CODE, settings);
        formElement = document.select("form").first();
    }

    @Test
    public void testFormElementConstruction() {
        FormElement formElement = new FormElement(new Tag("form"), BASE_URL, new Attributes());
        assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        Elements expectedElements = new Elements();
        expectedElements.add(new Element("input", new Attributes().put("type", "text").put("name", "textField").put("value", "test")))
                .add(new Element("select", new Attributes().put("name", "selectField")))
                .add(new Element("option", new Attributes().put("value", "option1")))
                .add(new Element("option", new Attributes().put("value", "option2").put("selected", "selected")))
                .add(new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxField").put("checked", "checked")))
                .add(new Element("input", new Attributes().put("type", "submit")));

        Elements actualElements = formElement.elements();
        assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element("input", new Attributes().put("type", "hidden").put("name", "hiddenField").put("value", "test"));
        FormElement formElement = new FormElement(new Tag("form"), BASE_URL, new Attributes());
        formElement.addElement(inputElement);

        Elements expectedElements = new Elements().add(inputElement);
        Elements actualElements = formElement.elements();

        assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testSubmit() throws Exception {
        Connection connection = formElement.submit();
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("textField", "test"));
        expectedFormData.add(new Connection.KeyVal("selectField", "option2"));
        expectedFormData.add(new Connection.KeyVal("checkboxField", "on"));
        expectedFormData.add(new Connection.KeyVal("hiddenField", "test"));

        List<Connection.KeyVal> actualFormData = formElement.formData();
        assertEquals(expectedFormData, actualFormData);

        String response = connection.execute().body();
        // Add assertions for response here if needed
    }
}