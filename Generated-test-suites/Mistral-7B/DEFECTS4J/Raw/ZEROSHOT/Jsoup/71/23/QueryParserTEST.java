package org.jsoup.select;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "p";
        Evaluator expectedEvaluator = new Evaluator.Tag("p");
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        Evaluator expectedEvaluator = new Evaluator.Class("class1");
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseIDSelector() {
        String query = "#id1";
        Evaluator expectedEvaluator = new Evaluator.Id("id1");
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "p > div";
        Evaluator expectedEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("p"),
                new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("div"))
        );
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "input[type='text']";
        Evaluator expectedEvaluator = new Evaluator.AttributeWithValue(
                "input",
                new Evaluator.Attribute("type"),
                "text"
        );
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseMultipleAttributeSelectors() {
        String query = "input[type='text'][name='username']";
        Evaluator expectedEvaluator = new CombiningEvaluator.And(
                new Evaluator.AttributeWithValue(
                        "input",
                        new Evaluator.Attribute("type"),
                        "text"
                ),
                new Evaluator.AttributeWithValue(
                        "input",
                        new Evaluator.Attribute("name"),
                        "username"
                )
        );
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParsePseudoClassSelectors() {
        String query = "p:first-child";
        Evaluator expectedEvaluator = new Evaluator.IsFirstChild();
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseNthChildSelector() {
        String query = "li:nth-child(2)";
        Evaluator expectedEvaluator = new Evaluator.IsNthChild(1, 1);
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseNthLastChildSelector() {
        String query = "li:nth-last-child(2)";
        Evaluator expectedEvaluator = new Evaluator.IsNthLastChild(1, 1);
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseNthOfTypeSelector() {
        String query = "li:nth-of-type(2)";
        Evaluator expectedEvaluator = new Evaluator.IsNthOfType(1, 1);
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(2)";
        Evaluator expectedEvaluator = new Evaluator.IsNthLastOfType(1, 1);
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseHasSelector() {
        String query = "p:has(a)";
        Evaluator expectedEvaluator = new StructuralEvaluator.Has(new Evaluator.Tag("a"));
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseContainsSelector() {
        String query = "p:contains(Java)";
        Evaluator expectedEvaluator = new Evaluator.ContainsText("Java");
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseContainsOwnSelector() {
        String query = "p:containsOwn(Java)";
        Evaluator expectedEvaluator = new Evaluator.ContainsOwnText("Java");
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseContainsDataSelector() {
        String query = "p:containsData(data:text/plain, Java)";
        Evaluator expectedEvaluator = new Evaluator.ContainsData("data:text/plain", "Java");
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseMatchesSelector() {
        String query = "p:matches(.+Java.+)";
        Evaluator expectedEvaluator = new Evaluator.Matches(Pattern.compile(".+Java."));
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test
    public void testParseMatchesOwnSelector() {
        String query = "p:matchesOwn(.+Java.+)";
        Evaluator expectedEvaluator = new Evaluator.MatchesOwn(Pattern.compile(".+Java."));
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluator, actualEvaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "p:invalidSelector";
        QueryParser.parse(query);
    }
}