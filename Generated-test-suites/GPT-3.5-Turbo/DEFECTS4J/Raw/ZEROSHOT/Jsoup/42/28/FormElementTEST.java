package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {
    
    private FormElement formElement;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        assertEquals(1, updatedFormElement.elements().size());
        assertEquals(element, updatedFormElement.elements().get(0));
    }
    
    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testSubmitWithAction() {
        formElement.attr("action", "/submit");
        Connection connection = formElement.submit();
        assertEquals("https://example.com/submit", connection.request().url().toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formElementWithoutBaseUri = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElementWithoutBaseUri.submit();
    }
    
    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }
    
    @Test
    public void testEquals() {
        FormElement anotherFormElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        assertTrue(formElement.equals(anotherFormElement));
    }
}