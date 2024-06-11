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
        Assert.assertEquals("form", formElement.tagName());
        Assert.assertEquals(baseUri, formElement.baseUri());
    }

    @Test
    public void testGetElements() {
        Element formElement = createFormElementWithControls();
        Elements expectedElements = new Elements();
        expectedElements.add(createInputElement("text", "textInput"));
        expectedElements.add(createInputElement("checkbox", "checkBox"));
        expectedElements.add(createSelectElement("select", "select"));

        Elements actualElements = formElement.elements();
        Assert.assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            Element expectedElement = expectedElements.get(i);
            Element actualElement = actualElements.get(i);
            Assert.assertEquals(expectedElement.tagName(), actualElement.tagName());
        }
    }

    @Test
    public void testAddElement() {
        Element formElement = createFormElement();
        Element inputElement = createInputElement("text", "textInput");
        formElement.addElement(inputElement);
        Elements actualElements = formElement.elements();
        Element expectedElement = createInputElement("text", "textInput");
        Assert.assertEquals(expectedElement.tagName(), actualElements.get(0).tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        Element formElement = createFormElement();
        formElement.submit();
    }

    @Test
    public void testSubmitValidFormData() {
        Element formElement = createFormElementWithControls();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> expectedFormData = createFormData();
        List<Connection.KeyVal> actualFormData = connection.requestBody();
        Assert.assertEquals(expectedFormData.size(), actualFormData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Connection.KeyVal expectedKeyVal = expectedFormData.get(i);
            Connection.KeyVal actualKeyVal = actualFormData.get(i);
            Assert.assertEquals(expectedKeyVal.key(), actualKeyVal.key());
            Assert.assertEquals(expectedKeyVal.value(), actualKeyVal.value());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidFormDataWhenNoName() {
        Element formElement = createFormElementWithControls();
        Element inputElementWithoutName = createInputElement("text", "");
        formElement.addElement(inputElementWithoutName);
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidFormDataWhenEmptyValue() {
        Element formElement = createFormElementWithControls();
        Element inputElementWithEmptyValue = createInputElement("text", "textInput");
        inputElementWithEmptyValue.attr("value", "");
        formElement.addElement(inputElementWithEmptyValue);
        formElement.submit();
    }

    @Test
    public void testFormDataSelectWithSingleOption() {
        Element formElement = createFormElementWithSelectSingle();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> expectedFormData = createFormDataWithSelectSingleOption("singleOptionValue");
        List<Connection.KeyVal> actualFormData = connection.requestBody();
        Assert.assertEquals(expectedFormData.size(), actualFormData.size());
        Assert.assertEquals(expectedFormData.get(0).key(), actualFormData.get(0).key());
        Assert.assertEquals(expectedFormData.get(0).value(), actualFormData.get(0).value());
    }

    @Test
    public void testFormDataSelectWithMultipleOptions() {
        Element formElement = createFormElementWithSelectMultiple();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> expectedFormData = createFormDataWithSelectMultipleOptions("option1Value", "option2Value");
        List<Connection.KeyVal> actualFormData = connection.requestBody();
        Assert.assertEquals(expectedFormData.size(), actualFormData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Connection.KeyVal expectedKeyVal = expectedFormData.get(i);
            Connection.KeyVal actualKeyVal = actualFormData.get(i);
            Assert.assertEquals(expectedKeyVal.key(), actualKeyVal.key());
            Assert.assertEquals(expectedKeyVal.value(), actualKeyVal.value());
        }
    }

    @Test
    public void testFormDataCheckboxesOrRadios() {
        Element formElement = createFormElementWithCheckboxesOrRadios();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> expectedFormData = createFormDataWithCheckboxesOrRadios("checkedOptionValue");
        List<Connection.KeyVal> actualFormData = connection.requestBody();
        Assert.assertEquals(expectedFormData.size(), actualFormData.size());
        Assert.assertEquals(expectedFormData.get(0).key(), actualFormData.get(0).key());
        Assert.assertEquals(expectedFormData.get(0).value(), actualFormData.get(0).value());
    }

    private Element createFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        return new FormElement(tag, baseUri, attributes);
    }

    private Element createFormElementWithControls() {
        Element formElement = createFormElement();
        Element inputElementText = createInputElement("text", "textInput");
        Element inputElementCheckbox = createInputElement("checkbox", "checkBox");
        Element selectElement = createSelectElement("select", "select");
        formElement.addElement(inputElementText);
        formElement.addElement(inputElementCheckbox);
        formElement.addElement(selectElement);
        return formElement;
    }

    private Element createInputElement(String type, String name) {
        Tag tagInput = new Tag("input");
        tagInput.attr("type", type);
        tagInput.attr("name", name);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        return new Element("input", baseUri, attributes) {
            @Override
            public String tagName() {
                return "input";
            }
        };
    }

    private Element createSelectElement(String tagName, String name) {
        Tag tagSelect = new Tag(tagName);
        tagSelect.attr("name", name);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        return new Element(tagSelect, baseUri, attributes) {
            @Override
            public String tagName() {
                return tagName;
            }
        };
    }

    private List<Connection.KeyVal> createFormData() {
        ArrayList<Connection.KeyVal> data = new ArrayList<>();
        data.add(HttpConnection.KeyVal.create("textInput", "textInputValue"));
        data.add(HttpConnection.KeyVal.create("checkBox", "checkBoxValue"));
        data.add(HttpConnection.KeyVal.create("select", "singleOptionValue"));
        return data;
    }

    private List<Connection.KeyVal> createFormDataWithSelectSingleOption(String value) {
        ArrayList<Connection.KeyVal> data = new ArrayList<>();
        data.add(HttpConnection.KeyVal.create("select", value));
        return data;
    }

    private List<Connection.KeyVal> createFormDataWithSelectMultipleOptions(String value1, String value2) {
        ArrayList<Connection.KeyVal> data = new ArrayList<>();
        data.add(HttpConnection.KeyVal.create("select[]", value1));
        data.add(HttpConnection.KeyVal.create("select[]", value2));
        return data;
    }

    private List<Connection.KeyVal> createFormDataWithCheckboxesOrRadios(String value) {
        ArrayList<Connection.KeyVal> data = new ArrayList<>();
        data.add(HttpConnection.KeyVal.create("checkBoxOrRadio", value));
        return data;
    }
}