import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @After
    public void tearDown() {
        formElement = null;
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri());
        Element element2 = new Element(Tag.valueOf("button"), formElement.baseUri());

        formElement.addElement(element1);
        assertEquals(1, formElement.elements().size());
        assertSame(element1, formElement.elements().get(0));

        formElement.addElement(element2);
        assertEquals(2, formElement.elements().size());
        assertSame(element2, formElement.elements().get(1));
    }

    @Test
    public void testSubmit_withActionAndMethodPOST() {
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
    }

    @Test
    public void testSubmit_withActionAndMethodGET() {
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "GET");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_withEmptyAction() {
        formElement.attr("method", "POST");

        formElement.submit();
    }

    @Test
    public void testSubmit_withNoActionAndMethodPOST() {
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmit_withNoActionAndMethodGET() {
        formElement.attr("method", "GET");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testFormData_withEmptyForm() {
        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_withInput() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri());
        input.attr("name", "username");
        input.val("user123");
        formElement.addElement(input);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("user123", formData.get(0).value());
    }

    @Test
    public void testFormData_withDisabledInput() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri());
        input.attr("name", "username");
        input.attr("disabled", "");
        formElement.addElement(input);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_withEmptyInputName() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri());
        input.attr("name", "");
        formElement.addElement(input);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_withSelect() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri());
        Element option1 = new Element(Tag.valueOf("option"), formElement.baseUri());
        option1.attr("value", "option1");
        option1.attr("selected", "");
        Element option2 = new Element(Tag.valueOf("option"), formElement.baseUri());
        option2.attr("value", "option2");
        select.appendChild(option1);
        select.appendChild(option2);
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("select", formData.get(0).key());
        assertEquals("option1", formData.get(0).value());
    }

    @Test
    public void testFormData_withSelectNoSelectedOption() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri());
        Element option1 = new Element(Tag.valueOf("option"), formElement.baseUri());
        option1.attr("value", "option1");
        Element option2 = new Element(Tag.valueOf("option"), formElement.baseUri());
        option2.attr("value", "option2");
        select.appendChild(option1);
        select.appendChild(option2);
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("select", formData.get(0).key());
        assertEquals("option1", formData.get(0).value());
    }

    @Test
    public void testFormData_withSelectNoOptions() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri());
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_withCheckboxChecked() {
        Element checkbox = new Element(Tag.valueOf("input"), formElement.baseUri());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "agree");
        checkbox.attr("checked", "");
        checkbox.val("true");
        formElement.addElement(checkbox);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("agree", formData.get(0).key());
        assertEquals("true", formData.get(0).value());
    }

    @Test
    public void testFormData_withCheckboxNotChecked() {
        Element checkbox = new Element(Tag.valueOf("input"), formElement.baseUri());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "agree");
        checkbox.val("true");
        formElement.addElement(checkbox);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_withRadioChecked() {
        Element radio = new Element(Tag.valueOf("input"), formElement.baseUri());
        radio.attr("type", "radio");
        radio.attr("name", "category");
        radio.attr("value", "1");
        radio.attr("checked", "");
        formElement.addElement(radio);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("category", formData.get(0).key());
        assertEquals("1", formData.get(0).value());
    }

    @Test
    public void testFormData_withRadioNotChecked() {
        Element radio = new Element(Tag.valueOf("input"), formElement.baseUri());
        radio.attr("type", "radio");
        radio.attr("name", "category");
        radio.attr("value", "1");
        formElement.addElement(radio);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_withTextarea() {
        Element textarea = new Element(Tag.valueOf("textarea"), formElement.baseUri());
        textarea.attr("name", "comment");
        textarea.text("This is a test comment");
        formElement.addElement(textarea);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("comment", formData.get(0).key());
        assertEquals("This is a test comment", formData.get(0).value());
    }

}