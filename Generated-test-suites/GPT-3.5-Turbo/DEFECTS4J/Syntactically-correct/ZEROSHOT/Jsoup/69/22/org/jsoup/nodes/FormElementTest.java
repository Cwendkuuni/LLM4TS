import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement result = formElement.addElement(element);
        assertSame(formElement, result);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertSame(element, elements.first());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(formElement.baseUri(), connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement.attr("name", "input1");
        inputElement.attr("value", "value1");
        formElement.addElement(inputElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "checkbox1");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);

        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes());
        selectElement.attr("name", "select1");
        selectElement.append("<option value='option1' selected>Option 1</option>");
        selectElement.append("<option value='option2'>Option 2</option>");
        formElement.addElement(selectElement);

        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(3, data.size());
        Connection.KeyVal inputKeyVal = data.get(0);
        assertEquals("input1", inputKeyVal.key());
        assertEquals("value1", inputKeyVal.value());
        Connection.KeyVal checkboxKeyVal = data.get(1);
        assertEquals("checkbox1", checkboxKeyVal.key());
        assertEquals("on", checkboxKeyVal.value());
        Connection.KeyVal selectKeyVal = data.get(2);
        assertEquals("select1", selectKeyVal.key());
        assertEquals("option1", selectKeyVal.value());
    }

}