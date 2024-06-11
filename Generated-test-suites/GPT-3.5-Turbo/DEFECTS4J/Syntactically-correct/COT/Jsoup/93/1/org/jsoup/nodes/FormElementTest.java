import org.jsoup.*;
import org.jsoup.Connection.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.jsoup.select.*;
import org.junit.*;
import java.util.*;

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
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "");
        FormElement result = formElement.addElement(element);
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(element, elements.get(0));
        Assert.assertSame(formElement, result);
    }
    
    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_exception() {
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(0, data.size());
    }
    
    @Test
    public void testFormData_withInput() {
        Element input = new Element(Tag.valueOf("input"), "");
        input.attr("name", "testname");
        input.attr("value", "testvalue");
        formElement.addElement(input);
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("testname", data.get(0).key());
        Assert.assertEquals("testvalue", data.get(0).value());
    }
    
    @Test
    public void testFormData_withCheckboxChecked() {
        Element checkbox = new Element(Tag.valueOf("input"), "");
        checkbox.attr("name", "testname");
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "");
        formElement.addElement(checkbox);
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("testname", data.get(0).key());
        Assert.assertEquals("on", data.get(0).value());
    }
    
    @Test
    public void testFormData_withCheckboxUnchecked() {
        Element checkbox = new Element(Tag.valueOf("input"), "");
        checkbox.attr("name", "testname");
        checkbox.attr("type", "checkbox");
        formElement.addElement(checkbox);
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(0, data.size());
    }
    
    @Test
    public void testFormData_withRadioChecked() {
        Element radio = new Element(Tag.valueOf("input"), "");
        radio.attr("name", "testname");
        radio.attr("type", "radio");
        radio.attr("checked", "");
        formElement.addElement(radio);
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("testname", data.get(0).key());
        Assert.assertEquals("on", data.get(0).value());
    }
    
    @Test
    public void testFormData_withRadioUnchecked() {
        Element radio = new Element(Tag.valueOf("input"), "");
        radio.attr("name", "testname");
        radio.attr("type", "radio");
        formElement.addElement(radio);
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(0, data.size());
    }
    
    @Test
    public void testFormData_withSelect() {
        Element select = new Element(Tag.valueOf("select"), "");
        Element option = new Element(Tag.valueOf("option"), "");
        option.attr("value", "option1");
        select.appendChild(option);
        formElement.addElement(select);
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("option1", data.get(0).value());
    }
    
    
}