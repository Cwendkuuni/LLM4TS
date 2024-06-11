package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testNewFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("name", "myForm");
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals("myForm", formElement.attr("name"));
        assertEquals(baseUri, formElement.baseUri());
    }

    @Test
    public void testFormElementEmpty() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        FormElement formElement = new FormElement(tag, baseUri, new Attributes());
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testFormElementWithElements() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        Tag inputTag2 = new Tag("input");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes()
                .put("name", "myForm")
                .put("id", "myFormId");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);

        assertEquals(2, formElement.elements().size());
        assertEquals(inputTag1, formElement.elements().get(0));
        assertEquals(inputTag2, formElement.elements().get(1));
        assertEquals("myFormId", formElement.attr("id"));
    }

    @Test
    public void testSubmitWithNoAction() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("name", "myForm");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        try {
            formElement.submit();
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testSubmit() {
        Tag formTag = new Tag("form");
        formTag.attr("action", "http://example.com/submit");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("name", "myForm");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Connection connection = formElement.submit();
        assertEquals("http://example.com/submit", connection.url());
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testFormDataEmpty() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("name", "myForm");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithSelect() {
        Tag formTag = new Tag("form");
        Tag selectTag = new Tag("select");
        Tag optionTag1 = new Tag("option").attr("value", "option1");
        Tag optionTag2 = new Tag("option").attr("selected", "selected").attr("value", "option2");
        selectTag.add(optionTag1);
        selectTag.add(optionTag2);
        formTag.add(selectTag);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("name", "myForm");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("myForm", keyVal.key());
        assertEquals("option2", keyVal.value());
    }

    @Test
    public void testFormDataWithCheckbox() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input").attr("type", "checkbox").attr("name", "myCheckbox");
        formTag.add(inputTag);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("name", "myForm");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // No checkbox selected
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        // Checkbox selected
        inputTag.attr("checked", "checked");
        formElement = new FormElement(formTag, baseUri, attributes);
        formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("myForm", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataWithTextInput() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input").attr("type", "text").attr("name", "myTextInput").attr("value", "value1");
        formTag.add(inputTag);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("name", "myForm");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("myForm", keyVal.key());
        assertEquals("value1", keyVal.value());
    }

    @Test
    public void testFormDataWithMultipleInputs() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input").attr("type", "text").attr("name", "myTextInput1").attr("value", "value1");
        Tag inputTag2 = new Tag("input").attr("type", "text").attr("name", "myTextInput2").attr("value", "value2");
        formTag.add(inputTag1);
        formTag.add(inputTag2);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("name", "myForm");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("myForm", keyVal1.key());
        assertEquals("value1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("myForm", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }
}