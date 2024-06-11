package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FormElementTest {
    
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = mock(Element.class);
        form.addElement(element);
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("action", "http://example.com/submit");
        form.attr("method", "POST");
        
        Connection con = form.submit();
        
        assertEquals(Connection.Method.POST, con.request().method());
        assertEquals("http://example.com/submit", con.request().url().toString());
        assertEquals(form.formData(), con.request().data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = mock(Element.class);
        when(input.tag()).thenReturn(Tag.valueOf("input"));
        when(input.attr("name")).thenReturn("name");
        when(input.attr("value")).thenReturn("John");
        form.addElement(input);
        
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(form1, form2);
    }
}