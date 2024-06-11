import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.*;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Elements elements = formElement.elements();

        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element inputElement = new Element(Tag.valueOf("input"), baseUri, new Attributes());
        FormElement result = formElement.addElement(inputElement);

        Assert.assertEquals(1, formElement.elements().size());
        Assert.assertEquals(inputElement, formElement.elements().get(0));
        Assert.assertEquals(formElement, result);
    }

    @Test
    public void testSubmit() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        formElement.submit();
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testEquals() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement1 = new FormElement(tag, baseUri, attributes);
        FormElement formElement2 = new FormElement(tag, baseUri, attributes);

        boolean result = formElement1.equals(formElement2);

        Assert.assertTrue(result);
    }

}