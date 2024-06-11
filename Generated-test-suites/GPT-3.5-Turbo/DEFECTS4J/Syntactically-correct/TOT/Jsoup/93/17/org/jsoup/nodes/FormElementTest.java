import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testAddElement() {
        // Typical Use Case
        Tag tag = Tag.valueOf("input");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = new Element(Tag.valueOf("input"), baseUri, attributes);
        FormElement result = formElement.addElement(element);

        assertEquals(result, formElement);
        assertTrue(formElement.elements().contains(element));

        // Edge Case: Adding a null element
        result = formElement.addElement(null);

        assertEquals(result, formElement);
    }

    @Test
    public void testRemoveChild() {
        // Typical Use Case
        Tag tag = Tag.valueOf("input");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = new Element(Tag.valueOf("input"), baseUri, attributes);
        formElement.addElement(element);

        formElement.removeChild(element);

        assertFalse(formElement.elements().contains(element));

        // Edge Case: Removing a child which is not present
        Element element2 = new Element(Tag.valueOf("input"), baseUri, attributes);
        formElement.removeChild(element2);
    }

    @Test
    public void testSubmit() {
        // Typical Use Case
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        // Add form controls
        Element element1 = new Element(Tag.valueOf("input"), baseUri, attributes);
        Element element2 = new Element(Tag.valueOf("textarea"), baseUri, attributes);

        formElement.addElement(element1)
                .addElement(element2);

        Connection connection = formElement.submit();

        assertNotNull(connection);

        // Edge Case: Submitting a form without action
        formElement.attributes().remove("action");

        try {
            formElement.submit();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        // Typical Use Case
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        // Add form controls
        Element element1 = new Element(Tag.valueOf("input"), baseUri, attributes);
        Element element2 = new Element(Tag.valueOf("textarea"), baseUri, attributes);

        formElement.addElement(element1)
                .addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(0, formData.size());

        // Edge Case: Form control elements with different types
        Element selectElement = new Element(Tag.valueOf("select"), baseUri, attributes);
        selectElement.attr("name", "selectInput");

        Element option1 = new Element(Tag.valueOf("option"), baseUri, attributes);
        option1.attr("selected", "selected");
        option1.attr("value", "option1");

        Element option2 = new Element(Tag.valueOf("option"), baseUri, attributes);
        option2.attr("value", "option2");

        selectedElement.addElement(option1)
                .addElement(option2);

        Element checkboxElement = new Element(Tag.valueOf("input"), baseUri, attributes);
        checkboxElement.attr("name", "checkboxInput");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");

        Element radioElement = new Element(Tag.valueOf("input"), baseUri, attributes);
        radioElement.attr("name", "radioInput");
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "checked");
        
        formElement.addElement(selectElement)
                .addElement(checkboxElement)
                .addElement(radioElement);

        formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(3, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("selectInput", keyVal1.key());
        assertEquals("option1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("checkboxInput", keyVal2.key());
        assertEquals("on", keyVal2.value());

        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("radioInput", keyVal3.key());
        assertEquals("on", keyVal3.value());
    }

}