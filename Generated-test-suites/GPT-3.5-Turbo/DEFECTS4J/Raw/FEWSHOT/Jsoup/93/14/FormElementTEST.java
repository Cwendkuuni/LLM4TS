package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FormElementTest {
    private FormElement formElement;
    private Element element;
    private Elements elements;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        element = Mockito.mock(Element.class);
        elements = new Elements();
        elements.add(element);
    }

    @Test
    public void testElements() {
        formElement.addElement(element);
        assertEquals(elements.size(), formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testAddElement() {
        formElement.addElement(element);
        assertEquals(elements.size(), formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testRemoveChild() {
        formElement.addElement(element);
        formElement.removeChild(element);
        assertEquals(0, formElement.elements().size());
        assertFalse(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://www.example.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        formElement.addElement(element);
        Mockito.when(element.tag()).thenReturn(Tag.valueOf("input"));
        Mockito.when(element.hasAttr("disabled")).thenReturn(false);
        Mockito.when(element.attr("name")).thenReturn("name");
        Mockito.when(element.attr("type")).thenReturn("text");
        Mockito.when(element.val()).thenReturn("value");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }

    @Test
    public void testFormDataDisabledInput() {
        formElement.addElement(element);
        Mockito.when(element.tag()).thenReturn(Tag.valueOf("input"));
        Mockito.when(element.hasAttr("disabled")).thenReturn(true);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataNoNameInput() {
        formElement.addElement(element);
        Mockito.when(element.tag()).thenReturn(Tag.valueOf("input"));
        Mockito.when(element.hasAttr("disabled")).thenReturn(false);
        Mockito.when(element.attr("name")).thenReturn("");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSelect() {
        formElement.addElement(element);
        Mockito.when(element.tag()).thenReturn(Tag.valueOf("select"));

        Element option1 = Mockito.mock(Element.class);
        Element option2 = Mockito.mock(Element.class);
        Elements options = new Elements();
        options.add(option1);
        options.add(option2);

        Mockito.when(element.select("option[selected]")).thenReturn(options);
        Mockito.when(option1.val()).thenReturn("value1");
        Mockito.when(option2.val()).thenReturn("value2");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("name", keyVal1.key());
        assertEquals("value1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("name", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }

    @Test
    public void testFormDataCheckboxChecked() {
        formElement.addElement(element);
        Mockito.when(element.tag()).thenReturn(Tag.valueOf("input"));
        Mockito.when(element.hasAttr("disabled")).thenReturn(false);
        Mockito.when(element.attr("name")).thenReturn("name");
        Mockito.when(element.attr("type")).thenReturn("checkbox");
        Mockito.when(element.hasAttr("checked")).thenReturn(true);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataCheckboxUnchecked() {
        formElement.addElement(element);
        Mockito.when(element.tag()).thenReturn(Tag.valueOf("input"));
        Mockito.when(element.hasAttr("disabled")).thenReturn(false);
        Mockito.when(element.attr("name")).thenReturn("name");
        Mockito.when(element.attr("type")).thenReturn("checkbox");
        Mockito.when(element.hasAttr("checked")).thenReturn(false);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataRadioChecked() {
        formElement.addElement(element);
        Mockito.when(element.tag()).thenReturn(Tag.valueOf("input"));
        Mockito.when(element.hasAttr("disabled")).thenReturn(false);
        Mockito.when(element.attr("name")).thenReturn("name");
        Mockito.when(element.attr("type")).thenReturn("radio");
        Mockito.when(element.hasAttr("checked")).thenReturn(true);
        Mockito.when(element.val()).thenReturn("value");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }

    @Test
    public void testFormDataRadioUnchecked() {
        formElement.addElement(element);
        Mockito.when(element.tag()).thenReturn(Tag.valueOf("input"));
        Mockito.when(element.hasAttr("disabled")).thenReturn(false);
        Mockito.when(element.attr("name")).thenReturn("name");
        Mockito.when(element.attr("type")).thenReturn("radio");
        Mockito.when(element.hasAttr("checked")).thenReturn(false);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataTextInput() {
        formElement.addElement(element);
        Mockito.when(element.tag()).thenReturn(Tag.valueOf("input"));
        Mockito.when(element.hasAttr("disabled")).thenReturn(false);
        Mockito.when(element.attr("name")).thenReturn("name");
        Mockito.when(element.attr("type")).thenReturn("text");
        Mockito.when(element.val()).thenReturn("value");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }
}