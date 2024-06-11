import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleQueryByTag() {
 QueryParser queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleQueryById() {
 QueryParser queryParser = new QueryParser("#myId");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleQueryByClass() {
 QueryParser queryParser = new QueryParser(".myClass");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleQueryByAttribute() {
 QueryParser queryParser = new QueryParser("[title='Test']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Attribute.class, evaluator.getClass());
 }

 @Test
 public void testParseComplexQueryWithCombination() {
 QueryParser queryParser = new QueryParser("div.myClass > ul > li:first-child");
 Evaluator evaluator = queryParser.parse();

 Evaluator tagEvaluator = new Evaluator.Tag("div");
 Evaluator classEvaluator = new Evaluator.Class("myClass");
 Evaluator immediateParentEvaluator = new StructuralEvaluator.ImmediateParent(tagEvaluator);
 Evaluator ulTagEvaluator = new Evaluator.Tag("ul");
 Evaluator liFirstChildEvaluator = new Evaluator.IndexLessThan(1);

 CombiningEvaluator andCombiningEvaluator = new CombiningEvaluator.And();
 andCombiningEvaluator.add(tagEvaluator);
 andCombiningEvaluator.add(classEvaluator);
 andCombiningEvaluator.add(immediateParentEvaluator);
 andCombiningEvaluator.add(ulTagEvaluator);
 andCombiningEvaluator.add(liFirstChildEvaluator);

 assertEquals(andCombiningEvaluator, evaluator);
 }

 @Test
 public void testParseComplexQueryWithCombinationAnd() {
 QueryParser queryParser = new QueryParser("div.myClass, ul.anotherClass > li:first-child");
 Evaluator evaluator = queryParser.parse();

 Evaluator tag1Evaluator = new Evaluator.Tag("div");
 Evaluator class1Evaluator = new Evaluator.Class("myClass");
 Evaluator tag2Evaluator = new Evaluator.Tag("ul");
 Evaluator class2Evaluator = new Evaluator.Class("anotherClass");
 Evaluator immediateParentEvaluator = new CombiningEvaluator.And(tag2Evaluator, new StructuralEvaluator.Parent(tag1Evaluator));
 Evaluator liFirstChildEvaluator = new Evaluator.IndexLessThan(1);

 CombiningEvaluator orCombiningEvaluator = new CombiningEvaluator.Or();
 orCombiningEvaluator.add(tag1Evaluator);
 orCombiningEvaluator.add(class1Evaluator);
 orCombiningEvaluator.add(new CombiningEvaluator.And(tag2Evaluator, immediateParentEvaluator, liFirstChildEvaluator));

 assertEquals(orCombiningEvaluator, evaluator);
 }

 @Test
 public void testParseComplexQueryWithCombinationNot() {
 QueryParser queryParser = new QueryParser("div:not(.myClass > ul > li:first-child)");
 Evaluator evaluator = queryParser.parse();

 Evaluator tagEvaluator = new Evaluator.Tag("div");
 Evaluator classEvaluator = new Evaluator.Class("myClass");
 Evaluator ulTagEvaluator = new Evaluator.Tag("ul");
 Evaluator liFirstChildEvaluator = new Evaluator.IndexLessThan(1);
 Evaluator notEvaluator = new StructuralEvaluator.Not(new CombiningEvaluator.And(ulTagEvaluator, new StructuralEvaluator.Parent(tagEvaluator), liFirstChildEvaluator));

 assertEquals(notEvaluator, evaluator);
 }
}