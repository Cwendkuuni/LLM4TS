import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 // Given
 Tag tag = new Tag("form");
 String baseUri = "http://test.com";
 Attributes attributes = new Attributes();

 // When
 FormElement formElement = new FormElement(tag, baseUri, attributes);

 // Then
 assertNotNull(formElement);
 assertEquals("form", formElement.tagName());
 assertEquals(baseUri, formElement.baseUri());
 }

 @Test
 public void testGetFormControls() {
 // Given
 Tag formTag = new Tag("form");
 Tag inputTag1 = new Tag("input");
 Tag inputTag2 = new Tag("input");
 String baseUri = "http://test.com";
 Attributes attributes = new Attributes();
 List<Element> elements = new ArrayList<>();
 elements.add(inputTag1);
 elements.add(inputTag2);
 FormElement formElement = new FormElement(formTag, baseUri, attributes);
 formElement.elements = elements;

 // When
 Elements formControls = formElement.elements();

 // Then
 assertEquals(2, formControls.size());
 assertEquals(inputTag1, formControls.get(0));
 assertEquals(inputTag2, formControls.get(1));
 }

 @Test
 public void testAddElement() {
 // Given
 Tag formTag = new Tag("form");
 Tag inputTag = new Tag("input");
 String baseUri = "http://test.com";
 Attributes attributes = new Attributes();
 FormElement formElement = new FormElement(formTag, baseUri, attributes);

 // When
 formElement.addElement(inputTag);

 // Then
 assertEquals(1, formElement.elements().size());
 assertEquals(inputTag, formElement.elements().get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseURI() {
 // Given
 Tag formTag = new Tag("form");
 FormElement formElement = new FormElement(formTag, null, new Attributes());

 // When & Then
 formElement.submit();
 }

 @Test
 public void testFormData() {
 // Given
 Tag formTag = new Tag("form");
 Tag inputTag1 = new Tag("input");
 inputTag1.attr("name", "input1");
 inputTag1.attr("type", "text");
 inputTag1.attr("value", "value1");
 Tag inputTag2 = new Tag("input");
 inputTag2.attr("name", "input2");
 inputTag2.attr("type", "text");
 inputTag2.attr("value", "value2");
 Tag selectTag = new Tag("select");
 Elements optionElements = new Elements();
 optionElements.add(new Tag("option").attr("value", "option1").attr("selected", "true"));
 optionElements.add(new Tag("option").attr("value", "option2"));
 selectTag.appendText("Select");
 selectTag.appendElements(optionElements);
 selectTag.attr("name", "select");
 String baseUri = "http://test.com";
 Attributes attributes = new Attributes();
 List<Element> elements = new ArrayList<>();
 elements.add(inputTag1);
 elements.add(inputTag2);
 elements.add(selectTag);
 FormElement formElement = new FormElement(formTag, baseUri, attributes);
 formElement.elements = elements;

 // When
 List<Connection.KeyVal> formData = formElement.formData();

 // Then
 assertEquals(6, formData.size());
 assertEquals("input1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 assertEquals("input2", formData.get(1).key);
 assertEquals("value2", formData.get(1).value);
 assertEquals("select", formData.get(2).key);
 assertEquals("option1", formData.get(3).value);
 assertEquals("select", formData.get(4).key);
 assertEquals("option2", formData.get(5).value);
 }
}