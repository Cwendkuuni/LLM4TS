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
        String baseUri = "https://example.com";
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
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement result = formElement.addElement(element);

        assertSame(formElement, result);
        assertEquals(1, formElement.elements().size());
        assertSame(element, formElement.elements().get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);

        formElement.removeChild(element);

        assertTrue(formElement.elements().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoActionUrl() {
        Connection connection = formElement.submit();
    }

    @Test
    public void testSubmitWithActionUrl() {
        formElement.attr("action", "https://example.com/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("https://example.com/submit", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.attr("value", "inputValue");

        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkboxElement.attr("name", "checkboxName");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");

        formElement.addElement(inputElement);
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());

        Connection.KeyVal inputKeyValue = formData.get(0);
        assertEquals("inputName", inputKeyValue.key());
        assertEquals("inputValue", inputKeyValue.value());

        Connection.KeyVal checkboxKeyValue = formData.get(1);
        assertEquals("checkboxName", checkboxKeyValue.key());
        assertEquals("on", checkboxKeyValue.value());
    }
}