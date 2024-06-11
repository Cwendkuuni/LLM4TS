import org.jsoup.Connection;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.jsoup.helper.HttpConnection;

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
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com");
        attributes.put("method", "POST");
        formElement = new FormElement(tag, "http://example.com", attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "");
        FormElement result = formElement.addElement(element);
        assertEquals(formElement, result);
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        // Test with valid action URL
        Connection con = formElement.submit();
        assertNotNull(con);

        // Test with invalid action URL
        formElement.removeAttr("action");
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormData() {
        Element selectElement = new Element(Tag.valueOf("select"), "");
        Element optionElement = new Element(Tag.valueOf("option"), "");
        optionElement.attr("selected", "");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("", formData.get(0).value());
    }

}