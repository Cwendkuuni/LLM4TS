
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));

        formElement.addElement(element); // add the same element again
        assertEquals(2, elements.size()); // make sure the element is duplicated in the list
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitEmptyForm() {
        formElement.submit();
    }
    
    @Test
    public void testSubmitFormData() {
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri())
                .attr("name", "name")
                .attr("value", "John Doe");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), formElement.baseUri())
                .attr("name", "email")
                .attr("value", "john@example.com");
        formElement.addElement(element2);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertTrue(connection.request().data().containsKey("name"));
        assertEquals("John Doe", connection.request().data().get("name"));
        assertTrue(connection.request().data().containsKey("email"));
        assertEquals("john@example.com", connection.request().data().get("email"));
    }
    
    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri())
                .attr("name", "name")
                .attr("value", "John Doe");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), formElement.baseUri())
                .attr("name", "email")
                .attr("value", "john@example.com");
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertTrue(formData.contains(Jsoup.connect("").request().data("name", "John Doe")));
        assertTrue(formData.contains(Jsoup.connect("").request().data("email", "john@example.com")));
    }

}

