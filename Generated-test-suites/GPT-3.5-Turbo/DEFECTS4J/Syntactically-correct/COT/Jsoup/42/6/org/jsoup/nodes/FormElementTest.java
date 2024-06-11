
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private FormElement form;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements_getElements() {
        Elements elements = form.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement_addsElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement updatedForm = form.addElement(element);
        List<Element> elementList = updatedForm.elements();
        assertEquals(1, elementList.size());
        assertEquals(element, elementList.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_invalidActionUrl() {
        Connection connection = form.submit();
    }

    @Test
    public void testSubmit_validActionUrl() {
        String validActionUrl = "http://example.com/action";
        form.attr("action", validActionUrl);
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals(HttpConnection.Method.GET, connection.method());
    }

    @Test
    public void testFormData_noFormControl() {
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_checkboxFormControl() {
        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("name", "check");
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "checked");
        form.addElement(checkbox);

        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal keyValue = formData.get(0);
        assertEquals("check", keyValue.key());
        assertEquals("checked", keyValue.value());
    }

    @Test
    public void testFormData_radioFormControl() {
        Element radio = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radio.attr("name", "radio");
        radio.attr("type", "radio");
        radio.attr("checked", "checked");
        form.addElement(radio);

        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal keyValue = formData.get(0);
        assertEquals("radio", keyValue.key());
        assertEquals("checked", keyValue.value());
    }

    @Test
    public void testFormData_selectFormControl_withSelectedOptions() {
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("selected", "selected");
        option1.val("value1");
        select.addElement(option1);
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.attr("selected", "selected");
        option2.val("value2");
        select.addElement(option2);

        form.addElement(select);

        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());

        Connection.KeyVal keyValue1 = formData.get(0);
        assertEquals("select", keyValue1.key());
        assertEquals("value1", keyValue1.value());

        Connection.KeyVal keyValue2 = formData.get(1);
        assertEquals("select", keyValue2.key());
        assertEquals("value2", keyValue2.value());
    }

    @Test
    public void testFormData_selectFormControl_noSelectedOption() {
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option.val("value");
        select.addElement(option);

        form.addElement(select);

        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal keyValue = formData.get(0);
        assertEquals("select", keyValue.key());
        assertEquals("value", keyValue.value());
    }
}

