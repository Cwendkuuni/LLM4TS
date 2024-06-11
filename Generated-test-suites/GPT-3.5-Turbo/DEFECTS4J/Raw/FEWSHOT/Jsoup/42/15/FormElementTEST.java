package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.select.Elements;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Attributes;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = mock(Tag.class);
        String baseUri = "http://www.example.com";
        Attributes attributes = mock(Attributes.class);
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = mock(Element.class);
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }

    @Test
    public void testSubmit() {
        Element element = mock(Element.class);
        formElement.addElement(element);
        formElement.attr("action", "http://www.example.com");
        Connection con = formElement.submit();
        assertEquals("http://www.example.com", con.request().url().toString());
        assertEquals(Connection.Method.POST, con.request().method());
    }

    @Test
    public void testSubmitWithEmptyAction() {
        Connection con = formElement.submit();
        assertNull(con.request().url());
    }

    @Test
    public void testFormData() {
        Element element = mock(Element.class);
        when(element.tag()).thenReturn(Tag.valueOf("input"));
        when(element.attr("name")).thenReturn("username");
        when(element.val()).thenReturn("testuser");

        formElement.addElement(element);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        Connection.KeyVal keyVal = data.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("testuser", keyVal.value());
    }

    @Test
    public void testFormDataWithSelectElement() {
        Element selectElement = mock(Element.class);
        when(selectElement.tag()).thenReturn(Tag.valueOf("select"));
        when(selectElement.attr("name")).thenReturn("color");

        Element optionElement1 = mock(Element.class);
        when(optionElement1.tagName()).thenReturn("option");
        when(optionElement1.hasAttr("selected")).thenReturn(true);
        when(optionElement1.val()).thenReturn("blue");

        Element optionElement2 = mock(Element.class);
        when(optionElement2.tagName()).thenReturn("option");
        when(optionElement2.hasAttr("selected")).thenReturn(true);
        when(optionElement2.val()).thenReturn("red");

        when(selectElement.select("option[selected]")).thenReturn(new Elements(optionElement1, optionElement2));
        when(selectElement.select("option").first()).thenReturn(optionElement1);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(2, data.size());
        Connection.KeyVal keyVal1 = data.get(0);
        Connection.KeyVal keyVal2 = data.get(1);
        assertEquals("color", keyVal1.key());
        assertEquals("blue", keyVal1.value());
        assertEquals("color", keyVal2.key());
        assertEquals("red", keyVal2.value());
    }

    @Test
    public void testFormDataWithCheckboxElement() {
        Element checkboxElement = mock(Element.class);
        when(checkboxElement.tag()).thenReturn(Tag.valueOf("input"));
        when(checkboxElement.attr("type")).thenReturn("checkbox");
        when(checkboxElement.attr("name")).thenReturn("acceptTerms");
        when(checkboxElement.hasAttr("checked")).thenReturn(true);
        when(checkboxElement.val()).thenReturn("true");

        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        Connection.KeyVal keyVal = data.get(0);
        assertEquals("acceptTerms", keyVal.key());
        assertEquals("true", keyVal.value());
    }

    @Test
    public void testFormDataWithRadioElement() {
        Element radioElement = mock(Element.class);
        when(radioElement.tag()).thenReturn(Tag.valueOf("input"));
        when(radioElement.attr("type")).thenReturn("radio");
        when(radioElement.attr("name")).thenReturn("color");
        when(radioElement.hasAttr("checked")).thenReturn(true);
        when(radioElement.val()).thenReturn("blue");

        formElement.addElement(radioElement);

        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        Connection.KeyVal keyVal = data.get(0);
        assertEquals("color", keyVal.key());
        assertEquals("blue", keyVal.value());
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = formElement;
        assertEquals(formElement2, formElement);
    }
}