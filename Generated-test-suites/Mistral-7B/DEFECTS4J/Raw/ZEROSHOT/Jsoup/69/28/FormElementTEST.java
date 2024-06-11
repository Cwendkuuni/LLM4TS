package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Parser parser = Jsoup.parser();
        Tag tag = parser.parseTag("form");
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testFormElementElements() {
        Parser parser = Jsoup.parser();
        Tag formTag = parser.parseTag("form");
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = parser.parseTag("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "userName");
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals("input", elements.get(0).tagName());
        assertEquals("userName", elements.get(0).attr("name"));
    }

    @Test
    public void testFormElementSubmit() {
        Parser parser = Jsoup.parser();
        String baseUri = "http://test.com";
        Tag formTag = parser.parseTag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element inputElement = parser.parseTag("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "userName");
        formElement.addElement(inputElement);

        Element textAreaElement = parser.parseTag("textarea");
        textAreaElement.attr("name", "message");
        formElement.addElement(textAreaElement);

        Element selectElement = parser.parseTag("select");
        Element optionElement1 = parser.parseTag("option");
        optionElement1.text("Option 1");
        optionElement1.attr("value", "option1");
        selectElement.add(optionElement1);
        Element optionElement2 = parser.parseTag("option");
        optionElement2.text("Option 2");
        optionElement2.attr("value", "option2");
        selectElement.add(optionElement2);
        selectElement.attr("name", "option");
        formElement.addElement(selectElement);

        Element checkboxElement = parser.parseTag("input");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "agree");
        formElement.addElement(checkboxElement);

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.data();

        // Test for text input
        assertEquals(3, formData.size());
        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("userName", keyVal1.key);
        assertEquals("", keyVal1.value);

        // Test for textarea input
        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("message", keyVal2.key);
        assertEquals("", keyVal2.value);

        // Test for select input
        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("option", keyVal3.key);
        assertEquals("", keyVal3.value);

        Mockito.doReturn("selectedOption1").when(optionElement1).getText();
        Mockito.doReturn("Option 2".getBytes()).when(optionElement2).getText();
        Mockito.doReturn("true").when(checkboxElement).hasAttr("checked");

        formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(inputElement);
        formElement.addElement(textAreaElement);
        formElement.addElement(selectElement);
        formElement.addElement(checkboxElement);

        connection = formElement.submit();
        formData = connection.data();

        // Test for text input with value
        Connection.KeyVal keyValInput1 = formData.get(0);
        assertEquals("userName", keyValInput1.key);
        assertEquals("", keyValInput1.value);

        // Test for textarea input with value
        Connection.KeyVal keyValTextArea = formData.get(1);
        assertEquals("message", keyValTextArea.key);
        assertEquals("", keyValTextArea.value);

        // Test for select input with selected value
        Connection.KeyVal keyValSelect = formData.get(2);
        assertEquals("option", keyValSelect.key);
        assertEquals("option1", keyValSelect.value);

        // Test for checkbox input with checked value
        Connection.KeyVal keyValCheckbox = formData.get(3);
        assertEquals("agree", keyValCheckbox.key);
        assertEquals("true", keyValCheckbox.value);
    }
}