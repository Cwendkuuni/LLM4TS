package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    private static final String BASE_URI = "http://example.com";
    private static final String FORM_HTML = "<form action='/submit' method='post' name='myForm'> " +
            "<input type='text' name='username' value='test'> " +
            "<input type='password' name='password' value='password'> " +
            "<select name='selectElement'> " +
            "<option value='option1'>Option 1</option>" +
            "<option value='option2' selected>Option 2</option>" +
            "</select>" +
            "<input type='checkbox' name='checkbox' value='checked'> " +
            "<input type='checkbox' name='checkbox' value='unchecked'> " +
            "<input type='radio' name='radio' value='option1'> " +
            "<input type='radio' name='radio' value='option2' checked>" +
            "</form>";

    private FormElement formElement;
    private Element rootElement;
    private Connection connection;

    @Before
    public void setup() {
        rootElement = Jsoup.parse(FORM_HTML, BASE_URI);
        Element formElementNode = rootElement.selectFirst("form");
        formElement = new FormElement(formElementNode.tag(), BASE_URI, formElementNode.attributes());
        connection = mock(Connection.class);
    }

    @Test
    public void testFormElementCreation() {
        assertEquals("form", formElement.tagName());
        assertEquals(BASE_URI, formElement.baseUri());
        assertEquals(Arrays.asList("action", "method"), formElement.attributes().keySet());
        assertEquals("method", formElement.attributes().get("method"));
        assertEquals("POST", formElement.attributes().get("method").toUpperCase());
    }

    @Test
    public void testElements() {
        Elements expectedElements = new Elements(Arrays.asList(
                new ElementTag("input", createInputAttributes("username", "text")),
                new ElementTag("input", createInputAttributes("password", "password")),
                new ElementTag("select", createSelectAttributes("selectElement")),
                new ElementTag("option", new Attributes().put("value", "option1")),
                new ElementTag("option", new Attributes().put("value", "option2").put("selected", "selected")),
                new ElementTag("input", createInputAttributes("checkbox", "checkbox")),
                new ElementTag("input", createInputAttributes("checkbox", "checkbox")),
                new ElementTag("input", createInputAttributes("radio", "radio")),
                new ElementTag("input", createInputAttributes("radio", "radio")),
                new ElementTag("form", new Attributes().put("action", "/submit").put("method", "post").put("name", "myForm"))
        ));

        Elements actualElements = formElement.elements();
        assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            assertEquals(expectedElements.get(i).tag(), actualElements.get(i).tag());
            assertEquals(expectedElements.get(i).attributes(), actualElements.get(i).attributes());
        }
    }

    @Test
    public void testSubmit() throws Exception {
        when(connection.requestBody(any(Connection.Request.class)))
                .thenReturn(new StringReader(""));

        List<Connection.KeyVal> formData = formElement.submit().data();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("username", "test"),
                new Connection.KeyVal("password", "password"),
                new Connection.KeyVal("selectElement", "option2"),
                new Connection.KeyVal("checkbox", "checked"),
                new Connection.KeyVal("radio", "option2")
        );

        assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            assertEquals(expectedFormData.get(i).key, formData.get(i).key);
            assertEquals(expectedFormData.get(i).val(), formData.get(i).val());
        }

        verify(connection).requestBody(any(Connection.Request.class));
        verifyNoMoreInteractions(connection);
    }

    private Attributes createInputAttributes(String name, String type) {
        return new Attributes().put("name", name).put("type", type);
    }

    private Attributes createSelectAttributes(String name) {
        return new Attributes().put("name", name);
    }
}