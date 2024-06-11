package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    private static final String TEST_BASE_URI = "http://test.com";
    private static final String TEST_ACTION_URL = "http://test.com/submit";

    private FormElement formElement;
    private Element inputElement;
    private Element selectElement;
    private Element checkboxElement;

    @Before
    public void setup() {
        InputStream inputStream = getClass().getResourceAsStream("/FormElementTest.html");
        Document document = Jsoup.parse(inputStream, "UTF-8");
        Tag formTag = document.select("form").first();
        formElement = new FormElement(formTag, TEST_BASE_URI, null);
        inputElement = createMock(Element.class);
        selectElement = createMock(Element.class);
        when(selectElement.tag()).thenReturn(new Tag("select"));
        when(selectElement.val()).thenReturn("value");
        when(selectElement.select("option[selected]")).thenReturn(Arrays.asList(selectElement));
        when(selectElement.select("option")).thenReturn(Arrays.asList(selectElement, createMock(Element.class)));
        checkboxElement = createMock(Element.class);
        when(checkboxElement.tag()).thenReturn(new Tag("input"));
        when(checkboxElement.attr("type")).thenReturn("checkbox");
        when(checkboxElement.hasAttr("checked")).thenReturn(true);

        Elements elements = new Elements();
        elements.add(inputElement);
        elements.add(selectElement);
        elements.add(checkboxElement);
        formElement.elements = elements;
    }

    @Test
    public void testFormElementCreation() {
        assertEquals("form", formElement.tag().name());
    }

    @Test
    public void testGetFormControls() {
        assertEquals(3, formElement.elements().size());
    }

    @Test
    public void testAddElement() {
        FormElement formElementCopy = formElement;
        Element textElement = createMock(Element.class);
        formElementCopy.addElement(textElement);
        verify(formElementCopy.elements).add(textElement);
    }

    @Test
    public void testRemoveChild() {
        FormElement formElementCopy = formElement;
        formElementCopy.removeChild(inputElement);
        verify(formElementCopy.elements).remove(inputElement);
    }

    @Test
    public void testSubmitWithGETMethod() {
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                HttpConnection.KeyVal.create("inputName", "inputValue"),
                HttpConnection.KeyVal.create("selectName", "value"),
                HttpConnection.KeyVal.create("checkboxName", "on")
        );

        Connection connection = formElement.submit();
        List<Connection.KeyVal> actualFormData = connection.data();

        assertEquals(3, actualFormData.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(expectedFormData.get(i).getName(), actualFormData.get(i).getName());
            assertEquals(expectedFormData.get(i).getValue(), actualFormData.get(i).getValue());
        }

        // submit with an empty form
        FormElement emptyFormElement = new FormElement(new Tag("form"), TEST_BASE_URI, null);
        Connection emptyFormConnection = emptyFormElement.submit();
        List<Connection.KeyVal> emptyFormData = emptyFormConnection.data();
        assertTrue(emptyFormData.isEmpty());
    }

    @Test
    public void testSubmitWithPOSTMethod() {
        // similar to testSubmitWithGETMethod but with POST method
    }
}