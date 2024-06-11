import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("method", "POST");
        attributes.put("action", "http://example.com/submit");

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
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        formElement.removeAttr("action"); // Remove action attribute to simulate no action URL
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("admin");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        selectElement.attr("name", "country");
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement1.attr("selected", "");
        optionElement1.val("USA");
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement2.val("Canada");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("admin", formData.get(0).value());
        assertEquals("country", formData.get(1).key());
        assertEquals("USA", formData.get(1).value());
    }

    @Test
    public void testFormDataDisabledElement() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("disabled", "");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataCheckboxChecked() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "accept_terms");
        inputElement.attr("type", "checkbox");
        inputElement.attr("checked", "");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("accept_terms", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormDataCheckboxNotChecked() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "accept_terms");
        inputElement.attr("type", "checkbox");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataRadioChecked() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "gender");
        inputElement.attr("type", "radio");
        inputElement.attr("checked", "");
        inputElement.val("male");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("gender", formData.get(0).key());
        assertEquals("male", formData.get(0).value());
    }

    @Test
    public void testFormDataRadioNotChecked() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "gender");
        inputElement.attr("type", "radio");
        inputElement.val("male");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataTextField() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("type", "text");
        inputElement.val("admin");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("admin", formData.get(0).value());
    }

}