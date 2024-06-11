import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(new Tag("form"), "http://www.example.com", new Attributes());
    }

    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element element = mock(Element.class);
        assertEquals(formElement, formElement.addElement(element));
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = mock(Connection.class);
        Jsoup jsoup = mock(Jsoup.class);
        when(jsoup.connect(anyString())).thenReturn(connection);
        when(connection.data(anyList())).thenReturn(connection);
        when(connection.method(any())).thenReturn(connection);

        formElement.addElement(new Element(new Tag("input"), "http://www.example.com", new Attributes()));
        Connection result = formElement.submit();

        assertNotNull(result);
        verify(jsoup).connect(anyString());
        verify(connection).data(anyList());
        verify(connection).method(any());
    }

    @Test
    public void testSubmit_noAction() {
        Connection connection = mock(Connection.class);
        Jsoup jsoup = mock(Jsoup.class);
        when(jsoup.connect(anyString())).thenReturn(connection);
        when(connection.data(anyList())).thenReturn(connection);
        when(connection.method(any())).thenReturn(connection);

        Connection result = formElement.submit();

        assertNotNull(result);
        verify(jsoup).connect(anyString());
        verify(connection).data(anyList());
        verify(connection).method(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_invalidAction() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(new Tag("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "input_name");
        inputElement.attr("value", "input_value");
        inputElement.attr("type", "text");

        Element selectElement = new Element(new Tag("select"), "http://www.example.com", new Attributes());
        Element optionElement = new Element(new Tag("option"), "http://www.example.com", new Attributes());
        optionElement.attr("selected", "");
        optionElement.attr("value", "option_value");
        selectElement.appendChild(optionElement);
        selectElement.attr("name", "select_name");

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        List<Connection.KeyVal> result = formElement.formData();

        assertEquals(2, result.size());
        assertEquals("input_name", result.get(0).key());
        assertEquals("input_value", result.get(0).value());
        assertEquals("select_name", result.get(1).key());
        assertEquals("option_value", result.get(1).value());
    }

    @Test
    public void testFormData_checkboxChecked() {
        Element checkboxElement = new Element(new Tag("input"), "http://www.example.com", new Attributes());
        checkboxElement.attr("name", "checkbox_name");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");

        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> result = formElement.formData();

        assertEquals(1, result.size());
        assertEquals("checkbox_name", result.get(0).key());
        assertEquals("on", result.get(0).value());
    }

    @Test
    public void testFormData_checkboxNotChecked() {
        Element checkboxElement = new Element(new Tag("input"), "http://www.example.com", new Attributes());
        checkboxElement.attr("name", "checkbox_name");
        checkboxElement.attr("type", "checkbox");

        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> result = formElement.formData();

        assertEquals(0, result.size());
    }

    @Test
    public void testFormData_radioChecked() {
        Element radioElement = new Element(new Tag("input"), "http://www.example.com", new Attributes());
        radioElement.attr("name", "radio_name");
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "");

        formElement.addElement(radioElement);
        List<Connection.KeyVal> result = formElement.formData();

        assertEquals(1, result.size());
        assertEquals("radio_name", result.get(0).key());
        assertEquals("on", result.get(0).value());
    }

    @Test
    public void testFormData_radioNotChecked() {
        Element radioElement = new Element(new Tag("input"), "http://www.example.com", new Attributes());
        radioElement.attr("name", "radio_name");
        radioElement.attr("type", "radio");

        formElement.addElement(radioElement);
        List<Connection.KeyVal> result = formElement.formData();

        assertEquals(0, result.size());
    }
}