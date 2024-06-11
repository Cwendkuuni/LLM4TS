
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("name", "inputName"));
        formElement.addElement(element);

        assertFalse(formElement.elements().isEmpty());
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().get(0));
    }

    @Test
    public void testSubmitWithActionAttribute() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.url().toString());
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testSubmitWithoutActionAttribute() {
        formElement.attr("method", "get");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com", connection.url().toString());
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formElementWithoutBaseUri = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Validate.notEmpty(formElementWithoutBaseUri.absUrl("action"), "Could not determine a form action URL for submit. Ensure you set a base URI when parsing.");
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes()
                .put("name", "inputName")
                .put("value", "inputValue"));
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes()
                .put("name", "selectName"));
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes()
                .put("selected", ""));
        selectElement.addElement(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(2, formData.size());
        assertEquals("inputName", formData.get(0).key());
        assertEquals("inputValue", formData.get(0).value());
        assertEquals("selectName", formData.get(1).key());
        assertEquals("", formData.get(1).value());
    }
}
