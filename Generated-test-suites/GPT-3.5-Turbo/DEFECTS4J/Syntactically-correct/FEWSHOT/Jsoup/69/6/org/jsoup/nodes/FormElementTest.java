import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    @InjectMocks
    private FormElement formElement;

    @Mock
    private Elements elementsMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testElements() {
        formElement.addElement(mock(Element.class));
        assertEquals(elementsMock, formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element elementMock = mock(Element.class);
        assertEquals(formElement, formElement.addElement(elementMock));
        verify(elementsMock).add(elementMock);
    }

    @Test
    public void testSubmit() {
        Connection connectionMock = mock(Connection.class);
        
        formElement.attr("action", "http://example.com");
        formElement.attr("method", "POST");
        
        when(formElement.absUrl("action")).thenReturn("http://example.com");
        when(formElement.hasAttr("action")).thenReturn(true);
        when(formElement.baseUri()).thenReturn("http://example.com");
        when(formElement.attr("method")).thenReturn("POST");
        when(Jsoup.connect("http://example.com")).thenReturn(connectionMock);
        
        Connection.Method expectedMethod = Connection.Method.POST;
        
        assertEquals(connectionMock, formElement.submit());
        verify(connectionMock).data(formElement.formData());
        verify(connectionMock).method(expectedMethod);
    }

    @Test
    public void testSubmitWithoutActionAttribute() {
        Connection connectionMock = mock(Connection.class);
        
        formElement.attr("method", "GET");
        
        when(formElement.absUrl("action")).thenReturn("");
        when(formElement.hasAttr("action")).thenReturn(false);
        when(formElement.baseUri()).thenReturn("http://example.com");
        when(formElement.attr("method")).thenReturn("GET");
        when(Jsoup.connect("http://example.com")).thenReturn(connectionMock);
        
        Connection.Method expectedMethod = Connection.Method.GET;
        
        assertEquals(connectionMock, formElement.submit());
        verify(connectionMock).data(formElement.formData());
        verify(connectionMock).method(expectedMethod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        formElement.attr("action", "http://example.com");
        
        when(formElement.hasAttr("action")).thenReturn(true);
        when(formElement.baseUri()).thenReturn("");
        
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);
        Element element3 = mock(Element.class);
        
        Elements optionsMock = mock(Elements.class);
        Elements selectMock = mock(Elements.class);
        Elements inputMock = mock(Elements.class);
        
        ArrayList<Connection.KeyVal> expectedData = new ArrayList<>();
        
        when(element1.tag()).thenReturn(Tag.valueOf("select"));
        when(element1.attr("name")).thenReturn("select1");
        when(element1.select("option[selected]")).thenReturn(optionsMock);
        when(element1.select("option")).thenReturn(optionsMock);
        
        when(element2.tag()).thenReturn(Tag.valueOf("checkbox"));
        when(element2.attr("name")).thenReturn("checkbox1");
        when(element2.attr("type")).thenReturn("checkbox");
        when(element2.hasAttr("checked")).thenReturn(true);
        
        when(element3.tag()).thenReturn(Tag.valueOf("input"));
        when(element3.attr("name")).thenReturn("input1");
        when(element3.attr("type")).thenReturn("text");
        when(element3.val()).thenReturn("text1");
        
        when(formElement.elements()).thenReturn(elementsMock);
        when(elementsMock.iterator()).thenReturn(List.of(element1, element2, element3).iterator());
        
        when(optionsMock.first()).thenReturn(mock(Element.class));
        when(optionMock.val()).thenReturn("option1");
        
        when(Elements.select("option[selected]")).thenReturn(optionsMock);

        expectedData.add(HttpConnection.KeyVal.create("select1", "option1"));
        expectedData.add(HttpConnection.KeyVal.create("checkbox1", "on"));
        expectedData.add(HttpConnection.KeyVal.create("input1", "text1"));
        
        assertEquals(expectedData, formElement.formData());
    }
}