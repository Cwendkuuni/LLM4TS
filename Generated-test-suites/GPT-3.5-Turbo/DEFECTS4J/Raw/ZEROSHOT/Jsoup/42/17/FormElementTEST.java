import org.jsoup.nodes.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FormElementTest {

    @Test
    public void testElementsReturnsElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Elements elements = formElement.elements();
        
        assertNotNull(elements);
    }
    
    @Test
    public void testAddElementReturnsFormElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        
        FormElement result = formElement.addElement(element);
        
        assertSame(formElement, result);
    }
    
    @Test
    public void testSubmitWithAbsoluteActionUrlReturnsConnection() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "http://example.com/submit");
        
        Connection result = formElement.submit();
        
        assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionUrlThrowsIllegalArgumentException() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        formElement.submit();
    }
    
    @Test
    public void testSubmitWithPostMethodSetsPOSTMethodInConnection() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "POST");
        formElement.attr("action", "http://example.com/submit");
        
        Connection result = formElement.submit();
        
        assertNotNull(result);
        assertEquals(Connection.Method.POST, result.method());
    }
    
    @Test
    public void testSubmitWithGetMethodSetsGETMethodInConnection() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "GET");
        formElement.attr("action", "http://example.com/submit");
        
        Connection result = formElement.submit();
        
        assertNotNull(result);
        assertEquals(Connection.Method.GET, result.method());
    }
    
    @Test
    public void testFormDataReturnsCorrectDataList() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("selected", "");
        optionElement.attr("value", "1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "testCheckbox");
        checkboxElement.attr("value", "on");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "testRadio");
        radioElement.attr("value", "1");
        radioElement.attr("checked", "");
        formElement.addElement(radioElement);
        Element textElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        textElement.attr("type", "text");
        textElement.attr("name", "testText");
        textElement.attr("value", "TestValue");
        formElement.addElement(textElement);
        
        List<Connection.KeyVal> result = formElement.formData();
        
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("1", result.get(0).value());
        assertEquals("testCheckbox", result.get(1).key());
        assertEquals("on", result.get(1).value());
        assertEquals("testRadio", result.get(2).key());
        assertEquals("1", result.get(2).value());
        assertEquals("testText", result.get(3).key());
        assertEquals("TestValue", result.get(3).value());
    }
    
    @Test
    public void testEqualsReturnsTrueForSameObject() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        boolean result = formElement.equals(formElement);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualsReturnsFalseForNull() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        boolean result = formElement.equals(null);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentClass() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Object obj = new Object();
        
        boolean result = formElement.equals(obj);
        
        assertFalse(result);
    }
}