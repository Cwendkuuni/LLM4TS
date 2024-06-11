package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FormElementTest {

    private static final String HTML = "<html lang=\"en\"><body>" +
            "<form action=\"https://example.com\" method=\"post\">" +
            "<input type=\"text\" name=\"username\" value=\"test\">" +
            "<select name=\"color\">" +
            "<option value=\"red\" selected>Red</option>" +
            "<option value=\"green\">Green</option>" +
            "</select>" +
            "<input type=\"checkbox\" name=\"agree\" value=\"yes\" checked>" +
            "</form>" +
            "</body></html>";

    private Element formElement;

    @Before
    public void setUp() {
        formElement = Jsoup.parse(HTML, null).body().selectFirst("form");
    }

    @Test
    public void testCreateFormElement() {
        FormElement formElement = new FormElement(Tag.FORM, null, new Attributes());
        assertThat(formElement, is(notNullValue()));
    }

    @Test
    public void testFormElementHasElements() {
        Elements elements = formElement.elements();
        assertThat(elements.size(), is(3));
    }

    @Test
    public void testAddElementToForm() {
        FormElement formElement = new FormElement(Tag.FORM, null, new Attributes());
        Element input = new Element("input", new Attributes().put("type", "text").put("name", "test").put("value", "testValue"));
        formElement.addElement(input);
        Elements elements = formElement.elements();
        assertThat(elements.size(), is(2));
        assertThat(elements.get(1), is(input));
    }

    @Test
    public void testRemoveElementFromForm() {
        FormElement formElement = new FormElement(Tag.FORM, null, new Attributes());
        Element input = new Element("input", new Attributes().put("type", "text").put("name", "test").put("value", "testValue"));
        formElement.addElement(input);
        formElement.removeChild(input);
        Elements elements = formElement.elements();
        assertThat(elements.size(), is(0));
    }

    @Test
    public void testFormElementSubmit() {
        Connection connection = formElement.submit();
        assertThat(connection, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithEmptyAction() {
        FormElement formElement = new FormElement(Tag.FORM, null, new Attributes().put("action", ""));
        formElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertThat(formData.size(), is(3));
        assertThat(formData.get(0).key, is("username"));
        assertThat(formData.get(0).value, is("test"));
        assertThat(formData.get(1).key, is("color"));
        assertThat(formData.get(1).value, is("red"));
        assertThat(formData.get(2).key, is("agree"));
        assertThat(formData.get(2).value, is("yes"));
    }
}