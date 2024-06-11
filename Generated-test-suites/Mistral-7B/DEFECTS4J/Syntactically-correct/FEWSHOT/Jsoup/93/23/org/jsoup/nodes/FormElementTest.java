package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit");

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tag().name());
        assertEquals("/submit", formElement.attr("action"));
    }

    @Test
    public void testElementsEmpty() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testAddElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        Tag inputTag = new Tag("input");
        Attributes inputAttributes = new Attributes().put("type", "text").put("name", "inputName").put("value", "inputValue");

        Element inputElement = new Element(inputTag, "", inputAttributes);
        formElement.addElement(inputElement);

        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().get(0));
    }

    @Test
    public void testRemoveElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes().put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        Tag inputTag = new Tag("input");
        Attributes inputAttributes = new Attributes().put("type", "text").put("name", "inputName").put("value", "inputValue");
        Element inputElement = new Element(inputTag, "", inputAttributes);

        formElement.addElement(inputElement);
        formElement.removeChild(inputElement);

        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmitNullBaseUri() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes().put("action", "/submit");

        FormElement formElement = new FormElement(formTag, null, formAttributes);
        assertNull(formElement.submit().url());
    }

    @Test
    public void testSubmitEmptyBaseUri() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes().put("action", "/submit");

        FormElement formElement = new FormElement(formTag, "", formAttributes);
        String baseUri = "http://example.com";
        Connection connection = formElement.submit();

        assertEquals(baseUri + "/submit", connection.url());
    }

    @Test
    public void testSubmitWithGetMethod() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes().put("action", "/submit").put("method", "get");

        FormElement formElement = new FormElement(formTag, "http://example.com", formAttributes);
        Connection connection = formElement.submit();

        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testSubmitWithPostMethod() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes().put("action", "/submit").put("method", "post");

        FormElement formElement = new FormElement(formTag, "http://example.com", formAttributes);
        Connection connection = formElement.submit();

        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormDataEmptyForm() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes().put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSelectElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes().put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag selectTag = new Tag("select");
        Attributes selectAttributes = new Attributes().put("name", "selectName");
        Element selectElement = new Element(selectTag, "", selectAttributes);

        Tag optionTag1 = new Tag("option").setText("Option 1").setValue("option1");
        Element optionElement1 = new Element(optionTag1, "");
        selectElement.addChild(optionElement1);
        formElement.addElement(selectElement);

        Tag optionTag2 = new Tag("option").setText("Option 2").setValue("option2");
        Element optionElement2 = new Element(optionTag2, "");
        selectElement.addChild(optionElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("selectName", keyVal1.key());
        assertEquals("option1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("selectName", keyVal2.key());
        assertEquals("option2", keyVal2.value());
    }

    @Test
    public void testFormDataCheckboxOrRadioElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes().put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag1 = new Tag("input");
        Attributes inputAttributes1 = new Attributes().put("type", "checkbox").put("name", "checkboxName");
        Element inputElement1 = new Element(inputTag1, "", inputAttributes1);
        inputElement1.setAttribute("checked", "checked");
        formElement.addElement(inputElement1);

        Tag inputTag2 = new Tag("input");
        Attributes inputAttributes2 = new Attributes().put("type", "radio").put("name", "radioName").put("checked", "checked");
        Element inputElement2 = new Element(inputTag2, "", inputAttributes2);
        formElement.addElement(inputElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("checkboxName", keyVal1.key());
        assertTrue(keyVal1.value().equals("on") || keyVal1.value().isEmpty());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("radioName", keyVal2.key());
        assertTrue(keyVal2.value().equals("on") || keyVal2.value().isEmpty());
    }

    @Test
    public void testFormDataTextElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes().put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag = new Tag("input");
        Attributes inputAttributes1 = new Attributes().put("type", "text").put("name", "textName");
        Element inputElement1 = new Element(inputTag, "", inputAttributes1);
        inputElement1.setValue("textValue");
        formElement.addElement(inputElement1);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("textName", keyVal.key());
        assertEquals("textValue", keyVal.value());
    }

    @Test
    public void testFormDataFormWithMixedElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes().put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag selectTag = new Tag("select");
        Attributes selectAttributes = new Attributes().put("name", "selectName");
        Element selectElement = new Element(selectTag, "", selectAttributes);

        Tag optionTag1 = new Tag("option").setText("Option 1").setValue("option1");
        Element optionElement1 = new Element(optionTag1, "");
        selectElement.addChild(optionElement1);
        formElement.addElement(selectElement);

        Tag inputTag1 = new Tag("input");
        Attributes inputAttributes1 = new Attributes().put("type", "checkbox").put("name", "checkboxName");
        Element inputElement1 = new Element(inputTag1, "", inputAttributes1);
        inputElement1.setAttribute("checked", "checked");
        formElement.addElement(inputElement1);

        Tag inputTag2 = new Tag("input");
        Attributes inputAttributes2 = new Attributes().put("type", "text").put("name", "textName");
        Element inputElement2 = new Element(inputTag2, "", inputAttributes2);
        inputElement2.setValue("textValue");
        formElement.addElement(inputElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(4, formData.size());

        Connection.KeyVal selectKeyVal = formData.get(0);
        assertEquals("selectName", selectKeyVal.key());
        assertEquals("option1", selectKeyVal.value());

        Connection.KeyVal checkboxKeyVal = formData.get(1);
        assertEquals("checkboxName", checkboxKeyVal.key());
        assertTrue(checkboxKeyVal.value().equals("on") || checkboxKeyVal.value().isEmpty());

        Connection.KeyVal textKeyVal = formData.get(2);
        assertEquals("textName", textKeyVal.key());
        assertEquals("textValue", textKeyVal.value());

        Connection.KeyVal lastKeyVal = formData.get(3);
        assertNull(lastKeyVal);
    }
}