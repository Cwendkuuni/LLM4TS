import org.jsoup.Connection;
import org.jsoup.select.Elements;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElementsMethod() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElementMethod() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithoutActionAttribute() {
        formElement.submit(); // Should throw IllegalArgumentException
    }

    @Test
    public void testSubmitMethodWithActionAttribute() {
        formElement.attr("action", "/submit");
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormDataMethod() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john_doe");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john_doe", formData.get(0).value());
    }

    @Test
    public void testEqualsMethod() {
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(formElement, formElement2);

        FormElement formElement3 = new FormElement(Tag.valueOf("form"), "http://example.org", new Attributes());
        assertNotEquals(formElement, formElement3);
    }
}