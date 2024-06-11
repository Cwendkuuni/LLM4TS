import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input.attr("type", "text");
        input.attr("name", "username");
        input.val("john_doe");
        formElement.addElement(input);
        
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john_doe", data.get(0).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormDataDisabledElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input.attr("name", "password");
        input.attr("disabled", "disabled");
        formElement.addElement(input);

        List<Connection.KeyVal> data = formElement.formData();
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormDataCheckbox() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "subscribe");
        checkbox.attr("checked", "checked");
        formElement.addElement(checkbox);

        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        assertEquals("subscribe", data.get(0).key());
        assertEquals("on", data.get(0).value());
    }
}