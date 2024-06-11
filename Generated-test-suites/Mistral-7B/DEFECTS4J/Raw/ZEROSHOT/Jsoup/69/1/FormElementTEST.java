import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

    private Element formElement;

    @Before
    public void setup() {
        String html = "<html><body>" +
                "<form action='/' method='post'>" +
                "<input type='text' name='name' value='John Doe'>" +
                "<input type='text' name='age' value='30'>" +
                "<select name='gender'>" +
                "<option value='male' selected>Male</option>" +
                "<option value='female'>Female</option>" +
                "</select>" +
                "<input type='submit' value='Submit'>" +
                "</form>" +
                "</body></html>";

        Document doc = Jsoup.parse(html);
        formElement = doc.selectFirst("form");
    }

    @Test
    public void testFormElementConstruction() {
        FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
        assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        Elements elements = formElement.elements();
        assertEquals(3, elements.size());
    }

    @Test
    public void testAddElement() {
        Element input = new Element("input");
        formElement.addElement(input);
        Elements elements = formElement.elements();
        assertEquals(4, elements.size());
        assertEquals(input, elements.get(3));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(6, formData.size());

        Connection.KeyVal nameJohnDoe = new Connection.KeyVal("name", "John Doe");
        Connection.KeyVal ageThirty = new Connection.KeyVal("age", "30");
        Connection.KeyVal genderMale = new Connection.KeyVal("gender", "male");

        for (Connection.KeyVal keyVal : formData) {
            switch (keyVal.key()) {
                case "name":
                    assertEquals(nameJohnDoe.value(), keyVal.value());
                    break;
                case "age":
                    assertEquals(ageThirty.value(), keyVal.value());
                    break;
                case "gender":
                    assertEquals(genderMale.value(), keyVal.value());
                    break;
                default:
                    fail("Unexpected key: " + keyVal.key());
            }
        }
    }
}