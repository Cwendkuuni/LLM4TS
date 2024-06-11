
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
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());

        FormElement updatedFormElement = formElement.addElement(element);
        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(formElement, updatedFormElement);
    }

    @Test
    public void testRemoveChild() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        formElement.addElement(element);

        formElement.removeChild(element);
        Elements elements = formElement.elements();
        Assert.assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        formElement.attr("action", "http://www.example.com/submit");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
        Assert.assertEquals("http://www.example.com/submit", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("username", keyVal.key());
        Assert.assertEquals("john", keyVal.value());
    }
}
