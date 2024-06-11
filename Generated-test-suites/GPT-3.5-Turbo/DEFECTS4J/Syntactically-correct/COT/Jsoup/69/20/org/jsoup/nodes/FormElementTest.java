
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
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        FormElement result = formElement.addElement(element);

        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(element, elements.get(0));

        Assert.assertSame(formElement, result);
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("https://www.example.com/submit", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_missingActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());

        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "name");
        inputElement.val("John Doe");

        Element selectElement = new Element(Tag.valueOf("select"), "https://www.example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "https://www.example.com", new Attributes());
        option1.attr("selected", "");
        option1.val("1");
        Element option2 = new Element(Tag.valueOf("option"), "https://www.example.com", new Attributes());
        option2.val("2");
        selectElement.appendChild(option1);
        selectElement.appendChild(option2);

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        Connection.KeyVal keyVal2 = formData.get(1);

        Assert.assertEquals("name", keyVal1.key());
        Assert.assertEquals("John Doe", keyVal1.value());

        Assert.assertEquals("name", keyVal2.key());
        Assert.assertEquals("1", keyVal2.value());
    }

    @Test
    public void testFormData_inputDisabled() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "https://www.example.com", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "name");
        inputElement.val("John Doe");
        inputElement.attr("disabled", "");

        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }
}
