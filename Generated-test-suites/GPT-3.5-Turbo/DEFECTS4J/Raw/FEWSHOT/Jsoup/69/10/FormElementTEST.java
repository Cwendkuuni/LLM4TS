import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(0, form.elements().size());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element1);
        form.addElement(element2);

        assertEquals(2, form.elements().size());
        assertTrue(form.elements().contains(element1));
        assertTrue(form.elements().contains(element2));
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element);

        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("method", "post");
        form.attr("action", "http://example.com/submit");

        Connection connection = form.submit();

        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitNoAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("method", "get");

        Connection connection = form.submit();

        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitEmptyAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element input1 = new Element(Tag.valueOf("input"), "", new Attributes());
        input1.attr("name", "username");
        input1.val("john");

        Element input2 = new Element(Tag.valueOf("input"), "", new Attributes());
        input2.attr("name", "password");
        input2.val("pass123");

        Element checkbox = new Element(Tag.valueOf("input"), "", new Attributes());
        checkbox.attr("name", "rememberMe");
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "");

        form.addElement(input1);
        form.addElement(input2);
        form.addElement(checkbox);

        List<Connection.KeyVal> data = form.formData();

        assertEquals(3, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john", data.get(0).value());
        assertEquals("password", data.get(1).key());
        assertEquals("pass123", data.get(1).value());
        assertEquals("rememberMe", data.get(2).key());
        assertEquals("on", data.get(2).value());
    }
}