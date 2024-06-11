import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

 private static QueryParser parseQuery(String query) {
 return QueryParser.parse(query);
 }

 @Test
 public void testParse_EmptyQuery() {
 QueryParser parser = new QueryParser("");
 assertNull(parser.parse());
 }

 @Test
 public void testParse_SimpleSelector() {
 QueryParser parser = new QueryParser("div.class");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Class);
 }

 @Test
 public void testParse_SelectorWithId() {
 QueryParser parser = new QueryParser("#id");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Id);
 }

 @Test
 public void testParse_CombinedSelectors() {
 QueryParser parser = new QueryParser("div#id > p.class");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator rootEval = (StructuralEvaluator) evaluator;
 assertEquals(StructuralEvaluator.Type.ELEMENT_CHILD, rootEval.getCombiner().getCombiningOperator());
 Evaluator childEval = rootEval.getLeftExpression();
 assertTrue(childEval instanceof Evaluator.Id);
 Evaluator classEval = rootEval.getRightExpression();
 assertTrue(classEval instanceof Evaluator.Class);
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParse_UnknownCombinator() {
 QueryParser parser = new QueryParser("div~unknownCombinator p");
 parser.parse(); // should throw an exception
 }

 @Test
 public void testParse_AttributeSelector() {
 QueryParser parser = new QueryParser("[href='http://example.com']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("href", evaluator.getAttributeName());
 assertEquals("http://example.com", evaluator.getAttributeValue());
 }

 @Test
 public void testParse_AttributeSelector_withQuotes() {
 QueryParser parser = new QueryParser("[title='The quick brown fox']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("title", evaluator.getAttributeName());
 assertEquals("The quick brown fox", evaluator.getAttributeValue());
 }

 @Test
 public void testParse_AttributeSelector_withoutQuotes() {
 QueryParser parser = new QueryParser("[class=red]");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("class", evaluator.getAttributeName());
 assertNull(evaluator.getAttributeValue());
 }

 @Test
 public void testParse_MultipleAttributeSelectors() {
 QueryParser parser = new QueryParser("[title='The quick brown fox'][class='red']");
 Evaluator evaluator = parser.parse();
 CombiningEvaluator andEval = (CombiningEvaluator.And) evaluator;
 Evaluator titleEval = andEval.getLeftExpression();
 assertTrue(titleEval instanceof Evaluator.Attribute);
 assertEquals("title", titleEval.getAttributeName());
 assertEquals("The quick brown fox", titleEval.getAttributeValue());
 Evaluator classEval = andEval.getRightExpression();
 assertTrue(classEval instanceof Evaluator.Class);
 assertEquals("red", classEval.getAttributeName());
 }

 @Test
 public void testParse_PseudoClassSelector() {
 QueryParser parser = new QueryParser("div:first-child");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsFirstChild);
 }

 @Test
 public void testParse_PseudoClassSelectorWithNumericValue() {
 QueryParser parser = new QueryParser("li:nth-child(3)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsNthChild);
 assertEquals(3, ((Evaluator.IsNthChild) evaluator).getIndex());
 }

 @Test
 public void testParse_PseudoClassSelectorWithNegativeValue() {
 QueryParser parser = new QueryParser("li:nth-last-child(-3)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
 assertEquals(-3, ((Evaluator.IsNthLastChild) evaluator).getIndex());
 }

 @Test
 public void testParse_PseudoClassSelectorWithType() {
 QueryParser parser = new QueryParser("li:last-of-type");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsLastOfType);
 }

 @Test
 public void testParse_PseudoClassSelectorWithTypeAndNegativeValue() {
 QueryParser parser = new QueryParser("li:nth-last-of-type(-2):first-child");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator rootEval = (StructuralEvaluator) evaluator;
 assertEquals(StructuralEvaluator.Type.ELEMENT_CHILD, rootEval.getCombiner().getCombiningOperator());
 Evaluator lastOfTypeEval = rootEval.getLeftExpression();
 assertTrue(lastOfTypeEval instanceof Evaluator.IsLastOfType);
 Evaluator isFirstChildEval = rootEval.getRightExpression();
 assertTrue(isFirstChildEval instanceof Evaluator.IsFirstChild);
 }

 @Test
 public void testParse_ElementSelectorAndAttributeSelector() {
 QueryParser parser = new QueryParser("ul li[class='foo']");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator rootEval = (StructuralEvaluator) evaluator;
 Evaluator tagEval = rootEval.getLeftExpression();
 assertTrue(tagEval instanceof Evaluator.Tag);
 assertEquals("li", tagEval.getTagName());
 Evaluator classEval = rootEval.getRightExpression();
 assertTrue(classEval instanceof Evaluator.Class);
 assertEquals("foo", classEval.getAttributeName());
 }

 @Test
 public void testParse_ElementSelectorWithNamespace() {
 QueryParser parser = new QueryParser("html:nth-child(1) body");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator rootEval = (StructuralEvaluator) evaluator;
 Evaluator tagEval = rootEval.getLeftExpression();
 assertTrue(tagEval instanceof Evaluator.Tag);
 assertEquals("body", tagEval.getTagName());
 }

 @Test
 public void testParse_CombinedSelectorsWithAttributeSelectors() {
 QueryParser parser = new QueryParser("div.class[href='http://example.com']");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator rootEval = (StructuralEvaluator) evaluator;
 Evaluator classEval = rootEval.getLeftExpression();
 assertTrue(classEval instanceof Evaluator.Class);
 assertEquals("class", classEval.getAttributeName());
 Evaluator hrefEval = rootEval.getRightExpression();
 assertTrue(hrefEval instanceof Evaluator.Attribute);
 assertEquals("href", hrefEval.getAttributeName());
 assertEquals("http://example.com", hrefEval.getAttributeValue());
 }

 @Test
 public void testParse_CombinedSelectorsWithPseudoClassSelectors() {
 QueryParser parser = new QueryParser("div#id:hover");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator rootEval = (StructuralEvaluator) evaluator;
 Evaluator idEval = rootEval.getLeftExpression();
 assertTrue(idEval instanceof Evaluator.Id);
 assertEquals("id", idEval.getAttributeName());
 Evaluator hoverEval = rootEval.getRightExpression();
 assertTrue(hoverEval instanceof Evaluator.PseudoClass);
 assertEquals(Evaluator.PseudoClass.HOVER, hoverEval.getPseudoClassType());
 }

 @Test
 public void testParse_CombinedSelectorsWithAttributeSelectorsAndPseudoClassSelectors() {
 QueryParser parser = new QueryParser("div#id:hover[class='foo']");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator rootEval = (StructuralEvaluator) evaluator;
 Evaluator idEval = rootEval.getLeftExpression();
 assertTrue(idEval instanceof Evaluator.Id);
 assertEquals("id", idEval.getAttributeName());
 Evaluator hoverEval = rootEval.getRightExpression();
 assertTrue(hoverEval instanceof Evaluator.PseudoClass);
 assertEquals(Evaluator.PseudoClass.HOVER, hoverEval.getPseudoClassType());
 Evaluator classEval = rootEval.getRightExpression().getRightExpression();
 assertTrue(classEval instanceof Evaluator.Class);
 assertEquals("foo", classEval.getAttributeName());
 }

 @Test
 public void testParse_ComplexSelector() {
 QueryParser parser = new QueryParser("ul > li:first-child > a[href^='http']:not([href='http://example.com'])");
 Evaluator evaluator = parser.parse();
 StructuralEvaluator rootEval = (StructuralEvaluator) evaluator;
 Evaluator tagEval = rootEval.getLeftExpression();
 assertTrue(tagEval instanceof Evaluator.Tag);
 assertEquals("a", tagEval.getTagName());
 StructuralEvaluator parentEval = (StructuralEvaluator) tagEval.getLeftExpression();
 assertTrue(parentEval.getCombiner().getCombiningOperator() == StructuralEvaluator.Type.ELEMENT_CHILD);
 assertTrue(parentEval.getLeftExpression() instanceof Evaluator.Tag);
 assertEquals("li", parentEval.getLeftExpression().getTagName());
 Evaluator firstChildEval = parentEval.getRightExpression();
 assertTrue(firstChildEval instanceof Evaluator.PseudoClass);
 assertEquals(Evaluator.PseudoClass.FIRST_CHILD, firstChildEval.getPseudoClassType());
 StructuralEvaluator attributeEval = (StructuralEvaluator) firstChildEval.getLeftExpression();
 assertTrue(attributeEval.getCombiner().getCombiningOperator() == StructuralEvaluator.Type.ATTRIBUTE_VALUE_PREFIX_MATCH);
 assertTrue(attributeEval.getLeftExpression() instanceof Evaluator.Attribute);
 assertEquals("href", attributeEval.getLeftExpression().getAttributeName());
 assertTrue(attributeEval.getRightExpression() instanceof Evaluator.StringLiteral);
 assertEquals("http", attributeEval.getRightExpression().getStringValue().substring(0, 4));
 Evaluator notEval = firstChildEval.getRightExpression();
 assertTrue(notEval instanceof Evaluator.Not);
 Evaluator hrefEval = notEval.getLeftExpression();
 assertTrue(hrefEval instanceof Evaluator.Attribute);
 assertEquals("href", hrefEval.getAttributeName());
 assertTrue(hrefEval.getAttributeValue().equals("http://example.com"));
 }

}