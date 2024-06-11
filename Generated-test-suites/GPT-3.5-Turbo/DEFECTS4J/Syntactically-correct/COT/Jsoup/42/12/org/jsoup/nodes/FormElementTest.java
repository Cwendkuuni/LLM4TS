
import org.jsoup.Jsoup;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", null);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        int initialSize = formElement.elements().size();
        formElement.addElement(new FormElement(Tag.valueOf("input"), "http://example.com", null));
        assertEquals(initialSize + 1, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", null);
        assertTrue(formElement.equals(formElement2));
    }
}
