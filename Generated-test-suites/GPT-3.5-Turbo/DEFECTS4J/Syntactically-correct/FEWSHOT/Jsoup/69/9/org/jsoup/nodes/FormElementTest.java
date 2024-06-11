import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        Elements elements = form.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());

        Element inputElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        form.addElement(inputElement);
        elements = form.elements();
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertEquals(elements.size(), 1);
        assertEquals(elements.get(0), inputElement);
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        form.addElement(inputElement);
        Elements elements = form.elements();
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertEquals(elements.size(), 1);
        assertEquals(elements.get(0), inputElement);
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        form.attr("action", "/submit");
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals(connection.request().url(), "https://example.com/submit");
        assertEquals(connection.request().method(), Connection.Method.GET);

        form.attr("method", "POST");
        connection = form.submit();
        assertNotNull(connection);
        assertEquals(connection.request().url(), "https://example.com/submit");
        assertEquals(connection.request().method(), Connection.Method.POST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionUrl() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("type", "text");
        inputElement.attr("value", "john");
        form.addElement(inputElement);

        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(formData.size(), 1);
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals(keyVal.key(), "username");
        assertEquals(keyVal.value(), "john");
    }
}