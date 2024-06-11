import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        String html = "<form action='/form' method='post'><input name='input1' type='text'><input name='input2' type='text'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        Assert.assertNotNull(formElement);
        Assert.assertEquals("form", formElement.tagName());
    }

    @Test
    public void testFormElementGetElements() {
        String html = "<form action='/form' method='post'><input name='input1' type='text'><input name='input2' type='text'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        Elements elements = formElement.elements();
        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(2, elements.size());
    }

    @Test
    public void testFormElementAddElement() {
        String html = "<form action='/form' method='post'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        Element inputElement = new Element("input");
        inputElement.attr("name", "input1");
        inputElement.attr("type", "text");
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("input", elements.get(0).tagName());
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        String html = "<form action='/form' method='post'><input name='input' type='text' value='test'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(connection);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("input", formData.get(0).key);
        Assert.assertEquals("test", formData.get(0).value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithoutBaseURI() throws Exception {
        String html = "<form action='' method='post'><input name='input' type='text' value='test'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormElementFormDataEmptyForm() {
        String html = "<form action='/form' method='post'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementFormDataWithTextInput() {
        String html = "<form action='/form' method='post'><input name='input' type='text' value='test'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("input", formData.get(0).key);
        Assert.assertEquals("test", formData.get(0).value);
    }

    @Test
    public void testFormElementFormDataWithSelectInput() {
        String html = "<form action='/form' method='post'><select name='input'><option value='test1'>Test 1</option><option value='test2'>Test 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("input", formData.get(0).key);
        Assert.assertEquals("test1", formData.get(0).value);
    }

    @Test
    public void testFormElementFormDataWithCheckboxInput() {
        String html = "<form action='/form' method 'post'><input type='checkbox' name='input'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());

        html = "<form action='/form' method 'post'><input type='checkbox' name='input' checked></form>";
        doc = Jsoup.parse(html);
        formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("input", formData.get(0).key);
        Assert.assertTrue(formData.get(0).value.isEmpty());
    }

    @Test
    public void testFormElementFormDataWithRadioInput() {
        String html = "<form action='/form' method='post'><input type='radio' name='input'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());

        html = "<form action='/form' method='post'><input type='radio' name='input' checked></form>";
        doc = Jsoup.parse(html);
        formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("input", formData.get(0).key);
        Assert.assertTrue(formData.get(0).value.isEmpty());
    }
}