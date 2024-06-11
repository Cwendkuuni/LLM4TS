import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.select.Elements;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(form.elements());
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = form.addElement(input);
        assertEquals(1, form.elements().size());
        assertEquals(input, form.elements().get(0));
        assertEquals(form, result);
    }

    @Test
    public void testRemoveChild() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(input);
        form.removeChild(input);
        assertEquals(0, form.elements().size());
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection con = form.submit();
        assertNotNull(con);
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input.attr("name", "username");
        input.val("JohnDoe");
        form.addElement(input);
        assertTrue(form.formData().size() > 0);
    }

}