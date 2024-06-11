
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        Tag tag = new Tag("form", new Attributes());
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Elements elements = formElement.elements();

        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Tag tag = new Tag("form", new Attributes());
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = new Element(tag, baseUri, attributes);
        FormElement result = formElement.addElement(element);

        assertEquals(1, result.elements().size());
        assertEquals(element, result.elements().get(0));
    }

    @Test
    public void testRemoveChild() {
        Tag tag = new Tag("form", new Attributes());
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = new Element(tag, baseUri, attributes);
        formElement.addElement(element);

        formElement.removeChild(element);

        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        Tag tag = new Tag("form", new Attributes());
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        formElement.addElement(new Element(tag, baseUri, attributes));

        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(Jsoup.connect(Mockito.anyString())).thenReturn(connection);

        Connection.Method method = Connection.Method.GET;
        Mockito.when(connection.method(Mockito.any(Connection.Method.class))).thenReturn(connection);
        Mockito.when(connection.data(Mockito.anyList())).thenReturn(connection);

        Connection result = formElement.submit();

        assertEquals(connection, result);
        Mockito.verify(connection).method(method);
        Mockito.verify(connection).data(formElement.formData());
    }

    @Test
    public void testFormData() {
        Tag tag = new Tag("form", new Attributes());
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element1 = new Element(tag, baseUri, attributes);
        element1.attr("name", "name1");
        element1.val("value1");
        formElement.addElement(element1);

        Element element2 = new Element(tag, baseUri, attributes);
        element2.attr("name", "name2");
        element2.val("value2");
        formElement.addElement(element2);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertEquals(2, data.size());
        assertEquals("name1", data.get(0).key());
        assertEquals("value1", data.get(0).value());
        assertEquals("name2", data.get(1).key());
        assertEquals("value2", data.get(1).value());
    }
}
