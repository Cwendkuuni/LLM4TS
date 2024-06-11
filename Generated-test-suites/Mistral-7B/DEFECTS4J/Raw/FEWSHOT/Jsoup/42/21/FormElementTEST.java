package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSettings;
import org.jsoup.select.Elements;

import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private static final String TEST_HTML = "" +
            "<html><body>" +
            "<form action=\"/submit\">" +
            "<input type=\"text\" name=\"foo\" value=\"bar\"/>" +
            "<select name=\"baz\">" +
            "<option value=\"qux\">Qux</option>" +
            "<option value=\"quux\" selected>" +
            "<option value=\"corge\">Corge</option>" +
            "</select>" +
            "<input type=\"checkbox\" name=\"corge\" value=\"corge\" checked/>" +
            "<input type=\"hidden\" name=\"qux\" value=\"qux\"/>" +
            "</form>" +
            "</body></html>";

    private Element formElement;
    private Connection connection;
    private HttpConnection httpConnectionMock;

    @Before
    public void setUp() throws Exception {
        ParseSettings parseSettings = new ParseSettings();
        parseSettings.setOmitWhitespace(true);
        formElement = Jsoup.parse(TEST_HTML, parseSettings).select("form").first();
        httpConnectionMock = mock(HttpConnection.class);
        connection = Jsoup.connect();
        when(connection.data(anyList())).then(i -> connection.data(i.iterator().next()));
        when(httpConnectionMock.keyValues(anyString())).thenCallRealMethod();
    }

    @Test
    public void testElements() {
        assertSame(formElement, formElement.elements().first());
        FormElement formElementWithInputs = formElement.select("input").first().ancestor(FormElement.class);
        assertEquals(formElementWithInputs.elements(), formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element newInputElement = Element.factory(new Tag("input"), "input", new Attributes());
        FormElement formElementWithNewInput = formElement.addElement(newInputElement);
        assertEquals(formElementWithNewInput, formElement.addElement(newInputElement));
        assertEquals(formElementWithNewInput.elements().size(), formElement.elements().size() + 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddElementThrowExceptionWhenNullIsPassed() {
        formElement.addElement(null);
    }

    @Test
    public void testSubmit() throws Exception {
        FormElement formWithSubmission = formElement.submit();
        assertEquals("/submit", formWithSubmission.baseUri());
        Connection connectionWithFormData = formWithSubmission.submit();

        List<Connection.KeyVal> formData = formWithSubmission.formData();

        // Check formData content
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("foo", "bar"),
                new Connection.KeyVal("baz", "quux"),
                new Connection.KeyVal("corge", "corge")
        );

        assertEquals(expectedFormData, formData);

        // Check Connection data is set properly
        assertEquals(expectedFormData, connectionWithFormData.data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWhenFormActionCouldNotBeDetermined() throws Exception {
        FormElement formWithoutAction = Jsoup.parse("<form></form>").select("form").first();
        formWithoutAction.submit();
    }

    @Test
    public void testFormDataForSelectElements() throws Exception {
        List<Connection.KeyVal> formDataForSelect = formElement.formData();
        assertEquals("quux", formDataForSelect.get(0).key);
        assertEquals("Qux", formDataForSelect.get(1).value);
    }

    @Test
    public void testFormDataForCheckboxOrRadioElements() throws Exception {
        List<Connection.KeyVal> formDataForCheckboxOrRadio = formElement.formData();
        assertEquals("corge", formDataForCheckboxOrRadio.get(0).key);
        assertEquals("corge", formDataForCheckboxOrRadio.get(0).value);
    }

    @Test
    public void testFormDataForHiddenElements() throws Exception {
        List<Connection.KeyVal> formDataForHidden = formElement.formData();
        assertEquals("qux", formDataForHidden.get(0).key);
        assertEquals("qux", formDataForHidden.get(0).value);
    }

    @Test
    public void testFormDataForNoNameElements() throws Exception {
        FormElement formWithoutName = Jsoup.parse("<input type=\"text\"/><form></form>").select("input").first()
                .ancestorsUntil(FormElement.class).last();
        List<Connection.KeyVal> formDataForNoName = formWithoutName.formData();
        assertEquals(0, formDataForNoName.size());
    }
}