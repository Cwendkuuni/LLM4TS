import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement formElement;
    private Element textInput;
    private Element checkboxInput;
    private Element selectInput;
    private Element option;

    @Before
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());

        textInput = new Element(Tag.valueOf("input"), "", new Attributes());
        textInput.attr("name", "username");
        textInput.attr("type", "text");
        textInput.val("john");

        checkboxInput = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxInput.attr("name", "notifications");
        checkboxInput.attr("type", "checkbox");
        checkboxInput.attr("checked", "checked");

        selectInput = new Element(Tag.valueOf("select"), "", new Attributes());
        selectInput.attr("name", "gender");

        option = new Element(Tag.valueOf("option"), "", new Attributes());
        option.val("male");
        option.attr("selected", "selected");
        selectInput.appendChild(option);
    }

    @Test
    public void testAddElement() {
        formElement.addElement(textInput);

        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(textInput));
    }

    @Test
    public void testSubmit() {
        formElement.addElement(textInput);
        formElement.addElement(checkboxInput);
        formElement.addElement(selectInput);

        Connection connection = formElement.submit();

        assertEquals("", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.method());
        assertFalse(connection.data().isEmpty());
    }

    @Test
    public void testFormData() {
        formElement.addElement(textInput);
        formElement.addElement(checkboxInput);
        formElement.addElement(selectInput);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(3, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("username", keyVal1.key());
        assertEquals("john", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("notifications", keyVal2.key());
        assertEquals("on", keyVal2.value());

        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("gender", keyVal3.key());
        assertEquals("male", keyVal3.value());
    }
}