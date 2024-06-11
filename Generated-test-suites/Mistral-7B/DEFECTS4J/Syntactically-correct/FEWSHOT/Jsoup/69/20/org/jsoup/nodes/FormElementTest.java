package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

public class FormElementTest {
    private static final String BASE_URI = "http://test.com";
    private static final String FORM_HTML = "<html><head></head><body><form action=\"/submit\" method=\"post\">" +
            "<input type=\"text\" name=\"name\" value=\"value\">" +
            "<input type=\"text\" name=\"anotherName\" value=\"anotherValue\">" +
            "<select name=\"selectName\">" +
            "<option value=\"option1\">Option 1</option>" +
            "<option value=\"option2\" selected>Option 2</option>" +
            "<option value=\"option3\">Option 3</option>" +
            "</select>" +
            "<input type=\"checkbox\" name=\"checkboxName\" value=\"checkValue\">" +
            "<input type=\"checkbox\" name=\"checkboxName\" value=\"checkValue\" checked>" +
            "</form></body></html>";

    private Element formElement;

    @Before
    public void setUp() {
        FormParser formParser = new FormParser();
        Parser parser = new Parser();
        formElement = parser.parse(new StringReader(FORM_HTML)).body().selectFirst("form");
        formElement.baseURI(BASE_URI);
    }

    @Test
    public void testCreateFormElement() {
        FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
        assertTrue(formElement instanceof FormElement);
    }

    @Test
    public void testGetElements() {
        assertEquals(2, formElement.elements().size());
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element("input");
        formElement.addElement(inputElement);
        assertTrue(formElement.elements().contains(inputElement));
    }

    @Test
    public void testSubmitWithNoAction() {
        try {
            formElement.submit();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSubmitWithEmptyAction() {
        formElement.attributes().put("action", "");
        formElement.submit();
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testSubmit() throws Exception {
        Connection connection = formElement.submit();
        String action = formElement.attr("action");
        assertEquals(action, connection.url());
        assertEquals(Connection.Method.POST, connection.method());

        List<Connection.KeyVal> formData = formElement.formData();
        List<Map.Entry<String, String>> expectedFormData = Arrays.asList(
                Map.entry("name", "value"),
                Map.entry("anotherName", "anotherValue"),
                Map.entry("selectName", "option2")
        );
        List<Map.entry<String, String>> actualFormData = formData.stream().map(Connection.KeyVal::keyValue).collect(Collectors.toList());
        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataWithCheckbox() throws Exception {
        Element checkboxElement = formElement.selectFirst("input[name='checkboxName'][checked]");
        if (checkboxElement == null) {
            fail("At least one checkbox should be checked");
        }
        formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();
        List<Map.entry<String, String>> expectedFormData = Arrays.asList(
                Map.entry("name", "value"),
                Map.entry("anotherName", "anotherValue"),
                Map.entry("checkboxName", "checkValue")
        );
        List<Map.entry<String, String>> actualFormData = formData.stream().map(Connection.KeyVal::keyValue).collect(Collectors.toList());
        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataWithNoCheckedCheckbox() throws Exception {
        List<Connection.KeyVal> formData = formElement.formData();
        List<Map.entry<String, String>> expectedFormData = Arrays.asList(
                Map.entry("name", "value"),
                Map.entry("anotherName", "anotherValue")
        );
        List<Map.entry<String, String>> actualFormData = formData.stream().map(Connection.KeyVal::keyValue).collect(Collectors.toList());
        assertEquals(expectedFormData, actualFormData);
    }
}

class FormParser extends Parser.ParserConfiguration {
    @Override
    public Document parse(String content, String encoding, File input, ParseSettings settings) {
        Document doc = new Document();
        doc.outputSettings().syntax(OutputSettings.Syntax.html());
        doc.outputSettings().prettyPrint(false);
        doc.parser().setInputEncoding(encoding);
        doc.parser().setDocument(doc);
        doc.parser().setBaseUri(BASE_URI);
        try {
            doc.parser().parseInput(new StringReader(content), settings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }
}