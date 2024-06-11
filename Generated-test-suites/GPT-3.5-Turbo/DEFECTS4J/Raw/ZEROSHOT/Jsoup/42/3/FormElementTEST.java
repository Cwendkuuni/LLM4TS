import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        formElement.submit();
    }

    @Test
    public void testSubmitWithValidAction() {
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals(0, connection.request().data().size());
    }

    @Test
    public void testFormData() {
        Element inputName = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputName.attr("name", "name");
        inputName.val("John Doe");

        Element inputEmail = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputEmail.attr("name", "email");
        inputEmail.val("john@example.com");

        formElement.addElement(inputName);
        formElement.addElement(inputEmail);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(2, formData.size());

        assertEquals("name", formData.get(0).key());
        assertEquals("John Doe", formData.get(0).value());

        assertEquals("email", formData.get(1).key());
        assertEquals("john@example.com", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement3 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        assertEquals(formElement, formElement1);
        assertNotEquals(formElement, formElement2);
        assertNotEquals(formElement, formElement3);
        assertNotEquals(formElement1, formElement2);
        assertNotEquals(formElement1, formElement3);
        assertNotEquals(formElement2, formElement3);
    }
}