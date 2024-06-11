package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FormElementTest {

    @Test
    public void testNewFormElement() {
        Tag tag = new Tag("form");
        FormElement form = new FormElement(tag, "baseUrl", new Attributes());
        assertSame(tag, form.tag());
        assertEquals("baseUrl", form.baseUri());
        assertTrue(form.elements().isEmpty());
    }

    @Test
    public void testFormElementWithElements() {
        Tag formTag = new Tag("form");
        Tag input1Tag = new Tag("input").attr("type", "text").attr("name", "input1");
        Tag input2Tag = new Tag("input").attr("type", "text").attr("name", "input2");
        Tag selectTag = new Tag("select").attr("name", "select");
        Tag option1Tag = new Tag("option").attr("value", "option1").text("Option 1");
        Tag option2Tag = new Tag("option").attr("value", "option2").text("Option 2");
        selectTag.appendElement(option1Tag);
        selectTag.appendElement(option2Tag);

        Elements elements = new Elements();
        elements.add(input1Tag);
        elements.add(input2Tag);
        elements.add(selectTag);

        FormElement form = new FormElement(formTag, "baseUrl", new Attributes().putAll(formTag.attributes()));
        form.elements().addAll(elements);

        assertSame(formTag, form.tag());
        assertEquals("baseUrl", form.baseUri());
        assertEquals(3, form.elements().size());

        Element input1 = form.elements().get(0);
        Element input2 = form.elements().get(1);
        Element select = form.elements().get(2);
        assertSame(input1Tag, input1);
        assertSame(input2Tag, input2);
        assertSame(selectTag, select);
    }

    @Test
    public void testFormElementSubmission() throws Exception {
        String baseUrl = "baseUrl";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form");
        formTag.putAttribute("action", formAction);
        formTag.putAttribute("method", "post");

        Tag input1Tag = new Tag("input").attr("type", "text").attr("name", "input1");
        input1Tag.putAttribute("value", "inputValue1");

        Tag input2Tag = new Tag("input").attr("type", "text").attr("name", "input2");
        input2Tag.putAttribute("value", "inputValue2");

        Tag selectTag = new Tag("select").attr("name", "select");
        Tag option1Tag = new Tag("option").attr("value", "option1").text("Option 1");
        selectTag.appendElement(option1Tag);
        Tag option2Tag = new Tag("option").attr("value", "option2").text("Option 2");
        selectTag.appendElement(option2Tag);

        Elements elements = new Elements();
        elements.add(input1Tag);
        elements.add(input2Tag);
        elements.add(selectTag);

        FormElement form = new FormElement(formTag, baseUrl, new Attributes().putAll(formTag.attributes()));
        form.elements().addAll(elements);

        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(4, formData.size());

        assertEquals("input1", formData.get(0).key);
        assertEquals("inputValue1", formData.get(0).value);

        assertEquals("input2", formData.get(1).key);
        assertEquals("inputValue2", formData.get(1).value);

        assertEquals("select", formData.get(2).key);
        assertEquals("option1", formData.get(3).key);
        assertEquals("option1", formData.get(3).value);

        Connection connection = form.submit();
        String submittedUrl = connection.url().toString();
        assertEquals(formAction, submittedUrl);
    }

    @Test
    public void testFormElementWithNoAction() {
        Tag formTag = new Tag("form");

        FormElement form = new FormElement(formTag, "baseUrl", new Attributes().putAll(formTag.attributes()));
        assertTrue(form.elements().isEmpty());
        assertNull(form.submit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementWithInvalidAction() {
        Tag formTag = new Tag("form");
        formTag.putAttribute("action", "");

        FormElement form = new FormElement(formTag, "baseUrl", new Attributes().putAll(formTag.attributes()));
        form.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementWithEmptyAction() {
        Tag formTag = new Tag("form");
        formTag.putAttribute("action", " ");

        FormElement form = new FormElement(formTag, "baseUrl", new Attributes().putAll(formTag.attributes()));
        form.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementWithNullBaseUri() {
        Tag formTag = new Tag("form");

        FormElement form = new FormElement(formTag, null, new Attributes().putAll(formTag.attributes()));
        form.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementWithEmptyBaseUri() {
        Tag formTag = new Tag("form");

        FormElement form = new FormElement(formTag, "", new Attributes().putAll(formTag.attributes()));
        form.submit();
    }

    @Test
    public void testFormElementEquals() {
        Tag formTag1 = new Tag("form");
        Tag formTag2 = new Tag("form");

        FormElement form1 = new FormElement(formTag1, "baseUrl1", new Attributes().putAll(formTag1.attributes()));
        FormElement form2 = new FormElement(formTag2, "baseUrl2", new Attributes().putAll(formTag2.attributes()));

        assertFalse(form1.equals(form2));

        Tag inputTag1 = new Tag("input").attr("type", "text").attr("name", "input");
        Tag inputTag2 = new Tag("input").attr("type", "text").attr("name", "input");

        FormElement form1WithInput = new FormElement(formTag1, "baseUrl1", new Attributes().putAll(formTag1.attributes()));
        form1WithInput.elements().add(inputTag1);

        FormElement form2WithInput = new FormElement(formTag2, "baseUrl2", new Attributes().putAll(formTag2.attributes()));
        form2WithInput.elements().add(inputTag2);

        assertFalse(form1.equals(form2WithInput));

        FormElement formWithSameElements = new FormElement(formTag1, "baseUrl1", new Attributes().putAll(formTag1.attributes()));
        formWithSameElements.elements().clear();
        formWithSameElements.elements().addAll(form1.elements());

        assertTrue(form1.equals(formWithSameElements));
    }
}