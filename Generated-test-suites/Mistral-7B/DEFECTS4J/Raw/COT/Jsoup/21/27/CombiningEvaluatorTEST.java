import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

 private CombiningEvaluator andEvaluator;
 private CombiningEvaluator orEvaluator;

 @Before
 public void setup() {
 andEvaluator = new CombiningEvaluator.And(Arrays.asList(new ElementFilter.ClassFilter("p"), new ElementFilter.ClassFilter("h1")));
 orEvaluator = new CombiningEvaluator.Or();
 orEvaluator.add(new ElementFilter.ClassFilter("p"));
 orEvaluator.add(new ElementFilter.ClassFilter("h1"));
 }

 @Test
 public void testAndEvaluator_Matches() {
 Element root = Element.parse("<html><body><p>Test</p><h1>Test</h1></body></html>");
 assertTrue(andEvaluator.matches(root, root));
 }

 @Test
 public void testAndEvaluator_DoesNotMatch() {
 Element root = Element.parse("<html><body><p>Test</p></body></html>");
 assertFalse(andEvaluator.matches(root, root));
 }

 @Test
 public void testOrEvaluator_Matches() {
 Element root = Element.parse("<html><body><p>Test</p><h1>Test</h1></body></html>");
 assertTrue(orEvaluator.matches(root, root));
 }

 @Test
 public void testOrEvaluator_FirstClauseMatches() {
 Element root = Element.parse("<html><body><p>Test</p></body></html>");
 orEvaluator.add(new ElementFilter.ClassFilter("p"));
 assertTrue(orEvaluator.matches(root, root));
 }

 @Test
 public void testOrEvaluator_SecondClauseMatches() {
 Element root = Element.parse("<html><body><h1>Test</h1></body></html>");
 orEvaluator.add(new ElementFilter.ClassFilter("h1"));
 assertTrue(orEvaluator.matches(root, root));
 }

 @Test
 public void testOrEvaluator_BothClausesMatch() {
 Element root = Element.parse("<html><body><p>Test</p><h1>Test</h1></body></html>");
 orEvaluator.add(new ElementFilter.ClassFilter("p"));
 orEvaluator.add(new ElementFilter.ClassFilter("h1"));
 assertTrue(orEvaluator.matches(root, root));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrEvaluator_NullEvaluator() {
 CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(null);
 }

 @Test
 public void testOrEvaluator_EmptyList() {
 CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new ArrayList<>());
 assertFalse(orEvaluator.matches(null, null));
 }

 @Test
 public void testOrEvaluator_SingleEvaluator() {
 CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(new ElementFilter.ClassFilter("p")));
 Element root = Element.parse("<html><body></body></html>");
 assertFalse(orEvaluator.matches(root, root));
 }
}