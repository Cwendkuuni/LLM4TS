import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;


public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        Assert.assertNotNull(elements);
    }
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = form.addElement(element);
        Assert.assertSame(form, result);
        Elements elements = form.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(element, elements.get(0));
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = form.submit();
        Assert.assertNotNull(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_missingActionUrl() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        form.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> formData = form.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_selectElement_selectedOption() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("value", "option1");
        optionElement.attr("selected", "");
        selectElement.appendChild(optionElement);
        form.addElement(selectElement);
        
        List<Connection.KeyVal> formData = form.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("select", keyVal.key());
        Assert.assertEquals("option1", keyVal.value());
    }
    
    @Test
    public void testFormData_selectElement_noSelectedOption() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("value", "option1");
        selectElement.appendChild(optionElement);
        form.addElement(selectElement);
        
        List<Connection.KeyVal> formData = form.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("select", keyVal.key());
        Assert.assertEquals("option1", keyVal.value());
    }
    
    @Test
    public void testFormData_checkboxElement_checked() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox1");
        checkboxElement.attr("checked", "");
        checkboxElement.val("value1");
        form.addElement(checkboxElement);
        
        List<Connection.KeyVal> formData = form.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("checkbox1", keyVal.key());
        Assert.assertEquals("value1", keyVal.value());
    }
    
    @Test
    public void testFormData_checkboxElement_notChecked() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox1");
        checkboxElement.val("value1");
        form.addElement(checkboxElement);
        
        List<Connection.KeyVal> formData = form.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }
    
    @Test
    public void testFormData_radioElement_checked() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio1");
        radioElement.attr("checked", "");
        radioElement.val("value1");
        form.addElement(radioElement);
        
        List<Connection.KeyVal> formData = form.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("radio1", keyVal.key());
        Assert.assertEquals("value1", keyVal.value());
    }
    
    @Test
    public void testFormData_radioElement_notChecked() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio1");
        radioElement.val("value1");
        form.addElement(radioElement);
        
        List<Connection.KeyVal> formData = form.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }
    
    @Test
    public void testFormData_inputElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "input1");
        inputElement.val("value1");
        form.addElement(inputElement);
        
        List<Connection.KeyVal> formData = form.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("input1", keyVal.key());
        Assert.assertEquals("value1", keyVal.value());
    }
}