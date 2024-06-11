import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFinder;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class CombiningEvaluatorTest {

 private ElementFinder finder;

 @Before
 public void setup() {
 finder = new ElementFinder(Jsoup.connect("file:src/test/resources/test.html").get());
 }

 @Test
 public void testAndEvaluator() {
 Element root = finder.find("html");

 // Test matching all conditions
 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("body > p"),
 new ElementSelector("body > ul > li")
 );
 And andEvaluator = new And(evaluators);

 assertTrue(andEvaluator.matches(root, root.getElementsByTag("body").first()));

 // Test non-matching condition
 evaluators.set(0, new ElementSelector("body > p:not(:first-child)"));
 andEvaluator = new And(evaluators);

 assertFalse(andEvaluator.matches(root, root.getElementsByTag("body").first()));
 }

 @Test
 public void testOrEvaluator() {
 Element root = finder.find("html");

 // Test matching any condition
 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("body > p"),
 new ElementSelector("body > ul > li")
 );
 Or orEvaluator = new Or(evaluators);

 assertTrue(orEvaluator.matches(root, root.getElementsByTag("body").first()));

 // Test matching multiple conditions
 evaluators = new ArrayList<>();
 evaluators.add(new ElementSelector("body > p"));
 evaluators.add(new ElementSelector("body > ul > li:first-child"));
 orEvaluator = new Or(evaluators);

 assertTrue(orEvaluator.matches(root, root.getElementsByTag("body").first()));

 // Test non-matching conditions
 evaluators.clear();
 evaluators.add(new ElementSelector("body > p:not(:first-child)"));
 evaluators.add(new ElementSelector("body > ul > li:not(:first-child)"));
 orEvaluator = new Or(evaluators);

 assertFalse(orEvaluator.matches(root, root.getElementsByTag("body").first()));
 }

 @Test
 public void testAddingEvaluatorToOr() {
 Element root = finder.find("html");

 Or orEvaluator = new Or();

 orEvaluator.add(new ElementSelector("body > p"));
 orEvaluator.add(new ElementSelector("body > ul > li"));

 assertTrue(orEvaluator.matches(root, root.getElementsByTag("body").first()));
 }
}