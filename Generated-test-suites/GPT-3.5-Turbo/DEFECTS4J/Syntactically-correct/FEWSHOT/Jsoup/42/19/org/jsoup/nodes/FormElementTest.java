
package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = new Elements();
        assertEquals(elements, formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(formElement, result);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(0, data.size());
    }

    @Test
    public void testEquals() {
        FormElement otherFormElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(formElement.equals(otherFormElement));
    }
}

