
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
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
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element1);
        formElement.addElement(element2);
        
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(2, elements.size());
        Assert.assertEquals(element1, elements.get(0));
        Assert.assertEquals(element2, elements.get(1));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formElementWithoutBaseUri = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElementWithoutBaseUri.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testEquals() {
        Object o = new Object();
        boolean result = formElement.equals(o);
        Assert.assertFalse(result);
    }
}
