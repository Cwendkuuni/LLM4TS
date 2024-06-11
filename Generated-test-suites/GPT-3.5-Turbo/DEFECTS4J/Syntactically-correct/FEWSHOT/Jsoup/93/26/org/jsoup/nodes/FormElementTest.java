import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        assertNotNull(formElement.elements());
        assertFalse(formElement.elements().isEmpty());
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        assertNotNull(formElement.elements());
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormDataWithTextInput() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "username");
        element.attr("value", "johnDoe");
        formElement.addElement(element);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("johnDoe", data.get(0).value());
    }

    @Test
    public void testFormDataWithDisabledInput() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "email");
        element.attr("value", "test@example.com");
        element.attr("disabled", "disabled");
        formElement.addElement(element);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormDataWithSelect() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "gender");

        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "male");
        option1.attr("selected", "selected");

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "female");

        selectElement.appendChild(option1);
        selectElement.appendChild(option2);

        formElement.addElement(selectElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("gender", data.get(0).key());
        assertEquals("male", data.get(0).value());
    }

    @Test
    public void testFormDataWithCheckbox() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "subscribe");
        checkboxElement.attr("checked", "checked");

        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("subscribe", data.get(0).key());
        assertEquals("on", data.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxUnchecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "subscribe");

        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void testFormDataWithRadio() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "gender");
        radioElement.attr("checked", "checked");
        radioElement.attr("value", "male");

        formElement.addElement(radioElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("gender", data.get(0).key());
        assertEquals("male", data.get(0).value());
    }

    @Test
    public void testFormDataWithRadioUnchecked() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "gender");
        radioElement.attr("value", "male");

        formElement.addElement(radioElement);
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

}