
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        Assert.assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithMissingActionUrl() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
    }

    @Test
    public void testFormDataWithOptions() {
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        optionElement.attr("selected", "selected");
        optionElement.attr("value", "option1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("option1", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox1");
        checkboxElement.attr("value", "checked");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("checked", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxNotChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox1");
        checkboxElement.attr("value", "not checked");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithRadioChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio1");
        radioElement.attr("value", "checked");
        radioElement.attr("checked", "checked");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("checked", formData.get(0).value());
    }

    @Test
    public void testFormDataWithRadioNotChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "radio1");
        radioElement.attr("value", "not checked");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }
}
