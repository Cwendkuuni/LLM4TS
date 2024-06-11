import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {
    
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        assertNotNull(form.elements());
    }
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        form.addElement(element);
        assertTrue(form.elements().contains(element));
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        assertNotNull(form.submit());
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        assertNotNull(form.formData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidActionUrlSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        form.submit();
    }
}