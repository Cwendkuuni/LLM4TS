package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 Element root = Element.parse("<html><body><div id='div1'><p>Test1</p></div>" +
 "<div id='div2'><p>Test2</p></div></body></html>");

 And andEvaluator = new And(Arrays.asList(
 new ElementStartsWithEvaluator("div"),
 new AttributeEvaluator("id", "div1")
 ));

 List<Element> result = root.select(andEvaluator.toString());

 assertTrue(result.size() == 1);
 Element expectedResult = root.select("div:contains(Test1)").get(0);
 assertTrue(result.get(0).equals(expectedResult));
 }

 @Test
 public void testOrEvaluator() {
 Element root = Element.parse("<html><body><div id='div1'><p>Test1 Test3</p></div>" +
 "<div id='div2'><p>Test2</p></div></body></html>");

 Or orEvaluator = new Or(Arrays.asList(
 new ElementStartsWithEvaluator("div"),
 new AttributeEvaluator("id", "div1"),
 new ElementContainsStringEvaluator("p", "Test3")
 ));

 List<Element> result = root.select(orEvaluator.toString());

 assertTrue(result.size() == 2);
 Element div1 = root.select("div:contains(Test1)").get(0);
 Element div2 = root.select("div p:contains(Test3)").get(0).parent();
 assertTrue(result.contains(div1));
 assertTrue(result.contains(div2));
 }

 @Test
 public void testAndEvaluatorWithMultipleElements() {
 Document doc = Elements.parseHtml("<html><body>" +
 "<div id='div1'><p>Test1</p><p>Test3</p></div>" +
 "<div id='div2'><p>Test2</p></div>" +
 "</body></html>");

 And andEvaluator = new And(Arrays.asList(
 new ElementStartsWithEvaluator("div"),
 new HasAttributeEvaluator("id", "div1")
 ));

 List<Element> result = doc.select(andEvaluator.toString());

 assertTrue(result.size() == 1);
 Element expectedResult = doc.select("div:has(p:contains(Test1)):has(p:contains(Test3))").get(0);
 assertTrue(result.get(0).equals(expectedResult));
 }

 @Test
 public void testOrEvaluatorWithMultipleElements() {
 Document doc = Elements.parseHtml("<html><body>" +
 "<div id='div1'><p>Test1</p></div>" +
 "<div id='div2'><p>Test2</p></div>" +
 "</body></html>");

 Or orEvaluator = new Or(Arrays.asList(
 new ElementStartsWithEvaluator("div"),
 new HasAttributeEvaluator("id", "div1"),
 new ElementContainsStringEvaluator("p", "Test2")
 ));

 List<Element> result = doc.select(orEvaluator.toString());

 assertTrue(result.size() == 2);
 Element div1 = doc.select("div:contains(Test1)").get(0);
 Element div2 = doc.select("div p:contains(Test2)").get(0).parent();
 assertTrue(result.contains(div1));
 assertTrue(result.contains(div2));
 }

 @Test
 public void testOrEvaluatorWithEmptyList() {
 Or orEvaluator = new Or(new ArrayList<>());
 List<Element> result = Elements.empty().select(orEvaluator.toString());
 assertTrue(result.isEmpty());
 }
}

class ElementStartsWithEvaluator extends ElementEvaluator {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().startsWith("div");
 }
}

class AttributeEvaluator extends AttributeEvaluator {
 @Override
 public boolean matches(Element root, Element node) {
 return "div1".equals(node.attr("id"));
 }
}

class HasAttributeEvaluator extends AttributeEvaluator {
 @Override
 public boolean matches(Element root, Element node) {
 return "id".equals(node.tagName()) && "div1".equals(node.attr("id"));
 }
}

class ElementContainsStringEvaluator extends ElementEvaluator {
 @Override
 public boolean matches(Element root, Element node) {
 return "p".equals(node.tagName()) && "Test3".equals(node.text());
 }
}