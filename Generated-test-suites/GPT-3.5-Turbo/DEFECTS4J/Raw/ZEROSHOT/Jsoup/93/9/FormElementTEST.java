import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Elements elements = formElement.elements();

        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com");
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();

        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testRemoveChild() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com");
        formElement.addElement(inputElement);

        formElement.removeChild(inputElement);

        Elements elements = formElement.elements();

        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(formElement.baseUri(), connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com");
        inputElement.attr("name", "test");
        inputElement.attr("value", "123");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("test", formData.get(0).key());
        assertEquals("123", formData.get(0).value());
    }
}