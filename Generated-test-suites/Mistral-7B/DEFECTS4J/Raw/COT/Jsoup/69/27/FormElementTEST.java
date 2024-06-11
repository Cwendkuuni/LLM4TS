import org.junit.Assert;
import org.junit.Test;
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
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Assert.assertNotNull(formElement);
        Assert.assertEquals("form", formElement.tagName());
        Assert.assertEquals(baseUri, formElement.baseUri());
        Assert.assertNull(formElement.elements());
    }

    @Test
    public void testGetElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element textInput = new Element("input", attributes.put("type", "text"));
        textInput.appendText("John Doe");

        Element selectElement = new Element("select", attributes.put("name", "country"));
        Element optionElement1 = new Element("option", attributes.put("value", "USA"));
        Element optionElement2 = new Element("option", attributes.put("value", "Canada"));
        selectElement.appendElement(optionElement1);
        selectElement.appendElement(optionElement2);

        formTag.appendElement(textInput);
        formTag.appendElement(selectElement);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(2, elements.size());
        Assert.assertTrue(elements.contains(textInput));
        Assert.assertTrue(elements.contains(selectElement));
    }

    @Test
    public void testAddElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element textInput = new Element("input", new Attributes().put("type", "text"));

        FormElement formElementAddedTextInput = formElement.addElement(textInput);

        Elements elements = formElementAddedTextInput.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(1, elements.size());
        Assert.assertTrue(elements.contains(textInput));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.submit();
    }

    @Test
    public void testSubmit() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        String actionUrl = "http://example.com/submit";
        Attributes attributes = new Attributes().put("action", actionUrl);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element textInput = new Element("input", new Attributes().put("name", "username").put("type", "text").put("value", "John Doe"));
        formElement.addElement(textInput);

        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals(actionUrl, connection.url());
    }

    @Test
    public void testFormDataEmptyForm() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithTextInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element textInput = new Element("input", new Attributes().put("name", "username").put("type", "text").put("value", "John Doe"));

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(textInput);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("username", formData.get(0).key());
        Assert.assertEquals("John Doe", formData.get(0).value());
    }

    @Test
    public void testFormDataWithSelectElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element selectElement = new Element("select", new Attributes().put("name", "country"));
        Element optionElement1 = new Element("option", new Attributes().put("value", "USA"));
        Element optionElement2 = new Element("option", new Attributes().put("value", "Canada"));
        selectElement.appendElement(optionElement1);
        selectElement.appendElement(optionElement2);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());

        selectElement.select("option[selected]").first().attr("selected", true);

        formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("country", formData.get(0).key());
        Assert.assertEquals("USA", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckbox() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element checkboxElement = new Element("input", new Attributes().put("name", "option1").put("type", "checkbox"));
        checkboxElement.attr("checked", "checked");

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("option1", formData.get(0).key());
        Assert.assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormDataWithRadio() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element radioGroup = new Element("div");
        radioGroup.attr("name", "gender");

        Element radioButtonMale = new Element("input", new Attributes().put("type", "radio").put("name", "gender").put("value", "male"));
        radioButtonMale.appendText("Male");
        radioGroup.appendElement(radioButtonMale);

        Element radioButtonFemale = new Element("input", new Attributes().put("type", "radio").put("name", "gender").put("value", "female"));
        radioButtonFemale.appendText("Female");
        radioGroup.appendElement(radioButtonFemale);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(radioGroup);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());

        radioButtonMale.attr("checked", true);

        formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("gender", formData.get(0).key());
        Assert.assertEquals("male", formData.get(0).value());
    }
}