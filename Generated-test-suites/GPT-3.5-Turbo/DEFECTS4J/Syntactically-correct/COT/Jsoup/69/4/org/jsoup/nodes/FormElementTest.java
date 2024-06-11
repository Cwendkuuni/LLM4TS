import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com");
        FormElement updatedFormElement = formElement.addElement(element);
        Elements elements = updatedFormElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertSame(element, elements.first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmitWithGetMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes().put("method", "get"));
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testSubmitWithPostMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes().put("method", "post"));
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com").attr("name", "name").val("John");
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com").attr("name", "gender");
        Element optionElement1 = new Element(Tag.valueOf("option"), "http://example.com").attr("value", "male").attr("selected", "");
        Element optionElement2 = new Element(Tag.valueOf("option"), "http://example.com").attr("value", "female");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        formElement.addElement(inputElement).addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        Connection.KeyVal formData1 = formData.get(0);
        Connection.KeyVal formData2 = formData.get(1);
        assertEquals("name", formData1.key());
        assertEquals("John", formData1.value());
        assertEquals("gender", formData2.key());
        assertEquals("male", formData2.value());
    }
}