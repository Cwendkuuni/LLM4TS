import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        assertNotNull(formElement);
        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testElementsMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElementMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element element = new Element(Tag.valueOf("input"), baseUri);
        formElement.addElement(element);
        
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmitMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(formElement.absUrl("action"), connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitMethodInvalidActionUrl() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        formElement.attr("action", "");
        
        try {
            formElement.submit();
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormDataMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element input = new Element(Tag.valueOf("input"), baseUri);
        input.attr("name", "username");
        input.val("JohnDoe");
        formElement.addElement(input);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("JohnDoe", keyVal.value());
    }

    @Test
    public void testFormDataMethodWithDisabledInput() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element input = new Element(Tag.valueOf("input"), baseUri);
        input.attr("name", "disabledInput");
        input.attr("disabled", "");
        formElement.addElement(input);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataMethodWithSelectElement() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element select = new Element(Tag.valueOf("select"), baseUri);
        select.attr("name", "gender");
        
        Element option1 = new Element(Tag.valueOf("option"), baseUri);
        option1.attr("value", "male");
        option1.attr("selected", "");
        
        Element option2 = new Element(Tag.valueOf("option"), baseUri);
        option2.attr("value", "female");
        
        select.appendChild(option1);
        select.appendChild(option2);
        
        formElement.addElement(select);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("gender", keyVal.key());
        assertEquals("male", keyVal.value());
    }

    @Test
    public void testFormDataMethodWithCheckboxElementChecked() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element checkbox = new Element(Tag.valueOf("input"), baseUri);
        checkbox.attr("name", "acceptTerms");
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "");
        
        formElement.addElement(checkbox);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("acceptTerms", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataMethodWithCheckboxElementUnchecked() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element checkbox = new Element(Tag.valueOf("input"), baseUri);
        checkbox.attr("name", "acceptTerms");
        checkbox.attr("type", "checkbox");
        
        formElement.addElement(checkbox);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataMethodWithRadioButtonElementChecked() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element radioButton = new Element(Tag.valueOf("input"), baseUri);
        radioButton.attr("name", "gender");
        radioButton.attr("type", "radio");
        radioButton.attr("checked", "");
        radioButton.attr("value", "male");
        
        formElement.addElement(radioButton);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("gender", keyVal.key());
        assertEquals("male", keyVal.value());
    }

    @Test
    public void testFormDataMethodWithRadioButtonElementUnchecked() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element radioButton = new Element(Tag.valueOf("input"), baseUri);
        radioButton.attr("name", "gender");
        radioButton.attr("type", "radio");
        radioButton.attr("value", "male");
        
        formElement.addElement(radioButton);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataMethodWithTextInput() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        
        Element input = new Element(Tag.valueOf("input"), baseUri);
        input.attr("name", "username");
        input.val("JohnDoe");
        
        formElement.addElement(input);
        
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("JohnDoe", keyVal.value());
    }
}