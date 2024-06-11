import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertNotNull(formElement);
    }

    @Test
    public void testGetFormControls() {
        String html = "<html><body><form><input type='text' name='input1'></form></body></html>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.select("form").first();
        Elements formControls = formElement.elements();
        Assert.assertNotNull(formControls);
        Assert.assertEquals(1, formControls.size());
    }

    @Test
    public void testAddElementToForm() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.select("form").first();
        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "input1");
        formElement.addElement(inputElement);
        Elements formControls = formElement.elements();
        Assert.assertEquals(1, formControls.size());
        Assert.assertEquals(inputElement, formControls.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoActionUrl() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.select("form").first();
        formElement.submit();
    }

    @Test
    public void testSubmitWithValidActionUrl() {
        String baseUri = "http://example.com";
        String html = "<html><body><form action='/submit' method='post'><input type='text' name='input1' value='test'></form></body></html>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.select("form").first();
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals("/submit", connection.url().toString());
    }

    @Test
    public void testFormDataEmptyForm() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSingleInput() {
        String html = "<html><body><form><input type='text' name='input1' value='test'></form></body></html>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("input1", keyVal.key());
        Assert.assertEquals("test", keyVal.val());
    }

    @Test
    public void testFormDataMultipleInputs() {
        String html = "<html><body><form><input type='text' name='input1' value='test1'><input type='text' name='input2' value='test2'></form></body></html>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());
        for (int i = 0; i < formData.size(); i++) {
            Connection.KeyVal keyVal = formData.get(i);
            if (i == 0) {
                Assert.assertEquals("input1", keyVal.key());
                Assert.assertEquals("test1", keyVal.val());
            } else {
                Assert.assertEquals("input2", keyVal.key());
                Assert.assertEquals("test2", keyVal.val());
            }
        }
    }

    @Test
    public void testFormDataSelectElement() {
        String html = "<html><body><form><select name='select1'><option value='opt1'>Option 1</option><option value='opt2' selected>Option 2</option></select></form></body></html>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("select1", keyVal.key());
        Assert.assertEquals("opt2", keyVal.val());
    }

    @Test
    public void testFormDataCheckboxOrRadio() {
        String html = "<html><body><form><input type='radio' name='radio1' value='opt1' checked><input type='checkbox' name='checkbox1' value='opt2' checked></form></body></html>";
        Document doc = Jsoup.parse(html);
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());
        for (int i = 0; i < formData.size(); i++) {
            Connection.KeyVal keyVal = formData.get(i);
            if (i == 0) {
                Assert.assertEquals("radio1", keyVal.key());
                Assert.assertEquals("opt1", keyVal.val());
            } else {
                Assert.assertEquals("checkbox1", keyVal.key());
                Assert.assertEquals("opt2", keyVal.val());
            }
        }
    }
}