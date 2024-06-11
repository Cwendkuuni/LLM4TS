import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        Elements elements = formElement.elements();

        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());

        FormElement updatedFormElement = formElement.addElement(element);

        Elements elements = updatedFormElement.elements();

        Assert.assertNotNull(elements);
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.first());
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        formElement.attr("action", "http://www.example.com/action");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals("http://www.example.com/action", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        Connection connection = formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("jsmith");

        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
        optionElement.val("option1");
        selectElement.appendChild(optionElement);

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("username", formData.get(0).key());
        Assert.assertEquals("jsmith", formData.get(0).value());
        Assert.assertEquals("select", formData.get(1).key());
        Assert.assertEquals("option1", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        FormElement formElement3 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        Assert.assertTrue(formElement1.equals(formElement2));
        Assert.assertTrue(formElement2.equals(formElement3));
        Assert.assertTrue(formElement1.equals(formElement3));
    }
}