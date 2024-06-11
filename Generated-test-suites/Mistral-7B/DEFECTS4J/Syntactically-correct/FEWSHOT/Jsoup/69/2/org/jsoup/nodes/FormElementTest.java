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
    public void testFormElement_Constructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "my-form");

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
        assertEquals("form", formElement.tagName());
        assertTrue(formElement instanceof FormElement);
    }

    @Test
    public void testFormElement_Elements() {
        Element form = new Elements().element(new Tag("form")).get(0);
        Element input1 = new Elements().element(new Tag("input")).get(0);
        Element input2 = new Elements().element(new Tag("input")).get(1);

        FormElement formElement = new FormElement(form.tag(), form.baseUri(), form.attributes());
        formElement.addElement(input1);
        formElement.addElement(input2);

        Elements elements = formElement.elements();

        assertEquals(2, elements.size());
        assertSame(input1, elements.get(0));
        assertSame(input2, elements.get(1));
    }

    @Test
    public void testFormElement_Submit() throws Exception {
        String baseUri = "http://example.com";
        Connection connection = Jsoup.connect(baseUri);
        Document document = connection.get();

        Element form = document.select("form").first();
        Element input = form.select("input[name=username]").first();
        input.val("test");

        FormElement formElement = new FormElement(form.tag(), baseUri, form.attributes());
        Connection submitConnection = formElement.submit();

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key);
        assertEquals("test", formData.get(0).value);

        String response = submitConnection.execute().body();
        assertTrue(response.contains("test"));
    }

    @Test
    public void testFormElement_SelectElement() {
        String baseUri = "http://example.com";
        Connection connection = Jsoup.connect(baseUri);
        Document document = connection.get();

        Element form = document.select("form").first();
        Element select = form.select("select[name=color]").first();
        select.select("option[value=red]").first().attr("selected", "selected");

        FormElement formElement = new FormElement(form.tag(), baseUri, form.attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("color", formData.get(0).key);
        assertEquals("red", formData.get(0).value);
    }

    @Test
    public void testFormElement_CheckboxOrRadio() {
        String baseUri = "http://example.com";
        Connection connection = Jsoup.connect(baseUri);
        Document document = connection.get();

        Element form = document.select("form").first();
        Element checkbox = form.select("input[name=agree][type=checkbox]").first();
        checkbox.attr("checked", "checked");

        FormElement formElement = new FormElement(form.tag(), baseUri, form.attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("agree", formData.get(0).key);
        assertEquals("on", formData.get(0).value);

        formElement = new FormElement(form.tag(), baseUri, form.attributes());
        formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElement_NoNameAttribute() {
        String baseUri = "http://example.com";
        Connection connection = Jsoup.connect(baseUri);
        Document document = connection.get();

        Element form = document.select("form").first();
        Element element = form.select("div").first();

        FormElement formElement = new FormElement(form.tag(), baseUri, form.attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        formElement = new FormElement(element.tag(), baseUri, element.attributes());
        formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElement_NoFormActionUrl() {
        String baseUri = "http://example.com";
        Connection connection = Jsoup.connect(baseUri);
        Document document = connection.get();

        Element form = document.select("form").first();
        FormElement formElement = new FormElement(form.tag(), baseUri, form.attributes());

        formElement.submit();
    }

    @Test
    public void testFormElement_EmptyFormData() {
        String baseUri = "http://example.com";
        Connection connection = Jsoup.connect(baseUri);
        Document document = connection.get();

        Element form = document.select("form").first();

        FormElement formElement = new FormElement(form.tag(), baseUri, form.attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElement_SubmitWithEmptyForm() {
        String baseUri = "http://example.com";
        Connection connection = Jsoup.connect(baseUri);
        Document document = connection.get();

        Element form = document.select("form").first();

        FormElement formElement = new FormElement(form.tag(), baseUri, form.attributes());
        Connection submitConnection = formElement.submit();

        String response = submitConnection.execute().body();
        assertTrue(response.isEmpty());
    }

    @Test
    public void testFormElement_FormWithDisabledInput() {
        String baseUri = "http://example.com";
        Connection connection = Jsoup.connect(baseUri);
        Document document = connection.get();

        Element form = document.select("form").first();
        Element input = form.select("input[name=password][disabled]").first();

        FormElement formElement = new FormElement(form.tag(), baseUri, form.attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormElement_FormWithSelectMultiple() {
        String baseUri = "http://example.com";
        Connection connection = Jsoup.connect(baseUri);
        Document document = connection.get();

        Element form = document.select("form").first();
        Element select = form.select("select[name=colors][multiple]").first();
        select.select("option[value=red]").first().attr("selected", "selected");
        select.select("option[value=blue]").first().attr("selected", "selected");

        FormElement formElement = new FormElement(form.tag(), baseUri, form.attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("colors", formData.get(0).key);
        assertEquals("red", formData.get(0).value);
        assertEquals("colors", formData.get(1).key);
        assertEquals("blue", formData.get(1).value);
    }
}