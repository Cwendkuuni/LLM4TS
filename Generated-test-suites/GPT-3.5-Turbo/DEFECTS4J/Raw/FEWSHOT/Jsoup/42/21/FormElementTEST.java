import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());

        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);
        elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("textarea"), formElement.baseUri(), new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(formElement, result);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }

    @Test
    public void testSubmit() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");

        String absoluteActionUrl = "https://www.example.com/submit";
        String baseUri = formElement.baseUri();

        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(Jsoup.connect(absoluteActionUrl)).thenReturn(connectionMock);
        Mockito.when(connectionMock.data(Mockito.any())).thenReturn(connectionMock);

        Connection result = formElement.submit();

        Mockito.verify(Jsoup).connect(absoluteActionUrl);
        Mockito.verify(connectionMock).data(formElement.formData());
        Mockito.verify(connectionMock).method(Connection.Method.POST);
        assertEquals(connectionMock, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_invalidActionUrl() {
        formElement.attr("action", "");
        formElement.attr("method", "POST");

        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("name", "username").put("value", "john.doe"));
        Element textareaElement = new Element(Tag.valueOf("textarea"), formElement.baseUri(), new Attributes().put("name", "message").put("value", "Hello World"));
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes().put("name", "gender"));
        Element optionElement = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes().put("value", "male").put("selected", ""));
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "checkbox").put("name", "newsletter").put("value", "true").put("checked", ""));
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "radio").put("name", "color").put("value", "red").put("checked", ""));
        Element ignoredElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "hidden").put("name", "ignore").put("value", "ignored"));

        formElement.addElement(inputElement);
        formElement.addElement(textareaElement);
        formElement.addElement(selectElement);
        selectElement.appendChild(optionElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(radioElement);
        formElement.addElement(ignoredElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(5, formData.size());
        assertEquals(HttpConnection.KeyVal.create("username", "john.doe"), formData.get(0));
        assertEquals(HttpConnection.KeyVal.create("message", "Hello World"), formData.get(1));
        assertEquals(HttpConnection.KeyVal.create("gender", "male"), formData.get(2));
        assertEquals(HttpConnection.KeyVal.create("newsletter", "true"), formData.get(3));
        assertEquals(HttpConnection.KeyVal.create("color", "red"), formData.get(4));
    }

    @Test
    public void testEquals() {
        assertFalse(formElement.equals(null));
        assertTrue(formElement.equals(formElement));
        assertFalse(formElement.equals(new Object()));
    }
}