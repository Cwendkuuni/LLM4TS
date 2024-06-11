import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("input");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        FormElement newFormElement = new FormElement(tag, baseUri, attributes);

        formElement.addElement(newFormElement);

        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(newFormElement, elements.first());
    }

    @Test
    public void testRemoveChild() {
        Tag tag = Tag.valueOf("input");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElementToRemove = new FormElement(tag, baseUri, attributes);

        formElement.addElement(formElementToRemove);
        formElement.removeChild(formElementToRemove);

        Elements elements = formElement.elements();
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testSubmitWithAction() throws IllegalArgumentException {
        String url = "http://www.example.com";
        formElement.attr("action", url);

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals(url, connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() throws IllegalArgumentException {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Tag inputTag = Tag.valueOf("input");
        String inputBaseUri = "http://www.example.com";
        Attributes inputAttributes = new Attributes();
        inputAttributes.put("name", "username");
        inputAttributes.put("value", "John");

        FormElement inputElement = new FormElement(inputTag, inputBaseUri, inputAttributes);
        formElement.addElement(inputElement);

        Tag selectTag = Tag.valueOf("select");
        String selectBaseUri = "http://www.example.com";
        Attributes selectAttributes = new Attributes();
        Element selectElement = new FormElement(selectTag, selectBaseUri, selectAttributes);

        Tag optionTag = Tag.valueOf("option");
        String optionBaseUri = "http://www.example.com";
        Attributes optionAttributes = new Attributes();
        optionAttributes.put("selected", "selected");
        Element optionElement = new FormElement(optionTag, optionBaseUri, optionAttributes);
        selectElement.addElement(optionElement);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();

        Assert.assertEquals(2, data.size());
        Assert.assertEquals("username", data.get(0).key());
        Assert.assertEquals("John", data.get(0).value());
        Assert.assertEquals("select", data.get(1).key());
        Assert.assertEquals("selected", data.get(1).value());

    }

}