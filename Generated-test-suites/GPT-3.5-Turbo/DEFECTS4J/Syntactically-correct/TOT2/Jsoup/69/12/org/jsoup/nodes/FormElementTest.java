import org.jsoup.parser.Tag;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        
        assertEquals(0, form.elements().size());
        form.addElement(element);
        assertEquals(1, form.elements().size());
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        // Test for IllegalArgumentException if action URL is not set
        try {
            form.submit();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        input.attr("name", "username");
        input.attr("value", "john_doe");
        form.addElement(input);
        
        Element select = new Element(Tag.valueOf("select"), "", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "1").attr("selected", "");
        select.appendChild(option1);
        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "2");
        select.appendChild(option2);
        form.addElement(select);
        
        assertEquals(2, form.formData().size());
    }

}