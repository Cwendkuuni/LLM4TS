
import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Elements elements = form.elements();

        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Element element = new Element(Tag.valueOf("input"), baseUri);
        form.addElement(element);

        Elements elements = form.elements();

        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Connection connection = form.submit();

        assertNotNull(connection);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSubmit_Exception() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        form.submit();
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);
        
        List<Connection.KeyVal> formData = form.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_SelectWithTag() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Element selectElement = new Element(Tag.valueOf("select"), baseUri);
        selectElement.attr("name", "colors");
        Element optionElement = new Element(Tag.valueOf("option"), baseUri);
        optionElement.attr("selected", "");
        optionElement.text("Red");
        selectElement.appendChild(optionElement);
        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();

        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("colors", formData.get(0).key());
        assertEquals("Red", formData.get(0).value());
    }

    @Test
    public void testFormData_SelectWithoutTag() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Element selectElement = new Element(Tag.valueOf("select"), baseUri);
        selectElement.attr("name", "colors");
        Element optionElement = new Element(Tag.valueOf("option"), baseUri);
        optionElement.text("Red");
        selectElement.appendChild(optionElement);
        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();

        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("colors", formData.get(0).key());
        assertEquals("Red", formData.get(0).value());
    }

    @Test
    public void testFormData_Checkbox() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Element checkboxElement = new Element(Tag.valueOf("input"), baseUri);
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("checked", "");
        checkboxElement.attr("value", "yes");
        form.addElement(checkboxElement);

        List<Connection.KeyVal> formData = form.formData();

        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("agree", formData.get(0).key());
        assertEquals("yes", formData.get(0).value());
    }

    @Test
    public void testFormData_Radio() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Element radioElement = new Element(Tag.valueOf("input"), baseUri);
        radioElement.attr("type", "radio");
        radioElement.attr("name", "agree");
        radioElement.attr("checked", "");
        radioElement.attr("value", "yes");
        form.addElement(radioElement);

        List<Connection.KeyVal> formData = form.formData();

        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("agree", formData.get(0).key());
        assertEquals("yes", formData.get(0).value());
    }

    @Test
    public void testFormData_OtherInput() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Element inputElement = new Element(Tag.valueOf("input"), baseUri);
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.attr("value", "John");
        form.addElement(inputElement);

        List<Connection.KeyVal> formData = form.formData();

        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
    }
}

