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
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, null, new Attributes());
        assertEquals("form", formElement.tagName());
        assertNull(formElement.attr("action"));
        assertTrue(formElement.hasAttr("method"));
        assertEquals("get", formElement.attr("method").toLowerCase());
        assertFalse(formElement.hasAttr("name"));
    }

    @Test
    public void testFormElementWithActionAndMethod() {
        Tag tag = new Tag("form");
        tag.putAttribute("action", "/myForm");
        tag.putAttribute("method", "post");

        FormElement formElement = new FormElement(tag, null, new Attributes());
        assertEquals("/myForm", formElement.absUrl("action"));
        assertEquals("post", formElement.attr("method").toLowerCase());
    }

    @Test
    public void testFormElementWithName() {
        Tag tag = new Tag("form");
        tag.putAttribute("name", "formName");

        FormElement formElement = new FormElement(tag, null, new Attributes());
        assertEquals("formName", formElement.attr("name"));
    }

    @Test
    public void testFormElementWithNoControls() {
        Tag tag = new Tag("form");

        FormElement formElement = new FormElement(tag, null, new Attributes());
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testFormElementWithControls() {
        Tag formTag = new Tag("form");

        Tag inputTag1 = new Tag("input");
        inputTag1.putAttribute("type", "text");
        inputTag1.putAttribute("name", "inputName1");
        inputTag1.putAttribute("value", "inputValue1");

        Tag inputTag2 = new Tag("input");
        inputTag2.putAttribute("type", "text");
        inputTag2.putAttribute("name", "inputName2");
        inputTag2.putAttribute("value", "inputValue2");
        inputTag2.putAttribute("checked", "checked");

        Tag selectTag = new Tag("select");
        selectTag.putAttribute("name", "selectName");
        Elements optionElements = new Elements();
        Elements options = selectTag.child(0).select("option[value='optionValue1']");
        optionElements.addAll(options);
        selectTag.children().clear();
        selectTag.appendChild(optionElements);

        formTag.appendChild(inputTag1);
        formTag.appendChild(inputTag2);
        formTag.appendChild(selectTag);

        FormElement formElement = new FormElement(formTag, null, new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(3, formData.size());
        assertEquals("inputName1", formData.get(0).key);
        assertEquals("inputValue1", formData.get(0).value);
        assertEquals("inputName2", formData.get(1).key);
        assertEquals("inputValue2", formData.get(1).value);
        assertEquals("selectName", formData.get(2).key);
        assertEquals("optionValue1", formData.get(2).value);
    }

    @Test
    public void testFormElementSubmission() {
        Tag formTag = new Tag("form");

        Tag inputTag1 = new Tag("input");
        inputTag1.putAttribute("type", "text");
        inputTag1.putAttribute("name", "inputName1");
        inputTag1.putAttribute("value", "inputValue1");

        Tag inputTag2 = new Tag("input");
        inputTag2.putAttribute("type", "text");
        inputTag2.putAttribute("name", "inputName2");
        inputTag2.putAttribute("value", "inputValue2");
        inputTag2.putAttribute("checked", "checked");

        Tag selectTag = new Tag("select");
        selectTag.putAttribute("name", "selectName");
        Elements optionElements = new Elements();
        Elements options = selectTag.child(0).select("option[value='optionValue1']");
        optionElements.addAll(options);
        selectTag.children().clear();
        selectTag.appendChild(optionElements);

        formTag.appendChild(inputTag1);
        formTag.appendChild(inputTag2);
        formTag.appendChild(selectTag);

        FormElement formElement = new FormElement(formTag, null, new Attributes());
        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();

        assertEquals(3, formData.size());
        assertEquals("inputName1", formData.get(0).key);
        assertEquals("inputValue1", formData.get(0).value);
        assertEquals("inputName2", formData.get(1).key);
        assertEquals("inputValue2", formData.get(1).value);
        assertEquals("selectName", formData.get(2).key);
        assertEquals("optionValue1", formData.get(2).value);
    }

    @Test
    public void testFormElementWithEmptyFormData() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }
}