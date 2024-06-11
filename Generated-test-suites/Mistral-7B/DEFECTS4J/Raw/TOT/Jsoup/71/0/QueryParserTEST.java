import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseSingleTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.asTag().getTagName());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("myClass", evaluator.asClass().getClassNames()[0]);
    }

    @Test
    public void testParseIdQuery() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("myId", evaluator.asId().getId());
    }

    @Test
    public void testParseComplexQueryWithCombinator() {
        String query = "div.myClass > ul > li";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And)evaluator;

        assertEquals(3, andEvaluator.getEvaluators().size());

        Evaluator firstEvaluator = andEvaluator.getEvaluators().get(0);
        assertTrue(firstEvaluator instanceof Evaluator.Tag);
        assertEquals("div", firstEvaluator.asTag().getTagName());

        Evaluator secondEvaluator = andEvaluator.getEvaluators().get(1);
        assertTrue(secondEvaluator instanceof Evaluator.Class);
        assertEquals("myClass", secondEvaluator.asClass().getClassNames()[0]);

        Evaluator thirdEvaluator = andEvaluator.getEvaluators().get(2);
        assertTrue(thirdEvaluator instanceof StructuralEvaluator.ImmediateChild);
        StructuralEvaluator.ImmediateChild immediateChild = (StructuralEvaluator.ImmediateChild)thirdEvaluator;
        assertTrue(immediateChild.getParentEvaluator() instanceof Evaluator.Tag);
        assertEquals("ul", ((Evaluator.Tag)immediateChild.getParentEvaluator()).getTagName());
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof Evaluator.Attribute);
        Evaluator.Attribute attributeEvaluator = (Evaluator.Attribute)evaluator;
        assertEquals("type", attributeEvaluator.getAttributeName());
        assertEquals("text", attributeEvaluator.getAttributeValue());
    }

    @Test
    public void testParseErrorQuery() {
        String query = "div#myId:not(div#anotherId) > ul > #myId";
        try {
            QueryParser.parse(query);
            fail("Expected IllegalArgumentException");
        } catch (Selector.SelectorParseException e) {
            String expectedMessage = "Unexpected combinator ':not' after '#myId'";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void testParsePseudoClassQuery() {
        String query = "input:checked";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClass);
        assertEquals("checked", evaluator.asPseudoClass().getPseudoClass());
    }

    @Test
    public void testParseNthChildQuery() {
        String query = "li:nth-child(3)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        Evaluator.IsNthChild isNthChild = (Evaluator.IsNthChild)evaluator;
        assertEquals(3, isNthChild.getIndex());
        assertEquals(false, isNthChild.isOfType());
    }

    @Test
    public void testParseNthLastChildQuery() {
        String query = "li:nth-last-child(-n+3)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        Evaluator.IsNthLastChild isNthLastChild = (Evaluator.IsNthLastChild)evaluator;
        assertEquals(3, isNthLastChild.getIndex());
        assertEquals(false, isNthLastChild.isOfType());
    }

    @Test
    public void testParseNthOfTypeQuery() {
        String query = "li:nth-of-type(3)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        Evaluator.IsNthOfType isNthOfType = (Evaluator.IsNthOfType)evaluator;
        assertEquals(3, isNthOfType.getIndex());
        assertEquals(true, isNthOfType.isOfType());
    }

    @Test
    public void testParseNthLastOfTypeQuery() {
        String query = "li:nth-last-of-type(-n+3)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        Evaluator.IsNthLastOfType isNthLastOfType = (Evaluator.IsNthLastOfType)evaluator;
        assertEquals(3, isNthLastOfType.getIndex());
        assertEquals(true, isNthLastOfType.isOfType());
    }

    @Test
    public void testParseDescendantQuery() {
        String query = "div > ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Descendant);
        StructuralEvaluator.Descendant descendant = (StructuralEvaluator.Descendant)evaluator;
        assertEquals(2, descendant.getDepth());
        assertTrue(descendant.getParentEvaluator() instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag)descendant.getParentEvaluator()).getTagName());
    }

    @Test
    public void testParseChildQuery() {
        String query = "div > ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Child);
        StructuralEvaluator.Child child = (StructuralEvaluator.Child)evaluator;
        assertEquals("ul", child.getParentEvaluator().asTag().getTagName());
    }

    @Test
    public void testParseAdjacentSiblingQuery() {
        String query = "li + li";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.AdjacentSibling);
        StructuralEvaluator.AdjacentSibling adjacentSibling = (StructuralEvaluator.AdjacentSibling)evaluator;
        assertEquals("li", adjacentSibling.getPreviousSiblingEvaluator().asTag().getTagName());
    }

    @Test
    public void testParseGeneralSiblingQuery() {
        String query = "li ~ li";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.GeneralSibling);
        StructuralEvaluator.GeneralSibling generalSibling = (StructuralEvaluator.GeneralSibling)evaluator;
        assertEquals("li", generalSibling.getPreviousSiblingEvaluator().asTag().getTagName());
    }

    @Test
    public void testParseAttributeAttributeQuery() {
        String query = "input[type='text'][name='username']";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And)evaluator;

        assertEquals(2, andEvaluator.getEvaluators().size());

        Evaluator firstEvaluator = andEvaluator.getEvaluators().get(0);
        assertTrue(firstEvaluator instanceof Evaluator.Attribute);
        Evaluator.Attribute firstAttributeEvaluator = (Evaluator.Attribute)firstEvaluator;
        assertEquals("type", firstAttributeEvaluator.getAttributeName());
        assertEquals("text", firstAttributeEvaluator.getAttributeValue());

        Evaluator secondEvaluator = andEvaluator.getEvaluators().get(1);
        assertTrue(secondEvaluator instanceof Evaluator.Attribute);
        Evaluator.Attribute secondAttributeEvaluator = (Evaluator.Attribute)secondEvaluator;
        assertEquals("name", secondAttributeEvaluator.getAttributeName());
        assertEquals("username", secondAttributeEvaluator.getAttributeValue());
    }

    @Test
    public void testParseAttributeStartsWithQuery() {
        String query = "div[class^='foo']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeStarting);
        Evaluator.AttributeStarting attributeStarting = (Evaluator.AttributeStarting)evaluator;
        assertEquals("class", attributeStarting.getAttributeName());
        assertEquals("foo", attributeStarting.getAttributeValuePrefix());
    }

    @Test
    public void testParseAttributeEndsWithQuery() {
        String query = "div[class$='foo']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeEnding);
        Evaluator.AttributeEnding attributeEnding = (Evaluator.AttributeEnding)evaluator;
        assertEquals("class", attributeEnding.getAttributeName());
        assertEquals("foo", attributeEnding.getAttributeValueSuffix());
    }

    @Test
    public void testParseAttributeContainsQuery() {
        String query = "div[class*='foo']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        Evaluator.AttributeWithValueContaining attributeWithValueContaining = (Evaluator.AttributeWithValueContaining)evaluator;
        assertEquals("class", attributeWithValueContaining.getAttributeName());
        assertEquals("foo", attributeWithValueContaining.getAttributeValue());
    }

    @Test
    public void testParseAttributeRegexQuery() {
        String query = "div[class~='foo|bar']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        Evaluator.AttributeWithValueMatching attributeWithValueMatching = (Evaluator.AttributeWithValueMatching)evaluator;
        Pattern regex = Pattern.compile("foo|bar");
        assertEquals(regex, attributeWithValueMatching.getAttributeValuePattern());
    }

    @Test
    public void testParsePseudoElementQuery() {
        String query = "ul > li:first-child:before";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof StructuralEvaluator.PseudoElement);
        StructuralEvaluator.PseudoElement pseudoElement = (StructuralEvaluator.PseudoElement)evaluator;
        assertEquals("before", pseudoElement.getPseudoElementType());
        assertEquals("li", pseudoElement.getParentEvaluator().asTag().getTagName());
    }

    @Test
    public void testParseElementQueryWithNamespace() {
        String query = "html>body > div.myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag)evaluator;
        assertEquals("div", tagEvaluator.getTagName());
        assertEquals("myClass", tagEvaluator.asClass().getClassNames()[0]);
    }
}