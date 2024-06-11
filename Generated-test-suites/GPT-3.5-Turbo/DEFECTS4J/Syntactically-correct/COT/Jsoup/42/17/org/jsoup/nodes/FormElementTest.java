import org.jsoup.Connection;
import org.jsoup.Jsoup;
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
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Elements elements = form.elements();
        Assert.assertNotNull(elements);
    }
    
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = form.addElement(input);
        Elements elements = form.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(input, elements.get(0));
        Assert.assertSame(form, result);
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Connection connection = form.submit();
        Assert.assertNotNull(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_noAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        form.submit();
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        List<Connection.KeyVal> data = form.formData();
        Assert.assertNotNull(data);
    }
    
    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Assert.assertEquals(form1, form2);
    }
}