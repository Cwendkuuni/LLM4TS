import org.jsoup.nodes.*; 
import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        
        formElement.addElement(element);
        
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        
        Connection connection = formElement.submit();

        assertNotNull(connection);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(formData.size(), connection.request().data().size());
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.val("inputValue");

        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("inputName", formData.get(0).key());
        assertEquals("inputValue", formData.get(0).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        
        formElement.submit();
    }

    @Test
    public void testSubmitWithPostMethod() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        attributes.put("method", "post");
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        
        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
    }

}