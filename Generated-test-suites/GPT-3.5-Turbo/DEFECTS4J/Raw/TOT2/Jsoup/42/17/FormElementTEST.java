import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("name", "username").put("value", "john"));
        formElement.addElement(inputElement);
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes().put("name", "gender"));
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes().put("value", "male").put("selected", ""));
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes().put("value", "female"));
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
        assertEquals("gender", formData.get(1).key());
        assertEquals("male", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(formElement1, formElement2);
    }
}