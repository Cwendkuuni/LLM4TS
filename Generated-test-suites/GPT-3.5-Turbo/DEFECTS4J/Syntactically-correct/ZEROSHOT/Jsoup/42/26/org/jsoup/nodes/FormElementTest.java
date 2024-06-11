import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FormElementTest {

    @Mock
    private Tag mockTag;
    @Mock
    private Element mockElement;
    @Mock
    private Attributes mockAttributes;

    private FormElement formElement;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        formElement = new FormElement(mockTag, "http://example.com", mockAttributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        formElement.addElement(mockElement);
        assertTrue(formElement.elements().contains(mockElement));
    }

    @Test
    public void testSubmit() {
        formElement.attr("action", "http://example.com");
        formElement.attr("method", "POST");

        Connection con = mock(Connection.class);
        when(Jsoup.connect("http://example.com")).thenReturn(con);
        when(con.data(anyList())).thenReturn(con);
        when(con.method(any(Connection.Method.class))).thenReturn(con);

        Connection returnedCon = formElement.submit();
        assertSame(con, returnedCon);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_missingActionUrl() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element el1 = mock(Element.class);
        Element el2 = mock(Element.class);
        Element el3 = mock(Element.class);

        when(mockElement.tag()).thenReturn(mock(Tag.class));
        when(el1.tag()).thenReturn(mock(Tag.class));
        when(el2.tag()).thenReturn(mock(Tag.class));
        when(el3.tag()).thenReturn(mock(Tag.class));

        when(mockElement.tag().isFormSubmittable()).thenReturn(true);
        when(el1.tag().isFormSubmittable()).thenReturn(true);
        when(el2.tag().isFormSubmittable()).thenReturn(false);
        when(el3.tag().isFormSubmittable()).thenReturn(true);

        when(mockElement.attr("name")).thenReturn("");
        when(el1.attr("name")).thenReturn("name1");
        when(el2.attr("name")).thenReturn("name2");
        when(el3.attr("name")).thenReturn("name3");

        when(mockElement.attr("type")).thenReturn("text");
        when(el1.attr("type")).thenReturn("checkbox");
        when(el2.attr("type")).thenReturn("radio");
        when(el3.attr("type")).thenReturn("text");

        when(mockElement.val()).thenReturn("");
        when(el1.val()).thenReturn("value1");
        when(el2.val()).thenReturn("value2");
        when(el3.val()).thenReturn("value3");

        formElement.addElement(mockElement);
        formElement.addElement(el1);
        formElement.addElement(el2);
        formElement.addElement(el3);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("name1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("name3", formData.get(1).key());
        assertEquals("value3", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        assertTrue(formElement.equals(formElement));
        assertFalse(formElement.equals(null));
        assertFalse(formElement.equals(new Object()));
    }
}