package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String HTML_FORM_SAMPLE = "<html>\n" +
         "<body>\n" +
         "<form action=\"http://test.com\" method=\"get\">\n" +
         "<input type=\"text\" name=\"username\" value=\"test\">\n" +
         "<select name=\"color\">\n" +
         "<option value=\"red\" selected>\nRed\n</option>\n" +
         "<option value=\"green\">\nGreen\n</option>\n" +
         "<option value=\"blue\">\nBlue\n</option>\n" +
         "</select>\n" +
         "<input type=\"checkbox\" name=\"agree\" checked>\nAgree\n" +
         "<input type=\"submit\" value=\"Submit\">\n" +
         "</form>\n" +
         "</body>\n" +
         "</html>";

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Assert.assertEquals("form", formElement.tagName());
 }

 @Test
 public void testFormElementsMethod() {
 FormElement formElement = Jsoup.parse(HTML_FORM_SAMPLE).body().selectFirst("form");
 List<Element> expectedElements = new ArrayList<Element>() {{
 add(formElement.selectFirst("input[name=\"username\"]"));
 add(formElement.selectFirst("select[name=\"color\"]"));
 add(formElement.selectFirst("input[type=\"checkbox\"][name=\"agree\"]"));
 }};
 List<Element> actualElements = formElement.elements();
 Assert.assertEquals(expectedElements, actualElements);
 }

 @Test(expected = IllegalStateException.class)
 public void testFormElementAddElementWithNullElement() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 formElement.addElement(null);
 }

 @Test
 public void testFormElementRemoveChild() {
 FormElement formElement = Jsoup.parse(HTML_FORM_SAMPLE).body().selectFirst("form");
 formElement.addElement(new Element("test"));
 formElement.removeChild(formElement.child(0));
 List<Element> expectedElements = new ArrayList<Element>() {{
 add(formElement.selectFirst("select[name=\"color\"]"));
 add(formElement.selectFirst("input[type=\"checkbox\"][name=\"agree\"]"));
 add(formElement.selectFirst("input[type=\"submit\"]"));
 }};
 List<Element> actualElements = formElement.elements();
 Assert.assertEquals(expectedElements, actualElements);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitWhenNoActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormElementSubmitWithGetMethod() {
 FormElement formElement = Jsoup.parse(HTML_FORM_SAMPLE).body().selectFirst("form");
 Connection connection = formElement.submit();
 String expectedUrl = "http://test.com?username=test&color=red&agree=on";
 String actualUrl = connection.url();
 Assert.assertEquals(expectedUrl, actualUrl);
 }

 @Test
 public void testFormElementSubmitWithPostMethod() {
 FormElement formElement = Jsoup.parse(HTML_FORM_SAMPLE).body().selectFirst("form");
 formElement.attr("method", "post");
 Connection connection = formElement.submit();
 String expectedUrl = "http://test.com";
 String actualUrl = connection.url();
 Assert.assertEquals(expectedUrl, actualUrl);
 List<Connection.KeyVal> expectedFormData = new ArrayList<Connection.KeyVal>() {{
 add(HttpConnection.KeyVal.create("username", "test"));
 add(HttpConnection.KeyVal.create("color", "red"));
 add(HttpConnection.KeyVal.create("agree", "on"));
 }};
 List<Connection.KeyVal> actualFormData = connection.requestBody();
 Assert.assertEquals(expectedFormData, actualFormData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitWithEmptyActionAttribute() {
 FormElement formElement = Jsoup.parse(HTML_FORM_SAMPLE).body().selectFirst("form");
 formElement.attr("action", "");
 formElement.submit();
 }
}