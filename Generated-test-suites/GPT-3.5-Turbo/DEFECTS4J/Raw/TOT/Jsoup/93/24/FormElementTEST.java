import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
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
    public void setup() {
        Tag tag = Tag.form();
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, "http://example.com", attributes);
    }

    @Test
    public void testElements_WhenNoElementsAdded_ShouldReturnEmptyElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testElements_WhenOneElementAdded_ShouldReturnElementsWithSingleElement() {
        Element element = new Element(Tag.input(), "http://example.com", new Attributes());
        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testAddElement_ShouldReturnFormElementWithAddedElement() {
        Element element = new Element(Tag.input(), "http://example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);

        assertNotNull(updatedFormElement);
        assertSame(formElement, updatedFormElement);
        assertTrue(updatedFormElement.elements().contains(element));
    }

    @Test
    public void testRemoveChild_WhenElementExists_ShouldRemoveElementFromElements() {
        Element element = new Element(Tag.input(), "http://example.com", new Attributes());
        formElement.addElement(element);

        formElement.removeChild(element);

        Elements elements = formElement.elements();
        assertFalse(elements.contains(element));
    }

    @Test
    public void testRemoveChild_WhenElementDoesNotExist_ShouldNotModifyElements() {
        Element element = new Element(Tag.input(), "http://example.com", new Attributes());

        formElement.removeChild(element);

        Elements elements = formElement.elements();
        assertFalse(elements.contains(element));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_WhenNoActionAttribute_ShouldThrowIllegalArgumentException() {
        formElement.submit();
    }

    @Test
    public void testSubmit_WhenGetMethod_ShouldReturnConnectionWithGetMethod() {
        formElement.attr("method", "GET");
        Connection connection = formElement.submit();

        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmit_WhenPostMethod_ShouldReturnConnectionWithPostMethod() {
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();

        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmit_WhenAbsoluteActionURL_ShouldReturnConnectionWithCorrectURL() {
        formElement.attr("action", "http://example.com/login");
        Connection connection = formElement.submit();

        assertEquals("http://example.com/login", connection.request().url().toString());
    }

    @Test
    public void testSubmit_WhenRelativeActionURL_ShouldReturnConnectionWithAbsoluteURL() {
        Connection connection = formElement.submit();

        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testFormData_WhenNoFormControlElements_ShouldReturnEmptyList() {
        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_WhenSubmitableElementsExist_ShouldReturnCorrectFormDataList() {
        Element inputElement = new Element(Tag.input(), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "john");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.select(), "http://example.com", new Attributes());
        Element optionElement1 = new Element(Tag.option(), "http://example.com", new Attributes());
        optionElement1.attr("selected", "selected");
        optionElement1.attr("value", "1");
        selectElement.addElement(optionElement1);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john", formData.get(0).value());
        assertEquals("select", formData.get(1).key());
        assertEquals("1", formData.get(1).value());
    }

    @Test
    public void testFormData_WhenDisabledElementsExist_ShouldNotIncludeDisabledElementsInFormData() {
        Element inputElement1 = new Element(Tag.input(), "http://example.com", new Attributes());
        inputElement1.attr("name", "username");
        formElement.addElement(inputElement1);

        Element inputElement2 = new Element(Tag.input(), "http://example.com", new Attributes());
        inputElement2.attr("name", "password");
        inputElement2.attr("value", "secret");
        inputElement2.attr("disabled", "disabled");
        formElement.addElement(inputElement2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertNull(formData.get(0).value());
    }
}