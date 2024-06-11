import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element);
        Elements elements = form.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = form.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> data = form.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(0, data.size());
    }

    // Additional test cases for edge cases and exceptions handling
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.submit();
    }

    @Test
    public void testSubmitWithPostMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("method", "POST");
        Connection connection = form.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitWithGetMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("method", "GET");
        Connection connection = form.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitWithAbsUrlAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("action", "/submit");
        Connection connection = form.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com/submit", connection.request().url().toString());
    }

    @Test
    public void testSubmitWithRelativeUrlAction() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.attr("action", "submit");
        Connection connection = form.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com/submit", connection.request().url().toString());
    }

    @Test
    public void testFormDataWithSelectElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("selected", "");
        optionElement.val("option1");
        selectElement.appendChild(optionElement);
        form.addElement(selectElement);
        List<Connection.KeyVal> data = form.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("option1", data.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("checked", "");
        form.addElement(checkboxElement);
        List<Connection.KeyVal> data = form.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("on", data.get(0).value());
    }

    @Test
    public void testFormDataWithRadioElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio");
        radioElement.attr("checked", "");
        form.addElement(radioElement);
        List<Connection.KeyVal> data = form.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("on", data.get(0).value());
    }

    @Test
    public void testFormDataWithDisabledElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "input");
        inputElement.attr("disabled", "");
        form.addElement(inputElement);
        List<Connection.KeyVal> data = form.formData();
        Assert.assertNotNull(data);
        Assert.assertEquals(0, data.size());
    }
}