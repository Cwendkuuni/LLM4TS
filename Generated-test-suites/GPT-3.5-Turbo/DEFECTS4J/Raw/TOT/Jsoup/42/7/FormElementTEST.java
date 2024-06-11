import org.jsoup.Connection;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.*;
import static org.junit.Assert.*;

public class FormElementTest {
    
    private FormElement form;

    @Before
    public void setup() {
        form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertNotNull("Elements should not be null", elements);
        assertTrue("Elements should be empty", elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        FormElement result = form.addElement(inputElement);
        assertEquals("Form element returned should be the same as the original form", form, result);
        Elements elements = form.elements();
        assertFalse("Elements should not be empty after adding an element", elements.isEmpty());
        assertEquals("Elements size should be 1 after adding an element", 1, elements.size());
        assertEquals("Element added should be the same as the original element", inputElement, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoActionAttribute() {
        form.submit();
    }

    @Test
    public void testSubmitWithAbsoluteActionURL() {
        form.attr("action", "http://www.example.com/submit");
        Connection connection = form.submit();
        assertEquals("Connection URL should match the form action URL", "http://www.example.com/submit", connection.request().url().toString());
        assertEquals("Connection method should be GET", Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitWithRelativeActionURL() {
        form.attr("action", "/submit");
        Connection connection = form.submit();
        assertEquals("Connection URL should match the base URI + form action URL", "http://www.example.com/submit", connection.request().url().toString());
        assertEquals("Connection method should be GET", Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormDataWithEmptyElements() {
        form.addElement(new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes()));
        List<Connection.KeyVal> data = form.formData();
        assertNotNull("Form data should not be null", data);
        assertTrue("Form data should be empty", data.isEmpty());
    }

    @Test
    public void testFormDataWithSelectElementWithoutSelectedOption() {
        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        form.addElement(selectElement);
        List<Connection.KeyVal> data = form.formData();
        assertNotNull("Form data should not be null", data);
        assertTrue("Form data should be empty", data.isEmpty());
    }

    @Test
    public void testFormDataWithSelectElementWithSelectedOption() {
        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
        selectElement.appendChild(new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes()).attr("selected", "selected").val("1"));
        form.addElement(selectElement);
        List<Connection.KeyVal> data = form.formData();
        assertNotNull("Form data should not be null", data);
        assertFalse("Form data should not be empty", data.isEmpty());
        assertEquals("Form data should contain the selected option value", "1", data.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxElementNotChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes()).attr("type", "checkbox").val("checkbox_value");
        form.addElement(checkboxElement);
        List<Connection.KeyVal> data = form.formData();
        assertNotNull("Form data should not be null", data);
        assertTrue("Form data should be empty", data.isEmpty());
    }

    @Test
    public void testFormDataWithCheckboxElementChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes()).attr("type", "checkbox").val("checkbox_value").attr("checked", "checked");
        form.addElement(checkboxElement);
        List<Connection.KeyVal> data = form.formData();
        assertNotNull("Form data should not be null", data);
        assertFalse("Form data should not be empty", data.isEmpty());
        assertEquals("Form data should contain the checkbox value", "checkbox_value", data.get(0).value());
    }

    @Test
    public void testFormDataWithOtherElementType() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes()).val("input_value");
        form.addElement(inputElement);
        List<Connection.KeyVal> data = form.formData();
        assertNotNull("Form data should not be null", data);
        assertFalse("Form data should not be empty", data.isEmpty());
        assertEquals("Form data should contain the input value", "input_value", data.get(0).value());
    }
    
    @Test
    public void testEquals() {
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        assertEquals("FormElements with the same attributes should be equal", form, form2);
        FormElement form3 = new FormElement(Tag.valueOf("form"), "http://www.example2.com", new Attributes());
        assertNotEquals("FormElements with different base URIs should not be equal", form, form3);
    }

}