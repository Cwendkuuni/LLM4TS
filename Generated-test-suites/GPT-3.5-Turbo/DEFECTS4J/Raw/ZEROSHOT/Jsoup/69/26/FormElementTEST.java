import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class FormElementTest {
  
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = mock(Element.class);

        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = mock(Connection.class);
        Validate.notEmpty(formElement.absUrl("action"), "Could not determine a form action URL for submit. Ensure you set a base URI when parsing.");
        String method = formElement.attr("method").toUpperCase().equals("POST") ? "POST" : "GET";
        Jsoup jsoup = mock(Jsoup.class);
        when(jsoup.connect(formElement.absUrl("action"))).thenReturn(connection);

        formElement.submit();

        verify(connection).data(formElement.formData());
        verify(connection).method(Connection.Method.valueOf(method));
    }

    @Test
    public void testFormData() {
        Element element = mock(Element.class);
        when(element.tagName()).thenReturn("input");
        when(element.attr("type")).thenReturn("text");
        when(element.hasAttr("disabled")).thenReturn(false);
        when(element.attr("name")).thenReturn("username");
        when(element.val()).thenReturn("john_doe");

        formElement.addElement(element);

        ArrayList<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("username", "john_doe"));

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(expectedData, formData);
    }
}