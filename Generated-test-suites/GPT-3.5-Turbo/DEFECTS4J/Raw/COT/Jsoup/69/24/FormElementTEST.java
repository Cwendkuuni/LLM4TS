

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
        // Arrange
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Act
        Elements elements = form.elements();

        // Assert
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        // Arrange
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com");

        // Act
        FormElement result = form.addElement(element);

        // Assert
        Assert.assertEquals(form, result);
        Assert.assertTrue(form.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        // Arrange
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("action", "http://example.com/action");
        form.attr("method", "POST");

        // Act
        Connection connection = form.submit();

        // Assert
        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com/action", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_noActionUrl() {
        // Arrange
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        // Act
        form.submit();
    }

    @Test
    public void testFormData() {
        // Arrange
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input = new Element(Tag.valueOf("input"), "http://example.com");
        input.attr("name", "name");
        input.val("John Doe");
        form.addElement(input);

        // Act
        List<Connection.KeyVal> formData = form.formData();

        // Assert
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("name", formData.get(0).key());
        Assert.assertEquals("John Doe", formData.get(0).value());
    }
}
