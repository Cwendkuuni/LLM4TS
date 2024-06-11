package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

    QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("");
    }

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = queryParser.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParseTagSelector() {
        String query = "p";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(".class1", evaluator.toString());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("#id1", evaluator.toString());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title=Test]";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("[title=Test]", evaluator.toString());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.class1 p#id1";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator[] children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.length);

        Evaluator firstChild = children[0];
        assertTrue(firstChild instanceof Evaluator.Tag);
        assertEquals("div", firstChild.toString());

        Evaluator secondChild = children[1];
        assertTrue(secondChild instanceof Evaluator.And);
        Evaluator[] grandChildren = ((Evaluator.And) secondChild).getChildren();
        assertEquals(2, grandChildren.length);

        Evaluator firstGrandChild = grandChildren[0];
        assertTrue(firstGrandChild instanceof Evaluator.Class);
        assertEquals(".class1", firstGrandChild.toString());

        Evaluator secondGrandChild = grandChildren[1];
        assertTrue(secondGrandChild instanceof Evaluator.Id);
        assertEquals("#id1", secondGrandChild.toString());
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "div p";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Descendant);
        assertEquals("div > p", evaluator.toString());
    }

    @Test
    public void testParseAdjacentSiblingSelector() {
        String query = "p + p";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.AdjacentSibling);
        assertEquals("p + p", evaluator.toString());
    }

    @Test
    public void testParseGeneralSiblingSelector() {
        String query = "p ~ p";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.GeneralSibling);
        assertEquals("p ~ p", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = ":first-child";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        assertEquals(":first-child", evaluator.toString());
    }

    @Test
    public void testParsePseudoElementSelector() {
        String query = ":before";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoElement);
        assertEquals(":before", evaluator.toString());
    }

    @Test
    public void testParseNegatedSelector() {
        String query = ":not(p)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertEquals("not(p)", evaluator.toString());
    }

    @Test
    public void testParseNthChildSelector() {
        String query = "li:nth-child(2)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals("li:nth-child(2)", evaluator.toString());
    }

    @Test
    public void testParseNthOfTypeSelector() {
        String query = "li:nth-of-type(2)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertEquals("li:nth-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseNthLastChildSelector() {
        String query = "li:nth-last-child(2)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertEquals("li:nth-last-child(2)", evaluator.toString());
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(2)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals("li:nth-last-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseContainsTextSelector() {
        String query = "p:contains(Test)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        assertEquals("p:contains(Test)", evaluator.toString());
    }

    @Test
    public void testParseContainsOwnTextSelector() {
        String query = "p:containsOwn(Test)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals("p:containsOwn(Test)", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueSelector() {
        String query = "input[type=text]";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("input[type=text]", evaluator.toString());
    }

    @Test
    public void testParseAttributeStartingSelector() {
        String query = "input[class^=text]";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("input[class^=text]", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueEndingSelector() {
        String query = "input[class$=text]";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("input[class$=text]", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueContainingSelector() {
        String query = "input[class*=text]";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("input[class*=text]", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueMatchingSelector() {
        String query = "input[class~=text]";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("input[class~=text]", evaluator.toString());
    }

    @Test
    public void testParseHasSelector() {
        String query = "div:has(p)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals("div:has(p)", evaluator.toString());
    }

    @Test
    public void testParseMatchesSelector() {
        String query = "div:matches(p)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Matches);
        assertEquals("div:matches(p)", evaluator.toString());
    }

    @Test
    public void testParseMatchesOwnSelector() {
        String query = "div:matchesOwn(p)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
        assertEquals("div:matchesOwn(p)", evaluator.toString());
    }

    @Test
    public void testParseContainsDataSelector() {
        String query = "div:containsData(Test)";
        Evaluator evaluator = queryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.ContainsData);
        assertEquals("div:containsData(Test)", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithUnknownOperatorFailed() {
        String query = "div:foo(p)";
        try {
            queryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: foo", e.getMessage());
        }
    }
}