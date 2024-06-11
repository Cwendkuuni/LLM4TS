import org.jsoup.Connection;
import org.jsoup.nodes.*;
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
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element);
        Elements elements = form.elements();
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        // Add form control elements to the form for testing

        Connection connection = form.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        // Add form control elements to the form for testing

        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        // Do not set action attribute
        form.submit();
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        assertTrue(form1.equals(form2));
    }
}