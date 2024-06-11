package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setUp() {
        form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element);
        assertEquals(1, form.elements().size());
        assertEquals(element, form.elements().get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = form.submit();
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(0, connection.request().data().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        form.submit();
    }

    @Test
    public void testSubmitWithAction() {
        form.attr("action", "http://example.com/submit");
        Connection connection = form.submit();
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(0, connection.request().data().size());
    }

    @Test
    public void testSubmitWithMethod() {
        form.attr("method", "POST");
        Connection connection = form.submit();
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(0, connection.request().data().size());
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "inputField");
        inputElement.val("test");
        form.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "selectField");
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("value", "option1");
        optionElement.attr("selected", "selected");
        selectElement.appendChild(optionElement);
        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal inputField = formData.get(0);
        assertEquals("inputField", inputField.key());
        assertEquals("test", inputField.value());

        Connection.KeyVal selectField = formData.get(1);
        assertEquals("selectField", selectField.key());
        assertEquals("option1", selectField.value());
    }

    @Test
    public void testFormDataEmpty() {
        List<Connection.KeyVal> formData = form.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testEquals() {
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(form.equals(form2));

        form2.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        assertFalse(form.equals(form2));

        form.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        assertTrue(form.equals(form2));
    }
}