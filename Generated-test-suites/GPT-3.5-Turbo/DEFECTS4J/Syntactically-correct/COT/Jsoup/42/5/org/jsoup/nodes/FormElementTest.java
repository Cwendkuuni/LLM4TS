import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com/";
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
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);
        assertSame(formElement, result);
        assertEquals(1, formElement.elements().size());
        assertSame(element, formElement.elements().get(0));
    }

    @Test
    public void testSubmitWithAction() {
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.val("testuser");

        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "subscribe");
        checkboxElement.attr("checked", "");

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement1.attr("value", "option1");
        optionElement1.attr("selected", "");
        selectElement.appendChild(optionElement1);
        Element optionElement2 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement2.attr("value", "option2");
        selectElement.appendChild(optionElement2);

        formElement.addElement(inputElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(3, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testuser", formData.get(0).value());
        assertEquals("subscribe", formData.get(1).key());
        assertEquals("", formData.get(1).value());
        assertEquals("subscribe", formData.get(2).key());
        assertEquals("option1", formData.get(2).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com/", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com/", new Attributes());
        FormElement formElement3 = new FormElement(Tag.valueOf("form"), "http://example.org/", new Attributes());

        assertTrue(formElement1.equals(formElement2));
        assertFalse(formElement1.equals(formElement3));
    }
}