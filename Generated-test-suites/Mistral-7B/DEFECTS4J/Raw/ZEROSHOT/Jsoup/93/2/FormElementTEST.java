package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    private static final String TEST_HTML = "" +
            "<html>\n" +
            "<head>\n" +
            "</head>\n" +
            "<body>\n" +
            "<form action=\"/\">\n" +
            "<input type=\"text\" name=\"textField\" value=\"textValue\">\n" +
            "<input type=\"hidden\" name=\"hiddenField\" value=\"hiddenValue\">\n" +
            "<textarea name=\"textArea\">textAreaValue</textarea>\n" +
            "<select name=\"selectField\">\n" +
            "<option value=\"option1\">Option 1</option>\n" +
            "<option value=\"option2\" selected>Option 2</option>\n" +
            "</select>\n" +
            "<input type=\"checkbox\" name=\"checkboxField\" value=\"checkValue\">Checkbox\n" +
            "<input type=\"radio\" name=\"radioField\" value=\"radioValue\" checked>Radio\n" +
            "</form>\n" +
            "</body>\n" +
            "</html>";

    private Element formElement;

    @Before
    public void setUp() {
        Document document = Jsoup.parse(TEST_HTML);
        formElement = document.select("form").first();
    }

    @Test
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertNotNull(formElement);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(7, elements.size());
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element("input");
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertEquals(8, elements.size());
        assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testRemoveChild() {
        Element inputElement = new Element("input");
        formElement.addElement(inputElement);
        formElement.removeChild(inputElement);
        Elements elements = formElement.elements();
        assertEquals(7, elements.size());
        assertFalse(elements.contains(inputElement));
    }

    @Test
    public void testSubmit() throws Exception {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("GET", connection.method().name());
        assertEquals("/", connection.url().toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(7, formData.size());

        Connection.KeyVal keyVal;

        keyVal = formData.get(0);
        assertEquals("textField", keyVal.getKey());
        assertEquals("textValue", keyVal.getValue());

        keyVal = formData.get(1);
        assertEquals("hiddenField", keyVal.getKey());
        assertEquals("hiddenValue", keyVal.getValue());

        keyVal = formData.get(2);
        assertEquals("textArea", keyVal.getKey());
        assertEquals("textAreaValue", keyVal.getValue());

        keyVal = formData.get(3);
        assertEquals("selectField", keyVal.getKey());
        assertEquals("option2", keyVal.getValue());

        keyVal = formData.get(4);
        assertEquals("checkboxField", keyVal.getKey());
        assertNull(keyVal.getValue());

        keyVal = formData.get(5);
        assertEquals("radioField", keyVal.getKey());
        assertEquals("radioValue", keyVal.getValue());

        keyVal = formData.get(6);
        assertEquals("", keyVal.getKey());
        assertEquals("", keyVal.getValue());
    }
}