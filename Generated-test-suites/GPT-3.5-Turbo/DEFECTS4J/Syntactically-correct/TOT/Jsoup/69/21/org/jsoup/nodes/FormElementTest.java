import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
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
        Assert.assertEquals(0, formElement.elements().size());

        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        formElement.addElement(new Element(Tag.valueOf("textarea"), "", new Attributes()));
        formElement.addElement(new Element(Tag.valueOf("button"), "", new Attributes()));

        Assert.assertEquals(3, formElement.elements().size());
    }

    @Test
    public void testAddElement() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result1 = formElement.addElement(element1);
        Assert.assertEquals(1, result1.elements().size());
        Assert.assertTrue(result1.elements().contains(element1));

        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());
        formElement.addElement(element2);
        Assert.assertEquals(2, formElement.elements().size());
        Assert.assertTrue(formElement.elements().contains(element2));

        Element element3 = new Element(Tag.valueOf("button"), "", new Attributes());
        formElement.addElement(element3);
        Assert.assertEquals(3, formElement.elements().size());
        Assert.assertTrue(formElement.elements().contains(element3));
    }

    @Test
    public void testSubmitWithActionAndMethodPOST() {
        formElement.attr("action", "/endpoint");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        Assert.assertEquals("http://example.com/endpoint", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitWithActionAndMethodGET() {
        formElement.attr("action", "/endpoint");
        formElement.attr("method", "get");

        Connection connection = formElement.submit();

        Assert.assertEquals("http://example.com/endpoint", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoActionUrl() {
        formElement.submit();
    }

    @Test
    public void testFormDataWithFormSubmittableElement() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("name", "username");
        element1.attr("value", "john");

        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());
        element2.attr("name", "comment");
        element2.text("Hello World");

        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("username=john", formData.get(0).toString());
        Assert.assertEquals("comment=Hello World", formData.get(1).toString());
    }

    @Test
    public void testFormDataWithDisabledElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "username");
        element.attr("value", "john");
        element.attr("disabled", "disabled");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithEmptyName() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "");
        element.attr("value", "john");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithSelectElementNoOptions() {
        Element element = new Element(Tag.valueOf("select"), "", new Attributes());
        element.attr("name", "country");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithSelectElementNoSelectedOptions() {
        Element element = new Element(Tag.valueOf("select"), "", new Attributes());
        element.attr("name", "country");

        Element option = new Element(Tag.valueOf("option"), "", new Attributes());

        element.addElement(option);

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithSelectElementAndSelectedOptions() {
        Element element = new Element(Tag.valueOf("select"), "", new Attributes());
        element.attr("name", "country");

        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("selected", "selected");
        option1.attr("value", "us");

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("selected", "selected");
        option2.attr("value", "uk");

        element.addElements(option1, option2);

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("country=us", formData.get(0).toString());
        Assert.assertEquals("country=uk", formData.get(1).toString());
    }

    @Test
    public void testFormDataWithCheckboxInputElementChecked() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("name", "newsletter");
        element1.attr("type", "checkbox");
        element1.attr("checked", "checked");

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("name", "marketing");
        element2.attr("type", "checkbox");

        Element element3 = new Element(Tag.valueOf("input"), "", new Attributes());
        element3.attr("name", "accept");
        element3.attr("type", "checkbox");
        element3.attr("checked", "checked");
        element3.attr("value", "on");

        formElement.addElements(element1, element2, element3);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("newsletter=on", formData.get(0).toString());
        Assert.assertEquals("accept=on", formData.get(1).toString());
    }

    @Test
    public void testFormDataWithCheckboxInputElementUnchecked() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "newsletter");
        element.attr("type", "checkbox");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithRadioInputElementChecked() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("name", "subscription");
        element1.attr("type", "radio");
        element1.attr("checked", "checked");

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("name", "subscription");
        element2.attr("type", "radio");

        formElement.addElements(element1, element2);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("subscription=", formData.get(0).toString());
    }

    @Test
    public void testFormDataWithRadioInputElementUnchecked() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "subscription");
        element.attr("type", "radio");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithOtherInputElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "username");
        element.attr("value", "john");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("username=john", formData.get(0).toString());
    }

}