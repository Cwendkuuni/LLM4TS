package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        // Test case for elements() method
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));
        
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
    }

    @Test
    public void testAddElement() {
        // Test case for addElement() method
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        
        FormElement returnedForm = form.addElement(element);
        assertEquals(form, returnedForm);
        assertEquals(1, form.elements().size());
    }

    @Test
    public void testSubmit() {
        // Test case for submit() method
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url());
    }

    @Test
    public void testFormData() {
        // Test case for formData() method
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));
        
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
    }
}