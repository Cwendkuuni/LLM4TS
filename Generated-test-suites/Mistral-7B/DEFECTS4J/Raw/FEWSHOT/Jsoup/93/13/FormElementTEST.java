package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ElementFactory;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    private static final String TEST_BASE_URI = "http://test.com";

    @Test
    public void testFormElement() {
        Attributes attributes = new Attributes();
        attributes.put("id", "test-form");
        attributes.put("action", "/submit");
        Tag formTag = ElementFactory.tag("form", attributes);

        FormElement formElement = new FormElement(formTag, TEST_BASE_URI, attributes);
        assertEquals("test-form", formElement.tagName());
        assertSame(TEST_BASE_URI, formElement.baseUri());
    }

    @Test
    public void testGetElements() {
        Attributes attributes = new Attributes();
        Tag formTag = ElementFactory.tag("form", attributes);

        FormElement formElement = new FormElement(formTag, TEST_BASE_URI, attributes);

        Element input1 = ElementFactory.createInputElement("input1");
        Element input2 = ElementFactory.createInputElement("input2");

        formElement.addElement(input1);
        formElement.addElement(input2);

        Elements elements = formElement.elements();

        assertEquals(2, elements.size());
        assertEquals(input1, elements.get(0));
        assertEquals(input2, elements.get(1));
    }

    @Test
    public void testSubmit() {
        Attributes attributes = new Attributes();
        Tag formTag = ElementFactory.tag("form", attributes);

        FormElement formElement = new FormElement(formTag, TEST_BASE_URI, attributes);

        Element input = ElementFactory.createInputElement("input");
        formElement.addElement(input);

        String expectedAction = TEST_BASE_URI + "/submit";
        Connection connection = formElement.submit();

        assertEquals(expectedAction, connection.url());
        assertTrue(connection.method().name().equalsIgnoreCase("POST"));
    }

    @Test
    public void testFormDataForEmptyForm() {
        Attributes attributes = new Attributes();
        Tag formTag = ElementFactory.tag("form", attributes);

        FormElement formElement = new FormElement(formTag, TEST_BASE_URI, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataForInput() {
        Attributes attributes = new Attributes();
        attributes.put("type", "text");
        attributes.put("name", "username");
        Tag inputTag = ElementFactory.tag("input", attributes);

        FormElement formElement = new FormElement(Tag.parseBodyFragment("<form><input/></form>"), TEST_BASE_URI, new Attributes());

        Element input = new ElementImplementation.GenericElement(inputTag);
        formElement.addElement(input);

        input.attributes().put("value", "test-user");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("test-user", formData.get(0).value());
    }

    @Test
    public void testFormDataForSelect() {
        Attributes attributes = new Attributes();
        attributes.put("type", "select-one");
        attributes.put("name", "country");

        Tag selectTag = ElementFactory.tag("select", attributes);
        Elements options = new Elements();

        options.add(ElementFactory.createOptionElement("United States"));
        options.add(ElementFactory.createOptionElement("Canada"));

        selectTag.appendText(options);

        FormElement formElement = new FormElement(Tag.parseBodyFragment("<form><select></select></form>"), TEST_BASE_URI, new Attributes());

        Element select = new ElementImplementation.GenericElement(selectTag);
        formElement.addElement(select);

        Element selectedOption = options.get(0);
        select.appendChild(selectedOption);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("country", formData.get(0).key());
        assertEquals("United States", formData.get(0).value());
    }

    @Test
    public void testFormDataForSelectWithMultipleSelected() {
        Attributes attributes = new Attributes();
        attributes.put("type", "select-multiple");
        attributes.put("name", "colors");

        Tag selectTag = ElementFactory.tag("select", attributes);
        Elements options = new Elements();

        options.add(ElementFactory.createOptionElement("Red"));
        options.add(ElementFactory.createOptionElement("Green"));
        options.add(ElementFactory.createOptionElement("Blue"));

        selectTag.appendText(options);

        FormElement formElement = new FormElement(Tag.parseBodyFragment("<form><select></select></form>"), TEST_BASE_URI, new Attributes());

        Element select = new ElementImplementation.GenericElement(selectTag);
        formElement.addElement(select);

        Element redOption = options.get(0);
        Element greenOption = options.get(1);

        select.appendChild(redOption);
        select.appendChild(greenOption);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("colors[]", formData.get(0).key());
        assertEquals("Red", formData.get(0).value());
        assertEquals("colors[]", formData.get(1).key());
        assertEquals("Green", formData.get(1).value());
    }

    @Test
    public void testFormDataForCheckbox() {
        Attributes attributes = new Attributes();
        attributes.put("type", "checkbox");
        attributes.put("name", "hobby");

        Tag checkboxTag = ElementFactory.tag("input", attributes);

        FormElement formElement = new FormElement(Tag.parseBodyFragment("<form><input></form>"), TEST_BASE_URI, new Attributes());

        Element checkbox = new ElementImplementation.GenericElement(checkboxTag);
        formElement.addElement(checkbox);

        checkbox.attributes().put("checked", "checked");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("hobby", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormDataForRadio() {
        Attributes attributes = new Attributes();
        attributes.put("type", "radio");
        attributes.put("name", "gender");
        attributes.put("id", "male");

        Tag radioTag = ElementFactory.tag("input", attributes);
        Tag labelTag = ElementFactory.tag("label", new Attributes().put("for", "male"));
        labelTag.appendText("Male");

        FormElement formElement = new FormElement(Tag.parseBodyFragment("<form><input><label/></form>"), TEST_BASE_URI, new Attributes());

        Element radio = new ElementImplementation.GenericElement(radioTag);
        Element label = new ElementImplementation.GenericElement(labelTag);

        formElement.addElement(radio);
        formElement.addElement(label);

        radio.attributes().put("checked", "checked");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("gender", formData.get(0).key());
        assertEquals("male", formData.get(0).value());
    }

    @Test
    public void testFormDataForDisabledInput() {
        Attributes attributes = new Attributes();
        attributes.put("type", "text");
        attributes.put("name", "username");
        attributes.put("disabled", "");

        Tag inputTag = ElementFactory.tag("input", attributes);

        FormElement formElement = new FormElement(Tag.parseBodyFragment("<form><input/></form>"), TEST_BASE_URI, new Attributes());

        Element input = new ElementImplementation.GenericElement(inputTag);
        formElement.addElement(input);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formElement = new FormElement(Tag.parseBodyFragment("<form></form>"), null, new Attributes());
        formElement.submit();
    }
}