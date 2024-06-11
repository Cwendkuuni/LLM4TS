import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        assertNotNull(formElement);
        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

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
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        
        FormElement result = formElement.addElement(element);
        
        assertNotNull(result);
        assertEquals(1, formElement.elements.size());
        assertEquals(element, formElement.elements().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Connection connection = formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john_doe");
        formElement.addElement(inputElement);
        
        List<Connection.KeyVal> data = formElement.formData();
        
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john_doe", data.get(0).value());
    }
}