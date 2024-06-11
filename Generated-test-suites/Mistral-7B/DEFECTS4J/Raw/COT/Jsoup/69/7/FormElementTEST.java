import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertNotNull(formElement);
    }

    @Test
    public void testElementsMethodReturnsEmptyListWhenNoElements() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        List<Element> elements = formElement.elements();
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testElementsMethodReturnsListOfElements() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Tag inputTag1 = new Tag("input");
        Attributes inputAttributes1 = new Attributes().put("name", "input1").put("type", "text");
        Element inputElement1 = new Element(inputTag1, baseUri, inputAttributes1);

        Tag inputTag2 = new Tag("input");
        Attributes inputAttributes2 = new Attributes().put("name", "input2").put("type", "text");
        Element inputElement2 = new Element(inputTag2, baseUri, inputAttributes2);

        formTag.appendElement(inputElement1);
        formTag.appendElement(inputElement2);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        List<Element> elements = formElement.elements();
        Assert.assertEquals(2, elements.size());
        Assert.assertTrue(elements.contains(inputElement1));
        Assert.assertTrue(elements.contains(inputElement2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitThrowsIllegalArgumentExceptionWhenNoActionAttribute() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.submit();
    }

    @Test
    public void testSubmitReturnsConnectionObject() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("action", "https://example.com/submit");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testFormDataContainsExpectedKeyValuePairs() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        // Create form elements
        Tag inputTag1 = new Tag("input");
        Attributes inputAttributes1 = new Attributes().put("name", "input1").put("type", "text").put("value", "value1");
        Element inputElement1 = new Element(inputTag1, baseUri, inputAttributes1);

        Tag inputTag2 = new Tag("input");
        Attributes inputAttributes2 = new Attributes().put("name", "input2").put("type", "text").put("value", "value2");
        Element inputElement2 = new Element(inputTag2, baseUri, inputAttributes2);

        Tag selectTag = new Tag("select");
        Attributes selectAttributes = new Attributes().put("name", "select");
        Element selectElement = new Element(selectTag, baseUri, selectAttributes);

        Tag optionTag1 = new Tag("option");
        Attributes optionAttributes1 = new Attributes().put("value", "option1").put("selected", "selected");
        Element optionElement1 = new Element(optionTag1, baseUri, optionAttributes1);
        selectElement.appendElement(optionElement1);

        Tag optionTag2 = new Tag("option");
        Attributes optionAttributes2 = new Attributes().put("value", "option2");
        Element optionElement2 = new Element(optionTag2, baseUri, optionAttributes2);
        selectElement.appendElement(optionElement2);

        formTag.appendElement(inputElement1);
        formTag.appendElement(inputElement2);
        formTag.appendElement(selectElement);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        List<Connection.KeyVal> formData = formElement.formData();

        // Check key-value pairs
        Assert.assertEquals(3, formData.size());
        Assert.assertEquals("input1", formData.get(0).key);
        Assert.assertEquals("value1", formData.get(0).value);
        Assert.assertEquals("input2", formData.get(1).key);
        Assert.assertEquals("value2", formData.get(1).value);
        Assert.assertEquals("select", formData.get(2).key);
        Assert.assertEquals("option1", formData.get(2).value);
    }

    // Add edge cases and exception handling tests here
}