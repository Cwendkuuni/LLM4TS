import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements_Empty() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement updatedForm = formElement.addElement(element);
        Elements elements = updatedForm.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }

    @Test
    public void testSubmit_NoAction() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertNull(connection.request().url().toString());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_NoBaseUrl() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        FormElement formElementNoBaseUrl = new FormElement(tag, "", attributes);
        formElementNoBaseUrl.submit();
    }

    @Test
    public void testSubmit_PostMethod() {
        formElement.attr("method", "POST");
        formElement.attr("action", "http://example.com");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmit_GetMethod() {
        formElement.attr("method", "GET");
        formElement.attr("action", "http://example.com");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testFormData_SelectWithOptionSelected() {
        Tag selectTag = Tag.valueOf("select");
        Attributes selectAttributes = new Attributes();
        Element selectElement = new Element(selectTag, "http://example.com", selectAttributes);
        selectElement.attr("name", "color");
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement1.attr("value", "red");
        optionElement1.attr("selected", "selected");
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement2.attr("value", "blue");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);

        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("color", formData.get(0).key());
        assertEquals("red", formData.get(0).value());
    }

    @Test
    public void testFormData_SelectWithoutOptionSelected() {
        Tag selectTag = Tag.valueOf("select");
        Attributes selectAttributes = new Attributes();
        Element selectElement = new Element(selectTag, "http://example.com", selectAttributes);
        selectElement.attr("name", "color");

        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("color", formData.get(0).key());
        assertEquals("", formData.get(0).value());
    }

    @Test
    public void testFormData_CheckboxChecked() {
        Tag checkboxTag = Tag.valueOf("input");
        Attributes checkboxAttributes = new Attributes();
        Element checkboxElement = new Element(checkboxTag, "http://example.com", checkboxAttributes);
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("checked", "checked");
        checkboxElement.val("true");

        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("agree", formData.get(0).key());
        assertEquals("true", formData.get(0).value());
    }

    @Test
    public void testFormData_CheckboxUnchecked() {
        Tag checkboxTag = Tag.valueOf("input");
        Attributes checkboxAttributes = new Attributes();
        Element checkboxElement = new Element(checkboxTag, "http://example.com", checkboxAttributes);
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "agree");

        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_RadioChecked() {
        Tag radioTag = Tag.valueOf("input");
        Attributes radioAttributes = new Attributes();
        Element radioElement = new Element(radioTag, "http://example.com", radioAttributes);
        radioElement.attr("type", "radio");
        radioElement.attr("name", "gender");
        radioElement.attr("checked", "checked");
        radioElement.val("male");

        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("gender", formData.get(0).key());
        assertEquals("male", formData.get(0).value());
    }

    @Test
    public void testFormData_RadioUnchecked() {
        Tag radioTag = Tag.valueOf("input");
        Attributes radioAttributes = new Attributes();
        Element radioElement = new Element(radioTag, "http://example.com", radioAttributes);
        radioElement.attr("type", "radio");
        radioElement.attr("name", "gender");

        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_InputElementWithValue() {
        Tag inputTag = Tag.valueOf("input");
        Attributes inputAttributes = new Attributes();
        Element inputElement = new Element(inputTag, "http://example.com", inputAttributes);
        inputElement.attr("name", "username");
        inputElement.attr("value", "john");

        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
    }

    @Test
    public void testFormData_InputElementWithoutValue() {
        Tag inputTag = Tag.valueOf("input");
        Attributes inputAttributes = new Attributes();
        Element inputElement = new Element(inputTag, "http://example.com", inputAttributes);
        inputElement.attr("name", "username");

        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(formElement.equals(formElement));
    }

    @Test
    public void testEquals_DifferentObject() {
        assertFalse(formElement.equals(new Object()));
    }

    @Test
    public void testEquals_Null() {
        assertFalse(formElement.equals(null));
    }

}