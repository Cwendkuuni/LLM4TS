import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        FormElement formElement = getFormElementWithControls();
        Elements elements = formElement.elements();
        Assert.assertFalse(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = getFormElementWithControls();
        Element controlElement = getControlElement();
        formElement.addElement(controlElement);
        Elements elements = formElement.elements();
        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(2, elements.size());
    }

    @Test
    public void testSubmitNullBaseUri() {
        FormElement formElement = getFormElementWithControls();
        try {
            formElement.submit();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubmitEmptyAction() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        try {
            formElement.submit();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubmitInvalidMethod() {
        FormElement formElement = getFormElementWithControls();
        try {
            formElement.submit().method(Connection.Method.HEAD);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormDataEmptyList() {
        FormElement formElement = getFormElementWithControls();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSingleControl() {
        FormElement formElement = getFormElementWithSingleControl();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("name1", keyVal.key);
        Assert.assertEquals("value1", keyVal.val);
    }

    @Test
    public void testFormDataMultipleControls() {
        FormElement formElement = getFormElementWithMultipleControls();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(3, formData.size());

        Connection.KeyVal name1Val1KeyVal = formData.get(0);
        Assert.assertEquals("name1", name1Val1KeyVal.key);
        Assert.assertEquals("value1", name1Val1KeyVal.val);

        Connection.KeyVal name2Val1KeyVal = formData.get(1);
        Assert.assertEquals("name2", name2Val1KeyVal.key);
        Assert.assertEquals("value2", name2Val1KeyVal.val);

        Connection.KeyVal name3Val2KeyVal = formData.get(2);
        Assert.assertEquals("name3", name3Val2KeyVal.key);
        Assert.assertEquals("value3", name3Val2KeyVal.val);
    }

    @Test
    public void testFormDataDisabledControl() {
        FormElement formElement = getFormElementWithDisabledControl();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
    }

    @Test
    public void testFormDataSelectEmpty() {
        FormElement formElement = getFormElementWithSelectEmpty();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSelectSingleOption() {
        FormElement formElement = getFormElementWithSelectSingleOption();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("name", keyVal.key);
        Assert.assertEquals("option1", keyVal.val);
    }

    @Test
    public void testFormDataSelectMultipleOptions() {
        FormElement formElement = getFormElementWithSelectMultipleOptions();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());

        Connection.KeyVal nameOption1KeyVal = formData.get(0);
        Assert.assertEquals("name", nameOption1KeyVal.key);
        Assert.assertEquals("option1", nameOption1KeyVal.val);

        Connection.KeyVal nameOption2KeyVal = formData.get(1);
        Assert.assertEquals("name", nameOption2KeyVal.key);
        Assert.assertEquals("option2", nameOption2KeyVal.val);
    }

    @Test
    public void testFormDataCheckboxUnchecked() {
        FormElement formElement = getFormElementWithCheckboxUnchecked();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataCheckboxChecked() {
        FormElement formElement = getFormElementWithCheckboxChecked();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("name", keyVal.key);
        Assert.assertEquals("on", keyVal.val);
    }

    private FormElement getFormElementWithControls() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element name1Tag = new Tag("input");
        name1Tag.puts("type", "text");
        name1Tag.puts("name", "name1");
        Element name2Tag = new Tag("textarea");
        name2Tag.puts("name", "name2");
        formElement.addElement(name1Tag);
        formElement.addElement(name2Tag);
        return formElement;
    }

    private Element getControlElement() {
        Tag tag = new Tag("input");
        tag.puts("type", "text");
        tag.puts("name", "name1");
        return new Element("input", tag.attributes());
    }

    private FormElement getFormElementWithSingleControl() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element controlElement = getControlElement();
        formElement.addElement(controlElement);
        return formElement;
    }

    private FormElement getFormElementWithMultipleControls() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element name1Tag = new Tag("input");
        name1Tag.puts("type", "text");
        name1Tag.puts("name", "name1");
        Element name2Tag = new Tag("input");
        name2Tag.puts("type", "text");
        name2Tag.puts("name", "name2");
        Element name3Tag = new Tag("textarea");
        name3Tag.puts("name", "name3");
        formElement.addElement(name1Tag);
        formElement.addElement(name2Tag);
        formElement.addElement(name3Tag);
        return formElement;
    }

    private FormElement getFormElementWithDisabledControl() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("disabled", "");
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element controlElement = getControlElement();
        formElement.addElement(controlElement);
        return formElement;
    }

    private FormElement getFormElementWithSelectEmpty() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element selectTag = new Tag("select");
        selectTag.puts("name", "name");
        formElement.addElement(selectTag);
        return formElement;
    }

    private FormElement getFormElementWithSelectSingleOption() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element selectTag = new Tag("select");
        selectTag.puts("name", "name");
        Element optionTag = new Tag("option");
        optionTag.puts("value", "option1");
        selectTag.add(optionTag);
        formElement.addElement(selectTag);
        return formElement;
    }

    private FormElement getFormElementWithSelectMultipleOptions() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element selectTag = new Tag("select");
        selectTag.puts("name", "name");
        Element optionTag1 = new Tag("option");
        optionTag1.puts("value", "option1");
        Element optionTag2 = new Tag("option");
        optionTag2.puts("value", "option2");
        selectTag.add(optionTag1);
        selectTag.add(optionTag2);
        formElement.addElement(selectTag);
        return formElement;
    }

    private FormElement getFormElementWithCheckboxUnchecked() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element checkboxTag = new Tag("input");
        checkboxTag.puts("type", "checkbox");
        checkboxTag.puts("name", "name");
        formElement.addElement(checkboxTag);
        return formElement;
    }

    private FormElement getFormElementWithCheckboxChecked() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element checkboxTag = new Tag("input");
        checkboxTag.puts("type", "checkbox");
        checkboxTag.puts("name", "name");
        checkboxTag.puts("checked", "");
        formElement.addElement(checkboxTag);
        return formElement;
    }
}