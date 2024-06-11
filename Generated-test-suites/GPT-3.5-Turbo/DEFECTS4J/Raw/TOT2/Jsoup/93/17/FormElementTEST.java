import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        assertEquals(0, formElement.elements().size());
        formElement.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        // test normal case of submitting a form
        Connection connection = formElement.submit();
        assertNotNull(connection);
        
        // test handling of missing action attribute
        formElement.removeAttr("action");
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("testuser");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testuser", formData.get(0).value());
        inputElement.attr("disabled", "");
        formData = formElement.formData();
        assertEquals(0, formData.size());
        inputElement.removeAttr("disabled");
        
        // test handling of different form element types
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        selectElement.attr("name", "dropdown");
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("selected", "");
        optionElement.val("option1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("dropdown", formData.get(0).key());
        assertEquals("option1", formData.get(0).value());
    }
}