import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmitWithAction() throws IOException {
        // Prepare test data
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("name", "input1").put("value", "value1"));
        Element element2 = new Element(Tag.valueOf("textarea"), formElement.baseUri(), new Attributes().put("name", "textarea1").put("value", "value2"));
        formElement.addElement(element1);
        formElement.addElement(element2);

        // Perform the submit
        Connection con = formElement.submit();

        // Verify the connection setup
        assertEquals(formElement.absUrl("action"), con.request().url().toString());
        assertTrue(con.request().data().contains(HttpConnection.KeyVal.create("input1", "value1")));
        assertTrue(con.request().data().contains(HttpConnection.KeyVal.create("textarea1", "value2")));
        assertEquals(Connection.Method.POST, con.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        // Prepare test data
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("name", "input1").put("value", "value1"));
        Element element2 = new Element(Tag.valueOf("textarea"), formElement.baseUri(), new Attributes().put("name", "textarea1").put("value", "value2"));
        formElement.addElement(element1);
        formElement.addElement(element2);

        // Get the form data
        List<Connection.KeyVal> formData = formElement.formData();

        // Verify the form data
        assertEquals(2, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("input1", "value1")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("textarea1", "value2")));
    }

    @Test
    public void testFormDataWithEmptyName() {
        // Prepare test data
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("value", "value1"));
        formElement.addElement(element);

        // Get the form data
        List<Connection.KeyVal> formData = formElement.formData();

        // Verify the form data
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithSelectElement() {
        // Prepare test data
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes().put("name", "select1"));
        Element optionElement1 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes().put("value", "option1").put("selected", ""));
        Element optionElement2 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes().put("value", "option2"));
        Element optionElement3 = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes().put("value", "option3").put("selected", ""));
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        selectElement.appendChild(optionElement3);
        formElement.addElement(selectElement);

        // Get the form data
        List<Connection.KeyVal> formData = formElement.formData();

        // Verify the form data
        assertEquals(2, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("select1", "option1")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("select1", "option3")));
    }

    @Test
    public void testFormDataWithNoSelectedOption() {
        // Prepare test data
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri(), new Attributes().put("name", "select1"));
        Element optionElement = new Element(Tag.valueOf("option"), formElement.baseUri(), new Attributes().put("value", "option1"));
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        // Get the form data
        List<Connection.KeyVal> formData = formElement.formData();

        // Verify the form data
        assertEquals(1, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("select1", "option1")));
    }

    @Test
    public void testFormDataWithCheckboxChecked() {
        // Prepare test data
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "checkbox").put("name", "checkbox1").put("value", "value1").put("checked", ""));
        formElement.addElement(checkboxElement);

        // Get the form data
        List<Connection.KeyVal> formData = formElement.formData();

        // Verify the form data
        assertEquals(1, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("checkbox1", "value1")));
    }

    @Test
    public void testFormDataWithCheckboxUnchecked() {
        // Prepare test data
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "checkbox").put("name", "checkbox1").put("value", "value1"));
        formElement.addElement(checkboxElement);

        // Get the form data
        List<Connection.KeyVal> formData = formElement.formData();

        // Verify the form data
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithRadioChecked() {
        // Prepare test data
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "radio").put("name", "radio1").put("value", "value1").put("checked", ""));
        formElement.addElement(radioElement);

        // Get the form data
        List<Connection.KeyVal> formData = formElement.formData();

        // Verify the form data
        assertEquals(1, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("radio1", "value1")));
    }

    @Test
    public void testFormDataWithRadioUnchecked() {
        // Prepare test data
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes().put("type", "radio").put("name", "radio1").put("value", "value1"));
        formElement.addElement(radioElement);

        // Get the form data
        List<Connection.KeyVal> formData = formElement.formData();

        // Verify the form data
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testEquals() {
        Element element1 = new Element(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element2 = new Element(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element3 = new Element(Tag.valueOf("form"), "http://example.com", new Attributes());

        FormElement formElement1 = new FormElement(element1.tag(), element1.baseUri(), element1.attributes());
        FormElement formElement2 = new FormElement(element2.tag(), element2.baseUri(), element2.attributes());
        FormElement formElement3 = new FormElement(element3.tag(), element3.baseUri(), element3.attributes());

        assertEquals(formElement1, formElement2);
        assertEquals(formElement1.hashCode(), formElement2.hashCode());
        assertNotEquals(formElement1, formElement3);
        assertNotEquals(formElement1.hashCode(), formElement3.hashCode());
    }
}