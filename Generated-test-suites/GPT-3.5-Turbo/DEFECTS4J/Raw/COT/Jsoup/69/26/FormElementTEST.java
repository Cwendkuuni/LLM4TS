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
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        Assert.assertEquals(1, updatedFormElement.elements().size());
        Assert.assertEquals(element, updatedFormElement.elements().get(0));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

}