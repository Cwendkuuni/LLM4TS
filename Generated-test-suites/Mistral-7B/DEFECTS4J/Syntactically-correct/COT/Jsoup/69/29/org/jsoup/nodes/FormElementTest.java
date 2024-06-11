import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testGetFormControls() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element input1 = new Element("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new Element("input", new Attributes().put("type", "text").put("name", "input2"));

        Elements elements = new Elements(input1, input2);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(input1);
        formElement.addElement(input2);

        Elements expectedControls = elements;
        Elements actualControls = formElement.elements();

        assertEquals(expectedControls.size(), actualControls.size());
        Assert.assertEquals(expectedControls, actualControls);
    }

    @Test
    public void testSubmitWithoutAction() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        try {
            formElement.submit();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testSubmitWithEmptyInputs() {
        Tag formTag = new Tag("form", new Attributes().put("action", "http://example.com"));
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Connection connection = formElement.submit();
        List<Connection.KeyVal> actualData = connection.data();
        List<Connection.KeyVal> expectedData = new ArrayList<>();

        assertEquals(0, actualData.size());
        assertEquals(0, expectedData.size());
    }

    @Test
    public void testSubmitWithSelectElement() {
        Tag formTag = new Tag("form", new Attributes().put("action", "http://example.com"));
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element selectTag = new Element("select", new Attributes().put("name", "selectElement"));
        Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", "true"));
        Element option2 = new Element("option", new Attributes().put("value", "option2"));

        selectTag.add(option1);
        selectTag.add(option2);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(selectTag);

        Connection connection = formElement.submit();
        List<Connection.KeyVal> actualData = connection.data();
        List<Connection.KeyVal> expectedData = Arrays.asList(new Connection.KeyVal[] {
                new Connection.KeyVal("selectElement", "option1")
        });

        assertArrayEquals(expectedData.toArray(new Connection.KeyVal[0]), actualData.toArray(new Connection.KeyVal[0]));
    }

    @Test
    public void testSubmitWithCheckboxOrRadioElement() {
        Tag formTag = new Tag("form", new Attributes().put("action", "http://example.com"));
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element checkboxTag = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxElement"));

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(checkboxTag);

        // checkbox is unchecked by default
        Connection connection = formElement.submit();
        List<Connection.KeyVal> actualData = connection.data();
        List<Connection.KeyVal> expectedData = new ArrayList<>();

        assertEquals(0, actualData.size());
        assertEquals(0, expectedData.size());

        // checkbox is checked
        checkboxTag.attributes().put("checked", "true");
        formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(checkboxTag);
        connection = formElement.submit();
        actualData = connection.data();
        expectedData = Arrays.asList(new Connection.KeyVal[] {
                new Connection.KeyVal("checkboxElement", "on")
        });

        assertArrayEquals(expectedData.toArray(new Connection.KeyVal[0]), actualData.toArray(new Connection.KeyVal[0]));
    }

    @Test
    public void testSubmitWithMultipleInputs() {
        Tag formTag = new Tag("form", new Attributes().put("action", "http://example.com"));
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Element input1 = new Element("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new Element("input", new Attributes().put("type", "text").put("name", "input2"));

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(input1);
        formElement.addElement(input2);

        String input1Value = "input1Value";
        String input2Value = "input2Value";

        input1.attributes().put("value", input1Value);
        input2.attributes().put("value", input2Value);

        Connection connection = formElement.submit();
        List<Connection.KeyVal> actualData = connection.data();
        List<Connection.KeyVal> expectedData = Arrays.asList(new Connection.KeyVal[] {
                new Connection.KeyVal("input1", input1Value),
                new Connection.KeyVal("input2", input2Value)
        });

        assertArrayEquals(expectedData.toArray(new Connection.KeyVal[0]), actualData.toArray(new Connection.KeyVal[0]));
    }
}