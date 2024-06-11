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
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);

        Elements elements = result.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmitWithValidActionAndMethod() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);

        Connection connection = formElement.submit();

        Assert.assertNotNull(connection);
        Assert.assertEquals("http://example.com", connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidActionUrl() {
        Connection connection = formElement.submit();
    }

    @Test
    public void testFormDataWithEmptyElements() {
        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithFormControlElements() {
        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        input.attr("name", "inputName");
        input.val("inputValue");
        formElement.addElement(input);

        Element select = new Element(Tag.valueOf("select"), "", new Attributes());
        select.attr("name", "selectName");
        Element option = new Element(Tag.valueOf("option"), "", new Attributes());
        option.attr("selected", "");
        option.val("optionValue");
        select.appendChild(option);
        formElement.addElement(select);

        Element checkbox = new Element(Tag.valueOf("input"), "", new Attributes());
        checkbox.attr("name", "checkboxName");
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "");
        formElement.addElement(checkbox);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull(formData);
        Assert.assertEquals(3, formData.size());

        for (Connection.KeyVal keyVal : formData) {
            switch (keyVal.key()) {
                case "inputName":
                    Assert.assertEquals("inputValue", keyVal.value());
                    break;
                case "selectName":
                    Assert.assertEquals("optionValue", keyVal.value());
                    break;
                case "checkboxName":
                    Assert.assertEquals("on", keyVal.value());
                    break;
            }
        }
    }
}