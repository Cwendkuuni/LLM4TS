// Your JUnit test file, FormElementTest.java, starts here.
package org.junit.platform.commons.annotation.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

    @Test
    public void testFormElementNullInputConstructor() {
        // Test with null tag, baseUri, and attributes
        FormElement formElement = new FormElement(null, null, null);
    }

    @Test
    public void testNewFormElement() {
        // Test creating a new FormElement with valid input
        FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
        assertNotNull(formElement);
    }

    @Test
    public void testEmptyElementsList() {
        FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
        assertEquals(0, formElement.elements().size());
    }

    @Test(expected = NullPointerException.class)
    public void testFormElementNullElementAdded() {
        FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
        formElement.addElement(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFormElementEmptySubmit() {
        FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormElementEmptyFormData() {
        FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(0, data.size());
    }

    @Test
    public void testFormElementValidInput() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "baseUri", new Attributes());
        Element element = new Element("input");
        formElement.addElement(element);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        assertEquals("input", data.get(0).key);
    }

    @Test
    public void testFormElementValidSubmit() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "baseUri", new Attributes());
        formElement.addElement(new Element("input"));
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormElementElements() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "baseUri", new Attributes());
        Element element1 = new Element("input");
        Element element2 = new Element("select");
        formElement.addElement(element1);
        formElement.addElement(element2);
        List<Element> elements = formElement.elements();
        assertEquals(2, elements.size());
        assertEquals(element1, elements.get(0));
        assertEquals(element2, elements.get(1));
    }

    @Test
    public void testFormElementFormDataWithSelect() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "baseUri", new Attributes());
        Element inputElement = new Element("input");
        inputElement.attr("name", "inputName");
        formElement.addElement(inputElement);

        Element selectElement = new Element("select");
        selectElement.attr("name", "selectName");
        Element option1 = new Element("option").attr("value", "option1");
        Element option2 = new Element("option").attr("value", "option2");
        selectElement.add(option1);
        selectElement.add(option2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(3, data.size());
        assertEquals("inputName", data.get(0).key);
        assertEquals("input", data.get(0).val());
        assertEquals("selectName", data.get(1).key);
        assertEquals("option1", data.get(2).val());
    }
}

// Your JUnit test file, FormElementTest.java, ends here.