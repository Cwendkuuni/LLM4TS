package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.ElementFilter;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

public class FormElementTest {

    private static final String HTML_WITH_FORM = "" +
            "<html lang=\"en\">" +
            "<head></head>" +
            "<body>" +
            "<form action=\"/submit\" method=\"get\">" +
            "<input type=\"text\" name=\"username\" />" +
            "<input type=\"text\" name=\"password\" />" +
            "<input type=\"submit\" value=\"Submit\" />" +
            "</form>" +
            "</body>" +
            "</html>";

    private Element formElement;

    @Before
    public void setUp() {
        formElement = Jsoup.parse(HTML_WITH_FORM).getElementByTag("form");
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        List<Element> expectedElements = Arrays.asList(
                formElement.getElementsByTag("input").get(0),
                formElement.getElementsByTag("input").get(1),
                formElement.getElementsByTag("input").get(2)
        );
        assertEquals(expectedElements, elements);
    }

    @Test
    public void testSubmitWithSingleElementForm() {
        FormElement formElement = (FormElement) Jsoup.parse(
                "<form action=\"/submit\" method=\"post\">" +
                "<input type=\"text\" name=\"username\" value=\"John Doe\" />" +
                "</form>").get(0);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key);
        assertEquals("John Doe", formData.get(0).val);
    }

    @Test
    public void testSubmitWithMultipleElementsForm() {
        FormElement formElement = (FormElement) Jsoup.parse(HTML_WITH_FORM).get(0);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        List<Connection.KeyVal> formData = formElement.formData();

        int expectedSize = 3;
        assertEquals(expectedSize, formData.size());
        for (Connection.KeyVal keyVal : formData) {
            assertNotNull(keyVal.key);
            assertNotNull(keyVal.val);
        }

        assertEquals("username", formData.get(0).key);
        assertEquals("", formData.get(0).val);
        assertEquals("password", formData.get(1).key);
        assertEquals("", formData.get(1).val);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        FormElement formElement = new FormElement(
                new Tag("form"), "", new Attributes());
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() {
        FormElement formElement = (FormElement) Jsoup.parse(
                "<form action=\"\" method=\"get\">" +
                "<input type=\"text\" name=\"username\" />" +
                "</form>").get(0);
        formElement.submit();
    }
}