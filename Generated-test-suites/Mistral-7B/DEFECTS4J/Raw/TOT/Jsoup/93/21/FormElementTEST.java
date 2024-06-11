import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link FormElement}.
 */
public class FormElementTest {

    private Element doc;

    @Before
    public void setUp() {
        doc = Jsoup.parseBodyElements(
                "<html><body>" +
                        "<form action=\"/submit\" method=\"post\">" +
                        "<input type=\"text\" name=\"username\" value=\"John\">" +
                        "<input type=\"hidden\" name=\"csrf\" value=\"12345\">" +
                        "<select name=\"colors\">" +
                        "<option value=\"red\" selected>Red</option>" +
                        "<option value=\"green\">Green</option>" +
                        "</select>" +
                        "<input type=\"checkbox\" name=\"agree\" checked>" +
                        "<input type=\"radio\" name=\"gender\" value=\"male\" checked>" +
                        "</form>" +
                 "</body></html>");
    }

    @Test
    public void testCreateFormElement() {
        FormElement formElement = (FormElement) doc.select("form").first();
        assertNotNull(formElement);
        assertEquals("form", formElement.tagName());
    }

    @Test
    public void testGetElements() {
        FormElement formElement = (FormElement) doc.select("form").first();
        Elements elements = formElement.elements();
        assertEquals(6, elements.size());

        for (Element el : elements) {
            System.out.println(el.tagName());
            assert (el.tagName().length() > 0);
        }
    }

    @Test
    public void testAddElement() {
        FormElement formElement = (FormElement) doc.select("form").first();
        Element newElement = new Element("input", new Attributes().put("type", "submit"));
        formElement.addElement(newElement);
        Elements elements = formElement.elements();
        assertEquals(7, elements.size());
        assert (elements.get(6).tagName().equals("input"));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertNotNull(connection.request().url());
        assertEquals("POST", connection.method().toString());

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(8, formData.size());

        for (Connection.KeyVal kv : formData) {
            System.out.println(kv.key + " = " + kv.val);
        }
    }

    @Test
    public void testEmptyFormData() {
        FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() {
        FormElement formElement = (FormElement) doc.select("form[action='']").first();
        formElement.submit();
    }
}