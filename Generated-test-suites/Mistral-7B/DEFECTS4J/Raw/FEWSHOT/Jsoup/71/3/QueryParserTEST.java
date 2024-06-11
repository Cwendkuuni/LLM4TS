package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.TokenType;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseTagQuery() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals(evaluator.toString(), "tagName=p");
    }

    @Test
    public void testParseIdQuery() {
        String query = "#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals(evaluator.toString(), "id=id1");
    }

    @Test
    public void testParseClassQuery() {
        String query = ".className";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(evaluator.toString(), "className=className");
    }

    @Test
    public void testParseCombinedQuery() {
        String query = "p#id1.className";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(children.size(), 2);

        Evaluator tagEval = children.get(0);
        assertTrue(tagEval instanceof Evaluator.Tag);
        assertEquals(tagEval.toString(), "tagName=p");

        Evaluator idEval = children.get(1);
        assertTrue(idEval instanceof Evaluator.Id);
        assertEquals(idEval.toString(), "id=id1");
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[attribute=value]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals(evaluator.toString(), "attribute=value");
    }

    @Test
    public void testParseAttributeWithValueQuery() {
        String query = "[attribute='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals(evaluator.toString(), "attribute='value'");
    }

    @Test
    public void testParseAttributeWithValueStartingQuery() {
        String query = "[attribute^='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals(evaluator.toString(), "attribute^='value'");
    }

    @Test
    public void testParseAttributeWithValueEndingQuery() {
        String query = "[attribute$='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals(evaluator.toString(), "attribute$='value'");
    }

    @Test
    public void testParseAttributeWithValueContainingQuery() {
        String query = "[attribute*='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals(evaluator.toString(), "attribute*='value'");
    }

    @Test
    public void testParseAttributeWithValueMatchingQuery() {
        String query = "[attribute~='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals(evaluator.toString(), "attribute~='value'");
    }

    @Test
    public void testParseAttributeWithValueNotQuery() {
        String query = "[attribute!='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
        assertEquals(evaluator.toString(), "attribute!='value'");
    }

    @Test
    public void testParseTagAndIdQuery() {
        String query = "p#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IdWithTag);
        assertEquals(evaluator.toString(), "tagName=p, id=id1");
    }

    @Test
    public void testParseCombinedTagAndIdQuery() {
        String query = "p#id1.className";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(children.size(), 2);

        Evaluator tagEval = children.get(0);
        assertTrue(tagEval instanceof Evaluator.Tag);
        assertEquals(tagEval.toString(), "tagName=p");

        Evaluator idEval = children.get(1);
        assertTrue(idEval instanceof Evaluator.Id);
        assertEquals(idEval.toString(), "id=id1");
    }

    @Test
    public void testParseTagAndClassQuery() {
        String query = "p.className";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(children.size(), 2);

        Evaluator tagEval = children.get(0);
        assertTrue(tagEval instanceof Evaluator.Tag);
        assertEquals(tagEval.toString(), "tagName=p");

        Evaluator classEval = children.get(1);
        assertTrue(classEval instanceof Evaluator.Class);
        assertEquals(classEval.toString(), "className=className");
    }

    @Test
    public void testParseCombinedTagIdAndClassQuery() {
        String query = "p#id1.className";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(children.size(), 3);

        Evaluator tagEval = children.get(0);
        assertTrue(tagEval instanceof Evaluator.Tag);
        assertEquals(tagEval.toString(), "tagName=p");

        Evaluator idEval = children.get(1);
        assertTrue(idEval instanceof Evaluator.Id);
        assertEquals(idEval.toString(), "id=id1");

        Evaluator classEval = children.get(2);
        assertTrue(classEval instanceof Evaluator.Class);
        assertEquals(classEval.toString(), "className=className");
    }

    @Test
    public void testParseNthChildQuery() {
        String query = ":nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals(evaluator.toString(), "nthChild=2");
    }

    @Test
    public void testParseNthLastChildQuery() {
        String query = ":nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertEquals(evaluator.toString(), "nthLastChild=2");
    }

    @Test
    public void testParseNthOfTypeQuery() {
        String query = ":nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertEquals(evaluator.toString(), "nthOfType=2");
    }

    @Test
    public void testParseNthLastOfTypeQuery() {
        String query = ":nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals(evaluator.toString(), "nthLastOfType=2");
    }

    @Test
    public void testParseFirstChildQuery() {
        String query = ":first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        assertEquals(evaluator.toString(), "isFirstChild=true");
    }

    @Test
    public void testParseLastChildQuery() {
        String query = ":last-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsLastChild);
        assertEquals(evaluator.toString(), "isLastChild=true");
    }

    @Test
    public void testParseFirstOfTypeQuery() {
        String query = ":first-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstOfType);
        assertEquals(evaluator.toString(), "isFirstOfType=true");
    }

    @Test
    public void testParseLastOfTypeQuery() {
        String query = ":last-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsLastOfType);
        assertEquals(evaluator.toString(), "isLastOfType=true");
    }

    @Test
    public void testParseOnlyChildQuery() {
        String query = ":only-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsOnlyChild);
        assertEquals(evaluator.toString(), "isOnlyChild=true");
    }

    @Test
    public void testParseOnlyOfTypeQuery() {
        String query = ":only-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsOnlyOfType);
        assertEquals(evaluator.toString(), "isOnlyOfType=true");
    }

    @Test
    public void testParseEmptyElementQuery() {
        String query = ":empty";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsEmpty);
        assertEquals(evaluator.toString(), "isEmpty=true");
    }

    @Test
    public void testParseRootQuery() {
        String query = ":root";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsRoot);
        assertEquals(evaluator.toString(), "isRoot=true");
    }

    @Test
    public void testParseHasQuery() {
        String query = ":has(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals(evaluator.toString(), "has(p)");
    }

    @Test
    public void testParseContainsQuery() {
        String query = ":contains(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        assertEquals(evaluator.toString(), "containsText=text");
    }

    @Test
    public void testParseContainsOwnQuery() {
        String query = ":containsOwn(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals(evaluator.toString(), "containsOwnText=text");
    }

    @Test
    public void testParseContainsDataQuery() {
        String query = ":containsData(data)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsData);
        assertEquals(evaluator.toString(), "containsData=data");
    }

    @Test
    public void testParseMatchesQuery() {
        String query = ":matches(regex)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Matches);
        assertEquals(evaluator.toString(), "matches(regex)");
    }

    @Test
    public void testParseMatchesOwnQuery() {
        String query = ":matchesOwn(regex)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
        assertEquals(evaluator.toString(), "matchesOwn(regex)");
    }

    @Test
    public void testParseNotQuery() {
        String query = ":not(selector)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertEquals(evaluator.toString(), "not(selector)");
    }
}