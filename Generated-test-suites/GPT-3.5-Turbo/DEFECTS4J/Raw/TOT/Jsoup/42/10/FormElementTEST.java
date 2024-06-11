import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement form;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        form.addElement(element);
        assertEquals(1, form.elements().size());
        assertEquals(element, form.elements().get(0));
    }

    @Test
    public void testSubmit() {
        // Test case for valid form action URL
        form.attr("action", "/submit");
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals(form.absUrl("action"), connection.request().url().toString());
        assertEquals(form.attr("method").toUpperCase().equals("POST") ? Connection.Method.POST : Connection.Method.GET, connection.request().method());

        // Test case for empty form action URL
        form.removeAttr("action");
        try {
            connection = form.submit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        // Test case for empty form
        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(0, data.size());

        // Test case for form control input with name and value
        Element inputElement = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("jsmith");
        form.addElement(inputElement);

        data = form.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("jsmith", data.get(0).value());

        // Test case for select element with options
        Element selectElement = new Element(Tag.valueOf("select"), form.baseUri(), new Attributes());
        selectElement.attr("name", "country");
        Element optionElement1 = new Element(Tag.valueOf("option"), form.baseUri(), new Attributes());
        optionElement1.attr("value", "USA");
        optionElement1.text("United States");
        selectElement.appendChild(optionElement1);
        form.addElement(selectElement);

        data = form.formData();
        assertNotNull(data);
        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("jsmith", data.get(0).value());
        assertEquals("country", data.get(1).key());
        assertEquals("USA", data.get(1).value());

        // Test case for select element with no option selected
        selectElement.removeChildren(); // remove previous option
        Element optionElement2 = new Element(Tag.valueOf("option"), form.baseUri(), new Attributes());
        optionElement2.attr("value", "UK");
        optionElement2.text("United Kingdom");
        selectElement.appendChild(optionElement2);
        data = form.formData();
        assertNotNull(data);
        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("jsmith", data.get(0).value());
        assertEquals("country", data.get(1).key());
        assertEquals("UK", data.get(1).value());

        // Test case for checkbox element with checked attribute
        Element checkboxElement = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "subscribe");
        checkboxElement.attr("checked", "");
        checkboxElement.val("true");
        form.addElement(checkboxElement);
        data = form.formData();
        assertNotNull(data);
        assertEquals(3, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("jsmith", data.get(0).value());
        assertEquals("country", data.get(1).key());
        assertEquals("UK", data.get(1).value());
        assertEquals("subscribe", data.get(2).key());
        assertEquals("true", data.get(2).value());

        // Test case for checkbox element without checked attribute
        checkboxElement.removeAttr("checked");
        data = form.formData();
        assertNotNull(data);
        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("jsmith", data.get(0).value());
        assertEquals("country", data.get(1).key());
        assertEquals("UK", data.get(1).value());
    }

    @Test
    public void testEquals() {
        // Test case for equal objects
        FormElement form1 = new FormElement(form.tag(), form.baseUri(), form.attributes());
        assertTrue(form.equals(form1));

        // Test case for objects with different attributes
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "form1");
        FormElement form2 = new FormElement(tag, baseUri, attributes);
        assertFalse(form.equals(form2));

        // Test case for objects with different tag
        Tag tag2 = Tag.valueOf("div");
        FormElement form3 = new FormElement(tag2, baseUri, attributes);
        assertFalse(form.equals(form3));

        // Test case for objects with different baseUri
        String baseUri2 = "http://example.org";
        FormElement form4 = new FormElement(tag, baseUri2, attributes);
        assertFalse(form.equals(form4));
    }
}