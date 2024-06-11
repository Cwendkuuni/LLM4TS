import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "");
        FormElement updatedFormElement = formElement.addElement(element);
        Elements elements = updatedFormElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertSame(element, elements.get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "");
        formElement.addElement(element);
        formElement.removeChild(element);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }
}