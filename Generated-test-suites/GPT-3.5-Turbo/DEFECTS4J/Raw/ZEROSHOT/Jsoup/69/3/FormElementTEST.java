import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        assertEquals(0, formElement.elements().size());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());

        formElement.addElement(element1);
        formElement.addElement(element2);

        assertEquals(2, formElement.elements().size());
        assertTrue(formElement.elements().contains(element1));
        assertTrue(formElement.elements().contains(element2));
    }

    @Test
    public void testSubmit() throws IllegalArgumentException {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("name", "input1");
        element1.attr("type", "text");
        element1.val("value1");

        Element element2 = new Element(Tag.valueOf("select"), "", new Attributes());
        element2.attr("name", "select1");
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "option1");
        option1.attr("selected", "");
        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "option2");
        element2.appendChild(option1);
        element2.appendChild(option2);

        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(3, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("select1", formData.get(1).key());
        assertEquals("option1", formData.get(1).value());
        assertEquals("select1", formData.get(2).key());
        assertEquals("option2", formData.get(2).value());
    }
}