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
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Assert.assertNotNull(formElement);
        Assert.assertEquals("form", formElement.tagName());
    }

    @Test
    public void testElementsMethod() {
        FormElement formElement = createFormElementWithElements();
        Elements expectedElements = new Elements();
        expectedElements.add(createInputElement());
        expectedElements.add(createTextareaElement());

        Elements actualElements = formElement.elements();

        Assert.assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            Assert.assertEquals(expectedElements.get(i).tagName(), actualElements.get(i).tagName());
        }
    }

    @Test
    public void testAddElementMethod() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element inputElement = createInputElement();

        formElement.addElement(inputElement);

        Elements actualElements = formElement.elements();
        Elements expectedElements = new Elements();
        expectedElements.add(inputElement);

        Assert.assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            Assert.assertEquals(expectedElements.get(i).tagName(), actualElements.get(i).tagName());
        }
    }

    @Test
    public void testRemoveChildMethod() {
        FormElement formElement = createFormElementWithElements();
        Element elementToRemove = formElement.elements().get(0);

        formElement.removeChild(elementToRemove);

        Elements actualElements = formElement.elements();
        Elements expectedElements = new Elements();
        expectedElements.add(formElement.elements().get(1));

        Assert.assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            Assert.assertEquals(expectedElements.get(i).tagName(), actualElements.get(i).tagName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithoutActionAttribute() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmitMethodWithValidActionAttribute() {
        FormElement formElement = createFormElementWithActionAttribute();
        Connection connection = formElement.submit();

        // Add assertions for connection object properties and methods here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormDataMethodWithoutFormControls() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataMethodWithSingleFormControl() {
        FormElement formElement = createFormElementWithSingleFormControl();
        List<Connection.KeyVal> formData = formElement.formData();

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("name", "value"));

        Assert.assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Assert.assertEquals(expectedFormData.get(i).key, formData.get(i).key);
            Assert.assertEquals(expectedFormData.get(i).value, formData.get(i).value);
        }
    }

    @Test
    public void testFormDataMethodWithMultipleFormControls() {
        FormElement formElement = createFormElementWithMultipleFormControls();
        List<Connection.KeyVal> formData = formElement.formData();

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("name1", "value1"));
        expectedFormData.add(HttpConnection.KeyVal.create("name2", "value2"));

        Assert.assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Assert.assertEquals(expectedFormData.get(i).key, formData.get(i).key);
            Assert.assertEquals(expectedFormData.get(i).value, formData.get(i).value);
        }
    }

    private FormElement createFormElementWithElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element inputElement = new InputElement("input");
        Element textareaElement = new TextareaElement("textarea");

        formTag.attributes().put("action", "http://example.com/submit");
        formTag.appendChild(inputElement);
        formTag.appendChild(textareaElement);

        return new FormElement(formTag, baseUri, attributes);
    }

    private FormElement createFormElementWithActionAttribute() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        formTag.attributes().put("action", "http://example.com/submit");

        return new FormElement(formTag, baseUri, attributes);
    }

    private FormElement createFormElementWithSingleFormControl() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element inputElement = new InputElement("input")
                .attributes()
                .put("name", "name")
                .put("value", "value")
                .end();

        formTag.appendChild(inputElement);

        return new FormElement(formTag, baseUri, attributes);
    }

    private FormElement createFormElementWithMultipleFormControls() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element inputElement1 = new InputElement("input")
                .attributes()
                .put("name", "name1")
                .put("value", "value1")
                .end();

        Element inputElement2 = new InputElement("input")
                .attributes()
                .put("name", "name2")
                .put("value", "value2")
                .end();

        Element textareaElement = new TextareaElement("textarea");

        formTag.appendChild(inputElement1);
        formTag.appendChild(inputElement2);
        formTag.appendChild(textareaElement);

        return new FormElement(formTag, baseUri, attributes);
    }

    private Element createInputElement() {
        Tag inputTag = new Tag("input");
        inputTag.attributes().put("type", "text");
        inputTag.attributes().put("name", "name");
        inputTag.attributes().put("value", "value");

        return inputTag;
    }

    private Element createTextareaElement() {
        Tag textareaTag = new Tag("textarea");
        textareaTag.attributes().put("name", "name");

        return textareaTag;
    }

    private static class InputElement extends Element {
        public InputElement(String tagName) {
            super(tagName);
        }

        public InputElement attributes() {
            return (InputElement) super.attributes();
        }
    }

    private static class TextareaElement extends Element {
        public TextareaElement(String tagName) {
            super(tagName);
        }

        public TextareaElement attributes() {
            return (TextareaElement) super.attributes();
        }
    }
}