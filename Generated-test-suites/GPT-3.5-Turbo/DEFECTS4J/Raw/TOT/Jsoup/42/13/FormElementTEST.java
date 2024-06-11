import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());
        Element element3 = new Element(Tag.valueOf("select"), "", new Attributes());

        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);

        Elements elements = formElement.elements();
        assertEquals(3, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
        assertTrue(elements.contains(element3));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "http://example.com");
        formElement.attr("method", "GET");

        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "john");

        formElement.addElement(inputElement);

        HttpConnection.Connection con = (HttpConnection.Connection) formElement.submit();
        assertNotNull(con);
        assertEquals("http://example.com", con.request().url().toString());
        assertEquals(HttpConnection.Method.GET, con.request().method());
        assertTrue(con.request().data().contains(HttpConnection.KeyVal.create("username", "john")));
    }

    @Test
    public void testSubmitWithNoActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "POST");

        try {
            formElement.submit();
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "john");

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("selected", "");
        option1.attr("value", "option1");
        selectElement.addElement(option1);

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "option2");
        selectElement.addElement(option2);

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<HttpConnection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        HttpConnection.KeyVal inputKv = HttpConnection.KeyVal.create("username", "john");
        assertTrue(formData.contains(inputKv));

        HttpConnection.KeyVal selectKv = HttpConnection.KeyVal.create("select", "option1");
        assertTrue(formData.contains(selectKv));
    }

    @Test
    public void testFormDataWithEmptyNameAttribute() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("value", "john");

        formElement.addElement(inputElement);

        List<HttpConnection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form1"), "http://example.com", new Attributes());
        FormElement formElement3 = new FormElement(Tag.valueOf("form"), "http://example2.com", new Attributes());

        assertTrue(formElement1.equals(formElement1));
        assertFalse(formElement1.equals(formElement2));
        assertFalse(formElement1.equals(formElement3));
        assertFalse(formElement1.equals(null));
    }
}