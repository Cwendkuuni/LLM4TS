import org.jsoup.Connection;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Element;
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
        formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionUrl() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        form.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john_doe");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("john_doe", keyVal.value());
    }
}