package org.jsoup.nodes;
import org.jsoup.Connection;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        // test if initially element list is empty
        assertEquals(0, elements.size());
        // add an element to the form
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        // test if element is added to the form
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        // test if element is added to the form
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
        // test if adding multiple elements to the form
        Element element2 = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        formElement.addElement(element2);
        assertEquals(2, elements.size());
        assertEquals(element2, elements.get(1));
    }

    @Test
    public void testSubmit() {
        // create a form element
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        // set the action attribute
        formElement.attr("action", "http://example.com/form");
        // set the method attribute
        formElement.attr("method", "POST");
        // add form control elements
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element1.attr("name", "username");
        element1.val("test_user");
        formElement.addElement(element1);
        Element element2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element2.attr("name", "password");
        element2.val("test_password");
        formElement.addElement(element2);
        // test if connection is prepared correctly
        Connection con = formElement.submit();
        assertEquals("http://example.com/form", con.request().url().toString());
        assertEquals("POST", con.request().method());
        assertEquals(2, con.request().data().size());
        assertEquals("username", con.request().data().get(0).key());
        assertEquals("test_user", con.request().data().get(0).value());
        assertEquals("password", con.request().data().get(1).key());
        assertEquals("test_password", con.request().data().get(1).value());
    }

    @Test
    public void testFormData() {
        // create a form element
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        // add form control elements
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element1.attr("name", "username");
        element1.val("test_user");
        formElement.addElement(element1);
        Element element2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element2.attr("name", "password");
        element2.val("test_password");
        formElement.addElement(element2);
        // test if form data is returned correctly
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("test_user", data.get(0).value());
        assertEquals("password", data.get(1).key());
        assertEquals("test_password", data.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertFalse(formElement1.equals(formElement2));
        formElement2 = formElement1;
        assertTrue(formElement1.equals(formElement2));
    }
}