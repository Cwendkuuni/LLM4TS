package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for FormElement
 */
public class FormElementTest {

    /**
     * Test construction of FormElement
     */
    @Test
    public void testConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Assert.assertNotNull(formElement);
        Assert.assertEquals(tag, formElement.tag());
        Assert.assertEquals(baseUri, formElement.baseUri());
        Assert.assertEquals(attributes, formElement.attributes());
    }

    /**
     * Test getElements method
     */
    @Test
    public void testGetElements() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Element formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element("input");
        Element textAreaElement = new Element("textarea");

        formElement.addElement(inputElement);
        formElement.addElement(textAreaElement);

        Elements expectedElements = new Elements();
        expectedElements.add(inputElement);
        expectedElements.add(textAreaElement);

        Elements actualElements = formElement.elements();

        Assert.assertEquals(expectedElements, actualElements);
    }

    /**
     * Test addElement method
     */
    @Test
    public void testAddElement() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element("input");

        FormElement expectedFormElement = formElement.addElement(inputElement);
        Elements expectedElements = new Elements();
        expectedElements.add(inputElement);

        Elements actualElements = formElement.elements();

        Assert.assertEquals(expectedFormElement, formElement);
        Assert.assertEquals(expectedElements, actualElements);
    }

    /**
     * Test removeChild method
     */
    @Test
    public void testRemoveChild() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element("input");
        Element textAreaElement = new Element("textarea");

        formElement.addElement(inputElement);
        formElement.addElement(textAreaElement);

        formElement.removeChild(inputElement);

        Elements expectedElements = new Elements();
        expectedElements.add(textAreaElement);

        Elements actualElements = formElement.elements();

        Assert.assertNotEquals(inputElement, formElement.elements().first());
        Assert.assertEquals(expectedElements, actualElements);
    }

    /**
     * Test submit method when form has an action URL
     */
    @Test
    public void testSubmitWithActionUrl() {
        Tag formTag = new Tag("form");
        formTag.attr("action", "https://example.com/submit");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Connection connection = formElement.submit();
        String expectedUrl = "https://example.com/submit";

        Assert.assertNotNull(connection);
        Assert.assertEquals(expectedUrl, connection.url());
    }

    /**
     * Test submit method when form does not have an action URL
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionUrl() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        formElement.submit();
    }

    /**
     * Test formData method when form has only input elements
     */
    @Test
    public void testFormDataWithInputElements() {
        Tag formTag = new Tag("form");

        Element inputElement1 = new Element("input").attr("name", "input1").attr("type", "text").attr("value", "value1");
        Element inputElement2 = new Element("input").attr("name", "input2").attr("type", "text").attr("value", "value2");

        formTag.appendElements(inputElement1, inputElement2);
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("input1", "value1"));
        expectedData.add(HttpConnection.KeyVal.create("input2", "value2"));

        List<Connection.KeyVal> actualData = formElement.formData();

        Assert.assertEquals(expectedData, actualData);
    }

    /**
     * Test formData method when form has a select element
     */
    @Test
    public void testFormDataWithSelectElement() {
        Tag formTag = new Tag("form");

        Element selectElement = new Element("select").attr("name", "select1");
        Element optionElement1 = new Element("option").attr("value", "option1").text("Option 1");
        Element optionElement2 = new Element("option").attr("value", "option2").text("Option 2");
        selectElement.appendElements(optionElement1, optionElement2);
        selectElement.attr("selectedIndex", "0");

        formTag.appendElement(selectElement);
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("select1", "option1"));

        List<Connection.KeyVal> actualData = formElement.formData();

        Assert.assertEquals(expectedData, actualData);
    }

    /**
     * Test formData method when form has checkbox elements
     */
    @Test
    public void testFormDataWithCheckboxElements() {
        Tag formTag = new Tag("form");

        Element checkboxElement1 = new Element("input").attr("name", "checkbox1").attr("type", "checkbox").attr("value", "value1");
        Element checkboxElement2 = new Element("input").attr("name", "checkbox1").attr("type", "checkbox").attr("value", "value2");

        formTag.appendElements(checkboxElement1, checkboxElement2);
        Element inputElement = new Element("input").attr("name", "input").attr("type", "hidden").attr("value", "hiddenValue");

        formTag.appendElement(inputElement);
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        checkboxElement1.attr("checked", "checked");

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("checkbox1", "value1"));

        List<Connection.KeyVal> actualData = formElement.formData();

        Assert.assertEquals(expectedData, actualData);
    }

    /**
     * Test formData method when form has radio elements
     */
    @Test
    public void testFormDataWithRadioElements() {
        Tag formTag = new Tag("form");

        Element radioGroupElement = new Element("input").attr("name", "radioGroup").attr("type", "radio").attr("value", "value1");
        Element radioElement1 = new Element("input").attr("name", "radioGroup").attr("type", "radio").attr("value", "value2");
        Element radioElement2 = new Element("input").attr("name", "radioGroup").attr("type", "radio").attr("value", "value3");
        radioElement1.attr("checked", "checked");

        formTag.appendElements(radioGroupElement, radioElement1, radioElement2);
        Element inputElement = new Element("input").attr("name", "input").attr("type", "hidden").attr("value", "hiddenValue");

        formTag.appendElement(inputElement);
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("radioGroup", "value2"));

        List<Connection.KeyVal> actualData = formElement.formData();

        Assert.assertEquals(expectedData, actualData);
    }

    /**
     * Test formData method when form has a disabled input element
     */
    @Test
    public void testFormDataWithDisabledInputElement() {
        Tag formTag = new Tag("form");

        Element inputElement = new Element("input").attr("name", "input").attr("type", "text").attr("value", "value1").attr("disabled", "disabled");

        formTag.appendElement(inputElement);
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> expectedData = new ArrayList<>();

        List<Connection.KeyVal> actualData = formElement.formData();

        Assert.assertEquals(expectedData, actualData);
    }
}