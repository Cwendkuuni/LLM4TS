import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.*;
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
    public void testElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element inputElement = new Element("input");
        formElement.addElement(inputElement);

        List<Element> expectedElements = new ArrayList<>();
        expectedElements.add(inputElement);
        List<Element> actualElements = formElement.elements();

        Assert.assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testAddElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element inputElement = new Element("input");
        FormElement result = formElement.addElement(inputElement);

        Assert.assertSame(formElement, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseURI() {
        Tag formTag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, null, attributes);
        formElement.submit();
    }

    @Test
    public void testSubmit() {
        Tag formTag = new Tag("form");
        formTag.attr("action", "http://example.com");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "http://example.com", attributes);

        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testFormDataEmptyForm() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        List<Connection.KeyVal> actualFormData = formElement.formData();

        Assert.assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataSingleInputElement() {
        Tag formTag = new Tag("form");
        formTag.attr("action", "http://example.com");
        Tag inputTag = new Tag("input");
        inputTag.attr("name", "name");
        inputTag.attr("type", "text");
        inputTag.attr("value", "value");
        Attributes attributes = new Attributes();
        attributes.put("name", "form");
        FormElement formElement = new FormElement(formTag, null, attributes);
        formElement.addElement(inputTag);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("name", "value"));
        List<Connection.KeyVal> actualFormData = formElement.formData();

        Assert.assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataMultipleInputElements() {
        Tag formTag = new Tag("form");
        formTag.attr("action", "http://example.com");
        Tag inputTag1 = new Tag("input");
        inputTag1.attr("name", "name1");
        inputTag1.attr("type", "text");
        inputTag1.attr("value", "value1");
        Tag inputTag2 = new Tag("input");
        inputTag2.attr("name", "name2");
        inputTag2.attr("type", "text");
        inputTag2.attr("value", "value2");
        Attributes attributes = new Attributes();
        attributes.put("name", "form");
        FormElement formElement = new FormElement(formTag, null, attributes);
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("name1", "value1"));
        expectedFormData.add(new Connection.KeyVal("name2", "value2"));
        List<Connection.KeyVal> actualFormData = formElement.formData();

        Assert.assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataSelectElement() {
        Tag formTag = new Tag("form");
        formTag.attr("action", "http://example.com");
        Tag selectTag = new Tag("select");
        selectTag.attr("name", "name");
        Element optionTag1 = new Element("option");
        optionTag1.attr("value", "option1");
        optionTag1.attr("selected", "selected");
        selectTag.add(optionTag1);
        Element optionTag2 = new Element("option");
        optionTag2.attr("value", "option2");
        selectTag.add(optionTag2);
        Attributes attributes = new Attributes();
        attributes.put("name", "form");
        FormElement formElement = new FormElement(formTag, null, attributes);
        formElement.addElement(selectTag);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("name", "option1"));
        List<Connection.KeyVal> actualFormData = formElement.formData();

        Assert.assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataCheckboxOrRadio() {
        Tag formTag = new Tag("form");
        formTag.attr("action", "http://example.com");
        Tag checkboxTag = new Element("input");
        checkboxTag.attr("name", "name");
        checkboxTag.attr("type", "checkbox");
        checkboxTag.attr("checked", "checked");
        Attributes attributes = new Attributes();
        attributes.put("name", "form");
        FormElement formElement = new FormElement(formTag, null, attributes);
        formElement.addElement(checkboxTag);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("name", ""));
        List<Connection.KeyVal> actualFormData = formElement.formData();

        Assert.assertEquals(expectedFormData, actualFormData);

        checkboxTag.attr("checked", "false");
        actualFormData = formElement.formData();

        Assert.assertEquals(expectedFormData, actualFormData);

        checkboxTag.attr("checked", "checked");
        expectedFormData.add(new Connection.KeyVal("name", ""));
        actualFormData = formElement.formData();

        Assert.assertEquals(expectedFormData, actualFormData);
    }
}