import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

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
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john.doe");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john.doe", data.get(0).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormDataWithDisabledElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "email");
        inputElement.attr("disabled", "disabled");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(0, data.size());
    }

}