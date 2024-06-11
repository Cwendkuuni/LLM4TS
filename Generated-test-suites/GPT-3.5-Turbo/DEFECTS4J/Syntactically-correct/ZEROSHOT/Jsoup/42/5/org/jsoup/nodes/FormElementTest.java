import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        assertEquals(0, formElement.elements().size());

        Element element1 = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        formElement.addElement(element1);

        assertEquals(1, formElement.elements().size());

        Element element2 = new Element(Tag.valueOf("textarea"), "http://www.example.com", new Attributes());
        formElement.addElement(element2);

        assertEquals(2, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        formElement.attr("action", "/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://www.example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        Element element1 = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        element1.attr("name", "username");
        element1.attr("value", "test");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        element2.attr("name", "password");
        element2.attr("value", "test123");
        formElement.addElement(element2);

        Element element3 = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        element3.attr("name", "country");
        Element option = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        option.attr("selected", "");
        option.attr("value", "US");
        option.text("United States");
        element3.appendChild(option);
        formElement.addElement(element3);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(3, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("username", keyVal1.key());
        assertEquals("test", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("password", keyVal2.key());
        assertEquals("test123", keyVal2.value());

        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("country", keyVal3.key());
        assertEquals("US", keyVal3.value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        assertTrue(formElement1.equals(formElement2));
    }
}