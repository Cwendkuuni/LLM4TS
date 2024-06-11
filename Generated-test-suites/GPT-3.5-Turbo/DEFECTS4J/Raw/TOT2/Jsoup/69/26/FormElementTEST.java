import org.jsoup.Connection;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(1, result.elements().size());
    }

    @Test
    public void testSubmit() {
        Connection conn = formElement.submit();
        assertNotNull(conn);
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username").val("john_doe");
        formElement.addElement(inputElement);
        
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john_doe", data.get(0).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        formElement.submit();
    }

}