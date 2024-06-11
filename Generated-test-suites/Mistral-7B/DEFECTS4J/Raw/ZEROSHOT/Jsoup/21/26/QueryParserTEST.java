import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import static org.jsoup.helper.StringUtil.*;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleTagQuery() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
 Assert.assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseClassQuery() {
 QueryParser parser = new QueryParser(".class-name");
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.Class.class, evaluator.getClass());
 Assert.assertEquals("class-name", evaluator.toString());
 }

 @Test
 public void testParseIdQuery() {
 QueryParser parser = new QueryParser("#id-name");
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.Id.class, evaluator.getClass());
 Assert.assertEquals("#id-name", evaluator.toString());
 }

 @Test
 public void testParseCombinedQuery() {
 QueryParser parser = new QueryParser("div.class-name");
 Evaluator evaluator = parser.parse();
 Evaluator tagEvaluator = new Evaluator.Tag("div");
 Evaluator classEvaluator = new Evaluator.Class("class-name");
 CombiningEvaluator combiningEvaluator = new CombiningEvaluator.And(tagEvaluator, classEvaluator);
 Assert.assertEquals(combiningEvaluator.getClass(), evaluator.getClass());
 Assert.assertEquals(combiningEvaluator, evaluator);
 }

 @Test
 public void testParseAttributeQuery() {
 QueryParser parser = new QueryParser("[attr='value']");
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
 Assert.assertEquals("[attr='value']", evaluator.toString());
 }

 @Test
 public void testParseMultipleSelectorsQuery() {
 QueryParser parser = new QueryParser("div.class-name, ul.menu > li:not(.disabled)");
 List<Evaluator> evaluators = parser.parse().getSubSelectors();
 Assert.assertEquals(2, evaluators.size());
 Evaluator divClassEvaluator = evaluators.get(0);
 Evaluator ulLiNotDisabledEvaluator = evaluators.get(1);
 CombiningEvaluator orEvaluator = (CombiningEvaluator) divClassEvaluator;
 Assert.assertEquals(3, orEvaluator.getSubSelectors().size());
 Evaluator tagDivEvaluator = orEvaluator.getSubSelectors().get(0);
 Evaluator classEvaluator = orEvaluator.getSubSelectors().get(1);
 CombiningEvaluator andEvaluator = (CombiningEvaluator) classEvaluator;
 Assert.assertEquals(2, andEvaluator.getSubSelectors().size());
 Evaluator tagClassEvaluator = andEvaluator.getSubSelectors().get(0);
 Assert.assertEquals("div", tagDivEvaluator.toString());
 Assert.assertEquals("class-name", tagClassEvaluator.toString());
 StructuralEvaluator notEvaluator = (StructuralEvaluator) andEvaluator.getSubSelectors().get(1);
 Evaluator liEvaluator = notEvaluator.getSubSelectors().get(0);
 Evaluator liClassDisabledEvaluator = notEvaluator.getSubSelectors().get(1);
 Assert.assertEquals("li", liEvaluator.toString());
 Assert.assertEquals("not(.disabled)", liClassDisabledEvaluator.toString());
 }

 @Test
 public void testParseComplexQuery() {
 QueryParser parser = new QueryParser("div#content > ul#menu > li > a[href^='http']:contains(Home):containsOwn(Logout)");
 List<Evaluator> evaluators = parser.parse().getSubSelectors();
 Assert.assertEquals(1, evaluators.size());
 CombiningEvaluator andEvaluator = (CombiningEvaluator) evaluators.get(0);
 StructuralEvaluator immediateParentEvaluator = (StructuralEvaluator) andEvaluator.getSubSelectors().get(0);
 Evaluator tagDivEvaluator = immediateParentEvaluator.getSubSelectors().get(0);
 Assert.assertEquals("div", tagDivEvaluator.toString());
 Evaluator idContentEvaluator = immediateParentEvaluator.getSubSelectors().get(1);
 Assert.assertEquals("#content", idContentEvaluator.toString());
 CombiningEvaluator andEvaluator2 = (CombiningEvaluator) andEvaluator.getSubSelectors().get(1);
 StructuralEvaluator parentEvaluator = (StructuralEvaluator) andEvaluator2.getSubSelectors().get(0);
 Evaluator tagUlEvaluator = parentEvaluator.getSubSelectors().get(0);
 Assert.assertEquals("ul", tagUlEvaluator.toString());
 Evaluator idMenuEvaluator = parentEvaluator.getSubSelectors().get(1);
 Assert.assertEquals("#menu", idMenuEvaluator.toString());
 CombiningEvaluator andEvaluator3 = (CombiningEvaluator) andEvaluator2.getSubSelectors().get(1);
 Evaluator tagLiEvaluator = andEvaluator3.getSubSelectors().get(0);
 Assert.assertEquals("li", tagLiEvaluator.toString());
 StructuralEvaluator immediatePreviousSiblingEvaluator = (StructuralEvaluator) andEvaluator3.getSubSelectors().get(1);
 Evaluator tagAEvaluator = immediatePreviousSiblingEvaluator.getSubSelectors().get(0);
 Assert.assertEquals("a", tagAEvaluator.toString());
 AttributeEvaluator hrefEvaluator = (AttributeEvaluator) tagAEvaluator;
 Assert.assertEquals("href", hrefEvaluator.getAttributeName());
 Assert.assertTrue(hrefEvaluator.getValue().startsWith("http"));
 AttributeEvaluator containsHomeEvaluator = (AttributeEvaluator) andEvaluator.getSubSelectors().get(2);
 Assert.assertEquals("Home", containsHomeEvaluator.getValue());
 AttributeEvaluator containsOwnLogoutEvaluator = (AttributeEvaluator) andEvaluator.getSubSelectors().get(3);
 Assert.assertEquals("Logout", containsOwnLogoutEvaluator.getValue());
 }

}