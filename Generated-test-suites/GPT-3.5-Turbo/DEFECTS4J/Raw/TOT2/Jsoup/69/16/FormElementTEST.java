import org.jsoup.nodes.*;
import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        // Check if FormElement is instantiated correctly
        assertNotNull(formElement);
    }

    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("input");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element element = new Element(tag, baseUri, attributes);
        
        FormElement result = formElement.addElement(element);
        
        // Check if element is added to FormElement successfully
        assertTrue(result.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        // Check if submit method returns a Connection object
        assertNotNull(formElement.submit());
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        // Add test elements to form
        Element inputElement = new Element(Tag.valueOf("input"), baseUri, attributes);
        inputElement.attr("name", "username");
        inputElement.val("JohnDoe");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), baseUri, attributes);
        Element optionElement1 = new Element(Tag.valueOf("option"), baseUri, attributes);
        optionElement1.attr("value", "1");
        optionElement1.attr("selected", "");
        selectElement.appendChild(optionElement1);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        // Check if formData contains the correct key-value pairs
        assertEquals(formData.size(), 2);
        assertEquals(formData.get(0).key(), "username");
        assertEquals(formData.get(0).value(), "JohnDoe");
        assertEquals(formData.get(1).key(), selectElement.attr("name"));
        assertEquals(formData.get(1).value(), optionElement1.val());
    }
}