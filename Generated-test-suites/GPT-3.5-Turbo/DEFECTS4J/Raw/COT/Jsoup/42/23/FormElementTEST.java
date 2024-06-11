
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
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
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();

        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());

        FormElement result = formElement.addElement(element);

        assertEquals(formElement, result);
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testSubmit_success() {
        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_invalidAction() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testFormData_noFormControlElements() {
        formElement.addElement(new Element(Tag.valueOf("div"), "", new Attributes()));
        formElement.addElement(new Element(Tag.valueOf("span"), "", new Attributes()));

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormData_selectElement_noSelectedOption() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormData_selectElement_withSelectedOption() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.appendElement("option").attr("selected", "selected").text("Option 1");
        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals("Option 1", data.get(0).value());
    }

    @Test
    public void testFormData_checkboxElement_notChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormData_checkboxElement_checked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals("", data.get(0).value());
    }

    @Test
    public void testFormData_radioElement_notChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormData_radioElement_checked() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "checked");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals("", data.get(0).value());
    }

    @Test
    public void testFormData_textElement() {
        Element textElement = new Element(Tag.valueOf("input"), "", new Attributes());
        textElement.attr("type", "text");
        textElement.attr("name", "input_name");
        textElement.val("Input Value");
        formElement.addElement(textElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals("Input Value", data.get(0).value());
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(formElement.equals(formElement));
    }

    @Test
    public void testEquals_nullObject() {
        assertFalse(formElement.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(formElement.equals(new Object()));
    }

    @Test
    public void testEquals_differentTag() {
        Tag tag = Tag.valueOf("div");
        FormElement differentElement = new FormElement(tag, "", new Attributes());

        assertFalse(formElement.equals(differentElement));
    }

    @Test
    public void testEquals_sameTag() {
        FormElement sameTagElement = new FormElement(Tag.valueOf("form"), "", new Attributes());

        assertTrue(formElement.equals(sameTagElement));
    }
}

