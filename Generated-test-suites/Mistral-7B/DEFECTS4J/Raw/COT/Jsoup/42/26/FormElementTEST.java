import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertEquals(tag, formElement.tag());
        Assert.assertEquals(baseUri, formElement.baseUri());
        Assert.assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testGetElements() {
        Element formElement = createFormElementWithControls();
        Elements elements = formElement.elements();
        // Add assertions to check the elements returned by the getElements() method
    }

    @Test
    public void testAddElement() {
        Element formElement = createFormElement();
        Element controlElement = createControlElement();
        formElement.addElement(controlElement);
        Elements elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(controlElement, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitThrowsIllegalArgumentException() {
        Element formElement = createFormElementWithoutAction();
        formElement.submit();
    }

    @Test
    public void testSubmitCreatesConnectionWithFormData() {
        Element formElement = createFormElementWithControls();
        Connection connection = formElement.submit();
        // Add assertions to check the connection object created by the submit() method
    }

    @Test
    public void testFormDataContainsCorrectKeyValuePairs() {
        Element formElement = createFormElementWithControls();
        List<Connection.KeyVal> formData = formElement.formData();
        // Add assertions to check the key-value pairs in the form data list
    }

    @Test
    public void testEqualsMethod() {
        Element formElement1 = createFormElement();
        Element formElement2 = createFormElement();
        Assert.assertTrue(formElement1.equals(formElement2));

        formElement1.tag(new Tag("button"));
        Assert.assertFalse(formElement1.equals(formElement2));
    }

    private Element createFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        return new FormElement(tag, baseUri, attributes);
    }

    private Element createFormElementWithControls() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element textInput = new Element("input", new Attributes().put("name", "username").put("type", "text"));
        Element selectElement = new Element("select", new Attributes().put("name", "color"));
        Element option1 = new Element("option", new Attributes().put("value", "red"));
        Element option2 = new Element("option", new Attributes().put("value", "green"));
        selectElement.add(option1);
        selectElement.add(option2);
        formTag.add(textInput);
        formTag.add(selectElement);
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(textInput);
        formElement.addElement(selectElement);
        return formElement;
    }

    private Element createControlElement() {
        Tag controlTag = new Tag("input");
        Element controlElement = new Element("input", new Attributes().put("name", "controlName").put("type", "text"));
        return controlElement;
    }

    private Element createFormElementWithoutAction() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        return formElement;
    }
}