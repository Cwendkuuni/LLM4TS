import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes(), null);
        FormElement updatedFormElement = formElement.addElement(element);
        assertEquals(updatedFormElement.elements().size(), 1);
        assertEquals(updatedFormElement.elements().get(0), element);
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
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }
}