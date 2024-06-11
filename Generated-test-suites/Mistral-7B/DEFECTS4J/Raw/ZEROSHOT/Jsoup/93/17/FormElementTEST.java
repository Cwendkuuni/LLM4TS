package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.helper.W3C.FORM_ELEMENT;

public class FormElementTest {

    private static final String HTML =
            "<html>" +
                    "<body>" +
                    "<form action=\"https://example.com\" method=\"post\">" +
                    "<input type=\"text\" name=\"username\" value=\"John\">" +
                    "<select name=\"color\">" +
                    "<option value=\"red\" selected>Red</option>" +
                    "<option value=\"green\">Green</option>" +
                    "</select>" +
                    "<input type=\"checkbox\" name=\"agree\" checked>" +
                    "<input type=\"hidden\" name=\"csrf\" value=\"token123\">" +
                    "<input type=\"submit\" value=\"Submit\">" +
                    "</form>" +
                    "</body>" +
                    "</html>";

    private Element form;

    @Before
    public void setup() {
        form = Jsoup.parse(HTML).body().selectFirst(FORM_ELEMENT);
    }

    @Test
    public void testFormElementIsCreated() {
        // Given a form tag
        // Then a FormElement instance should be created

        assertThat(form, is(instanceOf(FormElement.class)));
    }

    @Test
    public void testGetElementsReturnsEmptyListByDefault() {
        // Given a FormElement instance
        // Then getElements() should return an empty list

        Elements elements = form.elements();
        assertThat(elements.size(), is(0));
    }

    @Test
    public void testAddElementMethod() {
        // Given a FormElement instance with no children
        // When adding an Element as a child using addElement() method
        // Then the new Element should be added to the Elements list

        Element textInput = form.selectFirst("input[type=text]");
        form.addElement(textInput);

        Elements elements = form.elements();
        assertThat(elements.size(), is(1));
        assertThat(elements.get(0), is(textInput));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveChildMethodFromEmptyList() {
        // Given a FormElement instance with no children
        // When trying to remove a child using removeChild() method
        // Then an IllegalStateException should be thrown

        FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
        formElement.removeChild(new Element(""));
    }

    @Test
    public void testRemoveChildMethod() {
        // Given a FormElement instance with children
        // When removing a child using removeChild() method
        // Then the child should be removed from the Elements list

        Element textInput = form.selectFirst("input[type=text]");
        form.addElement(textInput);

        Elements elementsBeforeRemoval = form.elements();
        assertThat(elementsBeforeRemoval.size(), is(1));

        form.removeChild(textInput);
        Elements elementsAfterRemoval = form.elements();
        assertThat(elementsAfterRemoval.size(), is(0));
    }

    @Test
    public void testSubmitMethodReturnsConnectionObject() {
        // Given a FormElement instance
        // When calling submit() method
        // Then a Connection object should be returned

        Connection connection = form.submit();
        assertThat(connection, is(instanceOf(Connection.class)));
    }

    @Test
    public void testFormDataMethodReturnsListOfKeyValuePairs() {
        // Given a FormElement instance
        // When calling formData() method
        // Then a List of Key-Value pairs should be returned

        List<Connection.KeyVal> keyValues = form.formData();
        assertThat(keyValues.size(), is(6));

        List<String> expectedKeys = Arrays.asList(
                "csrf", "agree", "username", "color", "csrf", "username"
        );

        for (int i = 0; i < keyValues.size(); i++) {
            Connection.KeyVal keyValue = keyValues.get(i);
            String key = keyValue.key();
            String value = keyValue.value();

            // The order of elements in the DOM is not guaranteed, so we check keys instead
            // of comparing elements directly
            assertThat(expectedKeys.get(i), is(key));

            if (value.length() > 0) {
                String expectedValue = "";

                switch (i) {
                    case 0:
                        expectedValue = "token123";
                        break;
                    case 1:
                        expectedValue = "on";
                        break;
                    case 2:
                        expectedValue = "John";
                        break;
                    case 3:
                        expectedValue = "color";
                        break;
                    case 4:
                        expectedValue = "token123";
                        break;
                    case 5:
                        expectedValue = "John";
                        break;
                    default:
                        break;
                }

                assertThat(value, is(expectedValue));
            } else {
                assertThat(value, is(nullValue()));
            }
        }
    }
}