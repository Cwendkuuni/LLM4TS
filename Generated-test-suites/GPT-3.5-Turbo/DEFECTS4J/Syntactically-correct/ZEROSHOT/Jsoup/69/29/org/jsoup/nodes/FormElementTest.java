import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
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
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        Elements elements = updatedFormElement.elements();
        Assert.assertNotNull(elements);
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertNull(connection.request().data());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
        Assert.assertEquals("https://www.example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithInputElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "username");
        element.val("john");
        FormElement updatedFormElement = formElement.addElement(element);

        List<Connection.KeyVal> formData = updatedFormElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("username", formData.get(0).key());
        Assert.assertEquals("john", formData.get(0).value());
    }

    @Test
    public void testFormDataWithSelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement1.attr("selected", "");
        optionElement1.val("option1");
        selectElement.appendChild(optionElement1);
        FormElement updatedFormElement = formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = updatedFormElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("select", formData.get(0).key());
        Assert.assertEquals("option1", formData.get(0).value());
    }

    @Test
    public void testFormDataWithEmptySelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        FormElement updatedFormElement = formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = updatedFormElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("select", formData.get(0).key());
        Assert.assertEquals("", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxElement() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("checked", "");
        FormElement updatedFormElement = formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = updatedFormElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("agree", formData.get(0).key());
        Assert.assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormDataWithUncheckedCheckboxElement() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "agree");
        FormElement updatedFormElement = formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = updatedFormElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithRadioElement() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "gender");
        radioElement.attr("checked", "");
        FormElement updatedFormElement = formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = updatedFormElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("gender", formData.get(0).key());
        Assert.assertEquals("on", formData.get(0).value());
    }

    @Test
    public void testFormDataWithUncheckedRadioElement() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "gender");
        FormElement updatedFormElement = formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = updatedFormElement.formData();
        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }
}