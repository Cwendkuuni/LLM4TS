import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Elements;
import org.jsoup.select.And;
import org.jsoup.select.Or;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 Element root = Jsoup.parse("<html><body><div id='a'><p class='x y z'></p></div></body></html>");
 And andEvaluator = new And(Arrays.asList(
 new ElementStartsWith("div", "id", "a"),
 new ElementClassMatches("p", "x")
 ));
 Elements matchedElements = root.select(andEvaluator.toString());
 assertEquals(1, matchedElements.size());
 }

 @Test
 public void testAndEvaluator_noMatch() {
 Element root = Jsoup.parse("<html><body><div id='b'><p class='x y z'></p></div></body></html>");
 And andEvaluator = new And(Arrays.asList(
 new ElementStartsWith("div", "id", "a"),
 new ElementClassMatches("p", "x")
 ));
 Elements matchedElements = root.select(andEvaluator.toString());
 assertEquals(0, matchedElements.size());
 }

 @Test
 public void testOrEvaluator() {
 Element root = Jsoup.parse("<html><body><div id='a' class='classA'><p class='x y z'></p></div><div id='b' class='classB'><p class='x y z'></p></div></body></html>");
 Or orEvaluator = new Or();
 orEvaluator.add(new ElementStartsWith("div", "id", "a"));
 orEvaluator.add(new ElementClassMatches("p", "y"));
 Elements matchedElements = root.select(orEvaluator.toString());
 assertEquals(2, matchedElements.size());
 }

 @Test
 public void testOrEvaluator_noMatch() {
 Element root = Jsoup.parse("<html><body><div id='a' class='classA'><p class='x y z'></p></div></body></html>");
 Or orEvaluator = new Or();
 orEvaluator.add(new ElementStartsWith("div", "id", "b"));
 orEvaluator.add(new ElementClassMatches("p", "y"));
 Elements matchedElements = root.select(orEvaluator.toString());
 assertEquals(0, matchedElements.size());
 }

 @Test
 public void testOrEvaluator_emptyList() {
 Element root = Jsoup.parse("<html><body></body></html>");
 Or orEvaluator = new Or();
 Elements matchedElements = root.select(orEvaluator.toString());
 assertEquals(0, matchedElements.size());
 }
}