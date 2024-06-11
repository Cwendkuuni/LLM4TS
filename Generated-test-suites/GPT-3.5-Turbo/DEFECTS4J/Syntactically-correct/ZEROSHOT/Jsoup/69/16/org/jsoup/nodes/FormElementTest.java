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
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_invalidAction() {
        formElement.submit();
    }

    @Test
    public void testSubmit_getMethod() {
        formElement.attr("action", "http://www.example.com/getRequest");
        formElement.attr("method", "get");
        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals("http://www.example.com/getRequest", connection.request().url().toString());
    }

    @Test
    public void testSubmit_postMethod() {
        formElement.attr("action", "http://www.example.com/postRequest");
        formElement.attr("method", "post");
        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals("http://www.example.com/postRequest", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        element1.attr("name", "username");
        element1.attr("value", "john");
        Element element2 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        element2.attr("name", "password");
        element2.attr("value", "pass123");
        Element element3 = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        element3.attr("name", "gender");
        Element option1 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        option1.attr("selected", "");
        option1.val("male");
        Element option2 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        option2.val("female");
        element3.appendChild(option1);
        element3.appendChild(option2);
        
        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
        assertEquals("password", formData.get(1).key());
        assertEquals("pass123", formData.get(1).value());
        assertEquals("gender", formData.get(2).key());
        assertEquals("male", formData.get(2).value());
    }
}