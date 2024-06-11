
package org.jsoup.nodes;

import org.junit.Test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FormElementTest {
    @Test
    public void testAddElement() {
        Tag tag = mock(Tag.class);
        String baseUri = "http://example.com";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = mock(Element.class);
        FormElement result = formElement.addElement(element);

        assertTrue(result.elements().contains(element));
    }

    @Test
    public void testRemoveChild() {
        Tag tag = mock(Tag.class);
        String baseUri = "http://example.com";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = mock(Element.class);
        formElement.addElement(element);
        formElement.removeChild(element);

        assertFalse(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Tag tag = mock(Tag.class);
        String baseUri = "http://example.com";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        formElement.attr("action", "/submit");

        Connection.Method expectedMethod = Connection.Method.POST;

        Jsoup jsoupMock = mock(Jsoup.class);
        Connection connectionMock = mock(Connection.class);
        when(jsoupMock.connect(eq(baseUri + "/submit"))).thenReturn(connectionMock);

        Connection returnedConnection = formElement.submit();

        assertEquals(expectedMethod, returnedConnection.method());
        verify(connectionMock).data(eq(formElement.formData()));
    }

    @Test
    public void testSubmitWithoutAction() {
        Tag tag = mock(Tag.class);
        String baseUri = "http://example.com";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Jsoup jsoupMock = mock(Jsoup.class);
        Connection connectionMock = mock(Connection.class);
        when(jsoupMock.connect(eq(baseUri))).thenReturn(connectionMock);

        Connection returnedConnection = formElement.submit();

        verify(connectionMock).data(eq(formElement.formData()));
    }

    @Test
    public void testFormData() {
        Tag tag = mock(Tag.class);
        String baseUri = "http://example.com";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element1 = mock(Element.class);
        when(element1.tag()).thenReturn(mock(Tag.class));
        when(element1.tag().isFormSubmittable()).thenReturn(true);
        when(element1.attr(eq("disabled"))).thenReturn("");
        when(element1.attr(eq("name"))).thenReturn("name1");
        when(element1.attr(eq("type"))).thenReturn("");
        when(element1.val()).thenReturn("value1");

        Element element2 = mock(Element.class);
        when(element2.tag()).thenReturn(mock(Tag.class));
        when(element2.tag().isFormSubmittable()).thenReturn(true);
        when(element2.attr(eq("disabled"))).thenReturn("");
        when(element2.attr(eq("name"))).thenReturn("name2");
        when(element2.attr(eq("type"))).thenReturn("checkbox");
        when(element2.hasAttr(eq("checked"))).thenReturn(true);
        when(element2.val()).thenReturn("value2");

        Element element3 = mock(Element.class);
        when(element3.tag()).thenReturn(mock(Tag.class));
        when(element3.tag().isFormSubmittable()).thenReturn(true);
        when(element3.attr(eq("disabled"))).thenReturn("");
        when(element3.attr(eq("name"))).thenReturn("name3");
        when(element3.attr(eq("type"))).thenReturn("radio");
        when(element3.hasAttr(eq("checked"))).thenReturn(true);
        when(element3.val()).thenReturn("value3");

        Element element4 = mock(Element.class);
        when(element4.tag()).thenReturn(mock(Tag.class));
        when(element4.tag().isFormSubmittable()).thenReturn(true);
        when(element4.attr(eq("disabled"))).thenReturn("");
        when(element4.attr(eq("name"))).thenReturn("name4");
        when(element4.attr(eq("type"))).thenReturn("select");
        when(element4.select(eq("option[selected]"))).thenReturn(new Elements(element1, element2));
        when(element4.select(eq("option"))).thenReturn(new Elements());

        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);
        formElement.addElement(element4);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();

        expectedFormData.add(HttpConnection.KeyVal.create("name1", "value1"));
        expectedFormData.add(HttpConnection.KeyVal.create("name2", "value2"));
        expectedFormData.add(HttpConnection.KeyVal.create("name3", "value3"));
        expectedFormData.add(HttpConnection.KeyVal.create("name4", "value1"));

        assertEquals(expectedFormData, formElement.formData());
    }
}
