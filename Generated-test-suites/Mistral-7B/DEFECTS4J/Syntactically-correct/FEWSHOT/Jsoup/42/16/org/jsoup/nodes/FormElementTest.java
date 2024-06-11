package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = mock(Tag.class);
        when(tag.tagName()).thenReturn("form");

        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("name", "my-form");
        attributes.put("action", "/submit-url");

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals("form", formElement.tagName());
        assertEquals("my-form", formElement.attr("name"));
        assertEquals("/submit-url", formElement.absUrl("action"));
        assertEquals("http://example.com", formElement.baseUri());
    }

    @Test
    public void testFormElementEquals() {
        FormElement formElement1 = new FormElement(mock(Tag.class), "baseUri1", new Attributes());
        FormElement formElement2 = new FormElement(mock(Tag.class), "baseUri1", new Attributes());

        assertEquals(formElement1, formElement2);

        FormElement formElement3 = new FormElement(mock(Tag.class), "baseUri2", new Attributes());

        assertNotEquals(formElement1, formElement3);
    }

    @Test
    public void testFormElementElements() {
        FormElement formElement = new FormElement(mock(Tag.class), "baseUri", new Attributes());

        Element input1 = new Element("input") {
            {
                tagName("input");
                name("input1");
                value("value1");
            }
        };

        Element input2 = new Element("input") {
            {
                tagName("input");
                name("input2");
                value("value2");
            }
        };

        formElement.addElement(input1);
        formElement.addElement(input2);

        assertEquals(2, formElement.elements().size());

        List<Element> elements = Arrays.asList(input1, input2);
        assertEquals(elements, formElement.elements());
    }

    @Test
    public void testFormElementSubmit() {
        FormElement formElement = new FormElement(mock(Tag.class), "baseUri", new Attributes());

        String action = "http://example.com/submit-url";
        when(formElement.absUrl("action")).thenReturn(action);

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals(action, connection.url());

        String submitUrlWithParams = "http://example.com/submit-url?input1=value1&input2=value2";
        when(formElement.absUrl("")).thenReturn(submitUrlWithParams);
        connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals(submitUrlWithParams, connection.url());
    }

    @Test
    public void testFormElementFormData() {
        FormElement formElement = new FormElement(mock(Tag.class), "baseUri", new Attributes());

        Element input1 = new Element("input") {
            {
                tagName("input");
                name("input1");
                value("value1");
                attr("type", "text");
            }
        };

        Element selectElement = new Element("select") {
            {
                tagName("select");
                name("select");
                attr("multiple", "multiple");

                Element option1 = new Element("option") {
                    {
                        tagName("option");
                        text("Option 1");
                        attr("value", "option1");
                        attr("selected", "selected");
                    }
                };

                Element option2 = new Element("option") {
                    {
                        tagName("option");
                        text("Option 2");
                        attr("value", "option2");
                    }
                };

                addElement(option1);
                addElement(option2);

                addElement(input1);
            }
        };

        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());

        List<Connection.KeyVal> expectedKeyVals = Arrays.asList(
                HttpConnection.KeyVal.create("input1", "value1"),
                HttpConnection.KeyVal.create("select", "option1"),
                HttpConnection.KeyVal.create("select", "option2")
        );

        assertEquals(expectedKeyVals, formData);
    }

    @Test
    public void testFormElementFormDataEmpty() {
        FormElement formElement = new FormElement(mock(Tag.class), "baseUri", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }
}