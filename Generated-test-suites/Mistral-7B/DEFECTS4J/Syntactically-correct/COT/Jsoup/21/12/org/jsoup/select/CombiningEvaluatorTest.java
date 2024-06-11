import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

 private CombiningEvaluator andEvaluator;
 private CombiningEvaluator orEvaluator;

 @Test
 public void testAndConstructor() {
 andEvaluator = new CombiningEvaluator.And( Arrays.asList(new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("div");
 }
 }, new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("p");
 }
 }) );
 }

 @Test
 public void testAndMatches() {
 Element testElement = new Element("html").add("body", new Element("div"), new Element("p"), new Element("div"));
 assertTrue(andEvaluator.matches(new Element("html"), testElement));
 }

 @Test
 public void testAndDoesNotMatch() {
 Element testElement = new Element("html").add("body", new Element("div"), new Element("p"), new Element("span"));
 assertFalse(andEvaluator.matches(new Element("html"), testElement));
 }

 @Test
 public void testOrConstructor() {
 orEvaluator = new CombiningEvaluator.Or( Arrays.asList(new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("div");
 }
 }, new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("p");
 }
 }, new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("span");
 }
 }) );
 }

 @Test
 public void testOrMatches() {
 Element testElement = new Element("html").add("body", new Element("div"), new Element("p"), new Element("span"));
 assertTrue(orEvaluator.matches(new Element("html"), testElement));
 }

 @Test
 public void testOrDoesNotMatch() {
 Element testElement = new Element("html").add("body", new Element("div"), new Element("p"));
 assertFalse(orEvaluator.matches(new Element("html"), testElement));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrWithEmptyList() {
 new CombiningEvaluator.Or(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrWithOneEvaluator() {
 new CombiningEvaluator.Or(Arrays.asList(new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("div");
 }
 }));
 }

 @Test
 public void testAndToString() {
 assertEquals("div p", andEvaluator.toString());
 }

 @Test
 public void testOrToString() {
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("div");
 }
 });
 evaluators.add(new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("p");
 }
 });
 orEvaluator = new CombiningEvaluator.Or(evaluators);
 assertEquals(":or div p", orEvaluator.toString());
 }

 @Test
 public void testOrAddEvaluator() {
 orEvaluator = new CombiningEvaluator.Or(new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("div");
 }
 });
 orEvaluator.add(new Evaluator() {
 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals("p");
 }
 });
 assertEquals(":or div p", orEvaluator.toString());
 }

}