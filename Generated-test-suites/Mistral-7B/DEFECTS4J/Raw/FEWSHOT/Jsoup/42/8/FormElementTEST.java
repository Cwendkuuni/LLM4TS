package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testNewFormElement() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertEquals("form", formElement.tagName());
        assertNull(formElement.attr("action"));
        assertTrue(formElement.hasNoAttribute("action"));
    }

    @Test
    public void testFormElementWithActionAttribute() {
        Tag tag = new Tag("form");
        tag.putAttribute("action", "/test");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertEquals("/test", formElement.attr("action"));
        assertEquals("http://test/test", formElement.absUrl("action"));
    }

    @Test
    public void testSubmitFormWithEmptyActionAttribute() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertThrows("IllegalArgumentException", () -> formElement.submit());
    }

    @Test
    public void testSubmitForm() throws Exception {
        String baseUri = "http://test.com";
        String formAction = "http://test.com/submit";
        Tag formTag = new Tag("form", "form", Arrays.asList("action", formAction));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        Connection connection = formElement.submit();
        assertEquals("GET", connection.method().name());
        assertEquals(formAction, connection.url().toString());

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testSubmitFormWithElements() throws Exception {
        String baseUri = "http://test.com";
        String formAction = "http://test.com/submit";
        String inputName = "name";
        String inputValue = "value";
        String selectName = "select";
        String selectValue = "option1";
        String checkboxName = "checkbox";
        String checkboxValue = "checked";

        Tag formTag = new Tag("form", "form", Arrays.asList("action", formAction));
        Element inputElement = new Element("input", "input", Arrays.asList("name", inputName, "type", "text", "value", inputValue));
        Element selectElement = new Element("select", "select", Arrays.asList("name", selectName));
        Element selectOption1 = new Element("option", null, Arrays.asList("value", selectValue));
        Element selectOption2 = new Element("option", null, Arrays.asList("value", "option2"));
        Element checkboxElement = new Element("input", "input", Arrays.asList("name", checkboxName, "type", "checkbox", "checked", "true"));

        formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        formElement.addElement(selectOption1);
        formElement.addElement(selectOption2);
        formElement.addElement(checkboxElement);

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(4, formData.size());
        assertEquals(inputName, formData.get(0).key());
        assertEquals(inputValue, formData.get(0).value());
        assertEquals(selectName, formData.get(1).key());
        assertEquals(selectValue, formData.get(1).value());
        assertEquals(checkboxName, formData.get(2).key());
        assertEquals(checkboxValue, formData.get(2).value());
        assertEquals(selectName, formData.get(3).key());
        assertEquals(selectValue, formData.get(3).value());
    }

    @Test
    public void testFormDataWithoutNameAttribute() {
        String baseUri = "http://test.com";
        String formAction = "http://test.com/submit";
        String inputName = "name";
        String inputValue = "value";

        Tag formTag = new Tag("form", "form", Arrays.asList("action", formAction));
        Element inputElement = new Element("input", "input");
        formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }
}