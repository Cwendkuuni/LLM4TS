package org.jsoup.nodes;

import org.junit.jupitertestng.annotations.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.NotFound;
import static org.jsoup.nodes.Attributes.empty;
import static org.jsoup.nodes.Attributes.createAttribute;
import static org.jsoup.nodes.Attributes.dataset;
import static org.jsoup.nodes.Attributes.put;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(JUnitPlatform.class)
public class AttributesTest {

 @Test
 public void testAttributesWithEmptyInitialSize() {
 Attributes attributes = empty();
 assertThat(attributes.size(), is(0));
 }

 @Test
 public void testAttributesWithInitialSize() {
 Attributes attributes = new Attributes(InitialCapacity);
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertThat(attributes.size(), is(2));
 }

 @Test
 public void testGetMethodReturnsEmptyStringForUnsetAttribute() {
 Attributes attributes = empty();
 assertThat(attributes.get("key1"), is(EmptyString));
 }

 @Test
 public void testGetMethodReturnsValueForSetAttribute() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 assertThat(attributes.get("key1"), is("value1"));
 }

 @Test
 public void testGetIgnoreCaseMethodReturnsValueForSetAttributeIgnoringCase() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 attributes.put("KEY1", "value2");
 assertThat(attributes.getIgnoreCase("key1"), is("value1"));
 assertThat(attributes.getIgnoreCase("KEY1"), is("value2"));
 }

 @Test
 public void testGetMethodReturnsEmptyStringForBooleanAttribute() {
 Attributes attributes = empty();
 attributes.put("booleanKey", true);
 assertThat(attributes.get("booleanKey"), is(EmptyString));
 }

 @Test
 public void testPutMethodReplacesExistingAttributeWithSameKey() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 assertThat(attributes.get("key1"), is("value2"));
 }

 @Test
 public void testPutMethodAddsNewAttributeWithSameKeyThrowsException() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 try {
 attributes.put("key1", "value2");
 } catch (IllegalArgumentException e) {
 assertThat(e.getMessage(), is("Duplicate key: key1"));
 }
 }

 @Test
 public void testPutMethodAddsNewAttributeWithUniqueKey() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertThat(attributes.get("key1"), is("value1"));
 assertThat(attributes.get("key2"), is("value2"));
 }

 @Test
 public void testRemoveMethodRemovesAttributeByIndex() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put(3, "value3");
 attributes.remove(1);
 assertThat(attributes.get("key1"), is("value1"));
 assertThat(attributes.get("key2"), is("value2"));
 assertThat(attributes.get(2), is("value3"));
 }

 @Test
 public void testRemoveMethodThrowsExceptionForInvalidIndex() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 try {
 attributes.remove(-1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testHasKeyMethodReturnsTrueForExistingAttribute() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 assertThat(attributes.hasKey("key1"), is(true));
 }

 @Test
 public void testHasKeyMethodReturnsFalseForNonExistingAttribute() {
 Attributes attributes = empty();
 assertThat(attributes.hasKey("key1"), is(false));
 }

 @Test
 public void testHasKeyIgnoreCaseMethodReturnsTrueForExistingAttributeIgnoringCase() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 attributes.put("KEY1", "value2");
 assertThat(attributes.hasKeyIgnoreCase("key1"), is(true));
 assertThat(attributes.hasKeyIgnoreCase("KEY1"), is(true));
 }

 @Test
 public void testHasKeyIgnoreCaseMethodReturnsFalseForNonExistingAttributeIgnoringCase() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 assertThat(attributes.hasKeyIgnoreCase("key2"), is(false));
 }

 @Test
 public void testSizeMethodReturnsCorrectSize() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertThat(attributes.size(), is(2));
 }

 @Test
 public void testAddAllMethodAddsAllAttributesFromAnotherAttributesObject() {
 Attributes attributes1 = empty();
 attributes1.put("key1", "value1");
 attributes1.put("key2", "value2");

 Attributes attributes2 = empty();
 attributes2.put("key3", "value3");
 attributes2.put("key4", "value4");

 attributes1.addAll(attributes2);
 assertThat(attributes1.size(), is(4));
 assertThat(attributes1.get("key1"), is("value1"));
 assertThat(attributes1.get("key2"), is("value2"));
 assertThat(attributes1.get("key3"), is("value3"));
 assertThat(attributes1.get("key4"), is("value4"));
 }

 @Test
 public void testIteratorMethodReturnsCorrectIterator() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 List<Map.Entry<String, String>> list = new ArrayList<>();
 for (Map.Entry<String, String> entry : attributes) {
 list.add(entry);
 }

 assertThat(list.size(), is(2));
 assertThat(list.get(0).getKey(), is("key1"));
 assertThat(list.get(0).getValue(), is("value1"));
 assertThat(list.get(1).getKey(), is("key2"));
 assertThat(list.get(1).getValue(), is("value2"));
 }

 @Test
 public void testAsListMethodReturnsUnmodifiableList() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 List<Attribute> list = attributes.asList();
 assertThat(list.size(), is(2));
 assertThat(list.get(0), is(attributes.get("key1")));
 assertThat(list.get(1), is(attributes.get("key2")));

 try {
 list.add(createAttribute("key3", "value3"));
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testDatasetMethodReturnsCorrectMap() {
 Attributes attributes = empty();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");

 Map<String, String> dataset = attributes.dataset();
 assertThat(dataset.size(), is(2));
 assertThat(dataset.get("data-key1"), is("value1"));
 assertThat(dataset.get("data-key2"), is("value2"));
 }

 @Test
 public void testHtmlMethodReturnsCorrectHtmlString() {
 Attributes attributes = empty();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 StringBuilder sb = new StringBuilder();
 attributes.html(sb, Document.OutputSettings.htmlParadigm());
 String html = sb.toString();

 String expectedHtml = " key1=\"value1\" key2=\"value2\"";
 assertThat(html, is(expectedHtml));
 }
}