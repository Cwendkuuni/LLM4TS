import org.jsoup.select.*;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jsoup.internal.Normalizer.normalize;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertTrue(evaluator instanceof Evaluator.Tag);
    }

    @Test
    public void testCombinator() {
        Evaluator evaluator = QueryParser.parse("div > p");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.evals.get(0) instanceof Evaluator.Tag);
        assertTrue(and.evals.get(1) instanceof StructuralEvaluator.ImmediateParent);
    }

    @Test
    public void testAttribute() {
        Evaluator evaluator = QueryParser.parse("[href]");
        assertTrue(evaluator instanceof Evaluator.Attribute);
    }

    @Test
    public void testIndexLessThan() {
        Evaluator evaluator = QueryParser.parse(":lt(3)");
        assertTrue(evaluator instanceof Evaluator.IndexLessThan);
    }

    @Test
    public void testIndexGreaterThan() {
        Evaluator evaluator = QueryParser.parse(":gt(3)");
        assertTrue(evaluator instanceof Evaluator.IndexGreaterThan);
    }

    @Test
    public void testIndexEquals() {
        Evaluator evaluator = QueryParser.parse(":eq(3)");
        assertTrue(evaluator instanceof Evaluator.IndexEquals);
    }

    @Test
    public void testHas() {
        Evaluator evaluator = QueryParser.parse(":has(p)");
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testContains() {
        Evaluator evaluator = QueryParser.parse(":contains(text)");
        assertTrue(evaluator instanceof Evaluator.ContainsText);
    }

    @Test
    public void testContainsOwn() {
        Evaluator evaluator = QueryParser.parse(":containsOwn(text)");
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
    }

    @Test
    public void testContainsData() {
        Evaluator evaluator = QueryParser.parse(":containsData(data)");
        assertTrue(evaluator instanceof Evaluator.ContainsData);
    }

    @Test
    public void testMatches() {
        Evaluator evaluator = QueryParser.parse(":matches(regex)");
        assertTrue(evaluator instanceof Evaluator.Matches);
    }

    @Test
    public void testMatchesOwn() {
        Evaluator evaluator = QueryParser.parse(":matchesOwn(regex)");
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
    }

    @Test
    public void testNot() {
        Evaluator evaluator = QueryParser.parse(":not(p)");
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
    }

}