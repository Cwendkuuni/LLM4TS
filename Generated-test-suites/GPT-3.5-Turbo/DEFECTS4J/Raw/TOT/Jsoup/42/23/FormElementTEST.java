
package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;
    private Element element1, element2, element3;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com");
        formElement = new FormElement(tag, "", attributes);

        Tag tag1 = Tag.valueOf("label");
        element1 = new Element(tag1, "");
        element1.attr("name", "username");
        element1.attr("type", "text");

        Tag tag2 = Tag.valueOf("input");
        element2 = new Element(tag2, "");
        element2.attr("name", "password");
        element2.attr("type", "password");

        Tag tag3 = Tag.valueOf("input");
        element3 = new Element(tag3, "");
        element3.attr("name", "rememberme");
        element3.attr("type", "checkbox");
        element3.attr("checked", "checked");
    }

    @Test
    public void testAddElement() {
        FormElement addedElement = formElement.addElement(element1);

        assertEquals(1, addedElement.elements().size());
        assertTrue(addedElement.elements().contains(element1));
    }

    @Test
    public void testSubmit_withActionUrl() {
        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_withoutActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());

        formElement.submit();
    }

    @Test
    public void testFormData_withSelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), "");
        selectElement.attr("name", "gender");
        Element optionElement1 = new Element(Tag.valueOf("option"), "")
                .attr("selected", "selected")
                .val("male");
        Element optionElement2 = new Element(Tag.valueOf("option"), "")
                .val("female");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertEquals(1, data.size());
        assertEquals("gender", data.get(0).key());
        assertEquals("male", data.get(0).value());
    }

    @Test
    public void testFormData_withSelectElement_noOptionSelected() {
        Element selectElement = new Element(Tag.valueOf("select"), "");
        selectElement.attr("name", "gender");
        Element optionElement = new Element(Tag.valueOf("option"), "").val("female");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertEquals(1, data.size());
        assertEquals("gender", data.get(0).key());
        assertEquals("female", data.get(0).value());
    }

    @Test
    public void testFormData_withSelectElement_noOptionElement() {
        Element selectElement = new Element(Tag.valueOf("select"), "");
        selectElement.attr("name", "gender");
        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertEquals(0, data.size());
    }

    @Test
    public void testFormData_withCheckboxElement_checked() {
        formElement.addElement(element3);

        List<Connection.KeyVal> data = formElement.formData();

        assertEquals(1, data.size());
        assertEquals("rememberme", data.get(0).key());
        assertEquals("on", data.get(0).value());
    }

    @Test
    public void testFormData_withCheckboxElement_unchecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "")
                .attr("name", "subscribe")
                .attr("type", "checkbox");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertEquals(0, data.size());
    }

    @Test
    public void testFormData_withInputElement() {
        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Connection.KeyVal> data = formElement.formData();

        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("", data.get(0).value());
        assertEquals("password", data.get(1).key());
        assertEquals("", data.get(1).value());
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(formElement.equals(formElement));
    }

    @Test
    public void testEquals_differentObjectType() {
        assertFalse(formElement.equals("formElement"));
    }

    @Test
    public void testEquals_equalFormElements() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com");
        FormElement anotherFormElement = new FormElement(tag, "", attributes);

        assertTrue(formElement.equals(anotherFormElement));
    }

    @Test
    public void testEquals_differentTag() {
        Tag tag = Tag.valueOf("div");
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com");
        FormElement anotherFormElement = new FormElement(tag, "", attributes);

        assertFalse(formElement.equals(anotherFormElement));
    }

    @Test
    public void testEquals_differentActionUrl() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example2.com");
        FormElement anotherFormElement = new FormElement(tag, "", attributes);

        assertFalse(formElement.equals(anotherFormElement));
    }

    @Test
    public void testEquals_differentBaseUri() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com");
        FormElement anotherFormElement = new FormElement(tag, "http://example2.com", attributes);

        assertFalse(formElement.equals(anotherFormElement));
    }

    @Test
    public void testEquals_differentAttributes() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("id", "form1");
        attributes.put("action", "http://example.com");
        FormElement anotherFormElement = new FormElement(tag, "", attributes);

        assertFalse(formElement.equals(anotherFormElement));
    }

    @Test
    public void testEquals_null() {
        assertFalse(formElement.equals(null));
    }

}

