import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 private CombiningEvaluator andEvaluator;
 private CombiningEvaluator orEvaluator;

 @Before
 public void setUp() {
 andEvaluator = new CombiningEvaluator.And(
 Arrays.asList(
 new ElementFilter.TagName("h1"),
 new ElementFilter.ClassName("title")
 ));

 orEvaluator = new CombiningEvaluator.Or(
 Arrays.asList(
 new ElementFilter.TagName("h1"),
 new ElementFilter.ClassName("title"),
 new ElementFilter.TagName("h2")
 ));
 }

 @Test
 public void testAndEvaluator_matches() {
 Element root = Element.parse("<html><body><h1 class='title'></h1></body></html>");
 Element node = root.selectFirst("h1");
 assertTrue(andEvaluator.matches(root, node));
 }

 @Test
 public void testAndEvaluator_doesNotMatch() {
 Element root = Element.parse("<html><body><h1></h1></body></html>");
 Element node = root.selectFirst("h1");
 assertFalse(andEvaluator.matches(root, node));
 }

 @Test
 public void testOrEvaluator_matches() {
 Element root = Element.parse("<html><body><h1 class='title'></h1><h2></h2></body></html>");
 Element node = root.selectFirst("h1");
 assertTrue(orEvaluator.matches(root, node));
 }

 @Test
 public void testOrEvaluator_matches_otherElement() {
 Element root = Element.parse("<html><body><h1 class='title'></h1><h2></h2></body></html>");
 Element node = root.selectFirst("h2");
 assertTrue(orEvaluator.matches(root, node));
 }

 @Test
 public void testOrEvaluator_doesNotMatch() {
 Element root = Element.parse("<html><body><h1></h1></body></html>");
 Element node = root.selectFirst("h1");
 assertFalse(orEvaluator.matches(root, node));
 }

 @Test
 public void testOrEvaluator_addEvaluator() {
 CombiningEvaluator orEvaluatorNew = new CombiningEvaluator.Or(Arrays.asList(
 new ElementFilter.TagName("h1"),
 new ElementFilter.ClassName("title"),
 new ElementFilter.TagName("h2"),
 new ElementFilter.TagName("p")
 ));
 orEvaluatorNew.add(new ElementFilter.TagName("h3"));
 List<Evaluator> evaluators = orEvaluatorNew.evaluators;
 assertTrue(evaluators.size() == 5);
 }
}