package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form><input name=\"input1\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        Element form = doc.select("form").first();
        FormElement formElement = (FormElement) form;
        assertEquals("form", formElement.tagName());

        Tag tag = new Tag("form");
        FormElement formElement2 = new FormElement(tag, "", new Attributes());
        assertEquals("form", formElement2.tagName());
    }

    @Test
    public void testEmptyFormElement() {
        Element form = new FormElement(new Tag("form"), "", new Attributes());
        assertTrue(form.elements().isEmpty());
    }

    @Test
    public void testFormElementWithSingleInput() {
        String html = "<form><input name=\"input1\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Element input = doc.select("input").first();
        formElement.addElement(input);
        assertEquals(1, formElement.elements().size());
        assertEquals(input, formElement.elements().get(0));
    }

    @Test
    public void testFormElementWithMultipleInputs() {
        String html = "<form><input name=\"input1\" type=\"text\"><input name=\"input2\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Element input1 = doc.select("input").get(0);
        Element input2 = doc.select("input").get(1);
        formElement.addElement(input1);
        formElement.addElement(input2);
        assertEquals(2, formElement.elements().size());
        assertEquals(input1, formElement.elements().get(0));
        assertEquals(input2, formElement.elements().get(1));
    }

    @Test
    public void testFormSubmission() {
        String html = "<form action=\"submit.html\" method=\"post\"><input name=\"input1\" type=\"text\" value=\"Value1\"><input name=\"input2\" type=\"text\" value=\"Value2\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> keyVals = formElement.formData();
        assertEquals(2, keyVals.size());
        assertEquals("input1", keyVals.get(0).key());
        assertEquals("Value1", keyVals.get(0).value());
        assertEquals("input2", keyVals.get(1).key());
        assertEquals("Value2", keyVals.get(1).value());
    }

    @Test
    public void testFormSubmissionWithEmptyValues() {
        String html = "<form action=\"submit.html\" method=\"post\"><input name=\"input1\" type=\"text\"><input name=\"input2\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> keyVals = formElement.formData();
        assertEquals(2, keyVals.size());
        assertEquals("input1", keyVals.get(0).key());
        assertNull(keyVals.get(0).value());
        assertEquals("input2", keyVals.get(1).key());
        assertNull(keyVals.get(1).value());
    }

    @Test
    public void testFormSubmissionWithDisabledInputs() {
        String html = "<form action=\"submit.html\" method=\"post\"><input name=\"input1\" type=\"text\" disabled><input name=\"input2\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> keyVals = formElement.formData();
        assertEquals(0, keyVals.size());
    }

    @Test
    public void testFormSubmissionWithSelectElement() {
        String html = "<form action=\"submit.html\" method=\"post\"><select name=\"input1\"><option value=\"Option1\">Option 1</option><option value=\"Option2\" selected>Option 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> keyVals = formElement.formData();
        assertEquals(1, keyVals.size());
        assertEquals("input1", keyVals.get(0).key());
        assertEquals("Option2", keyVals.get(0).value());
    }

    @Test
    public void testFormSubmissionWithMultipleSelectOptions() {
        String html = "<form action=\"submit.html\" method=\"post\"><select name=\"input1\"><option value=\"Option1\">Option 1</option><option value=\"Option2\">Option 2</option><option value=\"Option3\" selected>Option 3</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> keyVals = formElement.formData();
        assertEquals(1, keyVals.size());
        assertEquals("input1", keyVals.get(0).key());
        assertEquals("Option3", keyVals.get(0).value());
    }

    @Test
    public void testFormSubmissionWithCheckboxOrRadio() {
        String html = "<form action=\"submit.html\" method=\"post\"><input name=\"input1\" type=\"checkbox\" checked><input name=\"input1\" type=\"radio\" checked></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> keyVals = formElement.formData();
        assertEquals(1, keyVals.size());
        assertEquals("input1", keyVals.get(0).key());
        assertEquals("on", keyVals.get(0).value());
    }

    @Test
    public void testFormSubmissionWithMultipleCheckboxesOrRadios() {
        String html = "<form action=\"submit.html\" method=\"post\"><input name=\"input1\" type=\"checkbox\" checked><input name=\"input1\" type=\"radio\" checked><input name=\"input1\" type=\"checkbox\"><input name=\"input1\" type=\"radio\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> keyVals = formElement.formData();
        assertEquals(2, keyVals.size());
        assertTrue(keyVals.get(0).key().startsWith("input1"));
        assertEquals("on", keyVals.get(0).value());
        assertTrue(keyVals.get(1).key().startsWith("input1"));
        assertEquals("on", keyVals.get(1).value());
    }
}