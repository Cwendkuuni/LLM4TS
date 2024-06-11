
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmitWithoutAction() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
        Assert.assertEquals(0, connection.request().data().size());
    }

    @Test
    public void testSubmitWithAction() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input.attr("name", "name");
        input.attr("value", "John");
        formElement.addElement(input);
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com/submit", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
        List<Connection.KeyVal> formData = connection.request().data();
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("name", keyVal.key());
        Assert.assertEquals("John", keyVal.value());
    }

    @Test
    public void testFormDataEmpty() {
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithInputElement() {
        Element input = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        input.attr("name", "name");
        input.attr("value", "John");
        formElement.addElement(input);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("name", keyVal.key());
        Assert.assertEquals("John", keyVal.value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Assert.assertTrue(formElement1.equals(formElement2));
    }
}
