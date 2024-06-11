import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form><input type='text' name='username'><input type='submit' value='Submit'></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Assert.assertNotNull(formElement);
    }

    @Test
    public void testEmptyFormElement() {
        String html = "<form></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Assert.assertNotNull(formElement);
        Assert.assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testFormElementWithMultipleElements() {
        String html = "<form><input type='text' name='username'><input type='text' name='password'><input type='submit' value='Submit'></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.selectFirst("form");
        List<Element> elements = formElement.elements();
        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(3, elements.size());
        Assert.assertEquals("username", elements.get(0).tagName());
        Assert.assertEquals("password", elements.get(1).tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementWithNoActionAttribute() {
        String html = "<form></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Connection connection = formElement.submit();
    }

    @Test
    public void testFormElementWithInvalidMethod() {
        String html = "<form action='/' method='HEAD'><input type='text' name='username'></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Connection connection = formElement.submit();
        connection.method(Connection.Method.GET);
        connection.execute();
    }

    @Test
    public void testFormElementWithValidMethodAndData() {
        String html = "<form action='/' method='POST'><input type='text' name='username' value='test'></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Connection connection = formElement.submit();
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("username", data.get(0).key);
        Assert.assertEquals("test", data.get(0).value);
        connection.execute();
    }

    @Test
    public void testFormElementWithSelectElement() {
        String html = "<form action='/' method='POST'><select name='country'><option value='US'>United States</option><option value='CA'>Canada</option></select><input type='submit' value='Submit'></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Connection connection = formElement.submit();
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("country", data.get(0).key);
        // The value of the selected option is "US" in this case
        Assert.assertEquals("US", data.get(0).value);
        connection.execute();
    }

    @Test
    public void testFormElementWithCheckboxOrRadioElement() {
        String html = "<form action='/' method='POST'><input type='checkbox' name='agreeToTerms' value='agree'> Agree to terms<input type='submit' value='Submit'></form>";
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.children().get(0).attr("checked", "checked");
        Connection connection = formElement.submit();
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("agreeToTerms", data.get(0).key);
        Assert.assertEquals("agree", data.get(0).value);
        connection.execute();
    }
}