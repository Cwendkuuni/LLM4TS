import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        Element element1 = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        Element element2 = new Element(Tag.valueOf("select"), "https://example.com", new Attributes());
        Element element3 = new Element(Tag.valueOf("textarea"), "https://example.com", new Attributes());

        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);

        Elements elements = formElement.elements();

        Assert.assertEquals(3, elements.size());
        Assert.assertEquals(element1, elements.get(0));
        Assert.assertEquals(element2, elements.get(1));
        Assert.assertEquals(element3, elements.get(2));
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());

        FormElement result = formElement.addElement(element);

        Assert.assertSame(formElement, result);
        Assert.assertEquals(1, formElement.elements().size());
        Assert.assertEquals(element, formElement.elements().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_NoActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        Connection connection = formElement.submit();
    }

    @Test
    public void testSubmit_GetMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        formElement.attributes().put("method", "GET");

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals("https://example.com/", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmit_PostMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        formElement.attributes().put("method", "POST");

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals("https://example.com/", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        Element selectElement = new Element(Tag.valueOf("select"), "https://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        Element textareaElement = new Element(Tag.valueOf("textarea"), "https://example.com", new Attributes());

        inputElement.attributes().put("name", "input_name");
        inputElement.attributes().put("type", "text");
        inputElement.val("input_value");

        selectElement.attributes().put("name", "select_name");
        Element optionElement = new Element(Tag.valueOf("option"), "https://example.com");
        optionElement.attributes().put("selected", "");
        optionElement.val("option_value");
        selectElement.appendChild(optionElement);

        checkboxElement.attributes().put("name", "checkbox_name");
        checkboxElement.attributes().put("type", "checkbox");
        checkboxElement.attributes().put("checked", "");
        checkboxElement.val("checkbox_value");

        radioElement.attributes().put("name", "radio_name");
        radioElement.attributes().put("type", "radio");
        radioElement.attributes().put("checked", "");
        radioElement.val("radio_value");

        textareaElement.attributes().put("name", "textarea_name");
        textareaElement.appendChild(new TextNode("textarea_value"));

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(radioElement);
        formElement.addElement(textareaElement);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(5, formData.size());
        Assert.assertEquals(HttpConnection.KeyVal.create("input_name", "input_value"), formData.get(0));
        Assert.assertEquals(HttpConnection.KeyVal.create("select_name", "option_value"), formData.get(1));
        Assert.assertEquals(HttpConnection.KeyVal.create("checkbox_name", "checkbox_value"), formData.get(2));
        Assert.assertEquals(HttpConnection.KeyVal.create("radio_name", "radio_value"), formData.get(3));
        Assert.assertEquals(HttpConnection.KeyVal.create("textarea_name", "textarea_value"), formData.get(4));
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());

        Assert.assertEquals(formElement1, formElement2);
    }
}