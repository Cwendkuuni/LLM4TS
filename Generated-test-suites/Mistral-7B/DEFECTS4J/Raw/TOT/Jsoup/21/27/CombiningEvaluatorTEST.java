import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Jsoup;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombiningEvaluatorTest {

 @Test
 void testAndCombiningEvaluator() {
 Document doc = Jsoup.parse("<html><body><div id='div1'><p>Test 1</p></div>" +
 "<div id='div2'><p>Test 2</p></div>" +
 "<div id='div3'><p>Test 3</p></div></body></html>");

 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("div#div1"),
 new ElementSelector("div#div2")
 );
 And andEvaluator = new And(evaluators);

 Element divElement = doc.selectFirst("div");
 Element testElement = null;

 while (divElement != null) {
 testElement = divElement.selectFirst(andEvaluator);
 divElement = divElement.nextSibling();
 }

 assertTrue(testElement != null);
 }

 @Test
 void testAndCombiningEvaluator_NoMatch() {
 Document doc = Jsoup.parse("<html><body><div id='div1'><p>Test 1</p></div>" +
 "<div id='div2'><p>Test 2</p></div>" +
 "<div id='div3'><p>Test 3</p></div></body></html>");

 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("div#div1"),
 new ElementSelector("div#div3")
 );
 And andEvaluator = new And(evaluators);

 Element divElement = doc.selectFirst("div");
 Element testElement = null;

 while (divElement != null) {
 testElement = divElement.selectFirst(andEvaluator);
 divElement = divElement.nextSibling();
 }

 assertFalse(testElement != null);
 }

 @Test
 void testOrCombiningEvaluator() {
 Document doc = Jsoup.parse("<html><body><div id='div1'><p>Test 1</p></div>" +
 "<div id='div2'><p>Test 2</p></div>" +
 "<div id='div3'><p>Test 3</p></div></body></html>");

 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("div#div1"),
 new ElementSelector("div#div2")
 );
 And andEvaluator = new And(evaluators);
 Or orEvaluator = new Or();
 orEvaluator.add(andEvaluator);
 orEvaluator.add(new ElementSelector("div"));

 Element divElement = doc.selectFirst("div");
 Element testElement = null;

 while (divElement != null) {
 testElement = divElement.selectFirst(orEvaluator);
 divElement = divElement.nextSibling();
 }

 assertTrue(testElement != null);
 }

 @Test
 void testOrCombiningEvaluator_NoMatch() {
 Document doc = Jsoup.parse("<html><body><div id='div1'><p>Test 1</p></div>" +
 "<div id='div2'><p>Test 2</p></div>" +
 "<div id='div3'><p>Test 3</p></div></body></html>");

 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("div#div1"),
 new ElementSelector("div#div4")
 );
 And andEvaluator = new And(evaluators);
 Or orEvaluator = new Or();
 orEvaluator.add(andEvaluator);
 orEvaluator.add(new ElementSelector("div"));

 Element divElement = doc.selectFirst("div");
 Element testElement = null;

 while (divElement != null) {
 testElement = divElement.selectFirst(orEvaluator);
 divElement = divElement.nextSibling();
 }

 assertFalse(testElement != null);
 }
}