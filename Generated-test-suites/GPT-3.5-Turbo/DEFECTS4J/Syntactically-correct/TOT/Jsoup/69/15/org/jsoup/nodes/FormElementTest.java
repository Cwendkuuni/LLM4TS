
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testAddElement() {
        assertEquals(0, formElement.elements().size());

        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element1);

        assertEquals(1, formElement.elements().size());
        assertEquals(element1, formElement.elements().get(0));

        Element element2 = new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes());
        formElement.addElement(element2);

        assertEquals(2, formElement.elements().size());
        assertEquals(element1, formElement.elements().get(0));
        assertEquals(element2, formElement.elements().get(1));
    }

    @Test
    public void testSubmit_noActionURL_absoluteBaseUri() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_noActionURL_noBaseUri() {
        formElement.submit();
        // Expecting IllegalArgumentException since the form's absolute action URL cannot be determined without a base URI
    }

    @Test
    public void testSubmit_absoluteActionURL() {
        formElement.attr("action", "http://example.com/form_handler");
        formElement.attr("method", "get");

        Connection connection = formElement.submit();

        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("http://example.com/form_handler", connection.request().url().toString());
    }

    @Test
    public void testSubmit_relativeActionURL() {
        formElement.attr("action", "/form_handler");
        formElement.attr("method", "get");

        Connection connection = formElement.submit();

        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("http://example.com/form_handler", connection.request().url().toString());
    }

    @Test
    public void testSubmit_invalidActionURL() {
        formElement.attr("action", "invalidActionURL");
        formElement.attr("method", "get");

        try {
            formElement.submit();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        Element input1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input1.attr("name", "field1");
        input1.val("value1");
        formElement.addElement(input1);

        Element input2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input2.attr("name", "field2");
        input2.val("");
        formElement.addElement(input2);

        Element checkbox1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox1.attr("type", "checkbox");
        checkbox1.attr("name", "field3");
        checkbox1.attr("value", "option1");
        checkbox1.attr("checked", "checked");
        formElement.addElement(checkbox1);

        Element checkbox2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox2.attr("type", "checkbox");
        checkbox2.attr("name", "field3");
        checkbox2.attr("value", "option2");
        formElement.addElement(checkbox2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(3, formData.size());

        assertEquals("field1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());

        assertEquals("field2", formData.get(1).key());
        assertEquals("", formData.get(1).value());

        assertEquals("field3", formData.get(2).key());
        assertEquals("option1", formData.get(2).value());
    }


}

