
import org.jsoup.nodes.*;
import org.junit.*;
import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Elements elements = formElement.elements();
        
        assertNotNull(elements);
    }
    
    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element element = new Element(Tag.valueOf("input"), baseUri);
        formElement.addElement(element);
        
        Elements elements = formElement.elements();
        
        assertTrue(elements.contains(element));
    }
    
    @Test
    public void testSubmit() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Connection connection = formElement.submit();
        
        assertNotNull(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertNotNull(formData);
    }
    
    @Test
    public void testFormDataSelect() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element select = new Element(Tag.valueOf("select"), baseUri);
        Element option = new Element(Tag.valueOf("option"), baseUri);
        option.attr("selected", "");
        select.appendChild(option);
        formElement.addElement(select);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(formData.size(), 1);
    }
    
    @Test
    public void testFormDataCheckboxChecked() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element checkbox = new Element(Tag.valueOf("input"), baseUri);
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "");
        formElement.addElement(checkbox);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(formData.size(), 1);
    }
    
    @Test
    public void testFormDataCheckboxUnchecked() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element checkbox = new Element(Tag.valueOf("input"), baseUri);
        checkbox.attr("type", "checkbox");
        formElement.addElement(checkbox);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(formData.size(), 0);
    }
    
    @Test
    public void testFormDataRadioChecked() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element radio = new Element(Tag.valueOf("input"), baseUri);
        radio.attr("type", "radio");
        radio.attr("checked", "");
        formElement.addElement(radio);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(formData.size(), 1);
    }
    
    @Test
    public void testFormDataRadioUnchecked() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element radio = new Element(Tag.valueOf("input"), baseUri);
        radio.attr("type", "radio");
        formElement.addElement(radio);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(formData.size(), 0);
    }
    
    @Test
    public void testFormDataOtherInput() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element input = new Element(Tag.valueOf("input"), baseUri);
        input.attr("name", "test");
        input.attr("value", "value");
        formElement.addElement(input);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(formData.size(), 1);
    }
    
}

