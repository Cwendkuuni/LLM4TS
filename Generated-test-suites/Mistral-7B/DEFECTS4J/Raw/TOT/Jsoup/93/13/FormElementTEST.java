import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<html><body><form action='/' method='get'><input type='text' name='username' /><input type='submit' name='submit' value='Submit' /></form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        // Test FormElement creation with valid tag, baseUri and attributes
        Tag tag = new Tag("form");
        FormElement expectedFormElement = new FormElement(tag, "/", new Attributes());
        Element actualFormElement = formElement;

        assert (expectedFormElement != null);
        assert (expectedFormElement.equals(actualFormElement));
    }

    @Test
    public void testGetFormControls() {
        String html = "<html><body><form action='/' method='get'><input type='text' name='username' /><input type='text' name='password' /><input type='submit' name='submit' value='Submit' /></form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        // Test getting form controls
        Elements expectedFormControls = new Elements().add(new Element("input", new Attributes().put("type", "text").put("name", "username")))
                .add(new Element("input", new Attributes().put("type", "text").put("name", "password")));
        Elements actualFormControls = formElement.elements();

        assert (expectedFormControls.size() == actualFormControls.size());
        for (int i = 0; i < expectedFormControls.size(); i++) {
            Element expectedControl = expectedFormControls.get(i);
            Element actualControl = actualFormControls.get(i);
            assert (expectedControl.tag().name().equals(actualControl.tag().name()));
            assert (expectedControl.attr("name").equals(actualControl.attr("name")));
        }
    }

    @Test
    public void testSubmitForm() {
        String html = "<html><body><form action='/' method='post'><input type='text' name='username' /><input type='password' name='password' /><input type='submit' name='submit' value='Submit' /></form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        // Test form submission
        Connection connection = formElement.submit();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("username", ""),
                new Connection.KeyVal("password", ""));
        List<Connection.KeyVal> actualFormData = connection.requestBody();

        assert (expectedFormData.size() == actualFormData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            Connection.KeyVal expectedData = expectedFormData.get(i);
            Connection.KeyVal actualData = actualFormData.get(i);
            assert (expectedData.key().equals(actualData.key()));
            assert (expectedData.value().equals(actualData.value()));
        }
    }

    @Test
    public void testDisabledFormControl() {
        String html = "<html><body><form action='/' method='get'><input type='text' name='username' disabled /><input type='submit' name='submit' value='Submit' /></form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");

        // Test getting disabled form control
        Element expectedDisabledFormControl = new Element("input", new Attributes().put("type", "text").put("name", "username").put("disabled", "disabled"));
        Element actualDisabledFormControl = formElement.elements().first();

        assert (expectedDisabledFormControl.tag().name().equals(actualDisabledFormControl.tag().name()));
        assert (expectedDisabledFormControl.attr("name").equals(actualDisabledFormControl.attr("name")));
        assert (expectedDisabledFormControl.hasAttr("disabled"));
        assert (!actualDisabledFormControl.hasAttr("checked"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDetermineFormActionUrlFailure() {
        String html = "<html><body><form /></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = document.selectFirst("form");
        formElement.submit();
    }
}