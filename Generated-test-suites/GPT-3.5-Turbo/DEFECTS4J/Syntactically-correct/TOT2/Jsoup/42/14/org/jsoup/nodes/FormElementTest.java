import org.jsoup.Connection;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john_doe");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john_doe", formData.get(0).value());
    }
}