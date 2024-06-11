
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

public class FormElementTest {

    @Test
    public void testElements() {
        Document doc = Jsoup.parse("<html><body><form><input type='text' name='name' /></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();
        Elements elements = formElement.elements();

        assertEquals(1, elements.size());
    }

    @Test
    public void testEmptyForm() {
        Document doc = Jsoup.parse("<html><body><form></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();
        Elements elements = formElement.elements();

        assertEquals(0, elements.size());
    }

    @Test
    public void testFormWithMultipleElements() {
        Document doc = Jsoup.parse("<html><body><form><input type='text' name='name1' /><input type='text' name='name2' /></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();
        Elements elements = formElement.elements();

        assertEquals(2, elements.size());
    }

    @Test
    public void testFormWithNonFormSubmittableElements() {
        Document doc = Jsoup.parse("<html><body><form><div name='name'></div></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();
        Elements elements = formElement.elements();

        assertEquals(0, elements.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormWithNullBaseURI() {
        FormElement formElement = new FormElement(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormWithInvalidFormActionURL() {
        Document doc = Jsoup.parse("<html><body><form action='javascript:alert(\"Hello\")'></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();
        formElement.submit();
    }

    @Test
    public void testFormWithNullOrEmptyFormName() {
        Document doc = Jsoup.parse("<html><body><form name=''></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();
        Elements elements = formElement.elements();

        assertEquals(0, elements.size());
    }

    @Test
    public void testFormWithVariousInputTypes() {
        Document doc = Jsoup.parse("<html><body><form><input type='text' name='name1'><input type='select' name='name2'><input type='radio' name='name3'><input type='checkbox' name='name4'></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();

        Elements elements = formElement.elements();
        assertEquals(4, elements.size());
    }

    @Test
    public void testFormWithMultipleSelectElements() {
        Document doc = Jsoup.parse("<html><body><form><select name='name' multiple><option value='option1'>Option 1</option><option value='option2'>Option 2</option></select></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();
        Elements elements = formElement.elements();

        assertEquals(1, elements.size());
        Element selectElement = elements.first();
        Elements options = selectElement.select("option");

        assertEquals(2, options.size());
    }

    @Test
    public void testFormWithCheckedRadioButtonsOrCheckboxes() {
        Document doc = Jsoup.parse("<html><body><form><input type='radio' name='name' value='value1' checked><input type='radio' name='name' value='value2'><input type='checkbox' name='name' value='value3' checked></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();
        Elements elements = formElement.elements();

        assertEquals(3, elements.size());
    }

    @Test
    public void testFormSubmission() throws Exception {
        Document doc = Jsoup.parse("<html><body><form name='form1' action='http://example.com' method='post'><input type='text' name='name' value='value'></form></body></html>");
        FormElement formElement = doc.getElementsByTag("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Connection connection = formElement.submit();

        connection.method(Connection.Method.POST);
        connection.execute();

        assertEquals("value", formData.get(0).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormSubmissionWithNullForm() {
        FormElement formElement = null;
        formElement.submit();
    }
}