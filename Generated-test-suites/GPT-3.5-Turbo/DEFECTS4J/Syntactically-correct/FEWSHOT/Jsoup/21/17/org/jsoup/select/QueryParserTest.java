package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        assertEquals(new Evaluator.Id("id"), QueryParser.parse("#id"));
        assertEquals(new Evaluator.Class("class"), QueryParser.parse(".class"));
        assertEquals(new Evaluator.Tag("tagName"), QueryParser.parse("tagName"));
        assertEquals(new Evaluator.Attribute("attr"), QueryParser.parse("[attr]"));
        assertEquals(new Evaluator.AttributeWithValueStarting("attr", "value"), QueryParser.parse("[attr^=value]"));
        assertEquals(new Evaluator.AttributeWithValueEnding("attr", "value"), QueryParser.parse("[attr$=value]"));
        assertEquals(new Evaluator.AttributeWithValueContaining("attr", "value"), QueryParser.parse("[attr*=value]"));
        assertEquals(new Evaluator.AttributeWithValueMatching("attr", Pattern.compile("regex")), QueryParser.parse("[attr~=regex]"));
        assertEquals(new Evaluator.AllElements(), QueryParser.parse("*"));
        assertEquals(new Evaluator.IndexLessThan(5), QueryParser.parse(":lt(5)"));
        assertEquals(new Evaluator.IndexGreaterThan(5), QueryParser.parse(":gt(5)"));
        assertEquals(new Evaluator.IndexEquals(5), QueryParser.parse(":eq(5)"));
        assertEquals(new StructuralEvaluator.Has(new Evaluator.Tag("tagName")), QueryParser.parse(":has(tagName)"));
        assertEquals(new Evaluator.ContainsText("text"), QueryParser.parse(":contains(text)"));
        assertEquals(new Evaluator.ContainsOwnText("text"), QueryParser.parse(":containsOwn(text)"));
        assertEquals(new Evaluator.Matches(Pattern.compile("regex")), QueryParser.parse(":matches(regex)"));
        assertEquals(new Evaluator.MatchesOwn(Pattern.compile("regex")), QueryParser.parse(":matchesOwn(regex)"));
        assertEquals(new StructuralEvaluator.Not(new Evaluator.Tag("tagName")), QueryParser.parse(":not(tagName)"));
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("");
        List<Evaluator> evals = new ArrayList<Evaluator>();
        evals.add(new Evaluator.Tag("tag1"));
        evals.add(new Evaluator.Tag("tag2"));
        parser.evals = evals;

        parser.combinator('>');
        assertEquals(new CombiningEvaluator.And(new Evaluator.Tag("tag2"), new StructuralEvaluator.ImmediateParent(new CombiningEvaluator.And(new Evaluator.Tag("tag1")))), parser.evals.get(0));

        parser.combinator(' ');
        assertEquals(new CombiningEvaluator.And(new Evaluator.Tag("tag2"), new StructuralEvaluator.Parent(new CombiningEvaluator.And(new Evaluator.Tag("tag1")))), parser.evals.get(0));

        parser.combinator('+');
        assertEquals(new CombiningEvaluator.And(new Evaluator.Tag("tag2"), new StructuralEvaluator.ImmediatePreviousSibling(new CombiningEvaluator.And(new Evaluator.Tag("tag1")))), parser.evals.get(0));

        parser.combinator('~');
        assertEquals(new CombiningEvaluator.And(new Evaluator.Tag("tag2"), new StructuralEvaluator.PreviousSibling(new CombiningEvaluator.And(new Evaluator.Tag("tag1")))), parser.evals.get(0));
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("[attr^=value] > tag");
        parser.tq = new TokenQueue("[attr^=value] > tag");
        String subQuery = parser.consumeSubQuery();
        assertEquals("[attr^=value]", subQuery);
        assertEquals(" > tag", parser.tq.remainder());
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("[attr^=value]");
        parser.tq = new TokenQueue("[attr^=value]");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.AttributeWithValueStarting("attr", "value"), parser.evals.get(0));

        parser = new QueryParser(".className");
        parser.tq = new TokenQueue(".className");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.Class("className"), parser.evals.get(0));

        parser = new QueryParser("tagName");
        parser.tq = new TokenQueue("tagName");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.Tag("tagName"), parser.evals.get(0));

        parser = new QueryParser("#id");
        parser.tq = new TokenQueue("#id");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.Id("id"), parser.evals.get(0));

        parser = new QueryParser("*");
        parser.tq = new TokenQueue("*");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.AllElements(), parser.evals.get(0));

        parser = new QueryParser(":lt(5)");
        parser.tq = new TokenQueue(":lt(5)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.IndexLessThan(5), parser.evals.get(0));

        parser = new QueryParser(":gt(5)");
        parser.tq = new TokenQueue(":gt(5)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.IndexGreaterThan(5), parser.evals.get(0));

        parser = new QueryParser(":eq(5)");
        parser.tq = new TokenQueue(":eq(5)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.IndexEquals(5), parser.evals.get(0));

        parser = new QueryParser(":has(tagName)");
        parser.tq = new TokenQueue(":has(tagName)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new StructuralEvaluator.Has(new Evaluator.Tag("tagName")), parser.evals.get(0));

        parser = new QueryParser(":contains(text)");
        parser.tq = new TokenQueue(":contains(text)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.ContainsText("text"), parser.evals.get(0));

        parser = new QueryParser(":containsOwn(text)");
        parser.tq = new TokenQueue(":containsOwn(text)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.ContainsOwnText("text"), parser.evals.get(0));

        parser = new QueryParser(":matches(regex)");
        parser.tq = new TokenQueue(":matches(regex)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.Matches(Pattern.compile("regex")), parser.evals.get(0));

        parser = new QueryParser(":matchesOwn(regex)");
        parser.tq = new TokenQueue(":matchesOwn(regex)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new Evaluator.MatchesOwn(Pattern.compile("regex")), parser.evals.get(0));

        parser = new QueryParser(":not(tagName)");
        parser.tq = new TokenQueue(":not(tagName)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertEquals(new StructuralEvaluator.Not(new Evaluator.Tag("tagName")), parser.evals.get(0));
    }
}