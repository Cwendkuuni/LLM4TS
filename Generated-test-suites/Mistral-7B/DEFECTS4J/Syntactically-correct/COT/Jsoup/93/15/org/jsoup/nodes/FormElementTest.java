package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @org.junit.Test
    public void testFormElementConstructor() {
        // given
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        // when
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        // then
        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertTrue(formElement instanceof FormElement);
    }

    @org.junit.Test
    public void testGetElementsMethod() {
        // given
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element textInputElement = new Element("input", new Attributes().put("type", "text"));
        Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox"));
        Element selectElement = new Element("select");
        Element optionElement1 = new Element("option", new Attributes().put("value", "Option1"));
        Element optionElement2 = new Element("option", new Attributes().put("value", "Option2"));
        selectElement.appendElements(optionElement1, optionElement2);

        formElement.addElement(textInputElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(selectElement);

        // when
        Elements elements = formElement.elements();

        // then
        assertEquals(3, elements.size());
        assertTrue(elements.contains(textInputElement));
        assertTrue(elements.contains(checkboxElement));
        assertTrue(elements.contains(selectElement));
    }

    @org.junit.Test
    public void testAddElementMethod() {
        // given
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // when
        Element textInputElement = new Element("input", new Attributes().put("type", "text"));
        FormElement newFormElement = formElement.addElement(textInputElement);

        // then
        assertEquals(textInputElement, newFormElement.elements().first());
        assertEquals(1, newFormElement.elements().size());
    }

    @org.junit.Test
    public void testRemoveChildMethod() {
        // given
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element textInputElement = new Element("input", new Attributes().put("type", "text"));

        formElement.addElement(textInputElement);

        // when
        formElement.removeChild(textInputElement);

        // then
        assertFalse(formElement.elements().contains(textInputElement));
        assertEquals(0, formElement.elements().size());
    }

    @org.junit.Test
    public void testSubmitMethod() {
        // given
        String baseUri = "http://example.com";
        ParseSetting.setDefaultParseTime(1000); // set a generous parse time for this test
        Element formElement = Jsoup.parse("<form action='/submit' method='post'><input name='name' type='text' /></form>", baseUri).first();

        // when
        Connection connection = formElement.submit();

        // then
        assertEquals("/submit", connection.url().toString());
        assertEquals("POST", connection.method().name());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithoutAction() {
        // given
        String baseUri = "http://example.com";
        Element formElement = Jsoup.parse("<form><input name='name' type='text' /></form>", baseUri).first();

        // when and then
        formElement.submit();
    }

    @org.junit.Test
    public void testFormDataMethod() {
        // given
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form");
        Attributes attributes = new Attributes().put("action", "/submit");
        formTag.attributes().put("method", "post");

        Element nameInputElement = new Element("input", new Attributes().put("name", "name").put("type", "text").put("value", "John Doe"));
        Element ageInputElement = new Element("input", new Attries().put("name", "age").put("type", "number").put("value", "30"));
        Element genderSelectElement = new Element("select");
        Element maleOptionElement = new Element("option").put("value", "male").put("selected", true);
        Element femaleOptionElement = new Element("option").put("value", "female");
        genderSelectElement.appendElements(maleOptionElement, femaleOptionElement);

        formTag.appendElements(nameInputElement, ageInputElement, genderSelectElement);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // when
        List<Connection.KeyVal> formData = formElement.formData();

        // then
        assertEquals(3, formData.size());
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("name", "John Doe"),
                new Connection.KeyVal("age", "30"),
                new Connection.KeyVal("gender", "male")
        );
        assertEquals(expectedFormData, formData);
    }

    @org.junit.Test
    public void testFormDataMethodWithEmptyForm() {
        // given
        String baseUri = "http://example.com";
        FormElement formElement = new FormElement(new Tag("form"), baseUri, new Attributes());

        // when
        List<Connection.KeyVal> formData = formElement.formData();

        // then
        assertEquals(0, formData.size());
    }

    @org.junit.Test
    public void testFormDataMethodWithDisabledFormControl() {
        // given
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form");
        Attributes attributes = new Attributes().put("action", "/submit");
        formTag.attributes().put("method", "post");

        Element nameInputElement = new Element("input", new Attributes().put("name", "name").put("type", "text").put("disabled", "disabled").put("value", "John Doe"));
        Element ageInputElement = new Element("input", new Attributes().put("name", "age").put("type", "number").put("value", "30"));
        Element genderSelectElement = new Element("select");
        Element maleOptionElement = new Element("option").put("value", "male").put("selected", true);
        Element femaleOptionElement = new Element("option").put("value", "female");
        genderSelectElement.appendElements(maleOptionElement, femaleOptionElement);

        formTag.appendElements(nameInputElement, ageInputElement, genderSelectElement);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // when
        List<Connection.KeyVal> formData = formElement.formData();

        // then
        assertEquals(1, formData.size());
        Connection.KeyVal formDataElement = formData.get(0);
        assertEquals("age", formDataElement.key());
        assertEquals("30", formDataElement.value());
    }
}