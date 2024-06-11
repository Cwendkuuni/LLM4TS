package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.HtmlParser;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

    private Document document;

    @Before
    public void setUp() {
        String html = "<html><head></head><body>" +
                "<form action='/submit' method='post'> " +
                "<input type='text' name='username' value='test'> " +
                "<input type='text' name='password' value='password'> " +
                "<select name='country'> " +
                "<option value='US'>United States</option> " +
                "<option value='CA' selected>Canada</option> " +
                "</select> " +
                "<input type='checkbox' name='agree' value='yes'> " +
                "<input type='hidden' name='csrf' value='abc123'> " +
                "<input type='submit' value='Submit'> " +
                "</form> " +
                "</body></html>";

        document = Jsoup.parse(html);
    }

    @After
    public void tearDown() {
        document = null;
    }

    @Test
    public void testFormElementCreation() {
        FormElement formElement = document.selectFirst("form");
        assertNotNull(formElement);
    }

    @Test
    public void testElementsMethodReturnsEmptyListForEmptyForm() {
        FormElement formElement = document.body();
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testElementsMethodReturnsFormControls() {
        FormElement formElement = document.selectFirst("form");
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(5, elements.size());

        List<Class<? extends Element>> expectedForms = Arrays.asList(
                InputElement.class, InputElement.class, SelectElement.class, InputElement.class, InputElement.class
        );

        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            Class<? extends Element> expectedType = expectedForms.get(i);
            assertEquals(expectedType.getName(), element.getClass().getName());
        }
    }

    @Test
    public void testSubmitMethodReturnsConnection() {
        FormElement formElement = document.selectFirst("form");
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodThrowsExceptionIfNoActionAttribute() {
        FormElement formElement = document.body();
        formElement.submit();
    }

    @Test
    public void testFormDataMethodReturnsCorrectValues() {
        FormElement formElement = document.selectFirst("form");

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(6, formData.size());

        Connection.KeyVal[] expectedData = {
                new Connection.KeyVal("username", "test"),
                new Connection.KeyVal("password", "password"),
                new Connection.KeyVal("country", "CA"),
                new Connection.KeyVal("agree", "yes"),
                new Connection.KeyVal("csrf", "abc123"),
                new Connection.KeyVal("", "")
        };

        for (int i = 0; i < formData.size(); i++) {
            Connection.KeyVal formDataElement = formData.get(i);
            Connection.KeyVal expectedDataElement = expectedData[i];
            assertEquals(expectedDataElement.key, formDataElement.key);
            assertEquals(expectedDataElement.value, formDataElement.value);
        }
    }

    @Test
    public void testAddElementMethodAddsElementToForm() {
        FormElement formElement = document.selectFirst("form");
        Element inputElement = new HtmlParser().parse("<input type='text' name='newInput'>").first();
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();
        assertEquals(6, elements.size());
        Element newInputElement = elements.get(5);
        assertEquals(InputElement.class, newInputElement.getClass());
    }

    @Test
    public void testRemoveElementMethodRemovesElementFromForm() {
        FormElement formElement = document.selectFirst("form");
        Element inputElement = formElement.selectFirst("input[name='username']");
        formElement.removeChild(inputElement);

        Elements elements = formElement.elements();
        assertEquals(4, elements.size());
        assertNull(formElement.selectFirst("input[name='username']"));
    }
}