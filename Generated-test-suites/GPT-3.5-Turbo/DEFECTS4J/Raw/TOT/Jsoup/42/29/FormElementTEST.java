
import org.jsoup.Connection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {
    
    private FormElement formElement;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull("Elements list should not be null", elements);
        assertEquals("Elements list should be empty", 0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertNotNull("Elements list should not be null", elements);
        assertEquals("Elements list should contain one element", 1, elements.size());
        assertTrue("Elements list should contain the added element", elements.contains(element));
    }
    
    @Test
    public void testSubmitWithoutActionUrl() {
        try {
            Connection connection = formElement.submit();
            fail("IllegalArgumentException should be thrown when form's absolute action URL cannot be determined");
        } catch (IllegalArgumentException e) {
            assertNotNull("IllegalArgumentException should be thrown", e);
        }
    }
    
    @Test
    public void testSubmitWithGetMethod() {
        formElement.attr("method", "GET");
        Connection connection = formElement.submit();
        assertEquals("Connection method should be GET", Connection.Method.GET, connection.method());
    }
    
    @Test
    public void testSubmitWithPostMethod() {
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        assertEquals("Connection method should be POST", Connection.Method.POST, connection.method());
    }
    
    @Test
    public void testFormDataWithEmptyElements() {
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull("Form data should not be null", data);
        assertEquals("Form data list should be empty", 0, data.size());
    }
    
    @Test
    public void testFormDataWithFormSubmittableElements() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("type", "text");
        element1.attr("name", "username");
        element1.val("testuser");
        formElement.addElement(element1);
        
        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("type", "password");
        element2.attr("name", "password");
        element2.val("testpass");
        formElement.addElement(element2);
        
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull("Form data should not be null", data);
        assertEquals("Form data list should contain two elements", 2, data.size());
        
        for (Connection.KeyVal keyVal : data) {
            if ("username".equals(keyVal.key())) {
                assertEquals("Username value should be 'testuser'", "testuser", keyVal.value());
            } else if ("password".equals(keyVal.key())) {
                assertEquals("Password value should be 'testpass'", "testpass", keyVal.value());
            } else {
                fail("Unexpected form data key");
            }
        }
    }
    
    @Test
    public void testFormDataWithSelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "gender");
        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement1.attr("value", "male");
        selectElement.appendChild(optionElement1);
        Element optionElement2 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement2.attr("value", "female");
        optionElement2.attr("selected", "selected");
        selectElement.appendChild(optionElement2);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull("Form data should not be null", data);
        assertEquals("Form data list should contain one element", 1, data.size());
        assertEquals("Form data key should be 'gender'", "gender", data.get(0).key());
        assertEquals("Form data value should be 'female'", "female", data.get(0).value());
    }
    
    @Test
    public void testFormDataWithCheckboxElement() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "accept_terms");
        checkboxElement.attr("checked", "checked");
        checkboxElement.val("true");
        formElement.addElement(checkboxElement);
        
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull("Form data should not be null", data);
        assertEquals("Form data list should contain one element", 1, data.size());
        assertEquals("Form data key should be 'accept_terms'", "accept_terms", data.get(0).key());
        assertEquals("Form data value should be 'true'", "true", data.get(0).value());
    }
    
    @Test
    public void testFormDataWithRadioButtonElement() {
        Element radioElement1 = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement1.attr("type", "radio");
        radioElement1.attr("name", "gender");
        radioElement1.attr("checked", "checked");
        radioElement1.val("male");
        formElement.addElement(radioElement1);
        
        Element radioElement2 = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement2.attr("type", "radio");
        radioElement2.attr("name", "gender");
        radioElement2.val("female");
        formElement.addElement(radioElement2);
        
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull("Form data should not be null", data);
        assertEquals("Form data list should contain one element", 1, data.size());
        assertEquals("Form data key should be 'gender'", "gender", data.get(0).key());
        assertEquals("Form data value should be 'male'", "male", data.get(0).value());
    }
    
    @Test
    public void testEquals() {
        Object object = new Object();
        assertFalse("FormElement should not be equal to null", formElement.equals(null));
        assertFalse("FormElement should not be equal to an object of different class", formElement.equals(object));
        assertTrue("FormElement should be equal to itself", formElement.equals(formElement));
    }
}
