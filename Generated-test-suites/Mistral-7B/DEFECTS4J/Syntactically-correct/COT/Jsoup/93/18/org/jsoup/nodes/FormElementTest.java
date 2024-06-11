package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.ElementColors;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the FormElement class.
 */
public class FormElementTest {
    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertEquals("form", formElement.tagName());
        Assert.assertEquals(baseUri, formElement.baseUri());
    }

    @Test
    public void testElementsMethod() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element textElement = new Element("p", null, null);
        formElement.addElement(textElement);
        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(textElement, elements.get(0));
    }

    @Test
    public void testAddElementMethod() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element textElement = new Element("p", null, null);
        FormElement expectedFormElement = formElement.addElement(textElement);
        Assert.assertSame(formElement, expectedFormElement);
        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(textElement, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithoutAction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        formElement.submit();
    }

    @Test
    public void testSubmitMethodWithAction() {
        Tag tag = new Tag("form", null, new Attributes().put("action", "http://example.com/submit"));
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithInvalidMethod() {
        Tag tag = new Tag("form", null, new Attributes().put("method", "INVALID"));
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        formElement.submit();
    }

    @Test
    public void testFormDataMethodWithOnlyTextInputs() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element inputTextTag1 = new Element("input", null, new Attributes().put("type", "text").put("name", "name1")
                .put("value", "value1"));
        Element inputTextTag2 = new Element("input", null, new Attributes().put("type", "text").put("name", "name2")
                .put("value", "value2"));
        formElement.addElement(inputTextTag1);
        formElement.addElement(inputTextTag2);

        List<Connection.KeyVal> formData = formElement.formData();
        List<String> expectedFormData = Arrays.asList("name1=value1", "name2=value2");
        Assert.assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Assert.assertEquals(expectedFormData.get(i), formData.get(i).keyVal());
        }
    }

    @Test
    public void testFormDataMethodWithSelectInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST");

        Element selectTag = new Element("select", null, new Attributes().put("name", "name"));
        Element optionTag1 = new Element("option", null, new Attributes().put("value", "value1").put("selected", "selected"));
        Element optionTag2 = new Element("option", null, new Attributes().put("value", "value2"));
        selectTag.addElement(optionTag1);
        selectTag.addElement(optionTag2);
        formElement.addElement(selectTag);

        List<Connection.KeyVal> formData = formElement.formData();
        List<String> expectedFormData = Arrays.asList("name=value1");
        Assert.assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Assert.assertEquals(expectedFormData.get(i), formData.get(i).keyVal());
        }
    }

    @Test
    public void testFormDataMethodWithCheckboxInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST");

        Element checkboxTag = new Element("input", null, new Attributes().put("type", "checkbox").put("name", "name")
                .put("checked", "checked").put("value", "value"));
        formElement.addElement(checkboxTag);

        List<Connection.KeyVal> formData = formElement.formData();
        List<String> expectedFormData = Arrays.asList("name=value");
        Assert.assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Assert.assertEquals(expectedFormData.get(i), formData.get(i).keyVal());
        }
    }

    @Test
    public void testFormDataMethodWithMultipleCheckboxesInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST");

        Element checkboxTag1 = new Element("input", null, new Attributes().put("type", "checkbox").put("name", "name")
                .put("checked", "checked").put("value", "value1"));
        Element checkboxTag2 = new Element("input", null, new Attributes().put("type", "checkbox").put("name", "name")
                .put("value", "value2"));
        formElement.addElement(checkboxTag1);
        formElement.addElement(checkboxTag2);

        List<Connection.KeyVal> formData = formElement.formData();
        List<String> expectedFormData = Arrays.asList("name=value1", "name=value2");
        Assert.assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Assert.assertEquals(expectedFormData.get(i), formData.get(i).keyVal());
        }
    }

    @Test
    public void testFormDataMethodWithEmptyInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST");

        Element emptyInputTag = new Element("input", null, new Attributes().put("type", "text").put("name", "name"));
        formElement.addElement(emptyInputTag);

        List<Connection.KeyVal> formData = formElement.formData();
        List<String> expectedFormData = new ArrayList<>();
        Assert.assertEquals(expectedFormData.size(), formData.size());
    }

    @Test
    public void testFormDataMethodWithDisabledInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element disabledInputTag = new Element("input", null, new Attributes().put("type", "text").put("name", "name")
                .put("disabled", "disabled"));
        formElement.addElement(disabledInputTag);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataMethodWithMixedInputTypes() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "POST");

        Element textInputTag = new Element("input", null, new Attributes().put("type", "text").put("name", "nameText")
                .put("value", "valueText"));
        Element selectInputTag = new Element("select", null, new Attributes().put("name", "nameSelect"));
        Element inputTextOptionTag = new Element("option", null, new Attributes().put("value", "valueOptionText"));
        Element inputCheckboxOptionTag = new Element("option", null, new Attributes().put("value", "valueOptionCheckbox")
                .put("selected", "selected"));
        selectInputTag.addElement(inputTextOptionTag);
        selectInputTag.addElement(inputCheckboxOptionTag);
        formElement.addElement(textInputTag);
        formElement.addElement(selectInputTag);

        List<Connection.KeyVal> formData = formElement.formData();
        List<String> expectedFormData = Arrays.asList("nameText=valueText", "nameSelect=valueOptionCheckbox");
        Assert.assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Assert.assertEquals(expectedFormData.get(i), formData.get(i).keyVal());
        }
    }
}