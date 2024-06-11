import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Assert.assertNotNull(formElement);
        Assert.assertEquals("form", formElement.tagName());
    }

    @Test
    public void testGetElements() {
        Parser parser = new Parser();
        String html = "<form><input type='text' name='username'/><input type='password' name='password'/></form>";
        Document document = parser.parse(html);
        Element formElement = document.select("form").first();

        Elements expectedElements = document.select("input");

        Elements actualElements = formElement.elements();

        Assert.assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            Assert.assertEquals(expectedElements.get(i), actualElements.get(i));
        }
    }

    @Test
    public void testAddElement() {
        Parser parser = new Parser();
        String html = "<form></form>";
        Document document = parser.parse(html);
        Element formElement = document.select("form").first();

        Element inputElement = parser.parseInput("text", "", "inputName").htmlElement();

        formElement.addElement(inputElement);

        Elements actualElements = formElement.elements();

        Assert.assertEquals(1, actualElements.size());
        Assert.assertEquals(inputElement, actualElements.get(0));
    }

    @Test
    public void testSubmitNormalCase() throws Exception {
        Parser parser = new Parser();
        String html = "<form action='/submit' method='post'><input type='text' name='username' value='testUser'/></form>";
        Document document = parser.parse(html);
        Element formElement = document.select("form").first();

        Connection connection = formElement.submit();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("username", formData.get(0).key);
        Assert.assertEquals("testUser", formData.get(0).value);

        String response = connection.toString();
        // Add assertions to check the response if needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMissingActionAttribute() {
        Parser parser = new Parser();
        String html = "<form><input type='text' name='username' value='testUser'/></form>";
        Document document = parser.parse(html);
        Element formElement = document.select("form").first();

        formElement.submit();
    }

    @Test
    public void testFormDataEmptyForm() {
        Parser parser = new Parser();
        String html = "<form></form>";
        Document document = parser.parse(html);
        Element formElement = document.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithSelectElement() {
        Parser parser = new Parser();
        String html = "<form><select name='colors'><option value='red' selected>Red</option><option value='green'>Green</option></select></form>";
        Document document = parser.parse(html);
        Element formElement = document.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("colors", formData.get(0).key);
        Assert.assertEquals("red", formData.get(0).value);
    }

    @Test
    public void testFormDataWithCheckboxOrRadioElement() {
        Parser parser = new Parser();
        String html = "<form><input type='checkbox' name='agree' value='yes' checked/>Agree</form>";
        Document document = parser.parse(html);
        Element formElement = document.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("agree", formData.get(0).key);
        Assert.assertEquals("yes", formData.get(0).value);
    }

    @Test
    public void testFormDataWithMultipleElements() {
        Parser parser = new Parser();
        String html = "<form><input type='text' name='username' value='testUser'/><input type='checkbox' name='agree' value='yes' checked/></form>";
        Document document = parser.parse(html);
        Element formElement = document.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("username", formData.get(0).key);
        Assert.assertEquals("testUser", formData.get(0).value);
        Assert.assertEquals("agree", formData.get(1).key);
        Assert.assertEquals("yes", formData.get(1).value);
    }
}