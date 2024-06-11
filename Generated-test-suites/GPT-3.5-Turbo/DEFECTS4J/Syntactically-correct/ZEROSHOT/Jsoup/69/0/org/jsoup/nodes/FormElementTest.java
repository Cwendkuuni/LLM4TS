import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();

        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());

        FormElement updatedFormElement = formElement.addElement(element);

        Assert.assertSame(formElement, updatedFormElement);
        Assert.assertEquals(1, formElement.elements().size());
        Assert.assertSame(element, formElement.elements().get(0));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "admin");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> data = formElement.formData();

        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("username", data.get(0).key());
        Assert.assertEquals("admin", data.get(0).value());
    }

    @Test
    public void testFormDataWithDisabledElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "admin");
        inputElement.attr("disabled", "");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> data = formElement.formData();

        Assert.assertNotNull(data);
        Assert.assertEquals(0, data.size());
    }

    @Test
    public void testFormDataWithSelectElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        selectElement.attr("name", "fruit");

        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("value", "apple");
        option1.attr("selected", "");
        selectElement.appendChild(option1);

        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.attr("value", "banana");
        selectElement.appendChild(option2);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();

        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("fruit", data.get(0).key());
        Assert.assertEquals("apple", data.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxElementChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "rememberMe");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("value", "true");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> data = formElement.formData();

        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("rememberMe", data.get(0).key());
        Assert.assertEquals("true", data.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxElementUnchecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "rememberMe");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("value", "true");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> data = formElement.formData();

        Assert.assertNotNull(data);
        Assert.assertEquals(0, data.size());
    }

    @Test
    public void testFormDataWithRadioButtonElementChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("name", "color");
        radioElement.attr("type", "radio");
        radioElement.attr("value", "red");
        radioElement.attr("checked", "");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> data = formElement.formData();

        Assert.assertNotNull(data);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("color", data.get(0).key());
        Assert.assertEquals("red", data.get(0).value());
    }

    @Test
    public void testFormDataWithRadioButtonElementUnchecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("name", "color");
        radioElement.attr("type", "radio");
        radioElement.attr("value", "red");
        formElement.addElement(radioElement);

        List<Connection.KeyVal> data = formElement.formData();

        Assert.assertNotNull(data);
        Assert.assertEquals(0, data.size());
    }

}