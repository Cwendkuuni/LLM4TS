import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.FormElement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FormElementTest {
    
    private FormElement formElement;
  
    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }
  
    @Test
    public void testElements() {
        Assert.assertNotNull(formElement.elements());
        Assert.assertTrue(formElement.elements().isEmpty());
        
        // Add a form control element to the form
        Element mockElement = mock(Element.class);
        formElement.addElement(mockElement);
        
        Assert.assertEquals(1, formElement.elements().size());
        Assert.assertEquals(mockElement, formElement.elements().first());
    }
  
    @Test
    public void testSubmit() {
        // Test when the form action is not present
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
        
        // Test when the form action is present
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");
        
        connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com/submit", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitThrowsException() {
        // Test when the form action is empty or null
        formElement.submit();
    }
  
    @Test
    public void testFormData() {
        // Test with empty form elements
        Assert.assertNotNull(formElement.formData());
        Assert.assertTrue(formElement.formData().isEmpty());
        
        // Test with form elements
        Element el1 = mock(Element.class);
        when(el1.tag()).thenReturn(Tag.valueOf("input"));
        when(el1.hasAttr("disabled")).thenReturn(false);
        when(el1.attr("name")).thenReturn("username");
        when(el1.attr("type")).thenReturn("text");
        when(el1.val()).thenReturn("john");
        
        Element el2 = mock(Element.class);
        when(el2.tag()).thenReturn(Tag.valueOf("select"));
        when(el2.hasAttr("disabled")).thenReturn(false);
        when(el2.attr("name")).thenReturn("color");
        when(el2.select("option[selected]")).thenReturn(new Elements());
        
        Element el3 = mock(Element.class);
        when(el3.tag()).thenReturn(Tag.valueOf("select"));
        when(el3.hasAttr("disabled")).thenReturn(false);
        when(el3.attr("name")).thenReturn("country");
        when(el3.select("option[selected]")).thenReturn(new Elements());
        when(el3.select("option").first()).thenReturn(mock(Element.class));
        
        formElement.addElement(el1);
        formElement.addElement(el2);
        formElement.addElement(el3);
        
        Assert.assertNotNull(formElement.formData());
        Assert.assertEquals(2, formElement.formData().size());
        
        Assert.assertEquals("username", formElement.formData().get(0).key());
        Assert.assertEquals("john", formElement.formData().get(0).value());
        
        Assert.assertEquals("country", formElement.formData().get(1).key());
        Assert.assertEquals("", formElement.formData().get(1).value());
    }
}