
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

import java.util.List;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Assert.assertEquals(0, form.elements().size());

        Element element1 = new Element(Tag.valueOf("input"), form.baseUri());
        form.addElement(element1);
        Assert.assertEquals(1, form.elements().size());

        Element element2 = new Element(Tag.valueOf("input"), form.baseUri());
        form.addElement(element2);
        Assert.assertEquals(2, form.elements().size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), form.baseUri());

        Assert.assertEquals(form, form.addElement(element));
        Assert.assertEquals(1, form.elements().size());
        Assert.assertTrue(form.elements().contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), form.baseUri());
        form.addElement(element);

        Assert.assertEquals(1, form.elements().size());

        form.removeChild(element);
        Assert.assertEquals(0, form.elements().size());
    }

    @Test
    public void testSubmit() {
        String action = "http://example.com";
        String method = "post";

        form.attr("action", action);
        form.attr("method", method);

        Connection connection = form.submit();

        Assert.assertEquals(action, connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmit_noActionUrl() {
        Validate.enableDebugMode();

        try {
            form.submit();
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        Element input1 = new Element(Tag.valueOf("input"), form.baseUri());
        input1.attr("name", "name");
        input1.attr("value", "John");
        form.addElement(input1);

        Element input2 = new Element(Tag.valueOf("input"), form.baseUri());
        input2.attr("name", "email");
        input2.attr("type", "email");
        input2.attr("value", "john@example.com");
        form.addElement(input2);

        List<Connection.KeyVal> formData = form.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("name", formData.get(0).key());
        Assert.assertEquals("John", formData.get(0).value());
        Assert.assertEquals("email", formData.get(1).key());
        Assert.assertEquals("john@example.com", formData.get(1).value());
    }

    @Test
    public void testFormData_selectInput() {
        Element select = new Element(Tag.valueOf("select"), form.baseUri());
        select.attr("name", "color");

        Element option1 = new Element(Tag.valueOf("option"), form.baseUri());
        option1.attr("value", "red");
        option1.attr("selected", "");
        select.appendChild(option1);

        Element option2 = new Element(Tag.valueOf("option"), form.baseUri());
        option2.attr("value", "blue");
        select.appendChild(option2);

        form.addElement(select);

        List<Connection.KeyVal> formData = form.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("color", formData.get(0).key());
        Assert.assertEquals("red", formData.get(0).value());
    }

    @Test
    public void testFormData_disabledInput() {
        Element input = new Element(Tag.valueOf("input"), form.baseUri());
        input.attr("name", "name");
        input.attr("disabled", "");
        form.addElement(input);

        List<Connection.KeyVal> formData = form.formData();

        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_checkboxInput_checked() {
        Element input = new Element(Tag.valueOf("input"), form.baseUri());
        input.attr("name", "agree");
        input.attr("type", "checkbox");
        input.attr("checked", "");
        form.addElement(input);

        List<Connection.KeyVal> formData = form.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("agree", formData.get(0).key());
        Assert.assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormData_checkboxInput_notChecked() {
        Element input = new Element(Tag.valueOf("input"), form.baseUri());
        input.attr("name", "agree");
        input.attr("type", "checkbox");
        form.addElement(input);

        List<Connection.KeyVal> formData = form.formData();

        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_radioInput_checked() {
        Element input = new Element(Tag.valueOf("input"), form.baseUri());
        input.attr("name", "gender");
        input.attr("type", "radio");
        input.attr("checked", "");
        form.addElement(input);

        List<Connection.KeyVal> formData = form.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("gender", formData.get(0).key());
        Assert.assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormData_radioInput_notChecked() {
        Element input = new Element(Tag.valueOf("input"), form.baseUri());
        input.attr("name", "gender");
        input.attr("type", "radio");
        form.addElement(input);

        List<Connection.KeyVal> formData = form.formData();

        Assert.assertEquals(0, formData.size());
    }
}
