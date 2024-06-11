import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.After;
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
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @After
    public void tearDown() {
        formElement = null;
    }

    // Test elements()
    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    // Test addElement()
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement returnedElement = formElement.addElement(element);
        assertEquals(formElement, returnedElement);
        assertFalse(formElement.elements().isEmpty());
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().get(0));
    }
    
    // Test removeChild()
    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);
        
        Node out = element;
        formElement.removeChild(out);
        
        assertTrue(formElement.elements().isEmpty());
        assertFalse(formElement.hasChildNodes());
    }

    // Test submit()
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        formElement.submit();
    }

    @Test
    public void testSubmitGetMethod() {
        formElement.attr("method", "get");

        Connection connection = formElement.submit();

        assertEquals(formElement.absUrl("action"), connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitPostMethod() {
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertEquals(formElement.absUrl("action"), connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    // Test formData()
    @Test
    public void testFormDataEmptyList() {
        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormDataWithElementValues() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("name", "username").put("value", "JohnDoe"));
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes().put("name", "gender"));
        Element optionElement = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes().put("value", "male")).text("Male");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals(2, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("JohnDoe", data.get(0).value());
        assertEquals("gender", data.get(1).key());
        assertEquals("male", data.get(1).value());
    }

    @Test
    public void testFormDataWithDisabledElement() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("name", "username").put("value", "JohnDoe").put("disabled", ""));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormDataWithCheckboxChecked() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "checkbox").put("name", "subscribe").put("value", "true").put("checked", ""));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals(1, data.size());
        assertEquals("subscribe", data.get(0).key());
        assertEquals("true", data.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxUnchecked() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "checkbox").put("name", "subscribe").put("value", "true"));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormDataWithRadioChecked() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "radio").put("name", "gender").put("value", "male").put("checked", ""));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals(1, data.size());
        assertEquals("gender", data.get(0).key());
        assertEquals("male", data.get(0).value());
    }

    @Test
    public void testFormDataWithRadioUnchecked() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "radio").put("name", "gender").put("value", "male"));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormDataWithOtherInputElement() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("name", "email").put("value", "abc@example.com"));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals(1, data.size());
        assertEquals("email", data.get(0).key());
        assertEquals("abc@example.com", data.get(0).value());
    }
}