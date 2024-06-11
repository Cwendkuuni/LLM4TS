import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParaserException;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertNotNull(formElement);
        Assert.assertEquals(tag.tagName(), formElement.tagName());
        Assert.assertEquals(baseUri, formElement.baseUri());
        Assert.assertNotNull(formElement.elements());
    }

    @Test
    public void testFormElementElements() {
        Element formElement = createFormElementWithElements();
        Elements elements = formElement.elements();
        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(3, elements.size());

        // Test getting the first element
        Element firstElement = elements.first();
        Assert.assertNotNull(firstElement);
        Assert.assertEquals("input", firstElement.tagName());
        Assert.assertEquals("type", firstElement.attr("type"));
        Assert.assertEquals("text", firstElement.attr("type"));

        // Test getting the last element
        Element lastElement = elements.last();
        Assert.assertNotNull(lastElement);
        Assert.assertEquals("input", lastElement.tagName());
        Assert.assertEquals("submit", lastElement.attr("type"));
    }

    @Test
    public void testFormElementAddElement() {
        Element formElement = createFormElement();
        Element inputElement = createInputElement();

        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(inputElement, elements.first());
    }

    @Test
    public void testFormElementSubmitNormalCase() throws ParserException {
        Element formElement = createFormElementWithElements();
        Connection connection = formElement.submit();
        // Add assertions here to check the Connection object returned by submit()
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitIllegalArgumentException() {
        Element formElement = createFormElement();
        formElement.submit(); // This should throw an IllegalArgumentException
    }

    @Test
    public void testFormElementFormDataNormalCase() {
        Element formElement = createFormElementWithElements();
        List<Connection.KeyVal> formData = formElement.formData();
        // Add assertions here to check the formData list
    }

    private Element createFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        return new FormElement(tag, baseUri, attributes);
    }

    private Element createFormElementWithElements() throws ParserException {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "form-1");
        formTag.attributes().putAll(attributes);

        Element textInput = new Element("input");
        textInput.attributes().put("name", "textInput");
        textInput.attributes().put("type", "text");

        Element selectInput = new Element("select");
        selectInput.attributes().put("name", "selectInput");

        Element option1 = new Element("option");
        option1.text("Option 1");
        option1.attributes().put("value", "Option1");
        selectInput.add(option1);

        Element option2 = new Element("option");
        option2.text("Option 2");
        option2.attributes().put("value", "Option2");
        selectInput.add(option2);

        selectInput.attributes().put("selected", "selected");

        Element checkboxInput = new Element("input");
        checkboxInput.attributes().put("name", "checkboxInput");
        checkboxInput.attributes().put("type", "checkbox");
        checkboxInput.attributes().put("checked", "checked");

        Element radioInput1 = new Element("input");
        radioInput1.attributes().put("name", "radioInput");
        radioInput1.attributes().put("type", "radio");
        radioInput1.attributes().put("value", "Radio1");
        radioInput1.attributes().put("checked", "checked");

        Element radioInput2 = new Element("input");
        radioInput2.attributes().put("name", "radioInput");
        radioInput2.attributes().put("type", "radio");
        radioInput2.attributes().put("value", "Radio2");

        formTag.appendElement(textInput);
        formTag.appendElement(selectInput);
        formTag.appendElement(checkboxInput);
        formTag.appendElement(radioInput1);
        formTag.appendElement(radioInput2);

        return new FormElement(formTag, baseUri, new Attributes());
    }

    private Element createInputElement() {
        Tag inputTag = new Tag("input");
        inputTag.attributes().put("name", "exampleInput");
        inputTag.attributes().put("type", "text");
        return new Element("input") {
            {
                this.tag = inputTag;
                this.baseUri = "http://example.com";
                this.attributes = new Attributes();
            }
        };
    }

}