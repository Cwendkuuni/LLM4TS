package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class FormElementTest {

    private FormElement formElement;
    private Tag tag;
    private Attributes attributes;
    private Connection connection;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tag = mock(Tag.class);
        attributes = mock(Attributes.class);
        formElement = new FormElement(tag, "baseUri", attributes);
        connection = mock(Connection.class);
    }

    // Test methods for FormElement constructor

    @Test
    public void testFormElementConstructor() {
        assertEquals(tag, formElement.getTag());
        assertEquals("baseUri", formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    // Test method for elements()

    @Test
    public void testElements() {
        Elements elements = new Elements();
        elements.add(mock(Element.class));
        when(formElement.elements()).thenReturn(elements);

        List<Element> formControlElements = formElement.elements().all();
        assertEquals(1, formControlElements.size());
    }

    // Test method for addElement()

    @Test
    public void testAddElement() {
        Element element = mock(Element.class);
        formElement.addElement(element);

        List<Element> formControlElements = formElement.elements().all();
        assertEquals(1, formControlElements.size());
        assertEquals(element, formControlElements.get(0));
    }

    // Test method for removeChild()

    @Test
    public void testRemoveChild() {
        Element element = mock(Element.class);
        formElement.addElement(element);
        formElement.removeChild(element);

        List<Element> formControlElements = formElement.elements().all();
        assertEquals(0, formControlElements.size());
    }

    // Test method for submit()

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        when(formElement.hasAttr("action")).thenReturn(false);
        formElement.submit();
    }

    @Test
    public void testSubmitWithAction() {
        when(formElement.hasAttr("action")).thenReturn(true);
        when(formElement.absUrl("action")).thenReturn("http://test.com");

        Connection submittedConnection = formElement.submit();
        assertNotNull(submittedConnection);
        assertEquals("http://test.com", submittedConnection.url());
    }

    // Test method for formData()

    @Test
    public void testFormDataEmptyForm() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithSingleSelectElement() {
        Element selectElement = mock(Element.class);
        when(selectElement.val()).thenReturn("Option1");
        Element optionElement1 = mock(Element.class);
        when(optionElement1.val()).thenReturn("Value1");
        Element optionElement2 = mock(Element.class);
        when(optionElement2.val()).thenReturn("Value2");
        Elements options = new Elements().add(optionElement1).add(optionElement2);
        when(selectElement.select("option[selected]")).thenReturn(options);

        Element formControlElement = mock(Element.class);
        when(formControlElement.tag().name()).thenReturn("select");
        when(formControlElement.attr("name")).thenReturn("TestSelect");
        when(formElement.elements()).thenReturn(new Elements().add(formControlElement));

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("TestSelect", keyVal.key());
        assertEquals("Value1", keyVal.value());
    }

    @Test
    public void testFormDataWithSingleCheckboxElement() {
        Element checkboxElement = mock(Element.class);
        when(checkboxElement.attr("name")).thenReturn("TestCheckbox");
        when(checkboxElement.hasAttr("checked")).thenReturn(true);

        Element formControlElement = mock(Element.class);
        when(formControlElement.tag().name()).thenReturn("input");
        when(formControlElement.attr("type")).thenReturn("checkbox");
        when(formElement.elements()).thenReturn(new Elements().add(formControlElement));

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("TestCheckbox", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataWithMultipleCheckboxElements() {
        Element checkboxElement1 = mock(Element.class);
        when(checkboxElement1.attr("name")).thenReturn("TestCheckbox");
        when(checkboxElement1.hasAttr("checked")).thenReturn(true);

        Element checkboxElement2 = mock(Element.class);
        when(checkboxElement2.attr("name")).thenReturn("TestCheckbox");
        when(checkboxElement2.hasAttr("checked")).thenReturn(false);

        Element formControlElement1 = mock(Element.class);
        when(formControlElement1.tag().name()).thenReturn("input");
        when(formControlElement1.attr("type")).thenReturn("checkbox");
        when(formElement.elements()).thenReturn(new Elements().add(formControlElement1).add(checkboxElement2));

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("TestCheckbox", keyVal1.key());
        assertEquals("on", keyVal1.value());
    }

    @Test
    public void testFormDataWithMultipleSelectElements() {
        Element selectElement1 = mock(Element.class);
        when(selectElement1.val()).thenReturn("Option1");
        Element optionElement1_1 = mock(Element.class);
        when(optionElement1_1.val()).thenReturn("Value1_1");
        Element optionElement1_2 = mock(Element.class);
        when(optionElement1_2.val()).thenReturn("Value1_2");
        Elements options1 = new Elements().add(optionElement1_1).add(optionElement1_2);
        when(selectElement1.select("option[selected]")).thenReturn(options1);

        Element selectElement2 = mock(Element.class);
        when(selectElement2.val()).thenReturn("Option2");
        Element optionElement2_1 = mock(Element.class);
        when(optionElement2_1.val()).thenReturn("Value2_1");
        Element optionElement2_2 = mock(Element.class);
        when(optionElement2_2.val()).thenReturn("Value2_2");
        Elements options2 = new Elements().add(optionElement2_1).add(optionElement2_2);
        when(selectElement2.select("option[selected]")).thenReturn(options2);

        Element formControlElement1 = mock(Element.class);
        when(formControlElement1.tag().name()).thenReturn("select");
        when(formControlElement1.attr("name")).thenReturn("TestSelect1");
        when(formElement.elements()).thenReturn(new Elements().add(formControlElement1));

        Element formControlElement2 = mock(Element.class);
        when(formControlElement2.tag().name()).thenReturn("select");
        when(formControlElement2.attr("name")).thenReturn("TestSelect2");
        when(formElement.elements()).thenReturn(new Elements().add(formControlElement1).add(formControlElement2));

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("TestSelect1", keyVal1.key());
        assertEquals("Value1_1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("TestSelect2", keyVal2.key());
        assertEquals("Option2", keyVal2.value());
    }
}