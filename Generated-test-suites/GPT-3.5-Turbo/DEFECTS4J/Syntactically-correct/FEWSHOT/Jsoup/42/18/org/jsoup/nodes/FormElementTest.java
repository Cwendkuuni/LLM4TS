package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FormElementTest {
    private FormElement form;

    @Before
    public void setUp() {
        Tag tag = mock(Tag.class);
        String baseUri = "http://example.com";
        Attributes attributes = mock(Attributes.class);
        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        assertNotNull(form.elements());
        assertTrue(form.elements().isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = mock(Element.class);
        form.addElement(element);
        assertFalse(form.elements().isEmpty());
        assertTrue(form.elements().contains(element));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        form.submit();
    }

    @Test
    public void testSubmitGetMethod() {
        form.attr("method", "GET");
        Connection con = form.submit();
        assertEquals(Connection.Method.GET, con.request().method());
    }

    @Test
    public void testSubmitPostMethod() {
        form.attr("method", "POST");
        Connection con = form.submit();
        assertEquals(Connection.Method.POST, con.request().method());
    }

    @Test
    public void testFormDataEmpty() {
        List<Connection.KeyVal> data = form.formData();
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormDataWithElements() {
        Element element1 = mock(Element.class);
        when(element1.tag()).thenReturn(Tag.valueOf("input"));
        when(element1.attr("name")).thenReturn("username");
        when(element1.val()).thenReturn("john");

        Element element2 = mock(Element.class);
        when(element2.tag()).thenReturn(Tag.valueOf("input"));
        when(element2.attr("name")).thenReturn("password");
        when(element2.val()).thenReturn("pass123");

        form.addElement(element1);
        form.addElement(element2);

        List<Connection.KeyVal> data = form.formData();
        assertFalse(data.isEmpty());
        assertEquals(2, data.size());
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(mock(Tag.class), "http://example.com", mock(Attributes.class));
        FormElement form2 = new FormElement(mock(Tag.class), "http://example.com", mock(Attributes.class));
        assertTrue(form1.equals(form2));
    }
}