import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Content;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes().put("id", "formElementTest");
        FormElement formElement = new FormElement(tag, "http://test.com", attributes);
        Assert.assertEquals("form", formElement.tagName());
        Assert.assertEquals("http://test.com", formElement.baseUri());
        Assert.assertNotNull(formElement.elements());
    }

    @Test
    public void testElementsMethodReturnsEmptyListByDefault() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "http://test.com", new Attributes());
        Assert.assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testAddElementMethodAddsElementToForm() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input");
        Attributes formAttributes = new Attributes().put("id", "formElementTest");
        FormElement formElement = new FormElement(formTag, "http://test.com", formAttributes);
        Attributes inputAttributes = new Attributes().put("type", "text").put("name", "inputName");
        Element inputElement = new Element(inputTag, "", inputAttributes);

        formElement.addElement(inputElement);
        Elements elements = formElement.elements();

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(inputElement, elements.get(0));
    }

    @Test
    public void testSubmitMethodThrowsIllegalArgumentExceptionWhenActionUrlCannotBeDetermined() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        try {
            formElement.submit();
            Assert.fail("Expected IllegalArgumentException when form's action URL cannot be determined.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubmitMethodPreparesConnectionWithFormValues() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes().put("id", "formElementTest");
        FormElement formElement = new FormElement(formTag, "http://test.com", formAttributes);

        Element inputTag = new Tag("input");
        Attributes inputAttributes = new Attributes().put("name", "inputName").put("type", "text").put("value", "testValue");
        Element inputElement = new Element(inputTag, "", inputAttributes);

        formElement.addElement(inputElement);

        Connection connection = formElement.submit();

        List<Connection.KeyVal> formData = connection.data();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("inputName", formData.get(0).key());
        Assert.assertEquals("testValue", formData.get(0).value());
    }

    @Test
    public void testFormDataMethodReturnsEmptyListWhenFormHasNoControls() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "http://test.com", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataMethodReturnsListWithValuesOfFormControls() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes().put("id", "formElementTest");
        FormElement formElement = new FormElement(formTag, "http://test.com", formAttributes);

        Element inputTag1 = new Tag("input");
        Attributes inputAttributes1 = new Attributes().put("name", "inputName1").put("type", "text").put("value", "testValue1");
        Element inputElement1 = new Element(inputTag1, "", inputAttributes1);
        formElement.addElement(inputElement1);

        Element inputTag2 = new Tag("input");
        Attributes inputAttributes2 = new Attributes().put("name", "inputName2").put("type", "text").put("value", "testValue2");
        Element inputElement2 = new Element(inputTag2, "", inputAttributes2);
        formElement.addElement(inputElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("inputName1", formData.get(0).key());
        Assert.assertEquals("testValue1", formData.get(0).value());
        Assert.assertEquals("inputName2", formData.get(1).key());
        Assert.assertEquals("testValue2", formData.get(1).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodThrowsIllegalArgumentExceptionWhenFormHasNoActionAttribute() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormDataMethodHandlesSelectElements() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes().put("id", "formElementTest");
        FormElement formElement = new FormElement(formTag, "http://test.com", formAttributes);

        Element selectTag = new Tag("select");
        Attributes selectAttributes = new Attributes().put("name", "selectName");
        Elements options = new Elements();

        Content optionContent1 = new Content("Option 1");
        Element optionElement1 = new Element("option", optionContent1, new Attributes().put("value", "optionValue1"));
        options.add(optionElement1);

        Content optionContent2 = new Content("Option 2");
        Element optionElement2 = new Element("option", optionContent2, new Attributes().put("selected", "true").put("value", "optionValue2"));
        options.add(optionElement2);

        Element selectElement = new Element(selectTag, "", selectAttributes);
        selectElement.select("option").addAll(options);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("selectName", formData.get(0).key());
        Assert.assertEquals("optionValue2", formData.get(0).value());
    }

    @Test
    public void testFormDataMethodHandlesCheckboxAndRadioElements() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes().put("id", "formElementTest");
        FormElement formElement = new FormElement(formTag, "http://test.com", formAttributes);

        Element checkboxTag = new Tag("input");
        Attributes checkboxAttributes = new Attributes().put("name", "checkboxName").put("type", "checkbox");
        Element checkboxElement = new Element(checkboxTag, "", checkboxAttributes);
        formElement.addElement(checkboxElement);

        Element checkboxCheckedTag = new Tag("input");
        Attributes checkboxCheckedAttributes = new Attributes().put("name", "checkboxName").put("type", "checkbox").put("checked", "checked");
        Element checkboxCheckedElement = new Element(checkboxCheckedTag, "", checkboxCheckedAttributes);
        formElement.addElement(checkboxCheckedElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("checkboxName", formData.get(0).key());
        Assert.assertEquals("on", formData.get(0).value());

        formElement.remove(checkboxElement);
        formElement.remove(checkboxCheckedElement);

        Element radioTag1 = new Tag("input");
        Attributes radioAttributes1 = new Attributes().put("name", "radioName").put("type", "radio");
        Element radioElement1 = new Element(radioTag1, "", new Attributes().put("value", "option1"));
        formElement.addElement(radioElement1);

        Element radioTag2 = new Tag("input");
        Attributes radioAttributes2 = new Attributes().put("name", "radioName").put("type", "radio");
        Element radioElement2 = new Element(radioTag2, "", new Attributes().put("value", "option2").put("checked", "checked"));
        formElement.addElement(radioElement2);

        formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("radioName", formData.get(0).key());
        Assert.assertEquals("option2", formData.get(0).value());
    }
}