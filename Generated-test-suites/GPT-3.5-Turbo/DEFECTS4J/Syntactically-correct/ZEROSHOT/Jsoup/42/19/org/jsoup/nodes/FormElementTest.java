import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element mockElement = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(mockElement);
        assertEquals(formElement, result);
        assertTrue(formElement.elements().contains(mockElement));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmitWithGetAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes().put("action", "https://www.example.com"));
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitWithPostAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes().put("action", "https://www.example.com").put("method", "POST"));
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element mockElement1 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "name1").put("type", "text").put("value", "value1"));
        Element mockElement2 = new Element(Tag.valueOf("select"), "", new Attributes().put("name", "name2"));
        Element mockElement3 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "name3").put("type", "checkbox").put("checked", ""));
        formElement.addElement(mockElement1);
        formElement.addElement(mockElement2);
        formElement.addElement(mockElement3);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
        assertEquals("name1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("name3", formData.get(2).key());
        assertNull(formData.get(2).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "", new Attributes().put("action", "https://www.example.com"));
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "", new Attributes().put("action", "https://www.example.com"));
        assertTrue(formElement1.equals(formElement2));
    }
}