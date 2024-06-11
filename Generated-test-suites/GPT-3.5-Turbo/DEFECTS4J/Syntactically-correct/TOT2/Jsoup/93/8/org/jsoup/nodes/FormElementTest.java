import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class FormElementTest {
    
    @Test
    public void testElementsMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        assertNotNull(elements);
    }
    
    @Test
    public void testAddElementMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));
        assertEquals(1, form.elements().size());
    }
    
    @Test
    public void testSubmitMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(form.submit());
    }
    
    @Test
    public void testFormDataMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertFalse(form.formData().isEmpty());
    }
}