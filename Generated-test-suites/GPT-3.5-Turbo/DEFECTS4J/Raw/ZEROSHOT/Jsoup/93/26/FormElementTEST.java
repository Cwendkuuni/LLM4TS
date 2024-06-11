import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.jsoup.Connection;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() throws Exception {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        assertEquals(0, formElement.elements().size());
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());

        formElement.removeChild(element);
        assertEquals(0, formElement.elements().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.submit();
    }

    @Test
    public void testSubmitWithAction() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("name", "name");
        element1.attr("value", "John");

        Element element2 = new Element(Tag.valueOf("select"), "", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("selected", "");
        option1.attr("value", "male");
        element2.appendChild(option1);

        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("male", formData.get(1).value());
    }
}