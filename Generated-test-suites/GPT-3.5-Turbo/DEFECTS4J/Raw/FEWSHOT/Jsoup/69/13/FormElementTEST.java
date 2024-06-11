import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
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
        assertNotNull(formElement.elements());
        assertTrue(formElement.elements().isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes());
        
        formElement.addElement(element1);
        assertFalse(formElement.elements().isEmpty());
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element1));
        
        formElement.addElement(element2);
        assertEquals(2, formElement.elements().size());
        assertTrue(formElement.elements().contains(element2));
    }
    
    @Test
    public void testSubmit() {
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "post");
        
        Connection connection = formElement.submit();
        
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMissingAction() {
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Element input1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input1.attr("name", "input1");
        input1.attr("value", "value1");
        
        Element input2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input2.attr("name", "input2");
        input2.attr("value", "value2");
        
        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "checkbox");
        checkbox.attr("value", "checked");
        
        Element radio = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radio.attr("type", "radio");
        radio.attr("name", "radio");
        radio.attr("value", "selected");
        
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("value", "option1");
        option1.attr("selected", "");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.attr("value", "option2");
        select.appendChild(option1);
        select.appendChild(option2);
        
        formElement.addElement(input1);
        formElement.addElement(input2);
        formElement.addElement(checkbox);
        formElement.addElement(radio);
        formElement.addElement(select);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(4, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("input1", "value1")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("input2", "value2")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("checkbox", "checked")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("radio", "selected")));
    }
}