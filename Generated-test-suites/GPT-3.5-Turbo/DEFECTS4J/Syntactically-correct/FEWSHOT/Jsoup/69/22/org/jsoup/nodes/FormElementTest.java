
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        assertTrue(formElement.elements().isEmpty());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().get(0));
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_NoActionUrl() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "admin");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        selectElement.attr("name", "gender");
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("value", "male");
        optionElement.attr("selected", "");
        selectElement.addChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("admin", formData.get(0).value());
        assertEquals("gender", formData.get(1).key());
        assertEquals("male", formData.get(1).value());
    }
}
