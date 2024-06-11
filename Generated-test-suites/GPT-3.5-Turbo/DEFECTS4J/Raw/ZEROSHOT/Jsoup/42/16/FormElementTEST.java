package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        // Arrange
        FormElement formElement = new FormElement(null, null, null);

        // Act
        Elements elements = formElement.elements();

        // Assert
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        // Arrange
        FormElement formElement = new FormElement(null, null, null);
        Element element = new Element(null, null, null);

        // Act
        FormElement result = formElement.addElement(element);

        // Assert
        assertSame(formElement, result);
        assertEquals(1, formElement.elements().size());
        assertSame(element, formElement.elements().get(0));
    }

    @Test
    public void testSubmit() {
        // Arrange
        FormElement formElement = new FormElement(null, null, null);

        // Act
        Connection connection = formElement.submit();

        // Assert
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        // Arrange
        FormElement formElement = new FormElement(null, null, null);

        // Act
        List<Connection.KeyVal> data = formElement.formData();

        // Assert
        assertNotNull(data);
    }

    @Test
    public void testEquals() {
        // Arrange
        FormElement formElement1 = new FormElement(null, null, null);
        FormElement formElement2 = new FormElement(null, null, null);

        // Act
        boolean result = formElement1.equals(formElement2);

        // Assert
        assertFalse(result);
    }
}