import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = formElement.addElement(element);
        assertTrue(formElement.elements().contains(element));
        assertEquals(formElement, result);
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("testuser");

        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testuser", formData.get(0).value());
    }
}