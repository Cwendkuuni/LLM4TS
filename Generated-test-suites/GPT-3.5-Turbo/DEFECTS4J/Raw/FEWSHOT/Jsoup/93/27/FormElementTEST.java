package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    @Test
    public void testElements() {
        // Initialize
        Tag tag = mock(Tag.class);
        String baseUri = "";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        // Test
        Elements result = formElement.elements();

        // Verify
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAddElement() {
        // Initialize
        Tag tag = mock(Tag.class);
        String baseUri = "";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = mock(Element.class);

        // Test
        FormElement result = formElement.addElement(element);

        // Verify
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
        assertEquals(formElement, result);
    }

    @Test
    public void testRemoveChild() {
        // Initialize
        Tag tag = mock(Tag.class);
        String baseUri = "";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = mock(Element.class);
        formElement.addElement(element);

        // Test
        formElement.removeChild(element);

        // Verify
        assertTrue(formElement.elements().isEmpty());
        verify(element).remove();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionURL() {
        // Initialize
        Tag tag = mock(Tag.class);
        String baseUri = "";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        // Test
        formElement.submit();
    }

    @Test
    public void testSubmitMethodPOST() {
        // Initialize
        Tag tag = mock(Tag.class);
        String baseUri = "";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        formElement.attr("method", "post");

        Connection connectionMock = mock(Connection.class);
        when(Jsoup.connect(anyString())).thenReturn(connectionMock);

        // Test
        Connection result = formElement.submit();

        // Verify
        verify(connectionMock).data(anyList());
        verify(connectionMock).method(Connection.Method.POST);
        assertEquals(connectionMock, result);
    }

    @Test
    public void testSubmitMethodGET() {
        // Initialize
        Tag tag = mock(Tag.class);
        String baseUri = "";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        formElement.attr("method", "get");

        Connection connectionMock = mock(Connection.class);
        when(Jsoup.connect(anyString())).thenReturn(connectionMock);

        // Test
        Connection result = formElement.submit();

        // Verify
        verify(connectionMock).data(anyList());
        verify(connectionMock).method(Connection.Method.GET);
        assertEquals(connectionMock, result);
    }

    @Test
    public void testFormData() {
        // Initialize
        Tag tag = mock(Tag.class);
        String baseUri = "";
        Attributes attributes = mock(Attributes.class);
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);
        Element element3 = mock(Element.class);

        when(element1.attr("name")).thenReturn("name1");
        when(element1.tag()).thenReturn(Tag.valueOf("input"));
        when(element1.val()).thenReturn("value1");

        when(element2.attr("name")).thenReturn("name2");
        when(element2.tag()).thenReturn(Tag.valueOf("select"));
        when(element2.select(anyString())).thenReturn(new Elements());
        when(element2.getElementsByTag(anyString())).thenReturn(new Elements());

        when(element3.attr("name")).thenReturn("name3");
        when(element3.tag()).thenReturn(Tag.valueOf("checkbox"));
        when(element3.hasAttr("checked")).thenReturn(true);
        when(element3.val()).thenReturn("value3");

        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);

        // Test
        List<Connection.KeyVal> result = formElement.formData();

        // Verify
        assertEquals(2, result.size());
        assertTrue(result.contains(HttpConnection.KeyVal.create("name1", "value1")));
        assertTrue(result.contains(HttpConnection.KeyVal.create("name3", "value3")));
    }
}