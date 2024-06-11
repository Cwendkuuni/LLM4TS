import org.jsoup.Connection;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "https://example.com");
        attributes.put("method", "POST");
        formElement = new FormElement(tag, "https://example.com", attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        FormElement returnedForm = formElement.addElement(element);
        assertNotNull(returnedForm);
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("testuser");

        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testuser", formData.get(0).value());
    }
}