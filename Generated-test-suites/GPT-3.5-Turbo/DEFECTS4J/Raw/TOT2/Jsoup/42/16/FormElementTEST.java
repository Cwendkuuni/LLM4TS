import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.select.Elements;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        String baseUri = "http://example.com";
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElementsMethod() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElementMethod() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
    }

    @Test
    public void testSubmitMethod() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithMissingActionUrl() {
        // Expecting IllegalArgumentException as the action URL is missing
        FormElement formElementWithoutAction = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElementWithoutAction.submit();
    }

    @Test
    public void testFormDataMethod() {
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
    }

    // Add more test cases for edge cases and error scenarios if necessary

}