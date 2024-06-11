import org.jsoup.nodes.*;
import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

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
        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(input);
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(input, elements.get(0));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection con = form.submit();
        assertNotNull(con);
        assertTrue(con instanceof Connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        input.attr("name", "username");
        input.val("john_doe");
        form.addElement(input);

        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john_doe", data.get(0).value());
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(form1.equals(form1));
        assertFalse(form1.equals(form2));
    }
}