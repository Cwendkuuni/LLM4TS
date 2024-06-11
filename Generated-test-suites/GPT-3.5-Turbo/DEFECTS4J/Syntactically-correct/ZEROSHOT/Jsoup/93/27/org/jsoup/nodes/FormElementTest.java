import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

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
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        FormElement result = formElement.addElement(element);
        assertNotNull(result);
        assertEquals(1, formElement.elements().size());
        assertSame(element, formElement.elements().get(0));
    }
    
    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        formElement.addElement(element);
        formElement.removeChild(element);
        assertEquals(0, formElement.elements().size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        formElement.submit();
    }
    
    @Test
    public void testSubmitGet() {
        formElement.attr("action", "http://example.com/action");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("GET", connection.request().method());
        assertEquals("http://example.com/action", connection.request().url().toString());
    }
    
    @Test
    public void testSubmitPost() {
        formElement.attr("action", "http://example.com/action");
        formElement.attr("method", "post");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("POST", connection.request().method());
        assertEquals("http://example.com/action", connection.request().url().toString());
    }
    
    @Test
    public void testFormData() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri());
        input.attr("name", "name");
        input.attr("value", "John Doe");
        formElement.addElement(input);
        
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri());
        select.attr("name", "country");
        Element option1 = new Element(Tag.valueOf("option"), formElement.baseUri());
        option1.attr("selected", "");
        option1.val("USA");
        Element option2 = new Element(Tag.valueOf("option"), formElement.baseUri());
        option2.val("UK");
        select.appendChild(option1);
        select.appendChild(option2);
        formElement.addElement(select);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("John Doe", formData.get(0).value());
        assertEquals("country", formData.get(1).key());
        assertEquals("USA", formData.get(1).value());
    }
}