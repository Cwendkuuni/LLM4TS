
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.Connection;

public class FormElementTest {
    
    // Test elements() method
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        assertNotNull(elements);
    }
    
    // Test addElement() method
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "http://example.com");
        FormElement newForm = form.addElement(input);
        assertEquals(1, newForm.elements().size());
        assertEquals(input, newForm.elements().first());
    }
    
    // Test submit() method without action attribute
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = form.submit(); // Should throw exception
    }
    
    // Test submit() method with method attribute set to POST
    @Test
    public void testSubmitWithPostMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("action", "http://example.com/submit");
        form.attr("method", "POST");
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
    }
    
    // Test formData() method without any form control elements
    @Test
    public void testFormDataWithNoElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }
    
    // Test formData() method with form control elements
    @Test
    public void testFormDataWithElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.addElement(new Element(Tag.valueOf("input"), "http://example.com").attr("name", "username").val("john"));
        form.addElement(new Element(Tag.valueOf("input"), "http://example.com").attr("name", "password").val("pass123"));
        List<Connection.KeyVal> data = form.formData();
        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john", data.get(0).value());
        assertEquals("password", data.get(1).key());
        assertEquals("pass123", data.get(1).value());
    }
}

