import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElementsMethod() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit-form");
        FormElement formElement = new FormElement(tag, "http://www.example.com", attributes);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElementMethod() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit-form");
        FormElement formElement = new FormElement(tag, "http://www.example.com", attributes);

        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes().put("name", "username"));
        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodInvalidAction() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://www.example.com", attributes);

        formElement.submit();
    }

    @Test
    public void testSubmitMethodWithGetMethod() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit-form");
        attributes.put("method", "get");
        FormElement formElement = new FormElement(tag, "http://www.example.com", attributes);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("http://www.example.com/submit-form", connection.request().url().toString());
    }

    @Test
    public void testSubmitMethodWithPostMethod() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit-form");
        attributes.put("method", "post");
        FormElement formElement = new FormElement(tag, "http://www.example.com", attributes);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://www.example.com/submit-form", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit-form");
        FormElement formElement = new FormElement(tag, "http://www.example.com", attributes);

        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes().put("name", "username").put("value", "testuser"));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("testuser", keyVal.value());
    }

    @Test
    public void testEqualsMethod() {
        Tag tag1 = Tag.valueOf("form");
        Attributes attributes1 = new Attributes();

        Tag tag2 = Tag.valueOf("form");
        Attributes attributes2 = new Attributes();

        FormElement formElement1 = new FormElement(tag1, "http://www.example.com", attributes1);
        FormElement formElement2 = new FormElement(tag2, "http://www.example.com", attributes2);

        assertTrue(formElement1.equals(formElement2));
    }

}