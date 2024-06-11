import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombiningEvaluatorTest {

 private CombiningEvaluator andEvaluator;
 private CombiningEvaluator orEvaluator;

 @Before
 public void setUp() {
 andEvaluator = new CombiningEvaluator.And(Arrays.asList(new ElementFilter.ElementFilterByTag("p"), new ElementFilter.ElementFilterByTag("h1")));
 orEvaluator = new CombiningEvaluator.Or();
 orEvaluator.add(new ElementFilter.ElementFilterByTag("p"));
 orEvaluator.add(new ElementFilter.ElementFilterByTag("h1"));
 }

 @Test
 public void testAndEvaluator_Matches() {
 Element root = Element.parse("<html><body><p>Test1</p><h1>Test2</h1><p>Test3</p></body></html>");
 Element node = root.body();
 assertTrue(andEvaluator.matches(root, node));
 }

 @Test
 public void testAndEvaluator_DoesNotMatch() {
 Element root = Element.parse("<html><body><p>Test1</p></body></html>");
 Element node = root.body();
 assertFalse(andEvaluator.matches(root, node));
 }

 @Test
 public void testOrEvaluator_Matches() {
 Element root = Element.parse("<html><body><p>Test1</p><h1>Test2</h1><p>Test3</p></body></html>");
 Element node = root.body();
 assertTrue(orEvaluator.matches(root, node));
 }

 @Test
 public void testOrEvaluator_DoesNotMatch() {
 Element root = Element.parse("<html><body><p>Test1</p></body></html>");
 Element node = root.body();
 assertFalse(orEvaluator.matches(root, node));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testOrEvaluator_AddEvaluator_ThrowsExceptionWhenOrHasOnlyOneEvaluator() {
 orEvaluator.add(new ElementFilter.ElementFilterByTag("div"));
 }

 @Test
 public void testOrEvaluator_AddEvaluator() {
 CombiningEvaluator orEvaluatorWithDiv = new CombiningEvaluator.Or();
 orEvaluatorWithDiv.add(new ElementFilter.ElementFilterByTag("p"));
 orEvaluatorWithDiv.add(new ElementFilter.ElementFilterByTag("h1"));
 orEvaluatorWithDiv.add(new ElementFilter.ElementFilterByTag("div"));
 List<Evaluator> evaluators = new ArrayList<>(orEvaluatorWithDiv.evaluators);
 assertTrue(evaluators.size() == 3);
 }
}