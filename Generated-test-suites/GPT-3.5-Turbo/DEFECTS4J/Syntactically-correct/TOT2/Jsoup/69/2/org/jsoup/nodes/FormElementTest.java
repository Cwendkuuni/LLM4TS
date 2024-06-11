import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
    }

    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        assertThrows(IllegalArgumentException.class, () -> formElement.submit());
    }

    @Test
    public void testFormData() {
        assertEquals(0, formElement.formData().size());
    }
}