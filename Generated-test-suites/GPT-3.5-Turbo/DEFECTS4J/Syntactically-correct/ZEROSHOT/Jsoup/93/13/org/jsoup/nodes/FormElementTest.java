package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com");
        
        FormElement updatedFormElement = formElement.addElement(inputElement);
        Elements elements = updatedFormElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertFalse(elements.isEmpty());
        Assert.assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testRemoveChild() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com");
        formElement.addElement(inputElement);
        
        formElement.removeChild(inputElement);
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.isEmpty());
        Assert.assertFalse(elements.contains(inputElement));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertTrue(formData.isEmpty());
    }
}