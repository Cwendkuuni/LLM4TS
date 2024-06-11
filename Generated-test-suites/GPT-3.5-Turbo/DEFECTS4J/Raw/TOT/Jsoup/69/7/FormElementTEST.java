import org.junit.jupiter.api.Test;

import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FormElementTest {

    @Test
    public void testFormElement_Initialization() {
        FormElement formElement = new FormElement(Tag.form(), "http://www.example.com", new Attributes());
        
        assertEquals(Tag.form(), formElement.tag());
        assertEquals("http://www.example.com", formElement.baseUri());
        assertTrue(formElement.attributes().isEmpty());
    }

    @Test
    public void testFormElement_AddElement() {
        FormElement formElement = new FormElement(Tag.form(), "http://www.example.com", new Attributes());
        Element element = new Element(Tag.input(), "http://www.example.com", new Attributes());
        
        formElement.addElement(element);
        
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testFormElement_Submit_WithAction() {
        FormElement formElement = new FormElement(Tag.form(), "http://www.example.com", new Attributes());
        formElement.attr("action", "http://www.submit.com");
        formElement.attr("method", "POST");
        
        Connection connection = formElement.submit();
        
        assertNotNull(connection);
        assertEquals("http://www.submit.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormElement_Submit_WithoutAction() {
        FormElement formElement = new FormElement(Tag.form(), "http://www.example.com", new Attributes());
        formElement.attr("method", "GET");
        
        assertThrows(IllegalArgumentException.class, () -> {
            formElement.submit();
        });
    }

    @Test
    public void testFormElement_FormData() {
        FormElement formElement = new FormElement(Tag.form(), "http://www.example.com", new Attributes());
        Element inputElement = new Element(Tag.input(), "http://www.example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("type", "text");
        inputElement.val("John");
        formElement.addElement(inputElement);
        Element checkboxElement = new Element(Tag.input(), "http://www.example.com", new Attributes());
        checkboxElement.attr("name", "subscribe");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
        assertEquals("subscribe", formData.get(1).key());
        assertEquals("on", formData.get(1).value());
    }
}