import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

 private static QueryParser createQueryParser(String query) {
 return new QueryParser(query);
 }

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = createQueryParser("");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEmpty(parser.evals);
 }

 @Test
 public void testParseRootQuery() {
 QueryParser parser = createQueryParser("*");
 Evaluator evaluator = parser.parse();
 assertSame(evaluator, new StructuralEvaluator.Root());
 assertEmpty(parser.evals);
 }

 @Test
 public void testParseBasicElementQuery() {
 QueryParser parser = createQueryParser("div");
 Evaluator evaluator = parser.parse();
 assertSame(evaluator, new Evaluator.Tag("div"));
 assertEmpty(parser.evals);
 }

 @Test
 public void testParseBasicClassQuery() {
 QueryParser parser = createQueryParser(".class");
 Evaluator evaluator = parser.parse();
 assertSame(evaluator, new Evaluator.Class("class"));
 assertEmpty(parser.evals);
 }

 @Test
 public void testParseBasicIdQuery() {
 QueryParser parser = createQueryParser("#id");
 Evaluator evaluator = parser.parse();
 assertSame(evaluator, new Evaluator.Id("id"));
 assertEmpty(parser.evals);
 }

 @Test
 public void testParseBasicCombinedQuery() {
 QueryParser parser = createQueryParser("div.class");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator.And and = (StructuralEvaluator.And) evaluator;
 assertEquals(2, and.size());
 Evaluator tag = and.get(0);
 assertEquals(Evaluator.Tag.class, tag.getClass());
 assertEquals("div", tag.asTag().getName());
 Evaluator classEvaluator = and.get(1);
 assertEquals(Evaluator.Class.class, classEvaluator.getClass());
 assertEquals("class", classEvaluator.asClass().getName());
 }

 @Test
 public void testParseNestedCombinedQuery() {
 QueryParser parser = createQueryParser("div.class1 > div.class2");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator.And and = (StructuralEvaluator.And) evaluator;
 assertEquals(3, and.size());
 Evaluator tag1 = and.get(0);
 assertEquals(Evaluator.Tag.class, tag1.getClass());
 assertEquals("div", tag1.asTag().getName());
 Evaluator class1Evaluator = and.get(1);
 assertEquals(Evaluator.Class.class, class1Evaluator.getClass());
 assertEquals("class1", class1Evaluator.asClass().getName());
 Evaluator tag2 = and.get(2);
 assertEquals(Evaluator.Tag.class, tag2.getClass());
 assertEquals("div", tag2.asTag().getName());
 }

 @Test
 public void testParseAttributeQuery() {
 QueryParser parser = createQueryParser("input[type='text']");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator.Has has = (StructuralEvaluator.Has) evaluator;
 Evaluator attribute = has.get(0);
 assertEquals(Evaluator.Attribute.class, attribute.getClass());
 assertEquals("input", attribute.asTag().getName());
 StructuralEvaluator.Attribute attributeEvaluator = (StructuralEvaluator.Attribute) attribute;
 assertEquals("type", attributeEvaluator.getAttributeName());
 assertEquals("text", attributeEvaluator.getAttributeValue());
 }

 @Test
 public void testParseNestedAttributeQuery() {
 QueryParser parser = createQueryParser("div.class > input[type='text'][name='username']");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator.And and = (StructuralEvaluator.And) evaluator;
 Evaluator tag1 = and.get(0);
 assertEquals(Evaluator.Tag.class, tag1.getClass());
 assertEquals("div", tag1.asTag().getName());
 StructuralEvaluator.Class classEvaluator = (StructuralEvaluator.Class) and.get(1);
 assertEquals("class", classEvaluator.asClass().getName());
 Evaluator tag2 = and.get(2);
 assertEquals(Evaluator.Tag.class, tag2.getClass());
 assertEquals("input", tag2.asTag().getName());
 StructuralEvaluator.Has has = (StructuralEvaluator.Has) tag2;
 Evaluator attribute = has.get(0);
 assertEquals(Evaluator.Attribute.class, attribute.getClass());
 StructuralEvaluator.Attribute attributeEvaluator = (StructuralEvaluator.Attribute) attribute;
 assertEquals("type", attributeEvaluator.getAttributeName());
 assertEquals("text", attributeEvaluator.getAttributeValue());
 StructuralEvaluator.Attribute attribute2 = (StructuralEvaluator.Attribute) has.get(1);
 assertEquals("name", attribute2.getAttributeName());
 assertEquals("username", attribute2.getAttributeValue());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = createQueryParser("invalid-query");
 Evaluator evaluator = parser.parse();
 fail("Parser should have thrown exception for invalid query");
 }

 @Test
 public void testParseComplexQueryWithNestedSelectors() {
 QueryParser parser = new QueryParser("div > div > div.class1 > div > div.class2 > input[type='text'][name='textfield']");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator.And and = (StructuralEvaluator.And) evaluator;
 assertEquals(7, and.size());
 Evaluator tag1 = and.get(0);
 assertEquals(Evaluator.Tag.class, tag1.getClass());
 assertEquals("div", tag1.asTag().getName());
 Evaluator tag2 = and.get(1);
 assertEquals(Evaluator.Tag.class, tag2.getClass());
 assertEquals("div", tag2.asTag().getName());
 Evaluator tag3 = and.get(2);
 assertEquals(Evaluator.Tag.class, tag3.getClass());
 assertEquals("div", tag3.asTag().getName());
 StructuralEvaluator.Class class1Evaluator = (StructuralEvaluator.Class) and.get(3);
 assertEquals("class1", class1Evaluator.asClass().getName());
 Evaluator tag4 = and.get(4);
 assertEquals(Evaluator.Tag.class, tag4.getClass());
 assertEquals("div", tag4.asTag().getName());
 Evaluator tag5 = and.get(5);
 assertEquals(Evaluator.Tag.class, tag5.getClass());
 assertEquals("div", tag5.asTag().getName());
 StructuralEvaluator.Class class2Evaluator = (StructuralEvaluator.Class) and.get(6);
 assertEquals("class2", class2Evaluator.asClass().getName());
 StructuralEvaluator.Has has = (StructuralEvaluator.Has) and.get(7);
 Evaluator attribute = has.get(0);
 assertEquals(Evaluator.Attribute.class, attribute.getClass());
 StructuralEvaluator.Attribute attributeEvaluator = (StructuralEvaluator.Attribute) attribute;
 assertEquals("type", attributeEvaluator.getAttributeName());
 assertEquals("text", attributeEvaluator.getAttributeValue());
 StructuralEvaluator.Attribute attribute2 = (StructuralEvaluator.Attribute) has.get(1);
 assertEquals("name", attribute2.getAttributeName());
 assertEquals("textfield", attribute2.getAttributeValue());
 }

 @Test
 public void testParseNegatedSelector() {
 QueryParser parser = createQueryParser("div:not(.class)");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator.Not not = (StructuralEvaluator.Not) evaluator;
 Evaluator tag = not.get(0);
 assertEquals(Evaluator.Tag.class, tag.getClass());
 assertEquals("div", tag.asTag().getName());
 StructuralEvaluator.Class classEvaluator = (StructuralEvaluator.Class) not.get(1);
 assertEquals("class", classEvaluator.asClass().getName());
 }

 @Test
 public void testParseComplexNegatedSelector() {
 QueryParser parser = createQueryParser("div:not(.class1) > div:not(.class2)");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator.And and = (StructuralEvaluator.And) evaluator;
 Evaluator tag1 = and.get(0);
 assertEquals(Evaluator.Tag.class, tag1.getClass());
 assertEquals("div", tag1.asTag().getName());
 StructuralEvaluator.Not not1 = (StructuralEvaluator.Not) tag1;
 Evaluator tag1Inner = not1.get(0);
 assertEquals(Evaluator.Tag.class, tag1Inner.getClass());
 assertEquals("div", tag1Inner.asTag().getName());
 StructuralEvaluator.Class class1Evaluator = (StructuralEvaluator.Class) not1.get(1);
 assertEquals("class1", class1Evaluator.asClass().getName());
 Evaluator tag2 = and.get(1);
 assertEquals(Evaluator.Tag.class, tag2.getClass());
 assertEquals("div", tag2.asTag().getName());
 StructuralEvaluator.Not not2 = (StructuralEvaluator.Not) tag2;
 Evaluator tag2Inner = not2.get(0);
 assertEquals(Evaluator.Tag.class, tag2Inner.getClass());
 assertEquals("div", tag2Inner.asTag().getName());
 StructuralEvaluator.Class class2Evaluator = (StructuralEvaluator.Class) not2.get(1);
 assertEquals("class2", class2Evaluator.asClass().getName());
 }

 private void assertEmpty(List<Evaluator> evaluators) {
 assertFalse(evaluators.isEmpty());
 for (Evaluator evaluator : evaluators) {
 assertEquals(0, evaluator.size());
 }
 }
}