import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());

        form.addElement(element);

        Elements elements = form.elements();

        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        String actionUrl = "http://www.example.com/submit";
        String method = "POST";
        form.attr("action", actionUrl);
        form.attr("method", method);

        Connection connection = form.submit();

        assertNotNull(connection);
        assertEquals(actionUrl, connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionUrl() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
        Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());

        form.addElement(inputElement);
        form.addElement(selectElement);

        // Input element without name attribute, should be skipped
        inputElement.attr("type", "text");

        // Select element with no options, name attribute, and no selected option, should be skipped
        selectElement.attr("name", "selectElement");
        selectElement.tag().isFormSubmittable();

        List<Connection.KeyVal> formData = form.formData();

        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());

        assertEquals(form1, form2);
    }
}