import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.select.QueryParser.*;

public class QueryParserTest {

 private static Evaluator parseQuery(String query) {
 return parse(query).parse();
 }

 @Test
 public void testParseRoot() {
 QueryParser parser = new QueryParser("#test");
 Evaluator rootEvaluator = parser.parse();
 StructuralEvaluator.Root root = (StructuralEvaluator.Root) rootEvaluator;
 Assert.assertEquals("#test", root.getCssText());
 }

 @Test
 public void testParseSimpleSelector() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 Evaluator.Tag tag = (Evaluator.Tag) evaluator;
 Assert.assertEquals("div", tag.getTagName());
 }

 @Test
 public void testParseComplexSelector() {
 QueryParser parser = new QueryParser("div.class1 > ul > li:eq(2)");
 Evaluator evaluator = parser.parse();

 StructuralEvaluator.And and = (StructuralEvaluator.And) evaluator;
 StructuralEvaluator.Tag divTag = (StructuralEvaluator.Tag) and.getChild(0);
 Evaluator.Class class1 = (Evaluator.Class) and.getChild(1);
 StructuralEvaluator.ImmediateChild immediateChild = (StructuralEvaluator.ImmediateChild) and.getChild(2);
 StructuralEvaluator.Tag ulTag = (StructuralEvaluator.Tag) immediateChild.getChild();
 StructuralEvaluator.Index index = (StructuralEvaluator.Index) and.getChild(4);

 Assert.assertEquals("div", divTag.getTagName());
 Assert.assertEquals("class1", class1.getClassName());
 Assert.assertEquals("ul", ulTag.getTagName());
 Assert.assertEquals(2, index.getIndex());
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser parser = new QueryParser("input[type=text]");
 Evaluator evaluator = parser.parse();
 Evaluator.Attribute attribute = (Evaluator.Attribute) evaluator;
 Assert.assertEquals("input", attribute.getTagName());
 Assert.assertEquals("type", attribute.getAttributeName());
 Assert.assertEquals("text", attribute.getAttributeValue());
 }

 @Test
 public void testParsePseudoClassSelector() {
 QueryParser parser = new QueryParser("a:hover");
 Evaluator evaluator = parser.parse();
 Evaluator.PseudoClass pseudoClass = (Evaluator.PseudoClass) evaluator;
 Assert.assertEquals("a", pseudoClass.getTagName());
 Assert.assertEquals("hover", pseudoClass.getPseudoClass());
 }

 @Test
 public void testParseNegativePseudoClassSelector() {
 QueryParser parser = new QueryParser("a:not(:hover)");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator.Not not = (StructuralEvaluator.Not) evaluator;
 Evaluator.PseudoClass hover = (Evaluator.PseudoClass) not.getChild();
 Assert.assertEquals("a", hover.getTagName());
 Assert.assertEquals("hover", hover.getPseudoClass());
 }

 @Test
 public void testParseComplexPseudoClassSelector() {
 QueryParser parser = new QueryParser("li:has(a:containsOwn(link))");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator.Has has = (StructuralEvaluator.Has) evaluator;
 Evaluator childEvaluator = has.getChild();
 Evaluator.Tag aTag = (Evaluator.Tag) childEvaluator.getChild();
 Evaluator.ContainsOwnText containsOwnText = (Evaluator.ContainsOwnText) childEvaluator.getChild(1);

 Assert.assertEquals("li", has.getParent().getTagName());
 Assert.assertEquals("a", aTag.getTagName());
 Assert.assertEquals("link", containsOwnText.getText());
 }

}