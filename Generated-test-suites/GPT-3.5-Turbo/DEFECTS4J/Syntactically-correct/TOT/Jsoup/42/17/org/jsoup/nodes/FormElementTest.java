import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private static final String BASE_URI = "http://example.com";

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), BASE_URI, new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), BASE_URI, new Attributes());
        Element element = new Element(Tag.valueOf("input"), BASE_URI, new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoBaseUri() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmitGetMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), BASE_URI, new Attributes().put("method", "get"));
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitPostMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), BASE_URI, new Attributes().put("method", "post"));
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), BASE_URI, new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), BASE_URI, new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.val("inputValue");
        formElement.addElement(inputElement);
        Element selectElement = new Element(Tag.valueOf("select"), BASE_URI, new Attributes());
        selectElement.attr("name", "selectName");
        Element optionElement = new Element(Tag.valueOf("option"), BASE_URI, new Attributes().put("selected", ""));
        optionElement.val("optionValue");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals(HttpConnection.KeyVal.create("inputName", "inputValue"), formData.get(0));
        assertEquals(HttpConnection.KeyVal.create("selectName", "optionValue"), formData.get(1));
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), BASE_URI, new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), BASE_URI, new Attributes());
        FormElement formElement3 = new FormElement(Tag.valueOf("input"), BASE_URI, new Attributes());
        assertTrue(formElement1.equals(formElement2));
        assertFalse(formElement1.equals(formElement3));
    }
}