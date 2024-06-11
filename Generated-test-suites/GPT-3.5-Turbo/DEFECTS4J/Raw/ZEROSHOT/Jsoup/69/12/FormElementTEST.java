import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Attributes attributes = new Attributes();
        Tag tag = Tag.valueOf("form");
        formElement = new FormElement(tag, "http://example.com", attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element elementMock = Mockito.mock(Element.class);
        formElement.addElement(elementMock);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(elementMock, elements.first());
    }

    @Test
    public void testSubmit() {
        Connection connectionMock = Mockito.mock(Connection.class);
        Jsoup jsoupMock = Mockito.mock(Jsoup.class);

        String actionUrl = "http://example.com/action";
        String method = "POST";
        String baseUri = "http://example.com";

        when(formElement.hasAttr("action")).thenReturn(true);
        when(formElement.absUrl("action")).thenReturn(actionUrl);
        when(formElement.baseUri()).thenReturn(baseUri);
        when(formElement.attr("method")).thenReturn(method.toUpperCase());
        when(Jsoup.connect(actionUrl)).thenReturn(connectionMock);

        Connection submittedConnection = formElement.submit();
        
        assertEquals(connectionMock, submittedConnection);
    }

    @Test
    public void testFormData() {
        Element elementMock = Mockito.mock(Element.class);
        when(elementMock.tag()).thenReturn(Tag.valueOf("input"));
        when(elementMock.hasAttr("disabled")).thenReturn(false);
        when(elementMock.attr("name")).thenReturn("inputName");
        when(elementMock.attr("type")).thenReturn("text");
        when(elementMock.val()).thenReturn("inputValue");

        formElement.addElement(elementMock);

        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("inputValue", keyVal.value());
    }
}