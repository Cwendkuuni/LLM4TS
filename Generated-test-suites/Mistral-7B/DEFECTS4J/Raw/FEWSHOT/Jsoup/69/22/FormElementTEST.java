package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    private static final String TEST_BASE_URI = "http://test.com";
    private static final String TEST_HTML = "<html><body>" +
            "<form action=\"http://test.com/submit\" method=\"post\">" +
            "<input type=\"text\" name=\"name1\" value=\"value1\">" +
            "<input type=\"text\" name=\"name2\" value=\"value2\">" +
            "<select name=\"name3\">" +
            "<option value=\"value3a\" selected></option>" +
            "<option value=\"value3b\"></option>" +
            "</select>" +
            "<input type=\"checkbox\" name=\"name4\" value=\"value4a\" checked>" +
            "</form>" +
            "</body></html>";
    private Element formElement;

    @Before
    public void setUp() {
        Document doc = Jsoup.parse(TEST_HTML);
        formElement = doc.select("form").first();
    }

    @Test
    public void testElements() {
        Elements expected = Arrays.asList(
                new Elements().add(new InputElement("input", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "text", null, "name1", null, null, "value1")),
                new Elements().add(new InputElement("input", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "text", null, "name2", null, null, "value2"))
        );
        assertEquals(expected, formElement.elements());
    }

    @Test
    public void testAddElement() {
        InputElement newElement = new InputElement("input", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "text", null, "name1", null, null, "value1");
        FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
        formElement.addElement(newElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(newElement, elements.first());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        String action = connection.url();
        assertEquals("http://test.com/submit", action);
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormDataEmptyForm() {
        List<Connection.KeyVal> expected = new ArrayList<>();
        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormDataTextInput() {
        List<Connection.KeyVal> expected = Arrays.asList(
                new Connection.KeyVal("name1", "value1"),
                new Connection.KeyVal("name2", "value2")
        );
        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormDataSelectInput() {
        List<Connection.KeyVal> expected = Arrays.asList(
                new Connection.KeyVal("name3", "value3a")
        );
        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormDataCheckboxInput() {
        List<Connection.KeyVal> expected = Arrays.asList(
                new Connection.KeyVal("name4", "value4a")
        );
        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }
}