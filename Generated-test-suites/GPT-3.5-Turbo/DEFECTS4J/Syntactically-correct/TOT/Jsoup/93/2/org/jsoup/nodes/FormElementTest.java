import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    
    private FormElement form;
    private Element element1;
    private Element element2;
    private Element element3;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
        
        element1 = new Element(Tag.valueOf("input"), baseUri, new Attributes().put("name", "name1").put("value", "value1"));
        element2 = new Element(Tag.valueOf("input"), baseUri, new Attributes().put("name", "name2").put("value", "value2"));
        element3 = new Element(Tag.valueOf("input"), baseUri, new Attributes().put("name", "name3").put("value", "value3"));
    }
    
    @Test
    public void testElements() {
        form.addElement(element1);
        form.addElement(element2);
        form.addElement(element3);
        
        Elements result = form.elements();
        
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(element1));
        assertTrue(result.contains(element2));
        assertTrue(result.contains(element3));
    }
    
    @Test
    public void testAddElement() {
        FormElement result = form.addElement(element1);
        
        assertNotNull(result);
        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element1));
    }
    
    @Test
    public void testRemoveChild() {
        form.addElement(element1);
        form.addElement(element2);
        form.addElement(element3);
        
        form.removeChild(element2);
        
        assertFalse(form.elements().contains(element2));
    }
    
    @Test
    public void testSubmit() {
        form.attr("action", "http://example.com/submit");
        Connection result = form.submit();
        
        assertNotNull(result);
        assertEquals(Connection.Method.GET, result.method());
        assertEquals("http://example.com/submit", result.request().url().toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_missingBaseUri() {
        Connection result = form.submit();
    }
    
    @Test
    public void testFormData() {
        form.addElement(element1);
        form.addElement(element2);
        form.addElement(element3);
        
        List<Connection.KeyVal> result = form.formData();
        
        assertNotNull(result);
        assertEquals(3, result.size());
    }
    
    @Test
    public void testFormData_disabledInput() {
        element2.attr("disabled", "");
        
        form.addElement(element1);
        form.addElement(element2);
        
        List<Connection.KeyVal> result = form.formData();
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testFormData_selectElement_withSelectedOptions() {
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes().put("value", "option1").put("selected", ""));
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes().put("value", "option2").put("selected", ""));
        
        selectElement.appendChild(option1);
        selectElement.appendChild(option2);
        
        form.addElement(selectElement);
        
        List<Connection.KeyVal> result = form.formData();
        
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    
    @Test
    public void testFormData_selectElement_withoutSelectedOptions() {
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes().put("value", "option1"));
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes().put("value", "option2"));
        
        selectElement.appendChild(option1);
        selectElement.appendChild(option2);
        
        form.addElement(selectElement);
        
        List<Connection.KeyVal> result = form.formData();
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testFormData_checkboxElement_checked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "checkbox").put("name", "checkbox1").put("value", "checkbox-value"));
        checkboxElement.attr("checked", "");
        
        form.addElement(checkboxElement);
        
        List<Connection.KeyVal> result = form.formData();
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testFormData_checkboxElement_notChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "checkbox").put("name", "checkbox1").put("value", "checkbox-value"));
        
        form.addElement(checkboxElement);
        
        List<Connection.KeyVal> result = form.formData();
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testFormData_radioElement_checked() {
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "radio").put("name", "radio1").put("value", "radio-value"));
        radioElement.attr("checked", "");
        
        form.addElement(radioElement);
        
        List<Connection.KeyVal> result = form.formData();
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testFormData_radioElement_notChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "radio").put("name", "radio1").put("value", "radio-value"));
        
        form.addElement(radioElement);
        
        List<Connection.KeyVal> result = form.formData();
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testFormData_otherInputElement() {
        Element otherElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "text").put("name", "other1").put("value", "other-value"));
        
        form.addElement(otherElement);
        
        List<Connection.KeyVal> result = form.formData();
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}