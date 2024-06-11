import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com/submit");
        attributes.put("method", "POST");
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @After
    public void tearDown() {
        formElement = null;
    }
    
    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
        assertTrue(formElement.elements().isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        
        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().get(0));
    }
    
    @Test
    public void testSubmit() {
        // Typical use case
        Connection connection = formElement.submit();
        assertNotNull(connection);
        
        // Error scenario
        formElement.attributes().remove("action"); // Removing action attribute
        try {
            formElement.submit();
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. " +
                "Ensure you set a base URI when parsing.", e.getMessage());
        }
    }
    
    @Test
    public void testFormData() {
        // Typical use case
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
        
        // Edge case
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "input1");
        inputElement.attr("type", "text");
        inputElement.val("Value1");
        formElement.addElement(inputElement);
        
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("Value1", formData.get(0).value());
        
        // Edge case
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "checkbox1");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        checkboxElement.val("CheckedValue");
        formElement.addElement(checkboxElement);
        
        formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("Value1", formData.get(0).value());
        assertEquals("checkbox1", formData.get(1).key());
        assertEquals("CheckedValue", formData.get(1).value());
    }
}