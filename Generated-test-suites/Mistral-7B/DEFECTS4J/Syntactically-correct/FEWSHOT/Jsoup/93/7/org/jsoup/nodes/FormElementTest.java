package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    private static final String TEST_BASE_URI = "http://example.com";
    private static final String TEST_ACTION_URI = "http://example.com/submit";

    private FormElement formElement;

    @Before
    public void setup() {
        String html = "<form action=\"%s\">" +
                "<input type=\"text\" name=\"text1\" value=\"test1\">" +
                "<select name=\"select1\">" +
                "<option value=\"opt1\">Option 1</option>" +
                "<option value=\"opt2\" selected>Option 2</option>" +
                "</select>" +
                "<input type=\"hidden\" name=\"hidden1\" value=\"hidden1\">" +
                "<input type=\"text\" name=\"text2\" disabled>" +
                "<input type=\"checkbox\" name=\"check1\" value=\"check1\">" +
                "<input type=\"checkbox\" name=\"check2\" checked>" +
                "<input type=\"radio\" name=\"radio1\" value=\"radio1\" checked>" +
                "<input type=\"radio\" name=\"radio2\" value=\"radio2\"></form>";

        formElement = new FormElement(Tag.parse(String.format(html, TEST_ACTION_URI)), TEST_BASE_URI, new Attributes());
    }

    @Test
    public void testFormElementConstructor() {
        assertEquals("form", formElement.tagName());
        assertEquals(TEST_ACTION_URI, formElement.attr("action"));
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testElements() {
        Element text1 = formElement.select("input[name=text1]").first();
        Element select1 = formElement.select("select[name=select1]").first();
        Element hidden1 = formElement.select("input[name=hidden1]").first();
        Element text2 = formElement.select("input[name=text2]").first();
        Element check1 = formElement.select("input[name=check1]").first();
        Element check2 = formElement.select("input[name=check2]").first();
        Element radio1 = formElement.select("input[name=radio1][checked]").first();
        Element radio2 = formElement.select("input[name=radio2]").first();

        List<Element> elements = formElement.elements();

        assertEquals(7, elements.size());
        assertTrue(elements.contains(text1));
        assertTrue(elements.contains(select1));
        assertTrue(elements.contains(hidden1));
        assertFalse(elements.contains(text2));
        assertTrue(elements.contains(check1));
        assertTrue(elements.contains(check2));
        assertTrue(elements.contains(radio1));
        assertTrue(elements.contains(radio2));
    }

    @Test
    public void testAddElement() {
        Element newInput = new Element("input", new Attributes().put("type", "text").put("name", "newInput").put("value", "newValue"));
        FormElement newForm = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
        newForm.addElement(newInput);

        assertEquals(1, newForm.elements().size());
        assertTrue(newForm.elements().contains(newInput));
    }

    @Test
    public void testRemoveChild() {
        Element text1 = formElement.select("input[name=text1]").first();
        Element newInput = new Element("input", new Attributes().put("type", "text").put("name", "newInput").put("value", "newValue"));
        formElement.addElement(newInput);
        formElement.removeChild(text1);

        assertEquals(6, formElement.elements().size());
        assertFalse(formElement.elements().contains(text1));
        assertTrue(formElement.elements().contains(newInput));
    }

    @Test
    public void testSubmit() {
        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(10, formData.size());

        assertEquals("text1", formData.get(0).key);
        assertEquals("test1", formData.get(0).value);

        assertEquals("select1", formData.get(1).key);
        assertEquals("opt2", formData.get(1).value);

        assertEquals("hidden1", formData.get(2).key);
        assertEquals("hidden1", formData.get(2).value);

        assertNull(formData.get(3).value); // hidden input has no value

        assertEquals("check1", formData.get(4).key);
        assertNull(formData.get(4).value); // unchecked checkbox has no value

        assertEquals("check2", formData.get(5).key);
        assertEquals("on", formData.get(5).value); // checked checkbox has "on" value

        assertEquals("radio1", formData.get(6).key);
        assertEquals("radio1", formData.get(6).value); // checked radio button has its name as value

        assertEquals("radio2", formData.get(7).key);
        assertNull(formData.get(7).value); // unchecked radio button has no value

        assertEquals("", formData.get(8).key); // empty name for hidden input
        assertEquals("", formData.get(8).value);

        assertEquals("check2", formData.get(9).key);
        assertEquals("on", formData.get(9).value); // checked checkbox has "on" value
    }

    @Test
    public void testFormDataWithDisabledInput() {
        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(9, formData.size());

        assertFalse(formData.get(4).value.isEmpty()); // disabled checkbox has no value
        assertTrue(formData.get(5).value.isEmpty()); // disabled text input has no value
    }

    @Test
    public void testFormDataWithMultipleSelectedOptions() {
        Element select1 = formElement.select("select[name=select1]").first();
        Element option1 = new Element("option", new Attributes().put("value", "opt1").put("selected", "selected"));
        Element option2 = new Element("option", new Attributes().put("value", "opt3").put("selected", "selected"));
        select1.appendText(option1);
        select1.appendText(option2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(12, formData.size());

        assertEquals("select1", formData.get(1).key);
        assertEquals("opt1opt3", formData.get(1).value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyActionURI() {
        FormElement emptyForm = new FormElement(Tag.parse("<form></form>"), "", new Attributes());
        emptyForm.submit();
    }
}