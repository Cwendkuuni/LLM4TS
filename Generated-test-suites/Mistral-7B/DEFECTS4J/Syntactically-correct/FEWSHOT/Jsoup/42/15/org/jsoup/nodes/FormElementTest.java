package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "baseUri", new Attributes());
        assertEquals("form", formElement.tagName());
        assertNull(formElement.attr("action"));
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testFormElementWithAction() {
        Tag tag = new Tag("form");
        tag.putAttribute("action", "/path/to/form");
        FormElement formElement = new FormElement(tag, "baseUri", new Attributes());
        assertEquals("/path/to/form", formElement.absUrl("action"));
    }

    @Test(expected = IllegalStateException.class)
    public void testFormElementWithoutBaseUri() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, null, new Attributes());
    }

    @Test
    public void testFormElementAddElement() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());

        Tag inputTag = new Tag("input");
        inputTag.putAttribute("name", "name");
        inputTag.putAttribute("type", "text");

        FormElement addedElement = formElement.addElement(new Elements(inputTag));
        assertEquals(1, formElement.elements().size());
        assertEquals(inputTag, formElement.elements().first());
    }

    @Test
    public void testFormElementAddMultipleElements() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());

        Tag input1Tag = new Tag("input");
        input1Tag.putAttribute("name", "name1");
        input1Tag.putAttribute("type", "text");

        Tag input2Tag = new Tag("input");
        input2Tag.putAttribute("name", "name2");
        input2Tag.putAttribute("type", "text");

        FormElement addedElement1 = formElement.addElement(new Elements(input1Tag));
        FormElement addedElement2 = formElement.addElement(new Elements(input2Tag));

        assertEquals(2, formElement.elements().size());
        assertEquals(input1Tag, formElement.elements().get(0));
        assertEquals(input2Tag, formElement.elements().get(1));
    }

    @Test
    public void testFormElementSubmit() {
        Tag formTag = new Tag("form");
        formTag.putAttribute("action", "/path/to/submit");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());

        Connection connection = formElement.submit();
        assertEquals("/path/to/submit", connection.url());
    }

    @Test
    public void testFormElementSubmitWithPostMethod() {
        Tag formTag = new Tag("form");
        formTag.putAttribute("action", "/path/to/submit");
        formTag.putAttribute("method", "post");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormElementSubmitWithGetMethod() {
        Tag formTag = new Tag("form");
        formTag.putAttribute("action", "/path/to/submit");
        formTag.putAttribute("method", "get");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testFormElementFormDataWithSelect() {
        Tag formTag = new Tag("form");
        Tag selectTag = new Tag("select");
        selectTag.putAttribute("name", "selectName");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        formElement.addElement(selectTag);

        Element option1Tag = new Tag("option");
        option1Tag.putAttribute("value", "value1");
        option1Tag.putAttribute("selected", "true");
        selectTag.add(new Elements(option1Tag));

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("selectName", formData.get(0).key);
        assertEquals("value1", formData.get(0).val);
    }

    @Test
    public void testFormElementFormDataWithMultipleSelectOptions() {
        Tag formTag = new Tag("form");
        Tag selectTag = new Tag("select");
        selectTag.putAttribute("name", "selectName");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        formElement.addElement(selectTag);

        Element option1Tag = new Tag("option");
        option1Tag.putAttribute("value", "value1");
        option1Tag.putAttribute("selected", "true");
        selectTag.add(new Elements(option1Tag));

        Element option2Tag = new Tag("option");
        option2Tag.putAttribute("value", "value2");
        selectTag.add(new Elements(option2Tag));

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("selectName", formData.get(0).key);
        assertEquals("value1", formData.get(0).val);
    }

    @Test
    public void testFormElementFormDataWithCheckbox() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input");
        inputTag.putAttribute("name", "checkboxName");
        inputTag.putAttribute("type", "checkbox");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        formElement.addElement(inputTag);

        inputTag.putAttribute("checked", "true");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("checkboxName", formData.get(0).key);
        assertEquals("on", formData.get(0).val);
    }

    @Test
    public void testFormElementFormDataWithMultipleCheckboxes() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        inputTag1.putAttribute("name", "checkboxName1");
        inputTag1.putAttribute("type", "checkbox");

        Tag inputTag2 = new Tag("input");
        inputTag2.putAttribute("name", "checkboxName1");
        inputTag2.putAttribute("type", "checkbox");
        inputTag2.putAttribute("checked", "true");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("checkboxName1", formData.get(0).key);
        assertEquals("on", formData.get(0).val);
        assertEquals("checkboxName1", formData.get(1).key);
        assertEquals("on", formData.get(1).val);
    }

    @Test
    public void testFormElementFormDataWithTextInput() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input");
        inputTag.putAttribute("name", "textName");
        inputTag.putAttribute("type", "text");
        inputTag.putAttribute("value", "textValue");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        formElement.addElement(inputTag);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("textName", formData.get(0).key);
        assertEquals("textValue", formData.get(0).val);
    }

    @Test
    public void testFormElementFormDataWithMultipleTextInputs() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        inputTag1.putAttribute("name", "textName1");
        inputTag1.putAttribute("type", "text");
        inputTag1.putAttribute("value", "textValue1");

        Tag inputTag2 = new Tag("input");
        inputTag2.putAttribute("name", "textName2");
        inputTag2.putAttribute("type", "text");
        inputTag2.putAttribute("value", "textValue2");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("textName1", formData.get(0).key);
        assertEquals("textValue1", formData.get(0).val);
        assertEquals("textName2", formData.get(1).key);
        assertEquals("textValue2", formData.get(1).val);
    }
}