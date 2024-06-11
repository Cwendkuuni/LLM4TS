import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        FormElement newFormElement = formElement.addElement(element);
        assertEquals(1, newFormElement.elements().size());
        assertEquals(element, newFormElement.elements().first());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(formElement.absUrl("action"), connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitEmptyAction() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testSubmitMethodPost() {
        formElement.attr("method", "post");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitMethodGet() {
        formElement.attr("method", "get");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.attr("value", "inputValue");
        inputElement.attr("type", "text");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        selectElement.attr("name", "selectName");
        Element optionElement = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        optionElement.attr("value", "optionValue");
        selectElement.addElement(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("inputName", formData.get(0).key());
        assertEquals("inputValue", formData.get(0).value());
        assertEquals("selectName", formData.get(1).key());
        assertEquals("optionValue", formData.get(1).value());
    }

    @Test
    public void testFormDataCheckboxChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        checkboxElement.attr("name", "checkboxName");
        checkboxElement.attr("value", "checkboxValue");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("checkboxName", formData.get(0).key());
        assertEquals("checkboxValue", formData.get(0).value());
    }

    @Test
    public void testFormDataCheckboxNotChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        checkboxElement.attr("name", "checkboxName");
        checkboxElement.attr("value", "checkboxValue");
        checkboxElement.attr("type", "checkbox");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataRadioChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        radioElement.attr("name", "radioName");
        radioElement.attr("value", "radioValue");
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("radioName", formData.get(0).key());
        assertEquals("radioValue", formData.get(0).value());
    }

    @Test
    public void testFormDataRadioNotChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        radioElement.attr("name", "radioName");
        radioElement.attr("value", "radioValue");
        radioElement.attr("type", "radio");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSkipDisabled() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.attr("value", "inputValue");
        inputElement.attr("type", "text");
        inputElement.attr("disabled", "");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataEmptyName() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("value", "inputValue");
        inputElement.attr("type", "text");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }
}