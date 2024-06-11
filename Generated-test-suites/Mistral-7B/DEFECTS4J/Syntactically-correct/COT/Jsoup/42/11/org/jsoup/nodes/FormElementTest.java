import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        Elements expectedElements = new Elements();
        Element element1 = new Element("input");
        Element element2 = new Element("textarea");
        expectedElements.add(element1);
        expectedElements.add(element2);
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.addElement(element1);
        formElement.addElement(element2);
        Elements actualElements = formElement.elements();
        Assert.assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element element = new Element("input");
        FormElement result = formElement.addElement(element);
        Elements elements = result.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitIllegalArgumentException() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormDataEmptyList() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSingleElement() {
        Element element = new Element("input");
        element.attr("name", "testName");
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.addElement(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Connection.KeyVal expectedKeyVal = new Connection.KeyVal("testName", element.val());
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals(expectedKeyVal, formData.get(0));
    }

    @Test
    public void testFormDataMultipleElements() {
        Element element1 = new Element("input");
        element1.attr("name", "testName1");
        element1.attr("value", "testValue1");

        Element element2 = new Element("input");
        element2.attr("name", "testName2");
        element2.attr("value", "testValue2");

        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();
        Connection.KeyVal expectedKeyVal1 = new Connection.KeyVal("testName1", "testValue1");
        Connection.KeyVal expectedKeyVal2 = new Connection.KeyVal("testName2", "testValue2");

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals(expectedKeyVal1, formData.get(0));
        Assert.assertEquals(expectedKeyVal2, formData.get(1));
    }

    @Test
    public void testSubmit() {
        // This test case is hard to write since it requires actually making a network request, which is beyond the scope of JUnit.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitIllegalBaseUri() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.submit();
    }
}