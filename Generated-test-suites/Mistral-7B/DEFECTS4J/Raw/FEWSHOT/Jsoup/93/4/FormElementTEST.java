package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ElementFactory;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private static final String BASE_URI = "http://example.com";
    private static final String FORM_HTML = "<form name=\"form\" action=\"/submit\" method=\"post\">" +
            "<input type=\"text\" name=\"input1\" value=\"value1\" />" +
            "<input type=\"text\" name=\"input2\" value=\"value2\" />" +
            "<select name=\"select\">" +
            "<option value=\"option1\">Option 1</option>" +
            "<option value=\"option2\" selected>" +
            "<option value=\"option3\">Option 3</option>" +
            "</select>" +
            "<input type=\"checkbox\" name=\"checkbox\" checked />" +
            "<input type=\"radio\" name=\"radio\" checked />" +
            "</form>";

    private FormElement formElement;
    private Connection connection;
    private Connection.Response connectionResponse;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        connectionResponse = mock(Connection.Response.class);

        Attributes attributes = new Attributes();
        attributes.put("name", "form");
        attributes.put("action", "/submit");
        attributes.put("method", "post");

        Tag tag = ElementFactory.parseTag("form", attributes);

        formElement = new FormElement(tag, BASE_URI, attributes);

        when(connection.data(anyList())).thenReturn(connection);
        when(connection.execute()).thenReturn(connectionResponse);
    }

    @Test
    public void testFormElementCreation() {
        assertEquals("form", formElement.tagName());
        assertEquals("/submit", formElement.attr("action"));
        assertEquals("post", formElement.attr("method"));
    }

    @Test
    public void testElements() {
        Parser parser = Jsoup.parser();
        Elements elements = parser.parseBodyFragment(FORM_HTML).select("input, select, [type~=checkbox], [type~=radio]");

        formElement.addElement(parser.parseBodyFragment(FORM_HTML).first());

        assertEquals(elements.size(), formElement.elements().size());
        for (Element el : elements) {
            FormElement addedElement = formElement.addElement(el);
            assertEquals(addedElement, formElement.elements().get(formElement.elements().size() - 1));
        }
    }

    @Test
    public void testRemoveChild() {
        Element input1 = new Element("input") {
            {
                attributes().put("name", "input1");
                attributes().put("value", "value1");
            }
        };
        Element input2 = new Element("input") {
            {
                attributes().put("name", "input2");
                attributes().put("value", "value2");
            }
        };

        formElement.addElement(input1);
        formElement.addElement(input2);

        FormElement removedElement = formElement.elements().remove(0);

        assertEquals(input1, removedElement);
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        List<Connection.KeyVal> formData = formElement.submit().data();

        verify(connection).data(eq(formData));
        verify(connection).execute();

        assertEquals(6, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("input2", formData.get(1).key());
        assertEquals("value2", formData.get(1).value());
        assertEquals("select", formData.get(2).key());
        assertEquals("option2", formData.get(2).value());
        assertTrue(formData.get(3).key().startsWith("input[name='checkbox']"));
        assertTrue(formData.get(3).value().equals("on"));
        assertTrue(formData.get(4).key().startsWith("input[name='radio']"));
        assertTrue(formData.get(4).value().equals("on"));
        assertEquals("form", formData.get(5).key());
    }

    @Test
    public void testFormDataEmptyForm() {
        FormElement emptyFormElement = new FormElement(ElementFactory.parseTag("form"), BASE_URI, new Attributes());
        List<Connection.KeyVal> formData = emptyFormElement.formData();
        assertEquals(0, formData.size());
    }
}