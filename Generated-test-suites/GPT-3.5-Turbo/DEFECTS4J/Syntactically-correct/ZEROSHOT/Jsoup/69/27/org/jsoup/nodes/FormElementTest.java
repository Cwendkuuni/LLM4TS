import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private FormElement formElement;
    
    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "");
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
    }
    
    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }
    
    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }
}