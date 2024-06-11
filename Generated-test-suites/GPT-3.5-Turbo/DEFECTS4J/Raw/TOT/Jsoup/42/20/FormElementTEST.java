import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        formElement = new FormElement(Tag.form(), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.input(), "http://example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        Elements elements = updatedFormElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        formElement.submit();
    }

    @Test
    public void testSubmitGetMethod() {
        formElement.attr("method", "GET");
        Connection connection = formElement.submit();
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
        Assert.assertEquals("http://example.com/", connection.request().url().toString());
    }

    @Test
    public void testSubmitPostMethod() {
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        Assert.assertEquals(Connection.Method.POST, connection.request().method());
        Assert.assertEquals("http://example.com/", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        Element nameInput = new Element(Tag.input(), "http://example.com", new Attributes());
        nameInput.attr("type", "text");
        nameInput.attr("name", "name");
        nameInput.val("John");
        formElement.addElement(nameInput);

        Element ageInput = new Element(Tag.input(), "http://example.com", new Attributes());
        ageInput.attr("type", "number");
        ageInput.attr("name", "age");
        ageInput.val("25");
        formElement.addElement(ageInput);

        Element checkboxInput = new Element(Tag.input(), "http://example.com", new Attributes());
        checkboxInput.attr("type", "checkbox");
        checkboxInput.attr("name", "subscribe");
        checkboxInput.attr("value", "yes");
        checkboxInput.attr("checked", "checked");
        formElement.addElement(checkboxInput);

        Element selectElement = new Element(Tag.select(), "http://example.com", new Attributes());
        formElement.addElement(selectElement);

        Element option1 = new Element(Tag.option(), "http://example.com", new Attributes());
        option1.attr("value", "option1");
        option1.attr("selected", "selected");
        selectElement.appendChild(option1);

        Element option2 = new Element(Tag.option(), "http://example.com", new Attributes());
        option2.attr("value", "option2");
        selectElement.appendChild(option2);

        Element option3 = new Element(Tag.option(), "http://example.com", new Attributes());
        option3.attr("value", "option3");
        selectElement.appendChild(option3);

        Element option4 = new Element(Tag.option(), "http://example.com", new Attributes());
        option4.attr("value", "option4");
        option4.attr("selected", "selected");
        selectElement.appendChild(option4);

        Element radioInput = new Element(Tag.input(), "http://example.com", new Attributes());
        radioInput.attr("type", "radio");
        radioInput.attr("name", "gender");
        radioInput.attr("value", "male");
        radioInput.attr("checked", "checked");
        formElement.addElement(radioInput);

        Element radioInput2 = new Element(Tag.input(), "http://example.com", new Attributes());
        radioInput2.attr("type", "radio");
        radioInput2.attr("name", "gender");
        radioInput2.attr("value", "female");
        formElement.addElement(radioInput2);

        Assert.assertEquals(Arrays.asList(
                HttpConnection.KeyVal.create("name", "John"),
                HttpConnection.KeyVal.create("age", "25"),
                HttpConnection.KeyVal.create("subscribe", "yes"),
                HttpConnection.KeyVal.create("gender", "male"),
                HttpConnection.KeyVal.create("option1", "option1"),
                HttpConnection.KeyVal.create("option4", "option4")
        ), formElement.formData());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(formElement, formElement);
        Assert.assertNotEquals(formElement, null);
    }
}