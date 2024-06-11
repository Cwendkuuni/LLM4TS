import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(formElement, result);
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "invalid");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    @Test
    public void testFormDataEmpty() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(0, data.size());
    }

    @Test
    public void testFormDataWithInput() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "john");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("john", data.get(0).value());
    }

    @Test
    public void testFormDataWithSelect() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("value", "1");
        optionElement.attr("selected", "");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        assertEquals("1", data.get(0).value());
    }

    @Test
    public void testFormDataWithDisabledInput() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "john");
        inputElement.attr("disabled", "");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(0, data.size());
    }

    @Test
    public void testFormDataWithCheckboxChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        assertEquals("on", data.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxUnchecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("type", "checkbox");
        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(0, data.size());
    }

    @Test
    public void testFormDataWithRadioChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("name", "gender");
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "");
        formElement.addElement(radioElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(1, data.size());
        assertEquals("on", data.get(0).value());
    }

    @Test
    public void testFormDataWithRadioUnchecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("name", "gender");
        radioElement.attr("type", "radio");
        formElement.addElement(radioElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(0, data.size());
    }
}