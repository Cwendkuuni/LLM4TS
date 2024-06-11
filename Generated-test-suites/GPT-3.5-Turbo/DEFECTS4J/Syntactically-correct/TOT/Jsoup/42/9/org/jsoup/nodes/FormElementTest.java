import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
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
        String baseUri = "http://example.com";
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
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);

        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        formElement.submit();
    }

    @Test
    public void testSubmitWithInvalidActionUrl() {
        formElement.attr("action", "invalid action url");

        try {
            formElement.submit();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testSubmitWithGetMethod() {
        formElement.attr("action", "");
        formElement.attr("method", "GET");

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testSubmitWithPostMethod() {
        formElement.attr("action", "");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormDataWithEmptyElements() {
        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithNonFormSubmittableElement() {
        Element nonSubmittableElement = new Element(Tag.valueOf("div"), "", new Attributes());
        formElement.addElement(nonSubmittableElement);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithTextInput() {
        Element textInput = new Element(Tag.valueOf("input"), "", new Attributes());
        textInput.attr("name", "text");
        textInput.val("Hello World");
        formElement.addElement(textInput);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("text", formData.get(0).key());
        Assert.assertEquals("Hello World", formData.get(0).value());
    }

    @Test
    public void testFormDataWithSelect() {
        Element select = new Element(Tag.valueOf("select"), "", new Attributes());
        select.attr("name", "options");

        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "1");

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "2");
        option2.attr("selected", "");

        select.appendChild(option1);
        select.appendChild(option2);

        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("options", formData.get(0).key());
        Assert.assertEquals("2", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckbox() {
        Element checkbox = new Element(Tag.valueOf("input"), "", new Attributes());
        checkbox.attr("name", "check");
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "");

        formElement.addElement(checkbox);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("check", formData.get(0).key());
        Assert.assertEquals("", formData.get(0).value());
    }

    @Test
    public void testFormDataWithErrorHandling() {
        Element errorElement = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(errorElement);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
    }

}