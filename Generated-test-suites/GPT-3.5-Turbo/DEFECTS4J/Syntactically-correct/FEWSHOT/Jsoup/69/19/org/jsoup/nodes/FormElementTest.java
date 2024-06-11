import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class FormElementTest {
    private FormElement formElement;
    private Elements elements;
    private Element element1;
    private Element element2;
    private Element element3;

    @Before
    public void setUp() {
        elements = new Elements();
        formElement = Mockito.spy(new FormElement(Tag.valueOf("form"), "base_uri", new Attributes()));

        element1 = Mockito.spy(new Element(Tag.valueOf("input"), "", new Attributes()));
        element2 = Mockito.spy(new Element(Tag.valueOf("select"), "", new Attributes()));
        element3 = Mockito.spy(new Element(Tag.valueOf("input"), "", new Attributes()));

        elements.add(element1);
        elements.add(element2);
        elements.add(element3);

        doReturn(elements).when(formElement).elements();
    }

    @Test
    public void testAddElement() {
        Element element4 = mock(Element.class);

        formElement.addElement(element4);

        assertEquals(4, formElement.elements().size());
        assertTrue(formElement.elements().contains(element4));
    }

    @Test
    public void testSubmit() {
        formElement.attr("method", "post");
        formElement.attr("action", "http://example.com");

        Connection connection = formElement.submit();

        assertEquals("http://example.com", connection.url());
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testSubmit_noAction() {
        Connection connection = formElement.submit();

        assertEquals("base_uri", connection.url());
        assertEquals(Connection.Method.GET, connection.method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_emptyAction() {
        formElement.attr("action", "");

        formElement.submit();
    }

    @Test
    public void testFormData() {
        doReturn(true).when(element1).tag().isFormSubmittable();
        doReturn("name1").when(element1).attr("name");
        doReturn("type1").when(element1).attr("type");
        doReturn("value1").when(element1).val();

        doReturn(true).when(element2).tag().isFormSubmittable();
        doReturn("name2").when(element2).attr("name");
        doReturn("select").when(element2).tagName();
        doReturn("option_value").when(element2).val();

        doReturn(false).when(element3).tag().isFormSubmittable();

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());
        assertEquals("name1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("name2", formData.get(1).key());
        assertEquals("option_value", formData.get(1).value());
    }
}