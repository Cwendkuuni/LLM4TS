import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        assertNotNull(formElement.elements());
        assertEquals(new Elements(), formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = formElement.addElement(element);

        assertNotNull(result);
        assertEquals(formElement, result);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        ArrayList<Connection.KeyVal> data = new ArrayList<>();

        Elements elements = formElement.elements();

        for (Element el: elements) {
            if (!el.tag().isFormSubmittable()) continue; // contents are form listable, superset of submitable
            if (el.hasAttr("disabled")) continue; // skip disabled form inputs
            String name = el.attr("name");
            if (name.length() == 0) continue;
            String type = el.attr("type");

            if ("select".equals(el.tagName())) {
                Elements options = el.select("option[selected]");
                boolean set = false;
                for (Element option: options) {
                    data.add(HttpConnection.KeyVal.create(name, option.val()));
                    set = true;
                }
                if (!set) {
                    Element option = el.select("option").first();
                    if (option != null)
                        data.add(HttpConnection.KeyVal.create(name, option.val()));
                }
            } else if ("checkbox".equalsIgnoreCase(type) || "radio".equalsIgnoreCase(type)) {
                // only add checkbox or radio if they have the checked attribute
                if (el.hasAttr("checked")) {
                    final String val = el.val().length() >  0 ? el.val() : "on";
                    data.add(HttpConnection.KeyVal.create(name, val));
                }
            } else {
                data.add(HttpConnection.KeyVal.create(name, el.val()));
            }
        }

        assertNotNull(data);
        assertEquals(new ArrayList<>(), data);
    }
}