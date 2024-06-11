import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 Element root = Jsoup.parse("<html><body><div id='div1' class='class1 class2'><p>Test</p></div><div id='div2' class='class2'><p>Test</p></div></body></html>");

 And evaluator1 = new And(Arrays.asList(
 new ElementSelector("div.class1"),
 new AttributeSelector("div[id=div2]")
 ));

 assertTrue(evaluator1.matches(root, root.selectFirst("div.class1.class2:eq(0)")));
 assertFalse(evaluator1.matches(root, root.selectFirst("div.class1.class2:eq(1)")));
 }

 @Test
 public void testOrEvaluator() {
 Element root = Jsoup.parse("<html><body><div id='div1' class='class1'><p>Test1</p></div><div id='div2' class='class1 class2'><p>Test2</p></div></body></html>");

 Or evaluator1 = new Or(Arrays.asList(
 new ElementSelector("div.class1"),
 new AttributeSelector("div[id=div2]")
 ));

 Or evaluator2 = new Or(
 Arrays.asList(
 new ElementSelector("div.class1"),
 new ElementSelector("div:has(p:contains(Test2))")
 )
 );

 evaluator2.add(evaluator1);

 assertTrue(evaluator2.matches(root, root.selectFirst("div.class1:eq(0)")));
 assertTrue(evaluator2.matches(root, root.selectFirst("div.class1.class2:eq(1)")));
 assertFalse(evaluator2.matches(root, root.selectFirst("div.class1:eq(1)")));
 }

 @Test
 public void testEmptyAndEvaluator() {
 And evaluator = new And(Collections.emptyList());

 assertTrue(evaluator.matches(null, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEvaluatorInAnd() {
 And evaluator = new And(Arrays.asList(null));
 }

 @Test
 public void testNullEvaluatorInOr() {
 Or evaluator = new Or(Arrays.asList(null));
 }

 @Test(expected = NullPointerException.class)
 public void testNullRootOrNode() {
 And evaluator = new And(Arrays.asList(new ElementSelector("div.class1")));
 evaluator.matches(null, null);
 }
}