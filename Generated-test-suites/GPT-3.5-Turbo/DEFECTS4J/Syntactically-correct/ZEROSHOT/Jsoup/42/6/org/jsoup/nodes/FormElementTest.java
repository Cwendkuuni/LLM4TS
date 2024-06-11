package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.form(), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.form(), "http://example.com", new Attributes());
        Element element = new Element(Tag.input(), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.form(), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertTrue(connection instanceof Connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        FormElement formElement = new FormElement(Tag.form(), "http://example.com", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.form(), "http://example.com", new Attributes());
        ArrayList<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.form(), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.form(), "http://example.com", new Attributes());
        assertEquals(formElement1, formElement2);
    }

    @Test
    public void testNotEquals() {
        FormElement formElement1 = new FormElement(Tag.form(), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.form(), "http://example.com", new Attributes().put("id", "form1"));
        assertNotEquals(formElement1, formElement2);
    }
}