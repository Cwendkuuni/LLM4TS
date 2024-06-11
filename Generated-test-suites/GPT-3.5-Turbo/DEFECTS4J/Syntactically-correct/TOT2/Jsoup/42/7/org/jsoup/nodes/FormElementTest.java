import org.jsoup.nodes.*;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertNotNull(formElement);
    }

    @Test
    public void testElementsMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElementMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element element = new Element(Tag.valueOf("input"), baseUri, attributes);
        formElement.addElement(element);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmitMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithInvalidActionUrl() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        formElement.submit();
    }

    @Test
    public void testFormDataMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }

    @Test
    public void testEqualsMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement1 = new FormElement(tag, baseUri, attributes);
        FormElement formElement2 = new FormElement(tag, baseUri, attributes);

        assertTrue(formElement1.equals(formElement2));
    }
}