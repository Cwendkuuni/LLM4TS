package org.jsoup.select.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementHelper;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CombiningEvaluatorTest {

 private CombiningEvaluator evaluator;
 private Element root;
 private Element node;

 public CombiningEvaluatorTest(Evaluator... evaluators) {
 this.evaluator = new Or(Arrays.asList(evaluators));
 this.root = ElementHelper.parseHtml("<html><body></body></html>");
 this.node = root.body();
 }

 @Parameterized.Parameters(name = "{0}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Add test data here
 });
 }

 @Test
 public void testAnd() {
 And andEvaluator = new And(Arrays.asList(new ElementStartsWith("html"), new ElementHasClass("body")));
 And and = (And) evaluator;
 And andTest = new And(Arrays.asList(new ElementHasText("test"), new ElementHasClass("testClass")));
 And andTest2 = new And(Arrays.asList(new ElementHasText("test2"), new ElementHasClass("testClass2")));
 And andTest3 = new And(Arrays.asList(new ElementHasText("test3"), new ElementHasClass("testClass3")));
 And andTestCombined = new And(Arrays.asList(andTest, andTest2, andTest3));

 assertTrue(andCombined.matches(root, node)); // test positive case
 assertFalse(andCombined.matches(root, root)); // test negative case with different node
 }

 @Test
 public void testOr() {
 Or orEvaluator = new Or(Arrays.asList(new ElementStartsWithTagName("html"), new ElementHasClass("body")));
 Or or = (Or) evaluator;
 Or orTest = new And(Arrays.asList(new ElementHasText("test"), new ElementHasClass("testClass")));
 Or orTest2 = new And(Arrays.asList(new ElementHasText("test2"), new ElementHasClass("testClass2")));
 Or orTest3 = new And(Arrays.asList(new ElementHasText("test3"), new ElementHasClass("testClass3")));
 Or orTestCombined = new Or(Arrays.asList(orTest, orTest2, orTest3));
 Or or = new Or();
 or.add(orTestCombined);

 assertTrue(or.matches(root, node)); // test positive case with any match
 assertFalse(or.matches(root, root)); // test negative case with different node
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyEvaluators() {
 evaluator = new Or(new ArrayList<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEvaluator() {
 evaluator = new And(null);
 }

 @Test
 public void testToString() {
 assertTrue(evaluator.toString().contains(":or"));
 }
}