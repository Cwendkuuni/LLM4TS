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
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());

        FormElement result = formElement.addElement(element);

        assertTrue(formElement.elements().contains(element));
        assertEquals(formElement, result);
    }

    @Test
    public void testSubmit() {
        // Test case with action attribute
        formElement.attr("action", "https://example.com/submit");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();

        assertEquals("https://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());

        // Test case without action attribute
        formElement.removeAttr("action");

        try {
            formElement.submit();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("John");

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement1.attr("selected", "");
        optionElement1.val("option1");
        Element optionElement2 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement2.val("option2");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
        assertEquals("name", formData.get(1).key());
        assertEquals("option1", formData.get(1).value());
    }
}