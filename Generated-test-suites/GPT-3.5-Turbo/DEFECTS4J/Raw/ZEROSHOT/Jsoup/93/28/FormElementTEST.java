import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
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
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);

        Elements elementsBeforeRemoval = formElement.elements();
        assertFalse(elementsBeforeRemoval.isEmpty());
        assertEquals(1, elementsBeforeRemoval.size());

        formElement.removeChild(element);

        Elements elementsAfterRemoval = formElement.elements();
        assertTrue(elementsAfterRemoval.isEmpty());
    }

    @Test
    public void testSubmit() {
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element1.attr("name", "username");
        element1.attr("value", "john");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element2.attr("name", "password");
        element2.attr("value", "password123");
        formElement.addElement(element2);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
        assertTrue(connection.request().data().containsKey("username"));
        assertTrue(connection.request().data().containsKey("password"));
        assertEquals("john", connection.request().data().get("username"));
        assertEquals("password123", connection.request().data().get("password"));
    }

    @Test
    public void testFormData() {
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("value", "option1");
        option1.attr("selected", "selected");
        selectElement.appendChild(option1);
        formElement.addElement(selectElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        checkboxElement.attr("value", "true");
        formElement.addElement(checkboxElement);

        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("name", "radio");
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "checked");
        radioElement.attr("value", "option1");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(3, formData.size());

        Connection.KeyVal selectKeyValue = formData.get(0);
        assertEquals("select", selectKeyValue.key());
        assertEquals("option1", selectKeyValue.value());

        Connection.KeyVal checkboxKeyValue = formData.get(1);
        assertEquals("checkbox", checkboxKeyValue.key());
        assertEquals("true", checkboxKeyValue.value());

        Connection.KeyVal radioKeyValue = formData.get(2);
        assertEquals("radio", radioKeyValue.key());
        assertEquals("option1", radioKeyValue.value());
    }
}