import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 // Given a new FormElement is created
 Tag formTag = new Tag("form");
 FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());

 // Then the FormElement should be initialized
 assertNotNull(formElement);
 assertNotNull(formElement.elements());
 assertEquals("form", formElement.tagName());
 }

 @Test
 public void testElementsMethod() {
 // Given a FormElement with Elements
 Element formElement = setupFormElementWithElements("form", "input", "select", "textarea");

 // When calling elements method
 Elements elements = formElement.elements();

 // Then the elements method should return a list of Elements
 assertEquals(4, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 // Given a FormElement and an Element
 Element formElement = setupFormElementWithElements("form");
 Element inputElement = setupInputElement("input", "name", "value");

 // When adding an Element using addElement method
 formElement.addElement(inputElement);

 // Then the Element should be added to the FormElement
 Elements elements = formElement.elements();
 assertTrue(elements.contains(inputElement));
 }

 @Test
 public void testSubmitMethod() {
 // Given a FormElement with a baseUri
 Element formElement = setupFormElementWithBaseUri("form", "baseUri");

 // When calling submit method
 Connection connection = formElement.submit();

 // Then a Connection object should be returned
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithEmptyActionUrl() {
 // Given a FormElement without a baseUri
 Element formElement = setupFormElement("form");

 // When calling submit method
 formElement.submit();

 // Then an IllegalArgumentException should be thrown
 }

 @Test
 public void testFormDataMethod() {
 // Given a FormElement with Elements
 Element formElement = setupFormElementWithElements("form", "input", "select");
 InputElement inputElement = (InputElement) setupInputElement("input", "name", "value");
 SelectElement selectElement = (SelectElement) setupSelectElement("select", "name", "Option1", "Option2");
 formElement.addElement(inputElement);
 formElement.addElement(selectElement);

 // When calling formData method
 List<Connection.KeyVal> formData = formElement.formData();

 // Then the formData method should return a list of Connection.KeyVal with the expected key-value pairs
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("name[input]", "value"));
 expectedData.add(new Connection.KeyVal("name[select]", "Option1"));
 assertEquals(expectedData, formData);
 }

 @Test
 public void testFormDataMethodWithEmptyForm() {
 // Given a FormElement without Elements
 Element formElement = setupFormElement("form");

 // When calling formData method
 List<Connection.KeyVal> formData = formElement.formData();

 // Then an empty list should be returned
 assertEquals(0, formData.size());
 }

 // Helper methods
 private Element setupFormElement(String... tags) {
 Element formTag = new Tag("form");
 FormElement formElement = new FormElement(formTag, "", new Attributes());
 for (String tag : tags) {
 Element element = setupElement(tag);
 formElement.addElement(element);
 }
 return formElement;
 }

 private Element setupFormElementWithBaseUri(String... tags) {
 Element formTag = new Tag("form");
 FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
 for (String tag : tags) {
 Element element = setupElement(tag);
 formElement.addElement(element);
 }
 return formElement;
 }

 private Element setupFormElementWithElements(String tag, String... otherTags) {
 Element formTag = new Tag(tag);
 FormElement formElement = new FormElement(formTag, "", new Attributes());
 for (String otherTag : otherTags) {
 Element element = setupElement(otherTag);
 formElement.addElement(element);
 }
 return formElement;
 }

 private Element setupInputElement(String tag, String name, String value) {
 InputElement input = new InputElement(new Tag(tag), "", new Attributes().put("name", name).put("value", value));
 return input;
 }

 private Element setupSelectElement(String tag, String name, String... options) {
 SelectElement select = new SelectElement(new Tag(tag), "", new Attributes().put("name", name));
 for (String option : options) {
 Option optionElement = new Option(new Tag("option"), "", new Attributes().put("value", option));
 select.addElement(optionElement);
 }
 return select;
 }

 private Element setupElement(String tag) {
 return new Element(new Tag(tag)) {
 };
 }
}

class InputElement extends Element {
 // Empty implementation
}

class SelectElement extends Element {
 // Empty implementation
}

class Option extends Element {
 // Empty implementation
}