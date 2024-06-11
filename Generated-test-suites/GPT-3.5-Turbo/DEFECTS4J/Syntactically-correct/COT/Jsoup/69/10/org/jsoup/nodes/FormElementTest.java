
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Test that initially the elements list is empty
        assertTrue(form.elements().isEmpty());

        // Test adding an element to the form
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element);
        assertEquals(1, form.elements().size());
        assertSame(element, form.elements().get(0));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Test that submit throws an exception if the action URL cannot be determined
        try {
            form.submit();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test submit with action URL
        form.attr("action", "http://example.com/submit");
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Test that initially the formData list is empty
        assertTrue(form.formData().isEmpty());

        // Test adding form control elements and checking the formData
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input.attr("name", "username");
        input.attr("value", "testuser");
        form.addElement(input);

        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("selected", "");
        option1.attr("value", "option1");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.attr("value", "option2");
        select.appendChild(option1);
        select.appendChild(option2);
        form.addElement(select);

        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "accept");
        checkbox.attr("checked", "");
        checkbox.attr("value", "true");
        form.addElement(checkbox);

        assertEquals(3, form.formData().size());
        assertEquals("username=testuser", form.formData().get(0).toString());
        assertEquals("option1=", form.formData().get(1).toString());
        assertEquals("accept=true", form.formData().get(2).toString());
    }
}

