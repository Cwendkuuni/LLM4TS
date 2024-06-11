package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement form;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        form = new FormElement(tag, "https://www.example.com", attributes);
    }

    @Test
    public void testElements() {
        assertTrue(form.elements().isEmpty());

        Element input = new Element(Tag.valueOf("input"), "https://www.example.com");
        form.addElement(input);
        
        assertFalse(form.elements().isEmpty());
        assertEquals(input, form.elements().get(0));
    }

    @Test
    public void testAddElement() {
        Element input = new Element(Tag.valueOf("input"), "https://www.example.com");
        FormElement returnedForm = form.addElement(input);
        
        assertEquals(form, returnedForm);
        assertFalse(form.elements().isEmpty());
        assertEquals(input, form.elements().get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = mock(Connection.class);
        Jsoup jsoup = mock(Jsoup.class);

        JsoupConnectMock mockConnection = new JsoupConnectMock();
        mockConnection.setConnection(connection);

        when(jsoup.connect("https://www.example.com")).thenReturn(mockConnection);
        
        form.addElement(new Element(Tag.valueOf("input"), "https://www.example.com"));
        connection = form.submit();

        verify(connection).data(form.formData());
        verify(connection).method(Connection.Method.GET);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidActionUrl() {
        form.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = form.formData();
        assertTrue(data.isEmpty());

        Element input = new Element(Tag.valueOf("input"), "https://www.example.com");
        input.attr("name", "field");
        input.attr("type", "text");
        input.attr("value", "value");

        form.addElement(input);

        data = form.formData();
        assertFalse(data.isEmpty());
        assertEquals(1, data.size());

        Connection.KeyVal keyVal = data.get(0);
        assertEquals("field", keyVal.key());
        assertEquals("value", keyVal.value());
    }
}