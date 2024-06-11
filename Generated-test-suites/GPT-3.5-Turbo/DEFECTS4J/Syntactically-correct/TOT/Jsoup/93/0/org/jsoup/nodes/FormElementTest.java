import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        Assert.assertEquals(0, elements.size());
    }
    
    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        
        Assert.assertSame(formElement, updatedFormElement);
        Assert.assertEquals(1, updatedFormElement.elements().size());
        Assert.assertSame(element, updatedFormElement.elements().get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);

        Assert.assertEquals(1, formElement.elements().size());

        formElement.removeChild(element);

        Assert.assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidActionUrl() {
        formElement.attr("method", "POST");

        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        element1.attr("name", "input1");
        element1.val("value1");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        element2.attr("name", "select1");
        Element option1 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        option1.attr("value", "option1");
        option1.attr("selected", "");
        element2.appendChild(option1);
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("input1=value1", formData.get(0).toString());
        Assert.assertEquals("select1=option1", formData.get(1).toString());
    }

    @Test
    public void testFormDataWithDisabledElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        element.attr("name", "input1");
        element.attr("disabled", "");
        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithCheckboxChecked() {
        Element checkbox1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkbox1.attr("name", "checkbox1");
        checkbox1.attr("checked", "");
        checkbox1.val("value1");
        formElement.addElement(checkbox1);

        Element checkbox2 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkbox2.attr("name", "checkbox2");
        checkbox2.attr("checked", "");
        formElement.addElement(checkbox2);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("checkbox1=value1", formData.get(0).toString());
        Assert.assertEquals("checkbox2=on", formData.get(1).toString());
    }

    @Test
    public void testFormDataWithCheckboxUnchecked() {
        Element checkbox1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkbox1.attr("name", "checkbox1");
        checkbox1.val("value1");
        formElement.addElement(checkbox1);

        Element checkbox2 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkbox2.attr("name", "checkbox2");
        checkbox2.val("value2");
        formElement.addElement(checkbox2);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithRadioChecked() {
        Element radio1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radio1.attr("name", "radio1");
        radio1.attr("checked", "");
        radio1.val("value1");
        formElement.addElement(radio1);

        Element radio2 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radio2.attr("name", "radio2");
        radio2.attr("checked", "");
        formElement.addElement(radio2);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("radio1=value1", formData.get(0).toString());
        Assert.assertEquals("radio2=on", formData.get(1).toString());
    }

    @Test
    public void testFormDataWithRadioUnchecked() {
        Element radio1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radio1.attr("name", "radio1");
        radio1.val("value1");
        formElement.addElement(radio1);

        Element radio2 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radio2.attr("name", "radio2");
        radio2.val("value2");
        formElement.addElement(radio2);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithType() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        element.attr("name", "input1");
        element.val("value1");
        element.attr("type", "text");
        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("input1=value1", formData.get(0).toString());
    }

    @Test
    public void testFormDataWithEmptyName() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        element.val("value1");
        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithSelectWithoutSelectedOption() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        select.attr("name", "select1");
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithSelectWithSelectedOption() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        select.attr("name", "select1");
        Element option = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        option.attr("value", "option1");
        option.attr("selected", "");
        select.appendChild(option);
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("select1=option1", formData.get(0).toString());
    }

    @Test
    public void testFormDataWithSelectWithoutSelectedOptionButWithFirstOption() {
        Element select = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        select.attr("name", "select1");
        Element option = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes());
        option.attr("value", "option1");
        select.appendChild(option);
        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("select1=option1", formData.get(0).toString());
    }

    @Test
    public void testFormDataWithEmptyElements() {
        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(0, formData.size());
    }
}