
package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;
import org.junit.Test;


public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());

        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWhenActionMissing() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmitWithGetMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "get");

        Connection connection = formElement.submit();

        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitWithPostMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormDataWithEmptyElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        assertEquals(0, formElement.formData().size());
    }

    @Test
    public void testFormDataWithSelectElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "selectElement");

        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("value", "option1");
        optionElement.attr("selected", "selected");

        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        assertEquals(1, formElement.formData().size());
        assertEquals("selectElement", formElement.formData().get(0).key());
        assertEquals("option1", formElement.formData().get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("name", "checkboxElement");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        checkboxElement.attr("value", "checkboxValue");

        formElement.addElement(checkboxElement);

        assertEquals(1, formElement.formData().size());
        assertEquals("checkboxElement", formElement.formData().get(0).key());
        assertEquals("checkboxValue", formElement.formData().get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxNotChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("name", "checkboxElement");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("value", "checkboxValue");

        formElement.addElement(checkboxElement);

        assertEquals(0, formElement.formData().size());
    }

    @Test
    public void testFormDataWithRadioChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("name", "radioElement");
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "checked");
        radioElement.attr("value", "radioValue");

        formElement.addElement(radioElement);

        assertEquals(1, formElement.formData().size());
        assertEquals("radioElement", formElement.formData().get(0).key());
        assertEquals("radioValue", formElement.formData().get(0).value());
    }

    @Test
    public void testFormDataWithRadioNotChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("name", "radioElement");
        radioElement.attr("type", "radio");
        radioElement.attr("value", "radioValue");

        formElement.addElement(radioElement);

        assertEquals(0, formElement.formData().size());
    }

    @Test
    public void testFormDataWithOtherInputElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "inputElement");
        inputElement.attr("type", "text");
        inputElement.attr("value", "inputValue");

        formElement.addElement(inputElement);

        assertEquals(1, formElement.formData().size());
        assertEquals("inputElement", formElement.formData().get(0).key());
        assertEquals("inputValue", formElement.formData().get(0).value());
    }
}

