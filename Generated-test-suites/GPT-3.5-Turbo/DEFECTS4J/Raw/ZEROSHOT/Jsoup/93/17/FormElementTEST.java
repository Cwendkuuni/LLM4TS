import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.*;
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
        formElement.addElement(element);
        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        Elements elements = formElement.elements();
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.val("inputValue");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("inputName", keyVal.key());
        Assert.assertEquals("inputValue", keyVal.value());
    }
}