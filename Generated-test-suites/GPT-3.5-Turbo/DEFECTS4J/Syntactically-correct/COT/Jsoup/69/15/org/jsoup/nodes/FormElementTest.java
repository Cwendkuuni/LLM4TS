
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement newFormElement = formElement.addElement(element);
        
        assertNotNull(newFormElement);
        assertEquals(1, newFormElement.elements().size());
        assertEquals(element, newFormElement.elements().get(0));
    }
    
    @Test
    public void testSubmit() {
        try {
            Connection connection = formElement.submit();
            assertNotNull(connection);
        } catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException");
        }
    }
    
    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(0, data.size());
    }
    
}
