import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FormElementTest {
    
    private FormElement formElement;
    
    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, "http://example.com", attributes);
    }
    
    @Test
    public void testFormElementConstructor() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        
        assertEquals(tag, formElement.tag());
        assertEquals("http://example.com", formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
        assertTrue(formElement.elements().isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        
        assertEquals(1, updatedFormElement.elements().size());
        assertTrue(updatedFormElement.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        formElement.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes("name", "username", "value", "john")));
        formElement.addAttribute("method", "post");
        
        Connection connection = formElement.submit();
        
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Method.POST, connection.request().method());
        List<Connection.KeyVal> expectedData = Arrays.asList(HttpConnection.KeyVal.create("username", "john"));
        assertEquals(expectedData, connection.request().data());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        formElement.addAttribute("method", "get");
        
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes("name", "color"));
        selectElement.addElement(new Element(Tag.valueOf("option"), "http://example.com", new Attributes("value", "red")));
        selectElement.addElement(new Element(Tag.valueOf("option"), "http://example.com", new Attributes("value", "blue", "selected", "")));
        
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes("type", "checkbox", "name", "subscribe", "checked", ""));
        
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes("type", "radio", "name", "payment_method"));
        radioElement.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes("type", "radio", "name", "payment_method", "checked", "")));
        
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes("type", "text", "name", "username", "value", "john"));
        
        formElement.addElement(selectElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(radioElement);
        formElement.addElement(inputElement);
        
        List<Connection.KeyVal> expectedData = Arrays.asList(
                HttpConnection.KeyVal.create("color", "blue"),
                HttpConnection.KeyVal.create("subscribe", "on"),
                HttpConnection.KeyVal.create("payment_method", "on"),
                HttpConnection.KeyVal.create("username", "john")
        );
        
        assertEquals(expectedData, formElement.formData());
    }
    
    @Test
    public void testFormDataEmpty() {
        assertTrue(formElement.formData().isEmpty());
    }
    
}