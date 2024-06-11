package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(new Tag("input"), "http://example.com", new Attributes());
        FormElement result = formElement.addElement(element);
        Assert.assertEquals(1, result.elements().size());
        Assert.assertEquals(element, result.elements().get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertNotNull(data);
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
        Assert.assertTrue(formElement.equals(formElement2));
    }
}