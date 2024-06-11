import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.Connection;

import org.junit.Before;
import org.junit.Test;

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
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmitMethod() {
        try {
            Connection connection = formElement.submit();
            assertNotNull(connection);
        } catch (IllegalArgumentException e) {
            fail("Submit method should not throw an IllegalArgumentException since the base URI is provided");
        }
    }

    @Test
    public void testFormDataMethod() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }
}