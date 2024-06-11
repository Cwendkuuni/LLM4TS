
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

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
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        Assert.assertEquals(formElement.getElements().size(), updatedFormElement.getElements().size()-1);
        Assert.assertTrue(updatedFormElement.getElements().contains(element));
    }
    
    @Test
    public void testRemoveChild() throws Exception {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);

        Field elementsField = formElement.getClass().getDeclaredField("elements");
        elementsField.setAccessible(true);
        Elements elements = (Elements) elementsField.get(formElement);

        int initialSize = elements.size();
        formElement.removeChild(element);
        Assert.assertEquals(initialSize - 1, elements.size());
    }
    
    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }
    
    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
    }
}
