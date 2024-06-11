
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element);
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMissingAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(0, data.size());
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form3 = new FormElement(Tag.valueOf("form"), "http://example.org", new Attributes());

        assertTrue(form1.equals(form1));
        assertTrue(form1.equals(form2));
        assertFalse(form1.equals(form3));
        assertFalse(form1.equals(null));
    }
}

