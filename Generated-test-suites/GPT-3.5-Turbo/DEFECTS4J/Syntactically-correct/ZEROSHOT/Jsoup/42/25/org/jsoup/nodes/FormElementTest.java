import org.jsoup.Connection;
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
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        Elements elements = updatedFormElement.elements();
        Assert.assertEquals(1, elements.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
    }

    @Test
    public void testSubmitWithAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "http://example.com/submit");
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("test_user");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Assert.assertTrue(formElement1.equals(formElement2));
    }
}