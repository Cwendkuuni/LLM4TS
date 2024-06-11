import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import org.jsoup.helper.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

 private CombiningEvaluator andEvaluator;
 private CombiningEvaluator orEvaluator;

 @Test
 public void testAndEvaluatorConstructionWithCollection() {
 andEvaluator = new CombiningEvaluator.And(Arrays.asList(new ElementFilter.NameStartsWith("div"), new ElementFilter.ClassContains("class1")));
 }

 @Test
 public void testAndEvaluatorConstructionWithEvaluators() {
 andEvaluator = new CombiningEvaluator.And(new ElementFilter.NameStartsWith("div"), new ElementFilter.ClassContains("class1"));
 }

 @Test
 public void testAndEvaluatorMatches() {
 Element element = new Element("div");
 element.addClass("class1");
 assertTrue(andEvaluator.matches(null, element));
 }

 @Test(expected = AssertionError.class)
 public void testAndEvaluatorDoesNotMatch() {
 Element element = new Element("span");
 assertFalse(andEvaluator.matches(null, element));
 }

 @Test
 public void testAndEvaluatorToString() {
 String expected = "div:hasClass(class1)";
 assertEquals(expected, andEvaluator.toString());
 }

 @Test
 public void testOrEvaluatorConstruction() {
 orEvaluator = new CombiningEvaluator.Or(Arrays.asList(new ElementFilter.NameStartsWith("div"), new ElementFilter.ClassContains("class2")));
 }

 @Test
 public void testOrEvaluatorAddEvaluator() {
 orEvaluator = new CombiningEvaluator.Or();
 orEvaluator.add(new ElementFilter.NameStartsWith("div"));
 orEvaluator.add(new ElementFilter.ClassContains("class3"));
 }

 @Test
 public void testOrEvaluatorMatches() {
 Element element = new Element("div");
 element.addClass("class2");
 assertTrue(orEvaluator.matches(null, element));
 }

 @Test
 public void testOrEvaluatorMatchesMultipleEvaluators() {
 Element element = new Element("div");
 element.addClass("class1");
 element.addClass("class2");
 assertTrue(orEvaluator.matches(null, element));
 }

 @Test(expected = AssertionError.class)
 public void testOrEvaluatorDoesNotMatchAllEvaluators() {
 Element element = new Element("span");
 assertFalse(orEvaluator.matches(null, element));
 }

 @Test
 public void testOrEvaluatorToString() {
 String expected = ":or(div:hasClass(class2), div:hasClass(class3))";
 assertEquals(expected, orEvaluator.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testCombiningEvaluatorNullRoot() {
 andEvaluator.matches(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testCombiningEvaluatorNullNode() {
 andEvaluator.matches(null, new Element("div"));
 }

 @Test
 public void testCombiningEvaluatorEmptyEvaluators() {
 CombiningEvaluator evaluator = new CombiningEvaluator();
 assertFalse(evaluator.matches(null, new Element("div")));
 }
}