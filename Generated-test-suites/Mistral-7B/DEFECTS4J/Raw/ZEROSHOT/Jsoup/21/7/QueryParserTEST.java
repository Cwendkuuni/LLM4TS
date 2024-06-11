import org.junit.Assert;
import org.junit.Test;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

 @Test
 public void testParseQuery() {
 String query = "div.class1.class2 > input[type='text']";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.DIV, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.ClassFilter.CLASS_NAMES, ((Evaluator.ClassFilter) evaluator).getClassFilter().getClass());
 Assert.assertEquals("class1", ((Evaluator.ClassFilter) evaluator).getClassFilter().getClassNames().get(0));
 Assert.assertEquals("class2", ((Evaluator.ClassFilter) evaluator).getClassFilter().getClassNames().get(1));
 Assert.assertEquals(Evaluator.CombiningLogic.CHILD_OF, ((CombiningEvaluator) evaluator).getCombiningLogic());
 Assert.assertEquals(Evaluator.Tag.INPUT, ((Evaluator.Tag) evaluator.getChild()).getTagName());
 Assert.assertEquals(Evaluator.AttributeFilter.TYPE, ((Evaluator.AttributeFilter) evaluator.getChild().getChild()).getAttributeName());
 Assert.assertEquals("text", ((Evaluator.AttributeFilter) evaluator.getChild().getChild()).getAttributeValue());
 }

 @Test
 public void testParseQueryWithCombiner() {
 String query = "div#id1 ~ p";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.DIV, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.IdFilter.ID, ((Evaluator.IdFilter) evaluator).getIdFilter().getIdType());
 Assert.assertEquals("id1", ((Evaluator.IdFilter) evaluator).getIdFilter().getIdValue());
 Assert.assertEquals(Evaluator.CombiningLogic.PREVIOUS_SIBLING, ((CombiningEvaluator) evaluator.getChild()).getCombiningLogic());
 Assert.assertEquals(Evaluator.Tag.P, ((Evaluator.Tag) evaluator.getChild().getChild()).getTagName());
 }

 @Test
 public void testParseQueryWithAttributeFilter() {
 String query = "input[type='text'][name='username']";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.INPUT, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.AttributeFilter.TYPE, ((Evaluator.AttributeFilter) evaluator).getAttributeName());
 Assert.assertEquals("text", ((Evaluator.AttributeFilter) evaluator).getAttributeValue());
 Assert.assertEquals(Evaluator.AttributeFilter.NAME, ((Evaluator.AttributeFilter) evaluator.getChild()).getAttributeName());
 Assert.assertEquals("username", ((Evaluator.AttributeFilter) evaluator.getChild()).getAttributeValue());
 }

 @Test
 public void testParseQueryWithPseudoClass() {
 String query = "a:link";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.A, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.PseudoClass.LINK, ((PseudoClassEvaluator) evaluator).getPseudoClass());
 }

 @Test
 public void testParseQueryWithPseudoElement() {
 String query = "ul > li:first-child::before";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.UL, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.PseudoElement.BEFORE, ((PseudoElementEvaluator) evaluator.getChild()).getPseudoElement());
 Assert.assertEquals(CombiningEvaluator.Logic.CHILD_OF, ((CombiningEvaluator) evaluator.getChild().getChild()).getCombiningLogic());
 Assert.assertEquals(Evaluator.Tag.LI, ((Evaluator.Tag) evaluator.getChild().getChild().getChild()).getTagName());
 Assert.assertEquals(Evaluator.PseudoClass.FIRST_CHILD, ((PseudoClassEvaluator) evaluator.getChild().getChild().getChild()).getPseudoClass());
 }

 @Test
 public void testParseQueryWithMultipleSelectors() {
 String query = "div#header div.container";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.DIV, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.IdFilter.ID, ((Evaluator.IdFilter) evaluator.getChild()).getIdType());
 Assert.assertEquals("header", ((Evaluator.IdFilter) evaluator.getChild()).getIdValue());
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getChild().getType());
 Assert.assertEquals(Evaluator.Tag.DIV, ((Evaluator.Tag) evaluator.getChild().getChild()).getTagName());
 Assert.assertEquals(Evaluator.ClassFilter.CLASS_NAMES, ((Evaluator.ClassFilter) evaluator.getChild().getChild()).getClassFilter().getClass());
 Assert.assertEquals("container", ((Evaluator.ClassFilter) evaluator.getChild().getChild()).getClassFilter().getClassNames().get(0));
 }

 @Test
 public void testParseQueryWithAttributeContains() {
 String query = "a[href*='google.com']";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.A, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.AttributeFilter.HREF, ((Evaluator.AttributeFilter) evaluator).getAttributeName());
 Assert.assertEquals(Evaluator.AttributeOperator.CONTAINS_SUBSTRING, ((Evaluator.AttributeFilter) evaluator).getAttributeOperator());
 Assert.assertEquals("google.com", ((Evaluator.AttributeFilter) evaluator).getAttributeValue());
 }

 @Test
 public void testParseQueryWithAttributeStartsWith() {
 String query = "input[name^='user']";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.INPUT, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.AttributeFilter.NAME, ((Evaluator.AttributeFilter) evaluator).getAttributeName());
 Assert.assertEquals(Evaluator.AttributeOperator.STARTS_WITH, ((Evaluator.AttributeFilter) evaluator).getAttributeOperator());
 Assert.assertEquals("user", ((Evaluator.AttributeFilter) evaluator).getAttributeValue());
 }

 @Test
 public void testParseQueryWithAttributeEndsWith() {
 String query = "input[name$='user']";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.INPUT, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.AttributeFilter.NAME, ((Evaluator.AttributeFilter) evaluator).getAttributeName());
 Assert.assertEquals(Evaluator.AttributeOperator.ENDS_WITH, ((Evaluator.AttributeFilter) evaluator).getAttributeOperator());
 Assert.assertEquals("user", ((Evaluator.AttributeFilter) evaluator).getAttributeValue());
 }

 @Test
 public void testParseQueryWithAttributeRegex() {
 String query = "input[name~='user\\[1-9]']";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.INPUT, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.AttributeFilter.NAME, ((Evaluator.AttributeFilter) evaluator).getAttributeName());
 Assert.assertEquals(Evaluator.AttributeOperator.MATCHES_REGEX, ((Evaluator.AttributeFilter) evaluator).getAttributeOperator());
 Assert.assertNotNull(((Evaluator.AttributeFilter) evaluator).getAttributeValueRegex());
 Assert.assertEquals("user\\[1-9]", ((Evaluator.AttributeFilter) evaluator).getAttributeValueRegex().pattern().pattern());
 }

 @Test
 public void testParseQueryWithMultipleAttributeFilters() {
 String query = "input[type='text'][name='username'][required]";
 Evaluator evaluator = QueryParser.parse(query);

 // Add your assertions here to check if the Evaluator tree matches the expected result
 // For example:
 Assert.assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Assert.assertEquals(Evaluator.Tag.INPUT, ((Evaluator.Tag) evaluator).getTagName());
 Assert.assertEquals(Evaluator.AttributeFilter.TYPE, ((Evaluator.AttributeFilter) evaluator).getAttributeName());
 Assert.assertEquals("text", ((Evaluator.AttributeFilter) evaluator).getAttributeValue());
 Assert.assertTrue(((Evaluator.AttributeFilter) evaluator).hasNextSibling());
 Assert.assertEquals(Evaluator.AttributeFilter.NAME, ((Evaluator.AttributeFilter) evaluator.getNextSibling()).getAttributeName());
 Assert.assertEquals("username", ((Evaluator.AttributeFilter) evaluator.getNextSibling()).getAttributeValue());
 Assert.assertTrue(((Evaluator.AttributeFilter) evaluator.getNextSibling()).hasNextSibling());
 Assert.assertEquals(Evaluator.PseudoClass.REQUIRED, ((PseudoClassEvaluator) evaluator.getNextSibling().getNextSibling()).getPseudoClass());
 }
}