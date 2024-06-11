import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form action='https://example.com' method='post'>" +
                "<input name='input1' type='text' value='value1'>" +
                "<select name='select1'>" +
                "<option value='option1'>Option 1</option>" +
                "<option selected value='option2'>Option 2</option>" +
                "</select>" +
                "<input name='input2' type='checkbox' checked>" +
                "</form>";

        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        assertThat(formElement, is(instanceOf(FormElement.class)));
        assertThat(formElement.elements(), is(not(nullValue())));
    }

    @Test
    public void testGetFormControls() {
        String html = "<form action='https://example.com' method='post'>" +
                "<input name='input1' type='text' value='value1'>" +
                "<select name='select1'>" +
                "<option value='option1'>Option 1</option>" +
                "<option selected value='option2'>Option 2</option>" +
                "</select>" +
                "<input name='input2' type='checkbox' checked>" +
                "</form>";

        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        List<Element> formControls = formElement.elements();
        List<String> expectedControls = Arrays.asList(
                "input1", "select1", "input2"
        );

        for (Element control : formControls) {
            assertTrue(expectedControls.contains(control.tagName()));
        }
    }

    @Test
    public void testSubmitFormWithEmptyAction() {
        String html = "<form action='' method='post'>" +
                "<input name='input1' type='text' value='value1'>" +
                "</form>";

        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        try {
            formElement.submit();
            // This statement should never be reached
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSubmitFormWithInvalidMethod() {
        String html = "<form action='https://example.com' method='invalid'>" +
                "<input name='input1' type='text' value='value1'>" +
                "</form>";

        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        try {
            formElement.submit();
            // This statement should never be reached
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testFormDataCreation() {
        String html = "<form action='https://example.com' method='post'>" +
                "<input name='input1' type='text' value='value1'>" +
                "<select name='select1'>" +
                "<option value='option1'>Option 1</option>" +
                "<option selected value='option2'>Option 2</option>" +
                "</select>" +
                "<input name='input2' type='checkbox' checked>" +
                "</form>";

        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(new Connection.KeyVal("input1", "value1"));
        expectedData.add(new Connection.KeyVal("select1", "option2"));
        expectedData.add(new Connection.KeyVal("input2", ""));

        for (Connection.KeyVal data : formData) {
            Connection.KeyVal expectedDataElement = expectedData.get(expectedData.size() - 1);
            assertThat(data.key, is(expectedDataElement.key));
            assertThat(data.val, is(expectedDataElement.val));
            expectedData.remove(expectedData.size() - 1);
        }
        // The remaining elements should be empty
        for (Connection.KeyVal data : formData) {
            assertTrue(data.key.isEmpty() && data.val.isEmpty());
        }
    }

    @Test
    public void testFormDataCreationWithNoName() {
        String html = "<form action='https://example.com' method='post'>" +
                "<input type='text' value='value1'>" +
                "</form>";

        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();

        assertTrue(formData.isEmpty());
    }
}