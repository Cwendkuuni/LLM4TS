package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Assert.assertNotNull(formElement.elements());
        Assert.assertEquals(0, formElement.elements().size());

        Element controlElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(controlElement);

        Assert.assertEquals(1, formElement.elements().size());
        Assert.assertTrue(formElement.elements().contains(controlElement));
    }

    @Test
    public void testAddElement() {
        Element controlElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(controlElement);

        Assert.assertEquals(1, formElement.elements().size());
        Assert.assertTrue(formElement.elements().contains(controlElement));
    }

    @Test
    public void testSubmit() {
        // Test case: submit form with absolute action URL
        formElement.attr("action", "http://example.com/action");
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        Assert.assertEquals("http://example.com/action", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());

        // Test case: submit form with relative action URL
        formElement.removeAttr("action");
        formElement.removeAttr("method");
        Connection connection2 = formElement.submit();
        String expectedUrl = "http://example.com";
        Assert.assertEquals(expectedUrl, connection2.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection2.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        // Test case: form with input elements only
        Element inputElement1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement1.attr("name", "input1");
        inputElement1.val("value1");
        formElement.addElement(inputElement1);

        Element inputElement2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement2.attr("name", "input2");
        inputElement2.val("value2");
        formElement.addElement(inputElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("input1", formData.get(0).key());
        Assert.assertEquals("value1", formData.get(0).value());
        Assert.assertEquals("input2", formData.get(1).key());
        Assert.assertEquals("value2", formData.get(1).value());

        // Test case: form with select element
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        selectElement.attr("name", "select");
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.val("optionValue");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData2 = formElement.formData();
        Assert.assertEquals(3, formData2.size());

        // Test case: form with checkbox element
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("value", "checkboxValue");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData3 = formElement.formData();
        Assert.assertEquals(4, formData3.size());
        Assert.assertEquals("checkbox", formData3.get(3).key());
        Assert.assertEquals("checkboxValue", formData3.get(3).value());

        // Test case: form with radio element
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("name", "radio");
        radioElement.attr("type", "radio");
        radioElement.attr("value", "radioValue");
        radioElement.attr("checked", "checked");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData4 = formElement.formData();
        Assert.assertEquals(5, formData4.size());
        Assert.assertEquals("radio", formData4.get(4).key());
        Assert.assertEquals("radioValue", formData4.get(4).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Assert.assertEquals(formElement, formElement2);

        formElement2.attr("action", "http://example.com/action");
        Assert.assertNotEquals(formElement, formElement2);
    }
}