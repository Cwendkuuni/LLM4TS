import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import org.jsoup.helper.Attributes;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testElementsMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        
        assertNotNull(elements);
    }

    @Test
    public void testAddElementMethod() {
        Tag tag = Tag.valueOf("input");
        Element element = new Element(tag, "http://example.com", new Attributes());
        
        Tag formTag = Tag.valueOf("form");
        FormElement formElement = new FormElement(formTag, "http://example.com", new Attributes());
        formElement.addElement(element);
        
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testSubmitMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        
        Connection connection = formElement.submit();
        
        assertNotNull(connection);
    }

    @Test
    public void testFormDataMethod() {
        Tag inputTag = Tag.valueOf("input");
        Element inputElement = new Element(inputTag, "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john_doe");
        
        Tag selectTag = Tag.valueOf("select");
        Element selectElement = new Element(selectTag, "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.val("1");
        selectElement.appendChild(optionElement);
        
        Tag formTag = Tag.valueOf("form");
        FormElement formElement = new FormElement(formTag, "http://example.com", new Attributes());
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john_doe", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("1", formData.get(1).value());
    }
}