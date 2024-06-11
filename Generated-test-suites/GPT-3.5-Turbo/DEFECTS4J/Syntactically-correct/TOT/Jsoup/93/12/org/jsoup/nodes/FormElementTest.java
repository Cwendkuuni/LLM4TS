import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

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
    public void testElements_NotNull() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testElements_InitiallyEmpty() {
        Elements elements = formElement.elements();
        Assert.assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);

        Elements elements = updatedFormElement.elements();
        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.first());
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);

        formElement.removeChild(element);

        Elements elements = formElement.elements();
        Assert.assertTrue(elements.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_NullActionUrl() {
        formElement.submit();
    }

    @Test
    public void testSubmit_Method_POST() {
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();

        Assert.assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testSubmit_Method_GET() {
        formElement.attr("method", "GET");
        Connection connection = formElement.submit();

        Assert.assertEquals(Connection.Method.GET, connection.method());
    }

    @Test
    public void testFormData_SelectElement_ValidValue() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        Element option = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        option.val("optionValue");
        select.appendChild(option);
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertFalse(formData.isEmpty());
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("optionValue", formData.get(0).value());
    }

    @Test
    public void testFormData_SelectElement_NoSelectedOption() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_SelectElement_NoOptions() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_CheckboxOrRadio_Checked() {
        Element checkbox = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "");
        formElement.addElement(checkbox);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertFalse(formData.isEmpty());
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormData_CheckboxOrRadio_NotChecked() {
        Element checkbox = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkbox.attr("type", "checkbox");
        formElement.addElement(checkbox);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_TextInput() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        input.attr("type", "text");
        input.attr("name", "inputName");
        input.val("inputValue");
        formElement.addElement(input);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertFalse(formData.isEmpty());
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("inputValue", formData.get(0).value());
    }

    @Test
    public void testFormData_TextInput_Disabled() {
        Element input = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        input.attr("type", "text");
        input.attr("disabled", "");
        formElement.addElement(input);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertTrue(formData.isEmpty());
    }

}