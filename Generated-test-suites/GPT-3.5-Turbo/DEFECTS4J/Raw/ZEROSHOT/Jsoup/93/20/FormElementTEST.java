import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(formElement, result);
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());
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