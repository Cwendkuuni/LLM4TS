import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.Connection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        formElement = new FormElement(tag, baseUri, new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = mock(Element.class);
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        // Test when action attribute is present
        formElement.attr("action", "http://www.submit.com");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://www.submit.com", connection.request().url().toString());

        // Test when action attribute is not present
        formElement.removeAttr("action");
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormData() {
        // Test when elements list is empty
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertTrue(data.isEmpty());

        // Test adding different types of form elements
        Element selectElement = mock(Element.class);
        when(selectElement.tagName()).thenReturn("select");
        when(selectElement.attr("name")).thenReturn("selectName");
        Element optionElement = mock(Element.class);
        when(optionElement.tagName()).thenReturn("option");
        when(optionElement.val()).thenReturn("optionValue");
        when(selectElement.select("option[selected]")).thenReturn(new Elements(optionElement));

        formElement.addElement(selectElement);

        Element inputElement = mock(Element.class);
        when(inputElement.tagName()).thenReturn("input");
        when(inputElement.attr("name")).thenReturn("inputName");
        when(inputElement.val()).thenReturn("inputValue");

        formElement.addElement(inputElement);

        data = formElement.formData();
        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals(2, data.size());
    }

    @Test
    public void testEquals() {
        FormElement sameFormElement = formElement;
        FormElement differentFormElement = new FormElement(Tag.valueOf("form"), "http://www.different.com", new Attributes());

        assertEquals(formElement, sameFormElement);
        assertNotEquals(formElement, differentFormElement);
    }
}