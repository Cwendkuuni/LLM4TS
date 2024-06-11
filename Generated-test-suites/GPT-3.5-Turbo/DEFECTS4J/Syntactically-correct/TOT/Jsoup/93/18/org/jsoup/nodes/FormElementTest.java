import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.Connection;
import org.junit.Before;

public class FormElementTest {
    
    private FormElement formElement;
    
    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }
    
    @Test
    public void testElements() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());
        formElement.addElement(element1);
        formElement.addElement(element2);
        
        Elements elements = formElement.elements();
        
        assertEquals(2, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        
        formElement.addElement(element);
        Elements elements = formElement.elements();
        
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }
    
    @Test
    public void testRemoveChild() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());
        formElement.addElement(element1);
        formElement.addElement(element2);
        
        formElement.removeChild(element1);
        
        Elements elements = formElement.elements();
        
        assertEquals(1, elements.size());
        assertFalse(elements.contains(element1));
        assertTrue(elements.contains(element2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        Connection connection = formElement.submit();
    }
    
    @Test
    public void testSubmitWithPostMethod() {
        formElement.attr("method", "post");
        Connection connection = formElement.submit();
        
        assertEquals(Connection.Method.POST, connection.method());
    }
    
    @Test
    public void testSubmitWithGetMethod() {
        formElement.attr("method", "get");
        Connection connection = formElement.submit();
        
        assertEquals(Connection.Method.GET, connection.method());
    }
    
    @Test
    public void testSubmitWithData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.val("inputValue");
        formElement.addElement(inputElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        assertEquals("inputName", formData.get(0).key());
        assertEquals("inputValue", formData.get(0).value());
    }
    
    @Test
    public void testSubmitWithDataSelect() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "selectName");
        
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.val("optionValue1");
        option1.attr("selected", "");
        
        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.val("optionValue2");
        
        selectElement.addElement(option1);
        selectElement.addElement(option2);
        
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        assertEquals("selectName", formData.get(0).key());
        assertEquals("optionValue1", formData.get(0).value());
    }
    
    @Test
    public void testSubmitWithDataSelectNoSelectedOption() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "selectName");
        
        Element option = new Element(Tag.valueOf("option"), "", new Attributes());
        option.val("optionValue");
        
        selectElement.addElement(option);
        
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        assertEquals("selectName", formData.get(0).key());
        assertEquals("optionValue", formData.get(0).value());
    }
    
    @Test
    public void testSubmitWithDataSelectOptionNoValue() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "selectName");
        
        Element option = new Element(Tag.valueOf("option"), "", new Attributes());
        option.attr("selected", "");
        
        selectElement.addElement(option);
        
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        assertEquals("selectName", formData.get(0).key());
        assertEquals("", formData.get(0).value());
    }
    
    @Test
    public void testSubmitWithDataCheckboxChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkboxName");
        checkboxElement.attr("checked", "");
        
        formElement.addElement(checkboxElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        assertEquals("checkboxName", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }
    
    @Test
    public void testSubmitWithDataCheckboxNotChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkboxName");
        
        formElement.addElement(checkboxElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(0, formData.size());
    }
    
    @Test
    public void testSubmitWithDataRadioButtonChecked() {
        Element radioButtonElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioButtonElement.attr("type", "radio");
        radioButtonElement.attr("name", "radioName");
        radioButtonElement.attr("checked", "");
        
        formElement.addElement(radioButtonElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        assertEquals("radioName", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }
    
    @Test
    public void testSubmitWithDataRadioButtonNotChecked() {
        Element radioButtonElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioButtonElement.attr("type", "radio");
        radioButtonElement.attr("name", "radioName");
        
        formElement.addElement(radioButtonElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(0, formData.size());
    }
    
    @Test
    public void testSubmitWithDataDefault() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "name");
        element.val("value");
        formElement.addElement(element);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("value", formData.get(0).value());
    }
    
}