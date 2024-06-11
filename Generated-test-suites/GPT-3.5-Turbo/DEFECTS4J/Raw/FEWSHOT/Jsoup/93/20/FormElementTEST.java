
package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertSame(element, elements.get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testSubmit() {
        formElement.attr("action", "http://example.com/form");
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com/form", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        input.attr("type", "text");
        input.attr("name", "username");
        input.val("john");
        formElement.addElement(input);

        Element textarea = new Element(Tag.valueOf("textarea"), formElement.baseUri(), new Attributes());
        textarea.attr("name", "message");
        textarea.text("Hello World");
        formElement.addElement(textarea);

        Element checkbox = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "subscribe");
        checkbox.attr("checked", "checked");
        formElement.addElement(checkbox);

        Element radio1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radio1.attr("type", "radio");
        radio1.attr("name", "gender");
        radio1.attr("value", "male");
        formElement.addElement(radio1);

        Element radio2 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radio2.attr("type", "radio");
        radio2.attr("name", "gender");
        radio2.attr("value", "female");
        formElement.addElement(radio2);

        Element select = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        option1.attr("value", "option1");
        option1.attr("selected", "selected");
        select.addElement(option1);
        Element option2 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        option2.attr("value", "option2");
        select.addElement(option2);
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(6, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("username", keyVal1.key());
        assertEquals("john", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("message", keyVal2.key());
        assertEquals("Hello World", keyVal2.value());

        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("subscribe", keyVal3.key());
        assertEquals("on", keyVal3.value());

        Connection.KeyVal keyVal4 = formData.get(3);
        assertEquals("gender", keyVal4.key());
        assertEquals("male", keyVal4.value());

        Connection.KeyVal keyVal5 = formData.get(4);
        assertEquals("gender", keyVal5.key());
        assertEquals("female", keyVal5.value());

        Connection.KeyVal keyVal6 = formData.get(5);
        assertEquals("option1", keyVal6.key());
        assertEquals("option1", keyVal6.value());
    }
}
