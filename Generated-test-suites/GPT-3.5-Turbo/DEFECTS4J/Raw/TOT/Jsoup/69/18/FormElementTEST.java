
import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);

        assertTrue(formElement.elements().contains(element));
        assertEquals(1, formElement.elements().size());
        assertEquals(formElement, result);
    }

    @Test
    public void testSubmit() {
        FormElement formElementWithAction = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes().put("action", "http://www.example.com/action"));
        formElementWithAction.attr("method", "post");

        Connection connection = formElementWithAction.submit();

        assertNotNull(connection);
        assertEquals("http://www.example.com/action", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionURL() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input1").put("value", "value1"));
        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input2").put("value", "value2"));
        Element disabledElement = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "disabledInput").put("disabled", ""));
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes().put("name", "select"));
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes().put("value", "option1").put("selected", ""));
        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes().put("value", "option2"));

        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(disabledElement);
        formElement.addElement(selectElement);
        selectElement.addElement(option1);
        selectElement.addElement(option2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(4, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("input1", "value1")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("input2", "value2")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("select", "option1")));
        assertFalse(formData.contains(HttpConnection.KeyVal.create("disabledInput", "")));
    }
}
