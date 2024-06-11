import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
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
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        Elements elements = updatedFormElement.elements();
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }

    @Test
    public void testEquals() {
        FormElement otherFormElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        boolean equal = formElement.equals(otherFormElement);
        assertTrue(equal);
    }

    // Additional test cases for edge cases and exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitEmptyAction() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testSubmitPostMethod() {
        formElement.attr("method", "POST");
        Connection.Method expectedMethod = Connection.Method.POST;
        Connection connection = formElement.submit();
        assertEquals(expectedMethod, connection.method());
    }

    @Test
    public void testSubmitGetMethod() {
        formElement.attr("method", "GET");
        Connection.Method expectedMethod = Connection.Method.GET;
        Connection connection = formElement.submit();
        assertEquals(expectedMethod, connection.method());
    }

    @Test
    public void testSubmitInvalidMethod() {
        formElement.attr("method", "INVALID");
        Connection.Method expectedMethod = Connection.Method.GET;
        Connection connection = formElement.submit();
        assertEquals(expectedMethod, connection.method());
    }

    @Test
    public void testFormDataSelect() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("selected", "selected");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertFalse(formData.isEmpty());
    }

    @Test
    public void testFormDataCheckboxChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("value", "value");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertFalse(formData.isEmpty());
    }

    @Test
    public void testFormDataCheckboxUnchecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("value", "value");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataRadioChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "checked");
        radioElement.attr("name", "radio");
        radioElement.attr("value", "value");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertFalse(formData.isEmpty());
    }

    @Test
    public void testFormDataRadioUnchecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio");
        radioElement.attr("value", "value");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }
}