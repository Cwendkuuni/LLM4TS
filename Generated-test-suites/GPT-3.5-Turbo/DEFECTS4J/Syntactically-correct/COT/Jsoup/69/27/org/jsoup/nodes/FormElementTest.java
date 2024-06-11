import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Connection;

public class FormElementTest {
    
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(formElement.elements());
    }
    
    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(inputElement));
    }
    
    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("JohnDoe");
        formElement.addElement(inputElement);
        assertEquals(1, formElement.formData().size());
        assertEquals("username=JohnDoe", formElement.formData().get(0).toString());
    }
    
    @Test
    public void testFormDataWithCheckboxChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "interest");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.val("sports");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);
        assertEquals(1, formElement.formData().size());
        assertEquals("interest=sports", formElement.formData().get(0).toString());
    }
    
    @Test
    public void testFormDataWithCheckboxUnchecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "interest");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.val("sports");
        formElement.addElement(checkboxElement);
        assertEquals(0, formElement.formData().size());
    }
    
    @Test
    public void testFormDataWithRadioButtonChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("name", "gender");
        radioElement.attr("type", "radio");
        radioElement.val("female");
        radioElement.attr("checked", "checked");
        formElement.addElement(radioElement);
        assertEquals(1, formElement.formData().size());
        assertEquals("gender=female", formElement.formData().get(0).toString());
    }
    
    @Test
    public void testFormDataWithRadioButtonUnchecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("name", "gender");
        radioElement.attr("type", "radio");
        radioElement.val("female");
        formElement.addElement(radioElement);
        assertEquals(0, formElement.formData().size());
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}