package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setup() {
        String html = "<html>\n" +
                "<body>\n" +
                "<form id=\"myForm\">\n" +
                "<input type=\"text\" name=\"username\" value=\"test\">\n" +
                "<input type=\"hidden\" name=\"password\" value=\"secret\">\n" +
                "<select name=\"select\">\n" +
                "<option value=\"option1\" selected>\n" +
                "Option 1\n" +
                "</option>\n" +
                "<option value=\"option2\">\n" +
                "Option 2\n" +
                "</option>\n" +
                "</select>\n" +
                "<input type=\"checkbox\" name=\"checkBox\" value=\"checked\">\n" +
                "<input type=\"radio\" name=\"radioButton\" value=\"option1\">\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>";

        form = Jsoup.parseBodyFragment(html).getElementById("myForm").getFormElement();
    }

    @Test
    public void testElements() {
        Elements expectedElements = new Elements(
                new Element[]{
                        new Element("input", Arrays.asList("type", "text"), Arrays.asList("name", "username")),
                        new Element("input", Arrays.asList("type", "hidden"), Arrays.asList("name", "password")),
                        new Element("select", Arrays.asList("name", "select"), null),
                        new Element("option", Arrays.asList("value", "option1"), Arrays.asList("selected", "value", "Option 1")),
                        new Element("option", Arrays.asList("value", "option2"), Arrays.asList("value", "Option 2"))
                }
        );

        assertEquals(expectedElements, form.elements());
    }

    @Test
    public void testAddElement() {
        Element input = new Element("input", Arrays.asList("type", "text"), Arrays.asList("name", "newInput"));
        FormElement expectedForm = new FormElement(new Tag("form"), null, null);
        expectedForm.elements().add(input);

        form.addElement(input);
        assertEquals(expectedForm.elements(), form.elements());
    }

    @Test
    public void testSubmit() {
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(new Connection.KeyVal("username", "test"));
        expectedData.add(new Connection.KeyVal("password", "secret"));
        expectedData.add(new Connection.KeyVal("select", "option1"));
        expectedData.add(new Connection.KeyVal("checkBox", "checked"));
        expectedData.add(new Connection.KeyVal("radioButton", "option1"));

        Connection connection = form.submit();

        assertEquals("http://", connection.url().scheme());
        assertEquals("/", connection.url().host());
        assertEquals("/", connection.url().path());
        assertEquals("GET", connection.method().name());
        assertEquals(expectedData, connection.data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formWithoutBaseUri = new FormElement(new Tag("form"), null, null);
        formWithoutBaseUri.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(new Connection.KeyVal("username", "test"));
        expectedData.add(new Connection.KeyVal("password", "secret"));
        expectedData.add(new Connection.KeyVal("select", "option1"));
        expectedData.add(new Connection.KeyVal("checkBox", "checked"));
        expectedData.add(new Connection.KeyVal("radioButton", "option1"));

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataEmptyForm() {
        FormElement emptyForm = new FormElement(new Tag("form"), null, null);
        List<Connection.KeyVal> formData = emptyForm.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataDisabledInput() {
        Element disabledInput = new Element("input", Arrays.asList("type", "text"), Arrays.asList("name", "disabledInput", "disabled"));
        FormElement formWithDisabledInput = new FormElement(new Tag("form"), null, null);
        formWithDisabledInput.elements().add(disabledInput);
        List<Connection.KeyVal> formData = formWithDisabledInput.formData();

        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataNoName() {
        Element noNameInput = new Element("input", Arrays.asList("type", "text"));
        FormElement formWithNoNameInput = new FormElement(new Tag("form"), null, null);
        formWithNoNameInput.elements().add(noNameInput);
        List<Connection.KeyVal> formData = formWithNoNameInput.formData();

        assertTrue(formData.isEmpty());
    }
}