import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        Elements elements = formElement.elements();
        
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        Element inputElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        formElement.addElement(inputElement);
        
        Elements elements = formElement.elements();
        
        Assert.assertFalse(elements.isEmpty());
        Assert.assertTrue(elements.contains(inputElement));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        formElement.submit();
    }
    
    @Test
    public void testSubmitWithGetMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        formElement.attr("method", "GET");
        
        Connection connection = formElement.submit();
        
        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
    }
    
    @Test
    public void testSubmitWithPostMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        formElement.attr("method", "POST");
        
        Connection connection = formElement.submit();
        
        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }
    
    @Test
    public void testFormFieldData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        Element inputElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("type", "text");
        inputElement.val("John");
        formElement.addElement(inputElement);
        
        Element selectElement = new Element(Tag.valueOf("select"), "https://example.com", new Attributes());
        selectElement.attr("name", "country");
        Element optionElement1 = new Element(Tag.valueOf("option"), "https://example.com", new Attributes());
        optionElement1.attr("selected", "selected");
        optionElement1.val("USA");
        selectElement.appendChild(optionElement1);
        Element optionElement2 = new Element(Tag.valueOf("option"), "https://example.com", new Attributes());
        optionElement2.val("UK");
        selectElement.appendChild(optionElement2);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("username", formData.get(0).key());
        Assert.assertEquals("John", formData.get(0).value());
        Assert.assertEquals("country", formData.get(1).key());
        Assert.assertEquals("USA", formData.get(1).value());
    }
    
    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        
        Assert.assertTrue(formElement1.equals(formElement2));
    }

}