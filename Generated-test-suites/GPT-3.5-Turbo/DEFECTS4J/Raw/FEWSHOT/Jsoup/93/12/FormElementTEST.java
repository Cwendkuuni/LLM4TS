import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        assertTrue(formElement.elements().isEmpty());

        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(inputElement);
        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().first());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        assertTrue(formElement.elements().isEmpty());

        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(inputElement);
        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().first());
    }

    @Test
    public void testRemoveChild() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(inputElement);

        formElement.removeChild(inputElement);
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testSubmitWithActionAttribute() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("action", "https://example.com");

        Connection connection = formElement.submit();
        assertEquals("https://example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmitWithoutActionAttribute() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());

        Connection connection = formElement.submit();
        assertEquals("https://example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmitWithMethodPOST() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("method", "post");

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitWithMethodGET() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("method", "get");

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyActionAndNoBaseUri() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());

        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.val("John");
        formElement.addElement(inputElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "subscribe");
        checkboxElement.attr("checked", "checked");
        checkboxElement.val("on");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
        assertEquals("subscribe", formData.get(1).key());
        assertEquals("on", formData.get(1).value());
    }
}