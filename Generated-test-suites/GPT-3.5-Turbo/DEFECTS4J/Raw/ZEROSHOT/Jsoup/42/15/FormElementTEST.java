import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
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
    public void setUp(){
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements(){
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement(){
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitEmptyAction(){
        formElement.submit();
    }

    @Test
    public void testSubmit(){
        formElement.attr("action", "http://example.com/form-action");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com/form-action", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData(){
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("type", "text");
        inputElement.val("john");
        formElement.addElement(inputElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        checkboxElement.val("true");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(2, formData.size());
        assertEquals("username=john", formData.get(0).toString());
        assertEquals("agree=true", formData.get(1).toString());
    }

    @Test
    public void testEquals(){
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(formElement, formElement2);
    }
}