package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;
    private Elements elements;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, "http://example.com", attributes);
        elements = new Elements();
    }

    @Test
    public void testElements() {
        assertSame(elements, formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = formElement.addElement(element);
        assertSame(formElement, result);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        // TODO: Implement test for submit()
    }

    @Test
    public void testFormData() {
        // TODO: Implement test for formData()
    }

    @Test
    public void testEquals() {
        Object obj = new Object();
        assertFalse(formElement.equals(obj));
    }
}