import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(form.elements());
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element);
        assertTrue(form.elements().contains(element));
    }

    @Test
    public void testSubmitWithValidAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("action", "http://submit.com");
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john_doe");
        form.addElement(inputElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "subscribe");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        form.addElement(checkboxElement);

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("selected", "selected");
        optionElement.val("option1");
        selectElement.appendChild(optionElement);
        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(3, formData.size());
        assertEquals("john_doe", formData.get(0).value());
        assertEquals("on", formData.get(1).value());
        assertEquals("option1", formData.get(2).value());
    }
}