import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        String html = "<html><body><form><input type='text' name='input1'></form></body></html>";
        Document document = Jsoup.parse(html);
        Element formElement = document.selectFirst("form");
        Elements expectedElements = document.select("input[name='input1']");
        Elements actualElements = formElement.elements();
        Assert.assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            Assert.assertEquals(expectedElements.get(i), actualElements.get(i));
        }
    }

    @Test
    public void testAddElement() {
        String html = "<html><body><form></form></body></html>";
        Document document = Jsoup.parse(html);
        Element formElement = document.selectFirst("form");
        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "input1");
        formElement.addElement(inputElement);
        Elements actualElements = formElement.elements();
        Element expectedElement = document.selectFirst("input[name='input1']");
        Assert.assertEquals(expectedElement, actualElements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodNoAction() {
        String html = "<html><body><form></form></body></html>";
        Document document = Jsoup.parse(html);
        Element formElement = document.selectFirst("form");
        formElement.submit();
    }

    @Test
    public void testSubmitMethodValidAction() {
        String html = "<html><body><form action='http://test.com'></form></body></html>";
        Document document = Jsoup.parse(html);
        Element formElement = document.selectFirst("form");
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormDataEmptyForm() {
        String html = "<html><body><form></form></body></html>";
        Document document = Jsoup.parse(html);
        Element formElement = document.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
    }

    @Test
    public void testFormDataBasicForm() {
        String html = "<html><body><form><input type='text' name='input1' value='test'></form></body></html>";
        Document document = Jsoup.parse(html);
        Element formElement = document.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("input1", formData.get(0).key);
        Assert.assertEquals("test", formData.get(0).value);
    }

    @Test
    public void testFormDataMultipleElements() {
        String html = "<html><body><form><input type='text' name='input1' value='test1'><input type='text' name='input2' value='test2'></form></body></html>";
        Document document = Jsoup.parse(html);
        Element formElement = document.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("input1", formData.get(0).key);
        Assert.assertEquals("test1", formData.get(0).value);
        Assert.assertEquals("input2", formData.get(1).key);
        Assert.assertEquals("test2", formData.get(1).value);
    }

    @Test
    public void testFormDataSelectElement() {
        String html = "<html><body><form><select name='select1'><option value='option1'>Option 1</option><option value='option2' selected>Option 2</option></select></form></body></html>";
        Document document = Jsoup.parse(html);
        Element formElement = document.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("select1", formData.get(0).key);
        Assert.assertEquals("option2", formData.get(0).value);
    }

    @Test
    public void testFormDataCheckboxAndRadioElements() {
        String html = "<html><body><form><input type='checkbox' name='checkbox1' checked><input type='radio' name='radio1' checked></form></body></html>";
        Document document = Jsoup.parse(html);
        Element formElement = document.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("checkbox1", formData.get(0).key);
        Assert.assertEquals("on", formData.get(0).value);
        Assert.assertEquals("radio1", formData.get(1).key);
        Assert.assertEquals("on", formData.get(1).value);
    }

    @Test
    public void testEqualsMethod() {
        Element element1 = new Element("form");
        Element element2 = new Element("form");
        Assert.assertTrue(element1.equals(element2));
    }

    @Test
    public void testEqualsMethodDifferentElements() {
        Element element1 = new Element("form");
        Element element2 = new Element("div");
        Assert.assertFalse(element1.equals(element2));
    }

    @Test
    public void testEqualsMethodSameElementsButDifferentAttributes() {
        Element element1 = new Element("form").attr("id", "form1");
        Element element2 = new Element("form").attr("id", "form2");
        Assert.assertFalse(element1.equals(element2));
    }

    @Test
    public void testEqualsMethodSameElementsButDifferentTags() {
        Element element1 = new Element("form").attr("tag", "form1");
        Element element2 = new Element("div").attr("tag", "form1");
        Assert.assertFalse(element1.equals(element2));
    }

    @Test
    public void testEqualsMethodSameElementsButDifferentText() {
        Element element1 = new Element("form").text("Form 1");
        Element element2 = new Element("form").text("Form 2");
        Assert.assertFalse(element1.equals(element2));
    }

    @Test
    public void testEqualsMethodSameElementsButSameContent() {
        Element element1 = new Element("form").text("Form 1");
        Element element2 = new Element("form").text("Form 1");
        Assert.assertTrue(element1.equals(element2));
    }
}