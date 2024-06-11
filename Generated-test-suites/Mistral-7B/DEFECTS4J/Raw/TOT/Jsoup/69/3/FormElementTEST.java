import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 // given a valid HTML tag for a form element
 Tag formTag = new Tag("form");

 // and some attributes
 Attributes attributes = new Attributes();
 attributes.put("action", "/submit");
 attributes.put("method", "post");
 formTag.attributes(attributes);

 // when creating a new FormElement instance
 FormElement formElement = new FormElement(formTag, "http://base.url", attributes);

 // then the FormElement instance should be created successfully
 assertNotNull(formElement);
 }

 @Test
 public void testEmptyFormElement() {
 // given an empty form tag
 Tag formTag = new Tag("form");

 // when creating a new FormElement instance
 FormElement formElement = new FormElement(formTag, "http://base.url", new Attributes());

 // then the FormElement instance should have an empty list of form controls
 assertEquals(0, formElement.elements().size());
 }

 @Test
 public void testFormElementWithOneInput() {
 // given a form tag with one input tag
 Tag formTag = new Tag("form");
 Tag inputTag = new Tag("input");
 inputTag.attr("type", "text");
 inputTag.attr("name", "username");
 formTag.add(inputTag);

 // and some attributes for the form tag
 Attributes attributes = new Attributes();
 attributes.put("action", "/submit");
 attributes.put("method", "post");
 formTag.attributes(attributes);

 // when creating a new FormElement instance
 FormElement formElement = new FormElement(formTag, "http://base.url", attributes);

 // then the FormElement instance should have one form control
 assertEquals(1, formElement.elements().size());

 // and the form control should be an input tag with the name "username"
 Element formControl = formElement.elements().first();
 assertEquals("input", formControl.tagName());
 assertEquals("username", formControl.attr("name"));
 }

 @Test
 public void testFormElementWithMultipleInputs() {
 // given a form tag with multiple input tags
 Tag formTag = new Tag("form");
 Tag input1Tag = new Tag("input");
 input1Tag.attr("type", "text");
 input1Tag.attr("name", "username");
 Tag input2Tag = new Tag("input");
 input2Tag.attr("type", "password");
 input2Tag.attr("name", "password");
 formTag.add(input1Tag);
 formTag.add(input2Tag);

 // and some attributes for the form tag
 Attributes attributes = new Attributes();
 attributes.put("action", "/submit");
 attributes.put("method", "post");
 formTag.attributes(attributes);

 // when creating a new FormElement instance
 FormElement formElement = new FormElement(formTag, "http://base.url", attributes);

 // then the FormElement instance should have two form controls
 assertEquals(2, formElement.elements().size());

 // and the first form control should be the username input
 Element usernameInput = formElement.elements().get(0);
 assertEquals("input", usernameInput.tagName());
 assertEquals("username", usernameInput.attr("name"));

 // and the second form control should be the password input
 Element passwordInput = formElement.elements().get(1);
 assertEquals("input", passwordInput.tagName());
 assertEquals("password", passwordInput.attr("name"));
 }

 @Test
 public void testFormElementSubmitWithValidData() {
 // given a form tag with two input tags and some valid data
 Tag formTag = new Tag("form");
 Tag input1Tag = new Tag("input");
 input1Tag.attr("type", "text");
 input1Tag.attr("name", "username");
 input1Tag.attr("value", "testuser");
 Tag input2Tag = new Tag("input");
 input2Tag.attr("type", "password");
 input2Tag.attr("name", "password");
 input2Tag.attr("value", "testpassword");
 formTag.add(input1Tag);
 formTag.add(input2Tag);

 // and some attributes for the form tag
 Attributes attributes = new Attributes();
 attributes.put("action", "/submit");
 attributes.put("method", "post");
 formTag.attributes(attributes);

 // when creating a new FormElement instance
 FormElement formElement = new FormElement(formTag, "http://base.url", attributes);

 // and when submitting the form with the valid data
 Connection connection = formElement.submit();

 // then the connection should have the correct form data
 List<Connection.KeyVal> formData = connection.data();
 assertEquals(4, formData.size());
 assertEquals("username", formData.get(0).key);
 assertEquals("testuser", formData.get(0).val);
 assertEquals("password", formData.get(1).key);
 assertEquals("testpassword", formData.get(1).val);
 assertEquals("", formData.get(2).key);
 assertEquals("", formData.get(2).val);
 assertEquals("", formData.get(3).key);
 assertEquals("", formData.get(3).val);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitWithoutBaseUri() {
 // given a form tag with two input tags
 Tag formTag = new Tag("form");
 Tag input1Tag = new Tag("input");
 input1Tag.attr("type", "text");
 input1Tag.attr("name", "username");
 Tag input2Tag = new Tag("input");
 input2Tag.attr("type", "password");
 input2Tag.attr("name", "password");
 formTag.add(input1Tag);
 formTag.add(input2Tag);

 // and some attributes for the form tag
 Attributes attributes = new Attributes();
 attributes.put("action", "/submit");
 attributes.put("method", "post");
 formTag.attributes(attributes);

 // when creating a new FormElement instance
 FormElement formElement = new FormElement(formTag, new Attributes(), attributes);

 // and when trying to submit the form without a base URI
 Connection connection = formElement.submit();

 // then an IllegalArgumentException should be thrown
 }
}