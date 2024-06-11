import org.jsoup.Connection;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection con = formElement.submit();
        // not easy to test return value, but should create a connection with correct setup
        assertNotNull(con);
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        input.attr("name", "username");
        input.val("testUser");
        formElement.addElement(input);

        Element select = new Element(Tag.valueOf("select"), "", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "1");
        option1.attr("selected", "");
        select.appendChild(option1);
        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "2");
        select.appendChild(option2);
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testUser", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("1", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(formElement.equals(formElement2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
        fail("Should have thrown IllegalArgumentException");
    }
}