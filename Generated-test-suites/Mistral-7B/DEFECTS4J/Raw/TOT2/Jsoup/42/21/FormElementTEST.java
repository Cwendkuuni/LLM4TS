package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class FormElementTest {
    @Test
    public void testSubmit() {
        Tag formTag = new Tag("form");
        formTag.puts("action", "http://example.com/submit");
        formTag.puts("method", "POST");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        Connection connection = Jsoup.connect("http://example.com/");
        when(connection.data(anyList())).thenReturn(connection);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("action", "http://example.com/submit"));
        assertEquals(expectedFormData, formElement.submit().data());
    }

    @Test
    public void testElements() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        Element inputElement = new Element("input");
        FormElement expectedFormElement = new FormElement(formTag, "", new Attributes());
        expectedFormElement.addElement(inputElement);

        FormElement actualFormElement = formElement.addElement(inputElement);

        assertEquals(expectedFormElement, actualFormElement);
    }

    @Test
    public void testElements_SameElementAddedMultipleTimes() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        Element inputElement = new Element("input");

        formElement.elements().add(inputElement);
        formElement.elements().add(inputElement);

        List<Element> expected = new ArrayList<>();
        expected.add(inputElement);
        List<Element> actual = formElement.elements();

        assertEquals(expected, actual);
    }

    @Test
    public void testFormData() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        Element inputElement = new Element("input");
        inputElement.puts("name", "name1");
        inputElement.puts("type", "text");
        Element selectElement = new Element("select");
        selectElement.puts("name", "name2");
        selectElement.puts("multiple", "multiple");
        selectElement.add(new Element("option").puts("value", "option1"));
        selectElement.add(new Element("option").puts("value", "option2"));

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("name1", ""));
        expectedFormData.add(new Connection.KeyVal("name2", "option1"));
        expectedFormData.add(new Connection.KeyVal("name2", "option2"));

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData, actualFormData);
    }
}