import org.junit.Test;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "text").put("name", "username"));
        Element buttonElement = new Element(Tag.valueOf("button"), "http://example.com", new Attributes().put("type", "submit").put("name", "submit"));
        
        form.addElement(inputElement);
        form.addElement(buttonElement);
        
        Elements elements = form.elements();
        
        assertNotNull(elements);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(inputElement));
        assertTrue(elements.contains(buttonElement));
        
    }
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "text").put("name", "username"));
        
        form.addElement(inputElement);
        Elements elements = form.elements();
        
        assertEquals(1, elements.size());
        assertTrue(elements.contains(inputElement));
    }
    
    @Test
    public void testRemoveChild() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "text").put("name", "username"));
        
        form.addElement(inputElement);
        form.removeChild(inputElement);
        
        Elements elements = form.elements();
        
        assertEquals(0, elements.size());
        assertFalse(elements.contains(inputElement));
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes().put("action", "http://example.com/submit").put("method", "POST"));
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "text").put("name", "username").put("value", "john"));
        
        form.addElement(inputElement);
        
        Connection connection = form.submit();
        
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals("username=john", connection.request().data());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes().put("method", "POST"));
        
        form.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "text").put("name", "username").put("value", "john"));
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes().put("name", "gender"));
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes().put("value", "male").put("selected", ""));
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes().put("value", "female"));
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "checkbox").put("name", "subscribe").put("checked", ""));
        
        form.addElement(inputElement);
        form.addElement(selectElement);
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        form.addElement(checkboxElement);
        
        List<Connection.KeyVal> formData = form.formData();
        
        assertNotNull(formData);
        assertEquals(3, formData.size());
        
        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("username", keyVal1.key());
        assertEquals("john", keyVal1.value());
        
        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("gender", keyVal2.key());
        assertEquals("male", keyVal2.value());
        
        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("subscribe", keyVal3.key());
        assertEquals("on", keyVal3.value());
    }
    
    @Test
    public void testFormDataNoSubmitableElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element divElement = new Element(Tag.valueOf("div"), "http://example.com", new Attributes());
        
        form.addElement(divElement);
        
        List<Connection.KeyVal> formData = form.formData();
        
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }
    
}