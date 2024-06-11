

import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        // Test case for elements() method
        FormElement formElement = new FormElement(null, null, null);
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        // Test case for addElement() method
        FormElement formElement = new FormElement(null, null, null);
        Element element = new Element(null, null, null);
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        // Test case for submit() method with form action URL present
        FormElement formElement = new FormElement(null, "http://example.com", null);
        assertNotNull(formElement.submit());

        // Test case for submit() method with form action URL empty
        formElement = new FormElement(null, null, null);
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        // Test case for formData() method with select element
        FormElement formElement = new FormElement(null, null, null);
        Element selectElement = new Element(Tag.valueOf("select"), null, null);
        Element optionElement = new Element(Tag.valueOf("option"), null, null);
        optionElement.attr("selected", "");
        optionElement.val("option1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        assertTrue(formElement.formData().contains(HttpConnection.KeyVal.create("name", "option1")));

        // Test case for formData() method with select element with no selected option
        formElement = new FormElement(null, null, null);
        selectElement = new Element(Tag.valueOf("select"), null, null);
        optionElement = new Element(Tag.valueOf("option"), null, null);
        optionElement.val("option1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        assertTrue(formElement.formData().contains(HttpConnection.KeyVal.create("name", "option1")));

        // Test case for formData() method with checkbox element with checked attribute
        formElement = new FormElement(null, null, null);
        Element checkboxElement = new Element(Tag.valueOf("input"), null, null);
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");
        checkboxElement.val("value1");
        formElement.addElement(checkboxElement);
        assertTrue(formElement.formData().contains(HttpConnection.KeyVal.create("name", "value1")));

        // Test case for formData() method with checkbox element without checked attribute
        formElement = new FormElement(null, null, null);
        checkboxElement = new Element(Tag.valueOf("input"), null, null);
        checkboxElement.attr("type", "checkbox");
        checkboxElement.val("value1");
        formElement.addElement(checkboxElement);
        assertFalse(formElement.formData().contains(HttpConnection.KeyVal.create("name", "value1")));

        // Test case for formData() method with other form control elements (text input)
        formElement = new FormElement(null, null, null);
        Element inputElement = new Element(Tag.valueOf("input"), null, null);
        inputElement.attr("type", "text");
        inputElement.val("value1");
        formElement.addElement(inputElement);
        assertTrue(formElement.formData().contains(HttpConnection.KeyVal.create("name", "value1")));
    }
}

