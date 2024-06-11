import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertNull(evaluator);
 }

 @Test
 public void testParseSingleTagSelector() {
 QueryParser parser = new QueryParser("p");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("p", evaluator.toString());
 }

 @Test
 public void testParseClassSelector() {
 QueryParser parser = new QueryParser(".myClass");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals(".myClass", evaluator.toString());
 }

 @Test
 public void testParseIdSelector() {
 QueryParser parser = new QueryParser("#myId");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("#myId", evaluator.toString());
 }

 @Test
 public void testParseMultipleSelectors() {
 QueryParser parser = new QueryParser(".myClass #myId > p");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 EqualsChain elementChain = ((CombiningEvaluator.And) evaluator).getChain();
 assertEquals(3, elementChain.size());
 assertEquals(Evaluator.Class.class, elementChain.get(0).getClass());
 assertEquals(".myClass", elementChain.get(0).toString());
 assertEquals(Evaluator.Id.class, elementChain.get(1).getClass());
 assertEquals("#myId", elementChain.get(1).toString());
 assertEquals(Evaluator.Tag.class, elementChain.get(2).getClass());
 assertEquals("p", elementChain.get(2).toString());
 }

 @Test
 public void testParseCombinatorSelectors() {
 QueryParser parser = new QueryParser("p > .myClass > #childId");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 EqualsChain elementChain = ((CombiningEvaluator.And) evaluator).getChain();
 assertEquals(3, elementChain.size());
 assertEquals(Evaluator.Tag.class, elementChain.get(0).getClass());
 assertEquals("p", elementChain.get(0).toString());
 assertEquals(Evaluator.Class.class, elementChain.get(1).getClass());
 assertEquals(".myClass", elementChain.get(1).toString());
 assertEquals(Evaluator.Id.class, elementChain.get(2).getClass());
 assertEquals("#childId", elementChain.get(2).toString());
 }

 @Test
 public void testParseAttributeSelectors() {
 QueryParser parser = new QueryParser("[title='Test']");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("[title='Test']", evaluator.toString());
 }

 @Test
 public void testParseAttributeWithValueSelectors() {
 QueryParser parser = new QueryParser("[title='Test'] > p");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.class, evaluator.getClass());
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 EqualsChain elementChain = ((CombiningEvaluator.And) evaluator).getChain();
 assertEquals(2, elementChain.size());
 assertEquals(Evaluator.Tag.class, elementChain.get(0).getClass());
 assertEquals("p", elementChain.get(0).toString());
 assertEquals(Evaluator.Attribute.class, elementChain.get(1).getClass());
 assertEquals("[title='Test']", elementChain.get(1).toString());
 }

 @Test
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser(":not p > .myClass > #childId:has(p)"); // missing opening parenthesis
 Evaluator evaluator = parser.parse();
 assertNull(evaluator);
 }
}