package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    }

    @After
    public void tearDown() {
        formElement = null;
    }

    @Test
    public void testElements() {
        // Test with no form control added
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());

        // Test with one form control added
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        formElement.addElement(inputElement);

        elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(inputElement, elements.get(0));
    }

    @Test
    public void testAddElement() {
        // Test adding a form control element
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(inputElement);

        Elements elements = updatedFormElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(inputElement, elements.get(0));

        // Test adding multiple form control elements
        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        FormElement updatedFormElement2 = updatedFormElement.addElement(selectElement);

        Elements updatedElements = updatedFormElement2.elements();
        assertNotNull(updatedElements);
        assertEquals(2, updatedElements.size());
        assertEquals(inputElement, updatedElements.get(0));
        assertEquals(selectElement, updatedElements.get(1));
    }

    @Test
    public void testRemoveChild() {
        // Test removing a child element
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        formElement.addElement(inputElement);

        formElement.removeChild(inputElement);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());

        // Test removing a child element - multiple elements
        Element inputElement1 = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        Element inputElement2 = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);

        formElement.removeChild(inputElement1);

        elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(inputElement2, elements.get(0));
    }

    @Test
    public void testSubmit() {
        // Test submitting a form with action and method attributes set
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "testuser");
        formElement.addElement(inputElement);
        formElement.attr("action", "http://www.example.com/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://www.example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testuser", formData.get(0).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        // Test submitting a form without action attribute set
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "testuser");
        formElement.addElement(inputElement);
        formElement.attr("method", "post");

        formElement.submit();
    }

    @Test
    public void testFormData() {
        // Test form data with different types of form control elements

        // Test with select element - option selected
        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        selectElement.attr("name", "country");

        Element optionElement1 = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        optionElement1.attr("value", "US");
        optionElement1.attr("selected", "selected");

        selectElement.addChild(optionElement1);

        Element optionElement2 = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        optionElement2.attr("value", "UK");

        selectElement.addChild(optionElement2);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("country", formData.get(0).key());
        assertEquals("US", formData.get(0).value());

        // Test with select element - no option selected
        formElement.removeChild(selectElement);
        selectElement.removeChild(optionElement1);

        formElement.addElement(selectElement);

        formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("country", formData.get(0).key());
        assertEquals("UK", formData.get(0).value());

        // Test with checkbox element - checked
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "newsletter");
        checkboxElement.attr("value", "on");
        checkboxElement.attr("checked", "checked");

        formElement.addElement(checkboxElement);

        formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("newsletter", formData.get(0).key());
        assertEquals("on", formData.get(0).value());

        // Test with checkbox element - unchecked
        formElement.removeChild(checkboxElement);

        checkboxElement.removeAttr("checked");

        formElement.addElement(checkboxElement);

        formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());

        // Test with radio element - checked
        Element radioElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "gender");
        radioElement.attr("value", "male");
        radioElement.attr("checked", "checked");

        formElement.addElement(radioElement);

        formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("gender", formData.get(0).key());
        assertEquals("male", formData.get(0).value());

        // Test with radio element - unchecked
        formElement.removeChild(radioElement);

        radioElement.removeAttr("checked");

        formElement.addElement(radioElement);

        formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());

        // Test with other form control elements - text, password, textarea
        Element textElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        textElement.attr("type", "text");
        textElement.attr("name", "username");
        textElement.attr("value", "testuser");

        Element passwordElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        passwordElement.attr("type", "password");
        passwordElement.attr("name", "password");
        passwordElement.attr("value", "TestPW");

        Element textareaElement = new Element(Tag.valueOf("textarea"), "http://www.example.com", new Attributes());
        textareaElement.attr("name", "description");
        textareaElement.html("This is a test description");

        formElement.addElement(textElement);
        formElement.addElement(passwordElement);
        formElement.addElement(textareaElement);

        formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(3, formData.size());

        assertEquals("username", formData.get(0).key());
        assertEquals("testuser", formData.get(0).value());

        assertEquals("password", formData.get(1).key());
        assertEquals("TestPW", formData.get(1).value());

        assertEquals("description", formData.get(2).key());
        assertEquals("This is a test description", formData.get(2).value());
    }
}