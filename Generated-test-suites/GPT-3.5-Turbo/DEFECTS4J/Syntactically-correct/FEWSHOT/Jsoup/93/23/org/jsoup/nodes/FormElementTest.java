import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FormElementTest {
    
    @Mock
    private Element mockElement;
    
    private FormElement formElement;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }
    
    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        formElement.addElement(mockElement);
        
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(mockElement, elements.get(0));
    }
    
    @Test
    public void testRemoveChild() {
        formElement.addElement(mockElement);
        
        formElement.removeChild(mockElement);
        
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testSubmit() {
        Connection connectionMock = mock(Connection.class);
        when(mockElement.hasAttr("action")).thenReturn(true);
        when(mockElement.absUrl("action")).thenReturn("http://example.com/action");
        when(mockElement.baseUri()).thenReturn("http://example.com");
        when(mockElement.attr("method")).thenReturn("POST");
        when(Jsoup.connect("http://example.com/action")).thenReturn(connectionMock);
        
        Connection result = formElement.submit();
        
        ArgumentCaptor<Connection.Method> methodCaptor = ArgumentCaptor.forClass(Connection.Method.class);
        verify(connectionMock).method(methodCaptor.capture());
        assertEquals(Connection.Method.POST, methodCaptor.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_noAction() {
        when(mockElement.hasAttr("action")).thenReturn(false);
        when(mockElement.baseUri()).thenReturn("");
        
        formElement.submit();
    }
    
    @Test
    public void testFormData() {
        Element selectElementMock = mock(Element.class);
        Elements optionsMock = mock(Elements.class);
        List<Element> optionsListMock = new ArrayList<>();
        Element optionMock = mock(Element.class);
        when(selectElementMock.normalName()).thenReturn("select");
        when(selectElementMock.select("option[selected]")).thenReturn(optionsMock);
        when(optionsMock.isEmpty()).thenReturn(false);
        when(optionsMock.first()).thenReturn(optionMock);
        when(optionMock.val()).thenReturn("optionValue");
        
        Element checkboxElementMock = mock(Element.class);
        when(checkboxElementMock.normalName()).thenReturn("input");
        when(checkboxElementMock.hasAttr("checked")).thenReturn(true);
        when(checkboxElementMock.val()).thenReturn("");
        
        Element radioElementMock = mock(Element.class);
        when(radioElementMock.normalName()).thenReturn("input");
        when(radioElementMock.hasAttr("checked")).thenReturn(true);
        when(radioElementMock.val()).thenReturn("radioValue");
        
        Element inputElementMock = mock(Element.class);
        when(inputElementMock.normalName()).thenReturn("input");
        when(inputElementMock.hasAttr("checked")).thenReturn(false);
        when(inputElementMock.val()).thenReturn("inputValue");
        
        Elements formElements = new Elements();
        formElements.add(selectElementMock);
        formElements.add(checkboxElementMock);
        formElements.add(radioElementMock);
        formElements.add(inputElementMock);
        formElement.addElement(selectElementMock);
        formElement.addElement(checkboxElementMock);
        formElement.addElement(radioElementMock);
        formElement.addElement(inputElementMock);
        
        List<Connection.KeyVal> formData = formElement.formData();
        
        assertEquals(3, formData.size());
    }
}