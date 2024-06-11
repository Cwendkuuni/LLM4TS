import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

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
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        assertNotNull(updatedFormElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmitWithEmptyAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testSubmitWithGetMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "get");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitWithPostMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "post");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "John");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(formElement1.equals(formElement1));
        assertFalse(formElement1.equals(null));
        assertFalse(formElement1.equals("formElement"));
        assertFalse(formElement1.equals(formElement2));
    }
}