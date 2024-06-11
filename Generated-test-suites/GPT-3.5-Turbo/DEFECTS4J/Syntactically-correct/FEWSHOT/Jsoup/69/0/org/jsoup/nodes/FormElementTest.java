import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement formElement;
    private Element element;

    @Before
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
    }
    
    @Test
    public void testConstructor() {
        assertEquals(Tag.valueOf("form"), formElement.tag());
        assertEquals("http://example.com", formElement.baseUri());
        assertTrue(formElement.attributes().isEmpty());
    }
    
    @Test
    public void testAddElement() {
        assertEquals(0, formElement.elements().size());
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        optionElement1.attr("selected", "");
        optionElement2.attr("selected", "");
        element.attr("name", "inputName");
        element.attr("value", "inputValue");
        selectElement.attr("name", "selectName");
        selectElement.attr("value", "selectValue");
        formElement.addElement(element);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
        assertEquals("inputName", formData.get(0).key());
        assertEquals("inputValue", formData.get(0).value());
        assertEquals("selectName", formData.get(1).key());
        assertEquals("selectValue", formData.get(1).value());
        assertEquals("selectName", formData.get(2).key());
        assertEquals("selectValue", formData.get(2).value());
    }
    
    @Test
    public void testFormDataNoName() {
        element.attr("value", "inputValue");
        formElement.addElement(element);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataDisabled() {
        element.attr("name", "inputName");
        element.attr("disabled", "");
        formElement.addElement(element);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataCheckbox() {
        element.attr("name", "checkboxName");
        element.attr("type", "checkbox");
        element.attr("checked", "");
        formElement.addElement(element);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("checkboxName", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }
    
    @Test
    public void testFormDataCheckboxUnchecked() {
        element.attr("name", "checkboxName");
        element.attr("type", "checkbox");
        formElement.addElement(element);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }
    
    @Test
    public void testFormDataRadio() {
        element.attr("name", "radioName");
        element.attr("type", "radio");
        element.attr("checked", "");
        formElement.addElement(element);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("radioName", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }
    
    @Test
    public void testFormDataRadioUnchecked() {
        element.attr("name", "radioName");
        element.attr("type", "radio");
        formElement.addElement(element);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }
}