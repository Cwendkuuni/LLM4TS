import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @After
    public void tearDown() {
        formElement = null;
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testAddElementMultiple() {
        Element element1 = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        Element element2 = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        formElement.addElement(element1);
        formElement.addElement(element2);
        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        assertEquals(element1, elements.get(0));
        assertEquals(element2, elements.get(1));
    }

    @Test
    public void testSubmit() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        assertEquals("http://www.example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionUrl() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("type", "text");
        inputElement.val("john.doe");
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        checkboxElement.attr("name", "accept_terms");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");
        formElement.addElement(inputElement);
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john.doe", formData.get(0).value());
        assertEquals("accept_terms", formData.get(1).key());
        assertEquals("", formData.get(1).value());
    }

    @Test
    public void testFormDataIgnoreNonFormSubmittableElements() {
        Element nonFormSubmittableElement = new Element(Tag.valueOf("div"), "http://www.example.com", new Attributes());
        nonFormSubmittableElement.attr("name", "info");
        nonFormSubmittableElement.attr("type", "text");
        nonFormSubmittableElement.val("some info");
        formElement.addElement(nonFormSubmittableElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testEquals() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement2 = new FormElement(tag, baseUri, attributes);
        assertEquals(formElement, formElement2);
    }

    @Test
    public void testNotEquals() {
        Tag tag = Tag.valueOf("div");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement2 = new FormElement(tag, baseUri, attributes);
        assertNotEquals(formElement, formElement2);
    }

}