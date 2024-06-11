import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombiningEvaluatorTest {

 private Document createTestDocument() {
 return Jsoup.parse("<html><body><div id='div1'>a b c</div><div id='div2'>d e f</div></body></html>");
 }

 @Test
 public void testAnd() {
 And andEvaluator = new And(Arrays.asList(
 new ElementFilter("div#div1 > *:contains(a)"),
 new ElementFilter("div#div1 > *:contains(b)")
 ));
 Element root = createTestDocument().body();
 List<Element> matchingElements = andEvaluator.select(root);
 List<String> expectedElementIds = Arrays.asList("div1");
 Assert.assertEquals(expectedElementIds, getElementIds(matchingElements));
 }

 @Test
 public void testAndEmptyEvaluators() {
 And andEvaluator = new And(Collections.emptyList());
 Element root = createTestDocument().body();
 List<Element> matchingElements = andEvaluator.select(root);
 List<String> expectedElementIds = Collections.emptyList();
 Assert.assertEquals(expectedElementIds, getElementIds(matchingElements));
 }

 @Test
 public void testAndSingleEvaluator() {
 And andEvaluator = new And(
 new ElementFilter("div:contains(a)")
 );
 Element root = createTestDocument().body();
 List<Element> matchingElements = andEvaluator.select(root);
 List<String> expectedElementIds = Arrays.asList("div1");
 Assert.assertEquals(expectedElementIds, getElementIds(matchingElements));
 }

 @Test
 public void testOr() {
 Or orEvaluator = new Or(
 new And(
 Arrays.asList(
 new ElementFilter("div#div1 > *:contains(a)"),
 new ElementFilter("div#div2 > *:contains(a)")
 )
 ),
 new ElementFilter("div#div1 > *:contains(c)")
 );
 Element root = createTestDocument().body();
 List<Element> matchingElements = orEvaluator.select(root);
 List<String> expectedElementIds = Arrays.asList("div1", "div2");
 Assert.assertEquals(expectedElementIds, getElementIds(matchingElements));
 }

 @Test
 public void testOrSingleEvaluator() {
 Or orEvaluator = new Or(
 new ElementFilter("div#div1 > *:contains(a)")
 );
 Element root = createTestDocument().body();
 List<Element> matchingElements = orEvaluator.select(root);
 List<String> expectedElementIds = Arrays.asList("div1");
 Assert.assertEquals(expectedElementIds, getElementIds(matchingElements));
 }

 @Test(expected = IllegalStateException.class)
 public void testOrNullEvaluator() {
 Or orEvaluator = new Or(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testAndNullEvaluator() {
 And andEvaluator = new And(null);
 }

 private List<String> getElementIds(List<Element> matchingElements) {
 List<String> elementIds = new ArrayList<>();
 for (Element element : matchingElements) {
 elementIds.add(element.id());
 }
 return elementIds;
 }

}