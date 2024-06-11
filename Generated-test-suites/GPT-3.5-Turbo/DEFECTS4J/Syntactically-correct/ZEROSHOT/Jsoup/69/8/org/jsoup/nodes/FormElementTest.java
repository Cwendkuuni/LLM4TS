import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.Connection;

import java.util.List;

public class FormElementTest {
    
    private FormElement form;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "";
        org.jsoup.nodes.Attributes attributes = new org.jsoup.nodes.Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        Elements elements = form.elements();
        
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new org.jsoup.nodes.Attributes());
        FormElement result = form.addElement(element);
        
        assertEquals(form, result);
        assertEquals(1, form.elements().size());
    }
    
    @Test
    public void testSubmit() {
        Connection connection = form.submit();
        
        assertNotNull(connection);
    }
    
    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = form.formData();
        
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }
}