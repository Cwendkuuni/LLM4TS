
package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        assertNotNull(form.elements());
        assertTrue(form.elements().isEmpty());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());

        form.addElement(element1);
        assertFalse(form.elements().isEmpty());
        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element1));

        form.addElement(element2);
        assertEquals(2, form.elements().size());
        assertTrue(form.elements().contains(element2));
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());

        assertEquals(form, form.addElement(element1));
        assertFalse(form.elements().isEmpty());
        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element1));

        assertEquals(form, form.addElement(element2));
        assertEquals(2, form.elements().size());
        assertTrue(form.elements().contains(element2));
    }

    @Test
    public void testRemoveChild() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());

        form.addElement(element1);
        form.addElement(element2);
        assertFalse(form.elements().isEmpty());
        assertEquals(2, form.elements().size());

        form.removeChild(element1);
        assertEquals(1, form.elements().size());
        assertFalse(form.elements().contains(element1));
        assertTrue(form.elements().contains(element2));
    }

    @Test
    public void testSubmit() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        form1.attr("action", "https://example.com");
        form1.attr("method", "GET");

        Connection connection1 = form1.submit();
        assertEquals("https://example.com", connection1.request().url().toString());
        assertEquals(Connection.Method.GET, connection1.request().method());

        FormElement form2 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        form2.attr("action", "https://example.com");
        form2.attr("method", "POST");

        Connection connection2 = form2.submit();
        assertEquals("https://example.com", connection2.request().url().toString());
        assertEquals(Connection.Method.POST, connection2.request().method());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSubmitMissingAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());

        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element input1 = new Element(Tag.valueOf("input"), "", new Attributes());
        input1.attr("name", "name");
        input1.val("John");
        form.addElement(input1);

        Element input2 = new Element(Tag.valueOf("input"), "", new Attributes());
        input2.attr("name", "age");
        input2.val("25");
        form.addElement(input2);

        Element select = new Element(Tag.valueOf("select"), "", new Attributes());
        select.attr("name", "country");
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.val("USA");
        option1.attr("selected", "");
        select.addElement(option1);
        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.val("UK");
        select.addElement(option2);
        form.addElement(select);

        List<Connection.KeyVal> formData = form.formData();
        assertEquals(3, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("name", "John")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("age", "25")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("country", "USA")));
    }
}
