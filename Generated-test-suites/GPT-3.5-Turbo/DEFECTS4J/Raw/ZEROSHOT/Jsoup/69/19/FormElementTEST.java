import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = Mockito.mock(Element.class);
        formElement.addElement(element);
        Elements elements = formElement.elements();
        Assert.assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertNotNull(data);
    }
}