import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    // Test method: elements()
    @Test
    public void testElements() {
        // Arrange
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        // Act
        Elements elements = formElement.elements();

        // Assert
        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());
    }

    // Test method: addElement(Element element)
    @Test
    public void testAddElement() {
        // Arrange
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element element = new Element(Tag.valueOf("input"), "");

        // Act
        FormElement result = formElement.addElement(element);

        // Assert
        List<Element> formElements = result.elements().toList();
        Assert.assertEquals(1, formElements.size());
        Assert.assertEquals(element, formElements.get(0));
    }

    // Test method: removeChild(Node out)
    @Test
    public void testRemoveChild() {
        // Arrange
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element element = new Element(Tag.valueOf("input"), "");
        formElement.addElement(element);

        // Act
        formElement.removeChild(element);

        // Assert
        List<Element> formElements = formElement.elements().toList();
        Assert.assertEquals(0, formElements.size());
    }

    // Test method: submit()
    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_emptyActionUrl() {
        // Arrange
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        // Act
        Connection connection = formElement.submit();

        // Assert
        // Exception is expected to be thrown
    }

    // Test method: submit()
    @Test
    public void testSubmit_validActionUrlAndMethod() {
        // Arrange
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        formElement.attr("action", "http://example.com/action");
        formElement.attr("method", "POST");

        // Act
        Connection connection = formElement.submit();

        // Assert
        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.POST, connection.method());
        Assert.assertEquals("http://example.com/action", connection.request().url().toString());
    }

    // Test method: submit()
    @Test
    public void testSubmit_validActionUrlAndInvalidMethod() {
        // Arrange
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        formElement.attr("action", "http://example.com/action");
        formElement.attr("method", "PUT");

        // Act
        Connection connection = formElement.submit();

        // Assert
        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.GET, connection.method());
        Assert.assertEquals("http://example.com/action", connection.request().url().toString());
    }

    // Test method: formData()
    @Test
    public void testFormData() {
        // Arrange
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element element1 = new Element(Tag.valueOf("input"), "");
        element1.attr("name", "name1");
        element1.val("value1");
        Element element2 = new Element(Tag.valueOf("input"), "");
        element2.attr("name", "name2");
        element2.attr("disabled", "");
        element2.val("value2");
        Element element3 = new Element(Tag.valueOf("select"), "");
        element3.attr("name", "name3");
        Element option1 = new Element(Tag.valueOf("option"), "");
        option1.attr("selected", "");
        option1.val("option1");
        element3.appendChild(option1);
        Element option2 = new Element(Tag.valueOf("option"), "");
        option2.val("option2");
        element3.appendChild(option2);
        Element element4 = new Element(Tag.valueOf("input"), "");
        element4.attr("type", "checkbox");
        element4.attr("name", "name4");
        element4.attr("checked", "");
        Element element5 = new Element(Tag.valueOf("input"), "");
        element5.attr("type", "checkbox");
        element5.attr("name", "name5");
        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);
        formElement.addElement(element4);
        formElement.addElement(element5);

        // Act
        List<Connection.KeyVal> formData = formElement.formData();

        // Assert
        Assert.assertEquals(4, formData.size());
        Assert.assertEquals("name1", formData.get(0).key());
        Assert.assertEquals("value1", formData.get(0).value());
        Assert.assertEquals("name3", formData.get(1).key());
        Assert.assertEquals("option1", formData.get(1).value());
        Assert.assertEquals("name4", formData.get(2).key());
        Assert.assertEquals("on", formData.get(2).value());
        Assert.assertEquals("name5", formData.get(3).key());
        Assert.assertEquals("", formData.get(3).value());
    }
}