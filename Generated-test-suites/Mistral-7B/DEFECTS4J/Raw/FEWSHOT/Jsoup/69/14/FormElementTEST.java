package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    private static final String HTML = "" +
            "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "  <title>FormElement Test</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <form method=\"get\" action=\"http://test.com/\">\n" +
            "    <input type=\"hidden\" name=\"foo\" value=\"bar\">\n" +
            "    <input type=\"text\" name=\"name\" value=\"John Doe\">\n" +
            "    <input type=\"email\" name=\"email\" value=\"john.doe@test.com\">\n" +
            "    <input type=\"checkbox\" name=\"agree\" value=\"yes\">\n" +
            "    <select name=\"color\">\n" +
            "      <option value=\"red\">Red</option>\n" +
            "      <option value=\"green\" selected>Green</option>\n" +
            "      <option value=\"blue\">Blue</option>\n" +
            "    </select>\n" +
            "    <input type=\"submit\" name=\"submit\">\n" +
            "  </form>\n" +
            "</body>\n" +
            "</html>";

    private Element formElement;

    @Before
    public void setUp() throws Exception {
        Document document = Jsoup.parse(HTML);
        formElement = document.getElementsByTag("form").first();
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(5, elements.size());
        assertTrue(elements.get(0) instanceof Elements);
        assertTrue(elements.get(1) instanceof InputElement);
        assertTrue(elements.get(2) instanceof InputElement);
        assertTrue(elements.get(3) instanceof SelectElement);
        assertTrue(elements.get(4) instanceof InputElement);
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(new Tag("form", null), null, new Attributes());
        InputElement inputElement = new InputElement(new Tag("input", null), null, new Attributes().put("type", "text")
                .put("name", "name").put("value", "John Doe"));
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.get(0).equals(inputElement));
    }

    @Test
    public void testSubmit() {
        List<Connection.KeyVal> formData = formElement.submit().data();
        assertEquals(5, formData.size());
        assertEquals("foo", formData.get(0).key);
        assertEquals("bar", formData.get(0).val);
        assertEquals("name", formData.get(1).key);
        assertEquals("John Doe", formData.get(1).val);
        assertEquals("email", formData.get(2).key);
        assertEquals("john.doe@test.com", formData.get(2).val);
        assertEquals("agree", formData.get(3).key);
        assertEquals("yes", formData.get(3).val);
        assertEquals("color", formData.get(4).key);
        assertEquals("green", formData.get(4).val);
    }

    @Test
    public void testFormDataEmptyForm() {
        FormElement formElement = new FormElement(new Tag("form", null), null, new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataHiddenInput() {
        FormElement formElement = new FormElement(new Tag("form", null), null, new Attributes()
                .put("method", "get")
                .put("action", "http://test.com/"));
        InputElement hiddenInput = new InputElement(new Tag("input", null), null, new Attributes()
                .put("type", "hidden")
                .put("name", "foo")
                .put("value", "bar"));
        formElement.addElement(hiddenInput);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("foo", formData.get(0).key);
        assertEquals("bar", formData.get(0).val);
    }

    @Test
    public void testFormDataCheckbox() {
        FormElement formElement = new FormElement(new Tag("form", null), null, new Attributes()
                .put("method", "get")
                .put("action", "http://test.com/"));
        InputElement checkbox = new InputElement(new Tag("input", null), null, new Attributes()
                .put("type", "checkbox")
                .put("name", "agree")
                .put("value", "yes"));
        formElement.addElement(checkbox);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        checkbox.attributes().put("checked", "checked");
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("agree", formData.get(0).key);
        assertEquals("yes", formData.get(0).val);
    }

    @Test
    public void testFormDataSelectElement() {
        FormElement formElement = new FormElement(new Tag("form", null), null, new Attributes()
                .put("method", "get")
                .put("action", "http://test.com/"));
        SelectElement selectElement = new SelectElement(new Tag("select", null), null, new Attributes().put("name", "color"));
        Elements options = selectElement.children();
        options.get(0).attributes().put("value", "red");
        options.get(1).attributes().put("value", "green").attributes().put("selected", "selected");
        options.get(2).attributes().put("value", "blue");
        formElement.addElement(selectElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("color", formData.get(0).key);
        assertEquals("green", formData.get(0).val);

        // unselected option
        formElement = new FormElement(new Tag("form", null), null, new Attributes()
                .put("method", "get")
                .put("action", "http://test.com/"));
        selectElement = new SelectElement(new Tag("select", null), null, new Attributes().put("name", "color"));
        options = selectElement.children();
        formElement.addElement(selectElement);
        formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() {
        FormElement formElement = new FormElement(new Tag("form", null), null, new Attributes()
                .put("method", "get")
                .put("action", ""));
        formElement.submit();
    }
}