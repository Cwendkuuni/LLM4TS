import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.jsoup.helper.HttpConnection;
import org.jsoup.Connection;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        FormElement formElement = new FormElement(tag, baseUri, new Attributes());
        assertNotNull(formElement);
        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
    }

    @Test
    public void testElementsMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElementMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testRemoveChildMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmitMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodNoAction() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormDataMethod() {
        Tag tag = Tag.valueOf("form");
        FormElement formElement = new FormElement(tag, "http://example.com", new Attributes());
        Element textInput = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        textInput.attr("name", "text");
        textInput.val("example");
        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "check");
        checkbox.attr("checked", "");
        formElement.addElement(textInput);
        formElement.addElement(checkbox);

        assertEquals(2, formElement.formData().size());
    }

}