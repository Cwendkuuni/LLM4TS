package org.jsoup.nodes;

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
        Element element = new Element(Tag.valueOf("input"), "http://example.com");
        formElement.addElement(element);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(formElement.submit());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(formElement.formData());
        assertEquals(0, formElement.formData().size());

        Element inputElement1 = new Element(Tag.valueOf("input"), "http://example.com");
        inputElement1.attr("name", "input1");
        inputElement1.attr("value", "value1");
        formElement.addElement(inputElement1);

        Element inputElement2 = new Element(Tag.valueOf("input"), "http://example.com");
        inputElement2.attr("name", "input2");
        inputElement2.attr("value", "value2");
        formElement.addElement(inputElement2);

        assertEquals(2, formElement.formData().size());
        assertEquals("input1", formElement.formData().get(0).getKey());
        assertEquals("value1", formElement.formData().get(0).getValue());
        assertEquals("input2", formElement.formData().get(1).getKey());
        assertEquals("value2", formElement.formData().get(1).getValue());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement3 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        assertEquals(formElement1, formElement1); // same instance
        assertEquals(formElement1, formElement2); // same properties
        assertNotEquals(formElement1, formElement3); // different instance
        
        formElement3.addElement(new Element(Tag.valueOf("input"), "http://example.com"));
        
        assertNotEquals(formElement1, formElement3); // different elements
    }
}