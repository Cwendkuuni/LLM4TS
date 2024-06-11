import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

    @InjectMocks
    private FormElement formElement;
    @Mock
    private Elements elements;
    @Mock
    private Element element;
    @Mock
    private Connection connection;

    @Before
    public void setUp() {
        formElement = Mockito.spy(new FormElement(Tag.valueOf("form"), "https://www.example.com/form", new Attributes()));
        elements = Mockito.spy(new Elements(Arrays.asList(element)));
        formElement.elements = elements;
    }

    @Test
    public void testElements() {
        assertEquals(elements, formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element newElement = mock(Element.class);
        formElement.addElement(newElement);
        verify(elements, times(1)).add(newElement);
    }

    @Test
    public void testSubmit() {
        String action = "https://www.example.com/action";
        formElement.attr("action", action);
        formElement.attr("method", "POST");
        
        formElement.absUrl("action");
        when(Jsoup.connect(action)).thenReturn(connection);
        
        Connection connection = formElement.submit();

        verify(formElement, times(1)).hasAttr("action");
        verify(formElement, times(1)).absUrl("action");
        verify(Jsoup, times(1)).connect(action);
        verify(connection, times(1)).data(formElement.formData());
        verify(connection, times(1)).method(Connection.Method.POST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element el1 = mock(Element.class);
        Element el2 = mock(Element.class);

        when(el1.tag()).thenReturn(Tag.valueOf("input"));
        when(el1.hasAttr("name")).thenReturn(true);
        when(el1.attr("name")).thenReturn("input1");
        when(el1.attr("type")).thenReturn("text");
        when(el1.val()).thenReturn("value1");

        when(el2.tag()).thenReturn(Tag.valueOf("input"));
        when(el2.hasAttr("name")).thenReturn(true);
        when(el2.attr("name")).thenReturn("input2");
        when(el2.attr("type")).thenReturn("checkbox");
        when(el2.hasAttr("checked")).thenReturn(true);
        when(el2.val()).thenReturn("on");

        elements = new Elements(Arrays.asList(el1, el2));
        formElement.elements = elements;

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("input2", formData.get(1).key());
        assertEquals("on", formData.get(1).value());

        verify(el1, times(1)).tag();
        verify(el1, times(1)).hasAttr("name");
        verify(el1, times(1)).attr("name");
        verify(el1, times(1)).attr("type");
        verify(el1, times(1)).val();

        verify(el2, times(1)).tag();
        verify(el2, times(1)).hasAttr("name");
        verify(el2, times(1)).attr("name");
        verify(el2, times(1)).attr("type");
        verify(el2, times(1)).hasAttr("checked");
        verify(el2, times(1)).val();
    }
}