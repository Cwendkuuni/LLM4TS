import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFinder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CombiningEvaluatorTest {

 private CombiningEvaluator andEvaluator;
 private CombiningEvaluator orEvaluator;

 @Before
 public void setUp() {
 // Create And evaluator with two evaluators
 Evaluator evaluator1 = new ElementFinder("h1");
 Evaluator evaluator2 = new ElementFinder("p");
 andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

 // Create Or evaluator with three evaluators
 Evaluator evaluator3 = new ElementFinder("h2");
 Evaluator evaluator4 = new ElementFinder("p");
 Evaluator evaluator5 = new ElementFinder("div");
 orEvaluator = new CombiningEvaluator.Or();
 orEvaluator.add(new CombiningEvaluator.And(evaluator3, evaluator4));
 orEvaluator.add(evaluator5);
 }

 @Test
 public void testAndEvaluator() {
 // Test And evaluator with valid elements
 Element root = new Element("html");
 Element node1 = new Element("body").add(new Element("h1", "Test1").add(new Element("p", "Test2")));
 Element node2 = new Element("body").add(new Element("h1", "Test3").add(new Element("p", "Test4")));
 root.appendChild(node1);
 root.appendChild(node2);

 assertTrue(andEvaluator.matches(root, node1));
 assertTrue(andEvaluator.matches(root, node2));

 // Test And evaluator with invalid elements
 Element node3 = new Element("body").add(new Element("h1", "Test5").add(new Element("p", "Test6")));
 root.appendChild(node3);

 assertFalse(andEvaluator.matches(root, node3));
 }

 @Test
 public void testOrEvaluator() {
 // Test Or evaluator with valid elements
 assertTrue(orEvaluator.matches(root, node1));
 assertTrue(orEvaluator.matches(root, node2));
 assertTrue(orEvaluator.matches(root, node3));

 // Test Or evaluator with invalid elements
 Element node4 = new Element("body").add(new Element("h2", "Test7").add(new Element("p", "Test8")));
 root.appendChild(node4);

 assertFalse(orEvaluator.matches(root, node4));
 }

 @Test
 public void testAndEvaluatorWithMultipleMethods() {
 // Test And evaluator with multiple methods
 Evaluator evaluator1 = new ElementFinder("h1");
 Evaluator evaluator2 = new ElementFinder("p");
 Evaluator evaluator6 = new ElementFinder("div");
 andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2, evaluator6);

 Element node5 = new Element("body").add(new Element("h1", "Test1").add(new Element("p", "Test2").add(new Element("div", "Test3"))));
 root.appendChild(node5);

 assertTrue(andEvaluator.matches(root, node5));
 }

 @Test
 public void testOrEvaluatorWithMultipleMethods() {
 // Test Or evaluator with multiple methods
 Evaluator evaluator1 = new ElementFinder("h1");
 Evaluator evaluator2 = new ElementFinder("p");
 Evaluator evaluator3 = new ElementFinder("div");
 orEvaluator = new CombiningEvaluator.Or();
 orEvaluator.add(new CombiningEvaluator.And(evaluator1, evaluator2));
 orEvaluator.add(new CombiningEvaluator.And(evaluator1, evaluator3));

 Element node5 = new Element("body").add(new Element("h1", "Test1").add(new Element("p", "Test2"))).add(new Element("body").add(new Element("h1", "Test1").add(new Element("div", "Test3"))));
 root.appendChild(node5);

 assertTrue(orEvaluator.matches(root, node5.getChild(0)));
 assertTrue(orEvaluator.matches(root, node5.getChild(1)));
 }
}