import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);
        assertSame(formElement, result);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        assertFalse(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        // Test case when action attribute is present
        formElement.attr("action", "http://example.com/action");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com/action", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());

        // Test case when action attribute is not present
        formElement.removeAttr("action");
        formElement.setBaseUri("http://example.com/base");
        connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com/base", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.val("inputValue");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "selectName");
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("selected", "");
        optionElement.val("optionValue");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("inputName", formData.get(0).key());
        assertEquals("inputValue", formData.get(0).value());
        assertEquals("selectName", formData.get(1).key());
        assertEquals("optionValue", formData.get(1).value());
    }
}