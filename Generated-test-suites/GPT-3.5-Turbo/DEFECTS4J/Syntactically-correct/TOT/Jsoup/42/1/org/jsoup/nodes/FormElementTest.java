import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FormElementTest {

    private FormElement form;
    private Element element;

    @Before
    public void setUp() {
        form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
    }

    @After
    public void tearDown() {
        form = null;
        element = null;
    }

    @Test
    public void testAddElement() {
        form.addElement(element);
        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = form.submit();
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals("http://example.com", connection.request().url().toString());

        form.attr("action", "/submit");
        connection = form.submit();
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
    }

    @Test
    public void testSubmitWithEmptyAction() {
        try {
            form.submit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = form.formData();
        assertEquals(0, data.size());

        form.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes("name", "username", "value", "john")));
        form.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes("name", "password", "value", "secret")));

        data = form.formData();
        assertEquals(2, data.size());
        assertEquals("john", data.get(0).value());
        assertEquals("secret", data.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(form, form2);
    }

}