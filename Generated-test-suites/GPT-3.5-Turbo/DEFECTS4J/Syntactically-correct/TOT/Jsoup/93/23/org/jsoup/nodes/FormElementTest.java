
import org.jsoup.Connection;
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
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        // check that initially the list of form control elements is empty
        Assert.assertTrue(elements.isEmpty());

        // add a form control element and check that it is included in the list
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(inputElement);
        elements = formElement.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertTrue(elements.contains(inputElement));

        // test removing a form control element
        formElement.removeChild(inputElement);
        elements = formElement.elements();
        Assert.assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmit() {
        // test submitting form with valid action URL and method
        formElement.attr("action", "http://submit-example.com");
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        Assert.assertEquals("http://submit-example.com", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());

        // test submitting form with invalid action URL
        formElement.removeAttr("action");
        try {
            formElement.submit();
            Assert.fail("IllegalArgumentException should have been thrown when submitting form with invalid action URL");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        // test getting form data
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("value", "john");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "gender");
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("selected", "");
        option1.attr("value", "male");
        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "female");
        selectElement.appendChild(option1);
        selectElement.appendChild(option2);
        formElement.addElement(selectElement);

        Element checkboxElement1 = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement1.attr("type", "checkbox");
        checkboxElement1.attr("name", "hobby");
        checkboxElement1.attr("value", "reading");
        checkboxElement1.attr("checked", "");
        formElement.addElement(checkboxElement1);

        Element checkboxElement2 = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement2.attr("type", "checkbox");
        checkboxElement2.attr("name", "hobby");
        checkboxElement2.attr("value", "sports");
        formElement.addElement(checkboxElement2);

        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("name", "category");
        radioElement.attr("value", "A");
        radioElement.attr("checked", "");
        formElement.addElement(radioElement);

        Element textareaElement = new Element(Tag.valueOf("textarea"), "", new Attributes());
        textareaElement.attr("name", "comment");
        textareaElement.text("This is a comment");
        formElement.addElement(textareaElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(5, formData.size());
        Assert.assertEquals("username", formData.get(0).key());
        Assert.assertEquals("john", formData.get(0).value());
        Assert.assertEquals("gender", formData.get(1).key());
        Assert.assertEquals("male", formData.get(1).value());
        Assert.assertEquals("hobby", formData.get(2).key());
        Assert.assertEquals("reading", formData.get(2).value());
        Assert.assertEquals("category", formData.get(3).key());
        Assert.assertEquals("A", formData.get(3).value());
        Assert.assertEquals("comment", formData.get(4).key());
        Assert.assertEquals("This is a comment", formData.get(4).value());
    }
}
