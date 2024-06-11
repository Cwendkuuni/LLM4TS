import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
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
        Element element = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());

        FormElement result = formElement.addElement(element);

        assertEquals(formElement, result);
        assertTrue(formElement.elements().contains(element));

        Elements elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);

        assertFalse(formElement.elements().contains(element));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyActionAttribute() {
        Connection connection = formElement.submit();
    }

    @Test
    public void testSubmitWithPostMethod() {
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(HttpConnection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitWithGetMethod() {
        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(HttpConnection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormDataWithEmptyElements() {
        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithDisabledFormInput() {
        Element element = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        element.attr("disabled", "disabled");
        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithEmptyNameAttribute() {
        Element element = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        element.attr("name", "");
        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithSelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), "https://www.example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "https://www.example.com", new Attributes());
        optionElement.attr("selected", "selected");
        selectElement.addElement(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(HttpConnection.KeyVal.create("", optionElement.val()), formData.get(0));
    }

    @Test
    public void testFormDataWithCheckboxElement() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(HttpConnection.KeyVal.create("checkbox", checkboxElement.val()), formData.get(0));
    }

    @Test
    public void testFormDataWithRadioElement() {
        Element radioElement = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio");
        radioElement.attr("checked", "checked");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(HttpConnection.KeyVal.create("radio", radioElement.val()), formData.get(0));
    }

    @Test
    public void testFormDataWithTextElement() {
        Element textElement = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        textElement.attr("type", "text");
        textElement.attr("name", "text");
        textElement.attr("value", "Sample Text");
        formElement.addElement(textElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(HttpConnection.KeyVal.create("text", textElement.val()), formData.get(0));
    }

}