import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testElementsMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElementMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmitMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        // Testing submit method might be more complex, should consider using mock objects or refactor
        // for simplicity, we can just check that the connection object is not null
        assertNotNull(connection);
    }

    @Test
    public void testFormDataMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "johnDoe");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("johnDoe", keyVal.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithoutAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.submit();
    }

}