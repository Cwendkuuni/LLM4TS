package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    private Element formElement;
    private Element inputElement1;
    private Element inputElement2;
    private Element selectElement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Tag formTag = mock(Tag.class);
        formElement = new FormElement(formTag, "", new Attributes());

        Tag inputTag1 = mock(Tag.class);
        inputElement1 = new Element(inputTag1, "", new Attributes().put("name", "inputName1").put("value", "inputValue1"));

        Tag inputTag2 = mock(Tag.class);
        inputElement2 = new Element(inputTag2, "", new Attributes().put("name", "inputName2").put("value", "inputValue2"));

        Tag selectTag = mock(Tag.class);
        selectElement = new Element(selectTag, "", new Attributes().put("name", "selectName").put("id", "selectId"));

        Elements options = new Elements();
        options.add(new Element(mock(Tag.class), "", new Attributes().put("value", "optionValue1").put("selected", "true")));
        options.add(new Element(mock(Tag.class), "", new Attributes().put("value", "optionValue2")));
        when(selectElement.select("option[selected]")).thenReturn(options);
        when(selectElement.select("option")).thenReturn(options);

        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);
        formElement.addElement(selectElement);
    }

    @Test
    public void testElements() {
        List<Element> expectedElements = Arrays.asList(inputElement1, inputElement2, selectElement);
        List<Element> actualElements = formElement.elements();
        assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testSubmit() {
        String baseUri = "http://example.com";
        String action = "http://example.com/submit";

        when(formElement.baseUri()).thenReturn(baseUri);
        when(formElement.attr("action")).thenReturn(action);

        Connection connection = formElement.submit();
        assertEquals(baseUri, connection.baseUri());
        assertEquals(action, connection.url());
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitEmptyAction() {
        FormElement emptyFormElement = new FormElement(mock(Tag.class), "", new Attributes());
        emptyFormElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("inputName1", "inputValue1"));
        expectedData.add(HttpConnection.KeyVal.create("inputName2", "inputValue2"));
        expectedData.add(HttpConnection.KeyVal.create("selectName", "optionValue1"));

        List<Connection.KeyVal> actualData = formElement.formData();
        assertEquals(expectedData, actualData);
    }
}