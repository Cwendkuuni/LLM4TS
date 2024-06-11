
import org.jsoup.Connection;
import org.jsoup.Jsoup;
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
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        Assert.assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
    }
}
