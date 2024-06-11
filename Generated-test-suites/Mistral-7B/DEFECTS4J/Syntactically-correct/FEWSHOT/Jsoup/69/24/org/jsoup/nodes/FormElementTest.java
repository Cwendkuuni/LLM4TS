package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        Tag formTag = new Tag("form");
        FormElement form = new FormElement(formTag, "", new Attributes());
        assertNotNull(form);
        assertEquals("form", form.tagName());
    }

    @Test
    public void testGetElements() {
        Tag formTag = new Tag("form");
        FormElement form = new FormElement(formTag, "", new Attributes());

        Element input1 = new Element("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new Element("input", new Attributes().put("type", "text").put("name", "input2"));

        form.addElement(input1);
        form.addElement(input2);

        Elements expectedElements = new Elements().add(input1).add(input2);
        assertEquals(expectedElements, form.elements());
    }

    @Test
    public void testSubmitEmptyForm() {
        Tag formTag = new Tag("form");
        FormElement form = new FormElement(formTag, "", new Attributes());

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();

        Connection connection = form.submit();
        List<Connection.KeyVal> actualFormData = connection.data();

        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testSubmitFormWithOneInput() {
        Tag formTag = new Tag("form");
        FormElement form = new FormElement(formTag, "", new Attributes());

        Element input = new Element("input", new Attributes().put("type", "text").put("name", "input1").put("value", "value1"));
        form.addElement(input);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(new Connection.KeyVal("input1", "value1"));

        Connection connection = form.submit();
        List<Connection.KeyVal> actualFormData = connection.data();

        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testSubmitFormWithSelectElement() {
        Tag formTag = new Tag("form");
        FormElement form = new FormElement(formTag, "", new Attributes());

        Element select = new Element("select", new Attributes().put("name", "select1"));
        Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", "selected"));
        Element option2 = new Element("option", new Attributes().put("value", "option2"));

        select.add(option1);
        select.add(option2);
        form.addElement(select);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(new Connection.KeyVal("select1", "option1"));

        Connection connection = form.submit();
        List<Connection.KeyVal> actualFormData = connection.data();

        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testSubmitFormWithMultipleSelectElements() {
        Tag formTag = new Tag("form");
        FormElement form = new FormElement(formTag, "", new Attributes());

        Element select1 = new Element("select", new Attributes().put("name", "select1"));
        Element option1_1 = new Element("option", new Attributes().put("value", "option1_1").put("selected", "selected"));
        Element option1_2 = new Element("option", new Attributes().put("value", "option1_2"));
        select1.add(option1_1);
        select1.add(option1_2);

        Element select2 = new Element("select", new Attributes().put("name", "select2"));
        Element option2_1 = new Element("option", new Attributes().put("value", "option2_1").put("selected", "selected"));
        Element option2_2 = new Element("option", new Attributes().put("value", "option2_2"));
        select2.add(option2_1);
        select2.add(option2_2);

        form.addElement(select1);
        form.addElement(select2);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("select1", "option1_1"),
                new Connection.KeyVal("select2", "option2_1")
        );

        Connection connection = form.submit();
        List<Connection.KeyVal> actualFormData = connection.data();

        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testSubmitFormWithCheckboxOrRadioButton() {
        Tag formTag = new Tag("form");
        FormElement form = new FormElement(formTag, "", new Attributes());

        Element input = new Element("input", new Attributes().put("type", "radio").put("name", "radio1").put("value", "value1"));
        form.addElement(input);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(new Connection.KeyVal("radio1", "value1"));

        input.setAttribute("checked", "checked");

        Connection connection = form.submit();
        List<Connection.KeyVal> actualFormData = connection.data();

        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testSubmitFormWithMultipleCheckboxOrRadioButtons() {
        Tag formTag = new Tag("form");
        FormElement form = new FormElement(formTag, "", new Attributes());

        Element input1 = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkbox1").put("value", "value1"));
        Element input2 = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkbox1").put("value", "value2"));
        form.addElement(input1);
        form.addElement(input2);

        List<Connection.KeyVal> expectedFormData = Arrays.asList(new Connection.KeyVal("checkbox1", "value1"), new Connection.KeyVal("checkbox1", "value2"));

        input1.setAttribute("checked", "checked");

        Connection connection = form.submit();
        List<Connection.KeyVal> actualFormData = connection.data();

        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testSubmitFormWithDisabledInput() {
        Tag formTag = new Tag("form");
        FormElement form = new FormElement(formTag, "", new Attributes());

        Element input = new Element("input", new Attributes().put("type", "text").put("name", "input1").put("disabled", "disabled"));
        form.addElement(input);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();

        Connection connection = form.submit();
        List<Connection.KeyVal> actualFormData = connection.data();

        assertEquals(expectedFormData, actualFormData);
    }
}