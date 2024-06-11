package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.SelectElement;
import org.jsoup.nodes.html.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    private static final String BASE_URI = "http://example.com";

    private FormElement form;
    private Connection connection;

    @Before
    public void setup() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/submit");
        form = new FormElement(formTag, BASE_URI, formAttributes);

        connection = mock(Connection.class);
    }

    @Test
    public void testGetElements() {
        Element input1 = new Element("input", new Attributes().put("type", "text").put("name", "name1").put("value", "value1"));
        Element input2 = new Element("input", new Attributes().put("type", "text").put("name", "name2").put("value", "value2"));
        Element select = new Element("select", new Attributes().put("name", "selectName"));
        Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", "selected"));
        Element option2 = new Element("option", new Attributes().put("value", "option2"));
        select.appendText("Option 1");
        select.appendText("Option 2");
        select.appendElement(option1);
        select.appendElement(option2);

        form.addElement(input1);
        form.addElement(input2);
        form.addElement(select);

        assertEquals(3, form.elements().size());
        assertEquals(input1, form.elements().get(0));
        assertEquals(input2, form.elements().get(1));
        assertEquals(select, form.elements().get(2));

        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testSubmit() {
        String expectedFormData = "name1=value1&name2=value2&selectName=option1";

        form.addElement(new Element("input", new Attributes().put("type", "text").put("name", "name1").put("value", "value1")));
        form.addElement(new Element("input", new Attributes().put("type", "text").put("name", "name2").put("value", "value2")));
        form.addElement(new Element("select", new Attributes().put("name", "selectName")))
                .appendText("Option 1")
                .appendText("Option 2")
                .appendElement(new Element("option", new Attributes().put("value", "option1").put("selected", "selected")))
                .appendElement(new Element("option", new Attributes().put("value", "option2")));

        when(connection.data(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Connection.KeyVal> actualFormData = form.submit().data();

        assertEquals(expectedFormData, actualFormData.toString());
        assertEquals(expectedFormData, form.formData().toString());
        verify(connection, times(1)).data(argThat(containsInAnyOrder(
                new Connection.KeyVal("name1", "value1"),
                new Connection.KeyVal("name2", "value2"),
                new Connection.KeyVal("selectName", "option1")
        )));
        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testSubmitEmptyForm() {
        FormElement emptyForm = new FormElement(new Tag("form"), BASE_URI, new Attributes());

        when(connection.data(anyList())).thenReturn(Arrays.asList());

        emptyForm.submit();

        verify(connection, times(1)).data(eq(Arrays.asList()));
        verifyNoMoreInteractions(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitFormWithoutActionAttribute() {
        FormElement formWithoutAction = new FormElement(new Tag("form"), BASE_URI, new Attributes());

        formWithoutAction.submit();
    }

    @Test
    public void testFormDataForSelectElementWithOneOptionSelected() {
        Element select = new Element("select", new Attributes().put("name", "selectName"));
        Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", "selected"));
        select.appendText("Option 1");
        select.appendElement(option1);

        FormElement form = new FormElement(new Tag("form"), BASE_URI, new Attributes());
        form.addElement(select);

        List<Connection.KeyVal> actualFormData = form.formData();

        List<Connection.KeyVal> expectedFormData = Arrays.asList(new Connection.KeyVal("selectName", "option1"));
        assertEquals(expectedFormData, actualFormData);

        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testFormDataForSelectElementWithMultipleOptionsSelected() {
        Element select = new Element("select", new Attributes().put("name", "selectName"));
        Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", "selected"));
        Element option2 = new Element("option", new Attributes().put("value", "option2").put("selected", "selected"));
        select.appendText("Option 1");
        select.appendText("Option 2");
        select.appendElement(option1);
        select.appendElement(option2);

        FormElement form = new FormElement(new Tag("form"), BASE_URI, new Attributes());
        form.addElement(select);

        List<Connection.KeyVal> actualFormData = form.formData();

        List<Connection.KeyVal> expectedFormData = Arrays.asList(new Connection.KeyVal("selectName", "option1"), new Connection.KeyVal("selectName", "option2"));
        assertEquals(expectedFormData, actualFormData);

        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testFormDataForCheckboxOrRadioInputWithCheckedAttribute() {
        Element checkbox = new Element("input", new Attributes().put("type", "checkbox").put("name", "name").put("value", "value"));
        checkbox.hasAttr("checked");

        FormElement form = new FormElement(new Tag("form"), BASE_URI, new Attributes());
        form.addElement(checkbox);

        List<Connection.KeyVal> actualFormData = form.formData();

        List<Connection.KeyVal> expectedFormData = Arrays.asList(new Connection.KeyVal("name", "value"));
        assertEquals(expectedFormData, actualFormData);

        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testFormDataForTextInput() {
        Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "name").put("value", "value"));

        FormElement form = new FormElement(new Tag("form"), BASE_URI, new Attributes());
        form.addElement(textInput);

        List<Connection.KeyVal> actualFormData = form.formData();

        List<Connection.KeyVal> expectedFormData = Arrays.asList(new Connection.KeyVal("name", "value"));
        assertEquals(expectedFormData, actualFormData);

        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testFormDataForTextInputWithEmptyValue() {
        Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "name"));

        FormElement form = new FormElement(new Tag("form"), BASE_URI, new Attributes());
        form.addElement(textInput);

        List<Connection.KeyVal> actualFormData = form.formData();

        List<Connection.KeyVal> expectedFormData = Arrays.asList();
        assertEquals(expectedFormData, actualFormData);

        verifyNoMoreInteractions(connection);
    }
}