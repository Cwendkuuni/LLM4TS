import org.jsoup.Connection;
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
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
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

        FormElement returnedFormElement = formElement.addElement(element);

        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(element, elements.get(0));
        Assert.assertSame(formElement, returnedFormElement);
    }

    @Test
    public void testSubmit_withActionAttribute() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("testuser");
        formElement.addElement(inputElement);

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com/submit", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
        Assert.assertEquals("username=testuser", connection.request().body());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_withoutActionAttribute() {
        formElement.attr("method", "GET");

        formElement.submit();
    }

    @Test
    public void testFormData_withEmptyForm() {
        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_withInputElements() {
        formElement.attr("method", "POST");
        Element hiddenElement = new Element(Tag.valueOf("input"), "", new Attributes());
        hiddenElement.attr("type", "hidden");
        hiddenElement.attr("name", "hiddenField");
        hiddenElement.val("hiddenValue");
        formElement.addElement(hiddenElement);

        Element textElement = new Element(Tag.valueOf("input"), "", new Attributes());
        textElement.attr("type", "text");
        textElement.attr("name", "textField");
        textElement.val("textValue");
        formElement.addElement(textElement);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(2, formData.size());

        Connection.KeyVal hiddenField = formData.get(0);
        Assert.assertEquals("hiddenField", hiddenField.key());
        Assert.assertEquals("hiddenValue", hiddenField.value());

        Connection.KeyVal textField = formData.get(1);
        Assert.assertEquals("textField", textField.key());
        Assert.assertEquals("textValue", textField.value());
    }
}