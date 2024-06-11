import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement form;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertNotNull(elements);
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = form.addElement(element);
        assertNotNull(result);
        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element));
    }
    
    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element);
        form.removeChild(element);
        assertFalse(form.elements().contains(element));
    }
  
    @Test
    public void testSubmit() {
        Connection connection = form.submit();
        assertNotNull(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        form.submit();
    }
  
    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }
    
    @Test
    public void testFormDataWithSelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.val("option1");
        selectElement.appendChild(optionElement);
        form.addElement(selectElement);
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("option1", formData.get(0).value());
    }
    
    @Test
    public void testFormDataWithCheckboxElementChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("checked", "checked");
        checkboxElement.val("checkbox1");
        checkboxElement.attr("name", "checkboxGroup");
        checkboxElement.attr("type", "checkbox");
        form.addElement(checkboxElement);
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("checkbox1", formData.get(0).value());
    }
    
    @Test
    public void testFormDataWithCheckboxElementUnchecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.val("");
        checkboxElement.attr("name", "checkboxGroup");
        checkboxElement.attr("type", "checkbox");
        form.addElement(checkboxElement);
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }
    
    @Test
    public void testFormDataWithRadioElementChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("checked", "checked");
        radioElement.val("radio1");
        radioElement.attr("name", "radioGroup");
        radioElement.attr("type", "radio");
        form.addElement(radioElement);
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("radio1", formData.get(0).value());
    }
    
    @Test
    public void testFormDataWithRadioElementUnchecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.val("");
        radioElement.attr("name", "radioGroup");
        radioElement.attr("type", "radio");
        form.addElement(radioElement);
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }
    
    @Test
    public void testFormDataWithOtherElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.val("value1");
        element.attr("name", "inputField");
        element.attr("type", "text");
        form.addElement(element);
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("value1", formData.get(0).value());
    }
}