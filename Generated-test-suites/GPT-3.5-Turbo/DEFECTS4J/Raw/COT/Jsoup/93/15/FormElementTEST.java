
import org.junit.Test;
import org.jsoup.Connection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(0, formElement.elements().size());

        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testSubmitWithEmptyAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        
        // Mock the static methods of Jsoup class to return a Connection object
        Connection connection = mock(Connection.class);
        when(Jsoup.connect(anyString())).thenReturn(connection);
        
        // Mock the Connection object's data() and method() methods
        when(connection.data(anyListOf(Connection.KeyVal.class))).thenReturn(connection);
        when(connection.method(Connection.Method.GET)).thenReturn(connection);
        
        Connection submitConnection = formElement.submit();
        
        // Verify that the Jsoup.connect() method is called with an empty string as the URL
        verify(Jsoup, times(1)).connect("");

        // Verify that the Connection.data() method is called with the form data
        verify(connection, times(1)).data(anyListOf(Connection.KeyVal.class));
        
        // Verify that the Connection.method() method is called with GET as the method
        verify(connection, times(1)).method(Connection.Method.GET);
    }

    @Test
    public void testSubmitWithAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        
        // Mock the static methods of Jsoup class to return a Connection object
        Connection connection = mock(Connection.class);
        when(Jsoup.connect(anyString())).thenReturn(connection);
        
        // Mock the Connection object's data() and method() methods
        when(connection.data(anyListOf(Connection.KeyVal.class))).thenReturn(connection);
        when(connection.method(Connection.Method.POST)).thenReturn(connection);
        
        Connection submitConnection = formElement.submit();
        
        // Verify that the Jsoup.connect() method is called with the correct URL
        verify(Jsoup, times(1)).connect("http://example.com");

        // Verify that the Connection.data() method is called with the form data
        verify(connection, times(1)).data(anyListOf(Connection.KeyVal.class));
        
        // Verify that the Connection.method() method is called with POST as the method
        verify(connection, times(1)).method(Connection.Method.POST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoBaseURI() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(0, formElement.formData().size());

        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        assertEquals(1, formElement.formData().size());
    }
}
