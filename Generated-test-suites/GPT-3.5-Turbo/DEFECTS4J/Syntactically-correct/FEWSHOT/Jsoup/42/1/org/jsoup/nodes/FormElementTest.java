package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;
    private Elements elements;

    @Before
    public void setUp() {
        Tag tag = Mockito.mock(Tag.class);
        elements = Mockito.mock(Elements.class);

        formElement = new FormElement(tag, "http://www.example.com", new Attributes());
        formElement.addElement(elements);
    }

    @Test
    public void testElements() {
        assertEquals(elements, formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element element = Mockito.mock(Element.class);
        formElement.addElement(element);

        assertEquals(2, formElement.elements().size());
        assertTrue(formElement.elements().contains(elements));
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement spyFormElement = spy(formElement);
        Connection connection = Mockito.mock(Connection.class);

        doReturn("http://www.example.com/form").when(spyFormElement).absUrl("action");
        doReturn("POST").when(spyFormElement).attr("method");
        doReturn(connection).when(Jsoup.connect("http://www.example.com/form"));

        Connection result = spyFormElement.submit();

        assertEquals(connection, result);
        verify(connection).data(spyFormElement.formData());
        verify(connection).method(Connection.Method.POST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionURL() {
        FormElement spyFormElement = spy(formElement);

        doReturn("").when(spyFormElement).absUrl("action");

        spyFormElement.submit();
    }

    @Test
    public void testFormData() {
        Element element1 = Mockito.mock(Element.class);
        Element element2 = Mockito.mock(Element.class);
        Element option = Mockito.mock(Element.class);
        Elements options = Mockito.mock(Elements.class);

        when(element1.tag()).thenReturn(Tag.valueOf("input"));
        when(element1.attr("name")).thenReturn("input1");
        when(element1.attr("type")).thenReturn("");

        when(element2.tag()).thenReturn(Tag.valueOf("select"));
        when(element2.attr("name")).thenReturn("select1");
        when(element2.tagName()).thenReturn("select");

        when(element2.select("option[selected]")).thenReturn(options);
        when(element2.select("option")).thenReturn(options);
        when(options.first()).thenReturn(option);
        when(option.val()).thenReturn("option1");

        when(option.val()).thenReturn("option1");

        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());
        assertEquals(HttpConnection.KeyVal.create("input1", ""), formData.get(0));
        assertEquals(HttpConnection.KeyVal.create("select1", "option1"), formData.get(1));
    }

    @Test
    public void testEquals() {
        assertFalse(formElement.equals(null));
        assertTrue(formElement.equals(formElement));
    }
}