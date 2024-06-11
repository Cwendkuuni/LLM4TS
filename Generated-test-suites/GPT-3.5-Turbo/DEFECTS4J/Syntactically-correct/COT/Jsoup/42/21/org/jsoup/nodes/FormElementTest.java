
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        Tag formTag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(formTag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(inputElement);
        
        assertNotNull(formElement.elements());
        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.submit();
    }

    @Test
    public void testSubmitWithInvalidMethod() {
        Attribute methodAttribute = new Attribute("method", "INVALID");
        formElement.attributes().put(methodAttribute);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitWithGetMethod() {
        Attribute methodAttribute = new Attribute("method", "get");
        formElement.attributes().put(methodAttribute);
        
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitWithPostMethod() {
        Attribute methodAttribute = new Attribute("method", "post");
        formElement.attributes().put(methodAttribute);
        
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        Attribute nameAttribute = new Attribute("name", "field1");
        Attribute valueAttribute = new Attribute("value", "value1");
        inputElement.attributes().put(nameAttribute);
        inputElement.attributes().put(valueAttribute);
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        selectElement.attributes().put(nameAttribute);

        Element option1 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        Attribute selectedAttribute = new Attribute("selected", "");
        option1.attributes().put(valueAttribute);
        option1.attributes().put(selectedAttribute);
        selectElement.appendChild(option1);

        Element option2 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        option2.attributes().put(valueAttribute);
        selectElement.appendChild(option2);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());
        assertEquals("field1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("field1", formData.get(1).key());
        assertEquals("value1", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        assertTrue(formElement.equals(formElement));
        assertFalse(formElement.equals(null));

        Tag formTag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement otherFormElement = new FormElement(formTag, baseUri, attributes);

        assertTrue(formElement.equals(otherFormElement));
    }
}
