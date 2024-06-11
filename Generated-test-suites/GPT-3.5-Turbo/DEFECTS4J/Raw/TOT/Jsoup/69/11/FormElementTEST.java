import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements_ReturnsElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());

        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(inputElement);

        elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(inputElement, elements.get(0));
    }

    @Test
    public void testAddElement_ReturnsFormElementWithAddedElement() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement addedElement = formElement.addElement(inputElement);
        Assert.assertNotNull(addedElement);
        Assert.assertEquals(1, formElement.elements().size());
        Assert.assertEquals(inputElement, formElement.elements().get(0));
        Assert.assertSame(addedElement, formElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_NoActionUrl_ThrowsIllegalArgumentException() {
        Connection connection = formElement.submit();
    }

    @Test
    public void testSubmit_ActionUrlWithGetMethod_ReturnsConnectionWithGetMethod() {
        formElement.attr("action", "http://www.example.com");
        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
        Assert.assertEquals("http://www.example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmit_ActionUrlWithPostMethod_ReturnsConnectionWithPostMethod() {
        formElement.attr("action", "http://www.example.com");
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
        Assert.assertEquals("http://www.example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmit_ActionUrlWithPostMethodAndFormData_ReturnsConnectionWithFormData() {
        formElement.attr("action", "http://www.example.com");
        formElement.attr("method", "POST");
        Element inputElement1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement1.attr("name", "username");
        inputElement1.attr("value", "John");
        formElement.addElement(inputElement1);
        Element inputElement2 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement2.attr("name", "password");
        inputElement2.attr("value", "password123");
        formElement.addElement(inputElement2);

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
        Assert.assertEquals("http://www.example.com", connection.request().url().toString());
        List<Connection.KeyVal> formData = connection.request().data();
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("username", formData.get(0).key());
        Assert.assertEquals("John", formData.get(0).value());
        Assert.assertEquals("password", formData.get(1).key());
        Assert.assertEquals("password123", formData.get(1).value());
    }

    @Test
    public void testFormData_ReturnsEmptyListForNoFormControls() {
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_ReturnsCorrectListForFormControls() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "John");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        optionElement1.attr("selected", "selected");
        optionElement1.attr("value", "option1");
        selectElement.addElement(optionElement1);
        Element optionElement2 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        optionElement2.attr("value", "option2");
        selectElement.addElement(optionElement2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("username", formData.get(0).key());
        Assert.assertEquals("John", formData.get(0).value());
        Assert.assertEquals("username", formData.get(1).key());
        Assert.assertEquals("option1", formData.get(1).value());
    }

}