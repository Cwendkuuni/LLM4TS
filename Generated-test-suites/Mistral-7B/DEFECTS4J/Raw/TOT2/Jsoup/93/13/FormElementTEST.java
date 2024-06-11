import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.junit.Test;

public class FormElementTest {
    @Test
    public void testElementsEmptyForm() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Elements expectedElements = new Elements();
        Elements actualElements = formElement.elements();
        assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testElementsWithFormControl() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element element = new Element("input");
        formElement.addElement(element);
        Elements expectedElements = new Elements(element);
        Elements actualElements = formElement.elements();
        assertEquals(expectedElements, actualElements);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithMissingBaseUri() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, null, attributes);
        formElement.submit();
    }

    @Test
    public void testFormDataEmptyForm() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        List<Connection.KeyVal> actualData = formElement.formData();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testFormDataWithFormControl() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element input = new Element("input");
        input.attr("name", "test");
        formElement.addElement(input);
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("test", ""));
        List<Connection.KeyVal> actualData = formElement.formData();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testFormDataWithFormControls() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element input1 = new Element("input");
        Element input2 = new Element("input");
        input1.attr("name", "test1");
        input2.attr("name", "test2");
        input1.attr("value", "value1");
        input2.attr("value", "value2");
        formElement.addElement(input1);
        formElement.addElement(input2);
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("test1", "value1"));
        expectedData.add(HttpConnection.KeyVal.create("test2", "value2"));
        List<Connection.KeyVal> actualData = formElement.formData();
        assertEquals(expectedData, actualData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidFormControl() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element element = new Element("not_a_form_control");
        formElement.addElement(element);
        formElement.submit();
    }

    @Test
    public void testFormDataWithDisabledFormControl() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element input = new Element("input");
        input.attr("name", "test");
        input.attr("type", "text");
        input.attr("disabled", "");
        formElement.addElement(input);
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        List<Connection.KeyVal> actualData = formElement.formData();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testFormDataWithLargeData() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        for (int i = 0; i < 1000; i++) {
            Element input = new Element("input");
            input.attr("name", "test" + i);
            input.attr("value", "large value " + i);
            formElement.addElement(input);
        }
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            expectedData.add(HttpConnection.KeyVal.create("test" + i, "large value " + i));
        }
        List<Connection.KeyVal> actualData = formElement.formData();
        assertEquals(expectedData, actualData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormDataWithInvalidInputData() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element input = new Element("input");
        input.attr("name", "test");
        input.attr("value", null);
        formElement.addElement(input);
        List<Connection.KeyVal> expectedData = new ArrayList<>();
        List<Connection.KeyVal> actualData = formElement.formData();
        assertEquals(expectedData, actualData);
    }
}