import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    
    private FormElement formElement;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements_WithNoAddedElements_ReturnsEmptyList() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testAddElement_AddingValidElement_ReturnsFormElementWithAddedElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }
    
    @Test
    public void testSubmit_WithValidForm_ReturnsConnectionWithCorrectActionAndMethod() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Connection con = formElement.submit();
        assertNotNull(con);
        assertEquals("http://example.com", con.request().url().toString());
        assertTrue(con.request().method().equals(Connection.Method.GET));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_WithNoActionURL_ThrowsIllegalArgumentException() {
        Connection con = formElement.submit();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_WithEmptyActionURL_ThrowsIllegalArgumentException() {
        formElement.attributes().put("action", "");
        Connection con = formElement.submit();
    }
    
    @Test
    public void testFormData_WithNoElements_ReturnsEmptyList() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormData_WithFormElements_ReturnsCorrectDataList() {
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element1.attr("name", "name1");
        element1.attr("value", "value1");
        
        Element element2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element2.attr("name", "name2");
        element2.attr("value", "value2");
        element2.attr("checked", "checked");
        
        formElement.addElement(element1);
        formElement.addElement(element2);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(2, formData.size());
        
        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("name1", keyVal1.key());
        assertEquals("value1", keyVal1.value());
        
        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("name2", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }
    
    @Test
    public void testEquals_WithSameObject_ReturnsTrue() {
        assertTrue(formElement.equals(formElement));
    }
    
    @Test
    public void testEquals_WithEqualObject_ReturnsTrue() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        
        FormElement formElement2 = new FormElement(tag, baseUri, attributes);
        
        assertTrue(formElement.equals(formElement2));
    }
    
    @Test
    public void testEquals_WithDifferentObject_ReturnsFalse() {
        Tag tag = Tag.valueOf("div");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        
        FormElement formElement2 = new FormElement(tag, baseUri, attributes);
        
        assertFalse(formElement.equals(formElement2));
    }
}