package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;

public class FormElementTest {

    private FormElement formElement;
    private Elements elements;
    private Element mockElement;
    private Tag tag;
    private Attributes attributes;
    private String baseUri;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tag = new Tag("form");
        attributes = new Attributes();
        baseUri = "http://example.com";

        mockElement = mock(Element.class);
        elements = new Elements();
        elements.add(mockElement);

        formElement = new FormElement(tag, baseUri, attributes);
        formElement.addElement(mockElement);
    }

    @After
    public void teardown() {
        formElement = null;
        elements = null;
        mockElement = null;
        tag = null;
        attributes = null;
        baseUri = null;
    }

    @Test
    public void testElements_ReturnsElements() {
        Elements result = formElement.elements();
        assertEquals(elements, result);
    }

    @Test
    public void testAddElement_ReturnsFormElement() {
        FormElement result = formElement.addElement(mockElement);
        assertEquals(formElement, result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSubmit_ThrowsIllegalArgumentException_IfActionUrlCannotBeDetermined() {
       formElement.submit();
    }

    @Test
    public void testSubmit_ReturnsConnection() {
        Attributes attr = new Attributes();
        attr.put("action", "http://example.com/action");
        attr.put("method", "POST");
        tag = new Tag("form", baseUri, attr);
        formElement = new FormElement(tag, baseUri, attr);
        Connection connection = mock(Connection.class);
        when(Jsoup.connect("http://example.com/action")).thenReturn(connection);

        Connection result = formElement.submit();
        assertEquals(connection, result);
        verify(connection, times(1)).data(formElement.formData());
        verify(connection, times(1)).method(Connection.Method.POST);
    }

    @Test
    public void testFormData_ReturnsList() {
        List<Connection.KeyVal> data = new ArrayList<>();
        Connection.KeyVal keyVal = mock(Connection.KeyVal.class);
        when(mockElement.tag()).thenReturn(tag);
        when(mockElement.hasAttr("disabled")).thenReturn(false);
        when(mockElement.attr("name")).thenReturn("name");
        when(mockElement.val()).thenReturn("value");

        data.add(keyVal);

        List<Connection.KeyVal> result = formElement.formData();
        assertEquals(data, result);
    }

    @Test
    public void testFormData_DoesNotAddDisabledElements() {
        when(mockElement.tag().isFormSubmittable()).thenReturn(true);
        when(mockElement.hasAttr("disabled")).thenReturn(true);

        List<Connection.KeyVal> result = formElement.formData();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFormData_DoesNotAddElementsWithoutName() {
        when(mockElement.tag().isFormSubmittable()).thenReturn(true);
        when(mockElement.hasAttr("disabled")).thenReturn(false);
        when(mockElement.attr("name")).thenReturn("");

        List<Connection.KeyVal> result = formElement.formData();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFormData_AddsSelectElementOptions() {
        Tag selectTag = new Tag("select");
        Attributes selectAttr = new Attributes();
        selectAttr.put("name", "select");
        Element selectElement = new Element(selectTag, baseUri, selectAttr);
        Elements selectOptions = new Elements();
        Element option1 = new Element("option");
        option1.attr("value", "value1");
        option1.attr("selected", "");
        Element option2 = new Element("option");
        option2.attr("value", "value2");
        selectOptions.add(option1);
        selectOptions.add(option2);
        selectElement.append(selectOptions);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> result = formElement.formData();
        assertEquals(1, result.size());
        assertEquals("select", result.get(0).key());
        assertEquals("value1", result.get(0).value());
    }

    @Test
    public void testFormData_AddsSelectElementFirstOptionAsDefaultValue() {
        Tag selectTag = new Tag("select");
        Attributes selectAttr = new Attributes();
        selectAttr.put("name", "select");
        Element selectElement = new Element(selectTag, baseUri, selectAttr);
        Elements selectOptions = new Elements();
        Element option1 = new Element("option");
        option1.attr("value", "value1");
        selectOptions.add(option1);
        selectElement.append(selectOptions);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> result = formElement.formData();
        assertEquals(1, result.size());
        assertEquals("select", result.get(0).key());
        assertEquals("value1", result.get(0).value());
    }

    @Test
    public void testFormData_AddsCheckboxElementWithCheckedAttribute() {
        Attributes checkboxAttr = new Attributes();
        checkboxAttr.put("name", "checkbox");
        checkboxAttr.put("type", "checkbox");
        checkboxAttr.put("checked", "");
        Element checkboxElement = new Element("input", baseUri, checkboxAttr);
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> result = formElement.formData();
        assertEquals(1, result.size());
        assertEquals("checkbox", result.get(0).key());
        assertEquals("on", result.get(0).value());
    }

    @Test
    public void testFormData_AddsRadioElementWithCheckedAttribute() {
        Attributes radioAttr = new Attributes();
        radioAttr.put("name", "radio");
        radioAttr.put("type", "radio");
        radioAttr.put("checked", "");
        Element radioElement = new Element("input", baseUri, radioAttr);
        formElement.addElement(radioElement);

        List<Connection.KeyVal> result = formElement.formData();
        assertEquals(1, result.size());
        assertEquals("radio", result.get(0).key());
        assertEquals("on", result.get(0).value());
    }

    @Test
    public void testFormData_AddsTextInputElementWithValue() {
        Attributes textInputAttr = new Attributes();
        textInputAttr.put("name", "text");
        textInputAttr.put("type", "text");
        textInputAttr.put("value", "input");
        Element textInputElement = new Element("input", baseUri, textInputAttr);
        formElement.addElement(textInputElement);

        List<Connection.KeyVal> result = formElement.formData();
        assertEquals(1, result.size());
        assertEquals("text", result.get(0).key());
        assertEquals("input", result.get(0).value());
    }

}