
package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, "https://example.com", attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement result = formElement.addElement(element);
        assertSame(formElement, result);
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionUrl() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    @Test
    public void testFormDataWithDisabledFormInput() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement.attr("disabled", "");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    @Test
    public void testFormDataWithEmptyName() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(inputElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    @Test
    public void testFormDataWithSelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        optionElement.attr("selected", "");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    @Test
    public void testFormDataWithSelectElementNoSelectedOption() {
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    @Test
    public void testFormDataWithCheckboxChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    @Test
    public void testFormDataWithCheckboxUnchecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkboxElement.attr("type", "checkbox");
        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    @Test
    public void testFormDataWithRadioChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "");
        formElement.addElement(radioElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    @Test
    public void testFormDataWithRadioUnchecked() {
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radioElement.attr("type", "radio");
        formElement.addElement(radioElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }
}
