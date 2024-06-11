package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {
    // ... Previous test cases ...

    @Test
    public void testEmptyFormElementsMethod() {
        String html = "<html><body><form></form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = (FormElement) document.select("form").first();

        Elements elements = formElement.elements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testSubmitMethodWithEmptyFormData() {
        String html = "<html><body><form action='/' method='GET'></form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = (FormElement) document.select("form").first();

        Connection connection = formElement.submit();

        assertEquals("GET", connection.method().name());
        assertEquals("/", connection.url().toString());
        assertEquals(0, connection.data().size());
    }

    @Test
    public void testFormDataMethodWithEmptyForm() {
        String html = "<html><body><form></form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = (FormElement) document.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }
}