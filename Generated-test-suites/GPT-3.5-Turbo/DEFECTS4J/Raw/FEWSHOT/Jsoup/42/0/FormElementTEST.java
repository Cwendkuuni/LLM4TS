package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());

        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertSame(inputElement, elements.first());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());

        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertSame(inputElement, elements.first());

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        formElement.addElement(selectElement);
        elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertEquals(2, elements.size());
        assertSame(selectElement, elements.last());
    }

    @Test
    public void testSubmit() {
        FormElement formElementWithAction = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElementWithAction.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.url());

        FormElement formElementWithoutAction = new FormElement(Tag.valueOf("form"), "", new Attributes());
        try {
            connection = formElementWithoutAction.submit();
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());

        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "user123");
        formElement.addElement(inputElement);

        formData = formElement.formData();
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("user123", keyVal.value());

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        selectElement.attr("name", "color");
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement1.attr("value", "red");
        optionElement1.attr("selected", "");
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement2.attr("value", "blue");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);

        formElement.addElement(selectElement);

        formData = formElement.formData();
        assertFalse(formData.isEmpty());
        assertEquals(2, formData.size());
        Connection.KeyVal keyVal1 = formData.get(1);
        assertEquals("color", keyVal1.key());
        assertEquals("red", keyVal1.value());
        Connection.KeyVal keyVal2 = formData.get(0);
        assertEquals("color", keyVal2.key());
        assertEquals("blue", keyVal2.value());
    }
    
}