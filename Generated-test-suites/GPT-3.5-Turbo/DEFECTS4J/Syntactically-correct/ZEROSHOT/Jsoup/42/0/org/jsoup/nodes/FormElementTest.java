import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, "https://example.com", attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(formElement, result);
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().first());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(formElement.absUrl("action"), connection.request().url());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_missingActionUrl() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        element1.attr("name", "input1");
        element1.val("value1");
        
        Element element2 = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        element2.attr("name", "input2");
        element2.val("value2");

        Element element3 = new Element(Tag.valueOf("select"), "https://example.com", new Attributes());
        element3.attr("name", "select1");
        
        Element option1 = new Element(Tag.valueOf("option"), "https://example.com", new Attributes());
        option1.attr("selected", "selected");
        option1.val("optionValue1");
        
        element3.appendChild(option1);
        
        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(3, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("input2", formData.get(1).key());
        assertEquals("value2", formData.get(1).value());
        assertEquals("select1", formData.get(2).key());
        assertEquals("optionValue1", formData.get(2).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        assertTrue(formElement.equals(formElement2));

        formElement2.attr("class", "form-element");
        assertFalse(formElement.equals(formElement2));
    }
}