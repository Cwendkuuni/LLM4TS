package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.Connection;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        // Test1: Test empty form
        FormElement formElement = new FormElement(Tag.form, "", new Attributes());
        assertEquals(0, formElement.elements().size());

        // Test2: Test form with elements
        Element element1 = new Element(Tag.input, "", new Attributes().put("name", "input1"));
        Element element2 = new Element(Tag.input, "", new Attributes().put("name", "input2"));
        formElement.addElement(element1).addElement(element2);
        assertEquals(2, formElement.elements().size());
        assertEquals(element1, formElement.elements().get(0));
        assertEquals(element2, formElement.elements().get(1));
    }

    @Test
    public void testAddElement() {
        // Test1: Add elements to form
        FormElement formElement = new FormElement(Tag.form, "", new Attributes());
        Element element1 = new Element(Tag.input, "", new Attributes().put("name", "input1"));
        Element element2 = new Element(Tag.input, "", new Attributes().put("name", "input2"));
        formElement.addElement(element1).addElement(element2);
        assertEquals(2, formElement.elements().size());
        assertEquals(element1, formElement.elements().get(0));
        assertEquals(element2, formElement.elements().get(1));

        // Test2: Add null element to form
        FormElement formElement2 = new FormElement(Tag.form, "", new Attributes());
        formElement2.addElement(null);
        assertEquals(0, formElement2.elements().size());
    }

    @Test
    public void testSubmit() {
        // Test1: Test form without action attribute
        FormElement formElement1 = new FormElement(Tag.form, "baseUri", new Attributes());
        Connection connection1 = formElement1.submit();
        assertEquals("baseUri", connection1.url());

        // Test2: Test form with action attribute
        FormElement formElement2 = new FormElement(Tag.form, "", new Attributes().put("action", "/path"));
        Connection connection2 = formElement2.submit();
        assertEquals("/path", connection2.url());
        assertEquals(Connection.Method.GET, connection2.getRequest().method());

        // Test3: Test form with method attribute set to POST
        FormElement formElement3 = new FormElement(Tag.form, "", new Attributes().put("action", "/path").put("method", "POST"));
        Connection connection3 = formElement3.submit();
        assertEquals("/path", connection3.url());
        assertEquals(Connection.Method.POST, connection3.getRequest().method());
    }

    @Test
    public void testFormData() {
        // Test1: Test form with no form control elements
        FormElement formElement1 = new FormElement(Tag.form, "", new Attributes());
        assertEquals(0, formElement1.formData().size());

        // Test2: Test form with null form control element
        FormElement formElement2 = new FormElement(Tag.form, "", new Attributes());
        formElement2.addElement(null);
        assertEquals(0, formElement2.formData().size());

        // Test3: Test form with text input element
        FormElement formElement3 = new FormElement(Tag.form, "", new Attributes());
        formElement3.addElement(new Element(Tag.input, "", new Attributes().put("name", "input1").put("value", "value1")));
        assertEquals(1, formElement3.formData().size());
        assertEquals("input1=value1", formElement3.formData().get(0).toString());

        // Test4: Test form with checkbox element not checked
        FormElement formElement4 = new FormElement(Tag.form, "", new Attributes());
        formElement4.addElement(new Element(Tag.input, "", new Attributes().put("name", "checkbox1").put("type", "checkbox")));
        assertEquals(0, formElement4.formData().size());

        // Test5: Test form with checkbox element checked
        FormElement formElement5 = new FormElement(Tag.form, "", new Attributes());
        Element checkbox = new Element(Tag.input, "", new Attributes().put("name", "checkbox1").put("type", "checkbox").put("checked", ""));
        formElement5.addElement(checkbox);
        assertEquals(1, formElement5.formData().size());
        assertEquals("checkbox1=null", formElement5.formData().get(0).toString());

        // Test6: Test form with select element with selected option
        FormElement formElement6 = new FormElement(Tag.form, "", new Attributes());
        Element select = new Element(Tag.select, "", new Attributes().put("name", "select1"));
        select.append("<option selected value=\"value1\">Option 1</option>");
        formElement6.addElement(select);
        assertEquals(1, formElement6.formData().size());
        assertEquals("select1=value1", formElement6.formData().get(0).toString());

        // Test7: Test form with select element without selected option
        FormElement formElement7 = new FormElement(Tag.form, "", new Attributes());
        Element select2 = new Element(Tag.select, "", new Attributes().put("name", "select1"));
        select2.append("<option value=\"value1\">Option 1</option>");
        select2.append("<option value=\"value2\">Option 2</option>");
        formElement7.addElement(select2);
        assertEquals(1, formElement7.formData().size());
        assertEquals("select1=value1", formElement7.formData().get(0).toString());

        // Test8: Test form with select element with no options
        FormElement formElement8 = new FormElement(Tag.form, "", new Attributes());
        formElement8.addElement(new Element(Tag.select, "", new Attributes().put("name", "select1")));
        assertEquals(0, formElement8.formData().size());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.form, "", new Attributes());
        FormElement formElement2 = new FormElement(Tag.form, "", new Attributes());
        assertTrue(formElement1.equals(formElement2));
        assertTrue(formElement2.equals(formElement1));
    }
}