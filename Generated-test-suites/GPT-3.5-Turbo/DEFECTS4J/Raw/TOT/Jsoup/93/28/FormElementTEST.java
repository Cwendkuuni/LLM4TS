import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Element;
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
    public void setup(){
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", null);
    }

    @Test
    public void testElements(){
        Elements elements = formElement.elements();

        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement(){
        Element element = new Element(Tag.valueOf("input"), "http://example.com", null);

        FormElement result = formElement.addElement(element);

        Assert.assertEquals(1, formElement.elements().size());
        Assert.assertEquals(element, formElement.elements().get(0));
        Assert.assertEquals(formElement, result);
    }

    @Test
    public void testRemoveChild(){
        Element element = new Element(Tag.valueOf("input"), "http://example.com", null);
        formElement.addElement(element);

        formElement.removeChild(element);

        Assert.assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmitWithAbsoluteActionURL() {
        formElement.attr("action", "http://example.com");

        // Test typical use case
        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.POST, connection.request().method());

        // Test edge case
        formElement.attr("method", "GET");
        Connection connectionGet = formElement.submit();

        Assert.assertEquals(Connection.Method.GET, connectionGet.request().method());

        // Test error scenario
        formElement.removeAttr("action");
        try {
            formElement.submit();
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", null);
        inputElement.attr("name", "inputName1");
        inputElement.val("inputValue1");

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", null);
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", null);
        optionElement.attr("selected", "");
        optionElement.val("optionValue1");
        selectElement.append(optionElement);

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        // Test typical use case
        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("inputName1", formData.get(0).key());
        Assert.assertEquals("inputValue1", formData.get(0).value());
        Assert.assertEquals("select", formData.get(1).key());
        Assert.assertEquals("optionValue1", formData.get(1).value());

        // Test edge case
        optionElement.removeAttr("selected");
        List<Connection.KeyVal> formDataNoSelectedOption = formElement.formData();

        Assert.assertEquals(2, formDataNoSelectedOption.size());
        Assert.assertEquals("inputName1", formDataNoSelectedOption.get(0).key());
        Assert.assertEquals("inputValue1", formDataNoSelectedOption.get(0).value());
        Assert.assertEquals("select", formDataNoSelectedOption.get(1).key());
        Assert.assertEquals("", formDataNoSelectedOption.get(1).value());

        // Test error scenario
        Element invalidElement = new Element(Tag.valueOf("input"), "http://example.com", null);
        formElement.addElement(invalidElement);

        List<Connection.KeyVal> formDataInvalidName = formElement.formData();

        Assert.assertEquals(2, formDataInvalidName.size());
    }
}