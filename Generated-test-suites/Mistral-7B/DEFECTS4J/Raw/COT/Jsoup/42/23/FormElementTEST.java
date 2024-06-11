import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
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
    }

    @Test
    public void testElementsMethod() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element element1 = new Element("input", new Attributes().put("type", "text").put("name", "name1"));
        Element element2 = new Element("input", new Attributes().put("type", "text").put("name", "name2"));
        formElement.addElement(element1);
        formElement.addElement(element2);
        Elements expectedElements = new Elements(element1, element2);
        Elements actualElements = formElement.elements();
        Assert.assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testAddElementMethod() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element element = new Element("input", new Attributes().put("type", "text").put("name", "name"));
        FormElement actualFormElement = formElement.addElement(element);
        Element expectedElement = element;
        Assert.assertEquals(expectedElement, actualFormElement.elements().first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodNoActionAttribute() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodInvalidMethod() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("method", "invalidMethod");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.submit();
    }

    @Test
    public void testFormDataMethodEmptyForm() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        List<Connection.KeyVal> actualData = formElement.formData();
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testFormDataMethodSingleTextInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element textInputTag = new Element("input", new Attributes().put("type", "text").put("name", "name"));
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(textInputTag);
        String textInputValue = "value";
        textInputTag.text(textInputValue);
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("name", textInputValue));
        List<Connection.KeyVal> actualData = formElement.formData();
        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testFormDataMethodSingleSelectElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element selectTag = new Element("select", new Attributes());
        Element option1Tag = new Element("option", new Attributes().put("value", "value1").put("selected", "selected"));
        Element option2Tag = new Element("option", new Attributes().put("value", "value2"));
        selectTag.add(option1Tag);
        selectTag.add(option2Tag);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(selectTag);
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("name", "value1"));
        List<Connection.KeyVal> actualData = formElement.formData();
        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testFormDataMethodMultipleElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element textInputTag1 = new Element("input", new Attributes().put("type", "text").put("name", "name1"));
        Element textInputTag2 = new Element("input", new Attributes().put("type", "text").put("name", "name2"));
        Element selectTag = new Element("select", new Attributes());
        Element option1Tag1 = new Element("option", new Attributes().put("value", "value11").put("selected", "selected"));
        Element option1Tag2 = new Element("option", new Attributes().put("value", "value12"));
        Element option2Tag = new Element("option", new Attributes().put("value", "value2"));
        selectTag.add(option1Tag1);
        selectTag.add(option1Tag2);
        selectTag.add(option2Tag);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(textInputTag1);
        formElement.addElement(textInputTag2);
        formElement.addElement(selectTag);
        textInputTag1.text("value1");
        textInputTag2.text("value2");
        option1Tag1.text("option1Value");
        option1Tag2.text("option2Value");
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("name1", "value1"));
        expectedData.add(HttpConnection.KeyVal.create("name2", "value2"));
        expectedData.add(HttpConnection.KeyVal.create("name", "option1Value"));
        List<Connection.KeyVal> actualData = formElement.formData();
        Assert.assertEquals(expectedData, actualData);
    }
}