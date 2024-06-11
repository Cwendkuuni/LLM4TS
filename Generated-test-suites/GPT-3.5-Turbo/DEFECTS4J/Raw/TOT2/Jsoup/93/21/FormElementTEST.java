import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

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
        formElement.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
        // Test for IllegalArgumentException if action URL is empty
        assertThrows(IllegalArgumentException.class, () -> {
            FormElement formElementEmptyAction = new FormElement(Tag.valueOf("form"), "", new Attributes());
            formElementEmptyAction.submit();
        });
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));
        formElement.addElement(new Element(Tag.valueOf("select"), "http://example.com", new Attributes()));
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size()); // Only the input element should be included in form data
    }
}