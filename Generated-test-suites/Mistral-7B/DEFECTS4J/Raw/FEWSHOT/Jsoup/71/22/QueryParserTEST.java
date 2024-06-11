package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.StructuralEvaluator.And;
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.IsFirstChild;
import org.jsoup.select.StructuralEvaluator.IsFirstOfType;
import org.jsoup.select.StructuralEvaluator.IsLastChild;
import org.jsoup.select.StructuralEvaluator.IsLastOfType;
import org.jsoup.select.StructuralEvaluator.IsNthChild;
import org.jsoup.select.StructuralEvaluator.IsNthOfType;
import org.jsoup.select.StructuralEvaluator.IsNthLastChild;
import org.jsoup.select.StructuralEvaluator.IsNthLastOfType;
import org.jsoup.select.StructuralEvaluator.IsOnlyChild;
import org.jsoup.select.StructuralEvaluator.IsOnlyOfType;
import org.jsoup.select.StructuralEvaluator.IsRoot;
import org.jsoup.select.StructuralEvaluator.Or;
import org.jsoup.select.Tag;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse_emptyQuery() {
        QueryParser parser = new QueryParser("");
        assertEquals(parser.parse(), Evaluator.AllElements());
    }

    @Test
    public void testParse_simpleTagSelector() {
        QueryParser parser = new QueryParser("div");
        assertEquals(parser.parse(), new Evaluator.Tag("div"));
    }

    @Test
    public void testParse_classSelector() {
        QueryParser parser = new QueryParser(".class");
        assertEquals(parser.parse(), new Evaluator.Class("class"));
    }

    @Test
    public void testParse_idSelector() {
        QueryParser parser = new QueryParser("#id");
        assertEquals(parser.parse(), new Evaluator.Id("id"));
    }

    @Test
    public void testParse_attributeSelector() {
        QueryParser parser = new QueryParser("[title]");
        assertEquals(parser.parse(), new Evaluator.Attribute("title"));
    }

    @Test
    public void testParse_attributeSelector_withValue() {
        QueryParser parser = new QueryParser("[title='foo']");
        assertEquals(parser.parse(), new Evaluator.AttributeWithValue("title", "foo"));
    }

    @Test
    public void testParse_attributeSelector_withValueStarting() {
        QueryParser parser = new QueryParser("[title^='foo']");
        assertEquals(parser.parse(), new Evaluator.AttributeWithValueStarting("title", "foo"));
    }

    @Test
    public void testParse_attributeSelector_withValueEnding() {
        QueryParser parser = new QueryParser("[title$='foo']");
        assertEquals(parser.parse(), new Evaluator.AttributeWithValueEnding("title", "foo"));
    }

    @Test
    public void testParse_attributeSelector_withValueContaining() {
        QueryParser parser = new QueryParser("[title*='foo']");
        assertEquals(parser.parse(), new Evaluator.AttributeWithValueContaining("title", "foo"));
    }

    @Test
    public void testParse_attributeSelector_withValueMatching() {
        QueryParser parser = new QueryParser("[title~='foo']");
        assertEquals(parser.parse(), new Evaluator.AttributeWithValueMatching("title", Pattern.compile("foo")));
    }

    @Test
    public void testParse_tagAndClassSelector() {
        QueryParser parser = new QueryParser("div.class");
        assertEquals(parser.parse(), new And(new Evaluator.Tag("div"), new Evaluator.Class("class")));
    }

    @Test
    public void testParse_tagAndIdSelector() {
        QueryParser parser = new QueryParser("div#id");
        assertEquals(parser.parse(), new And(new Evaluator.Tag("div"), new Evaluator.Id("id")));
    }

    @Test
    public void testParse_tagAndAttributeSelector() {
        QueryParser parser = new QueryParser("div[title]");
        assertEquals(parser.parse(), new And(new Evaluator.Tag("div"), new Evaluator.Attribute("title")));
    }

    @Test
    public void testParse_multipleAttributeSelectors() {
        QueryParser parser = new QueryParser("[title='foo'][class='bar']");
        And expected = new And(new And(new Evaluator.AttributeWithValue("title", "foo"), new Evaluator.Class("bar")), new Evaluator.Tag("*"));
        assertEquals(parser.parse(), expected);
    }

    @Test
    public void testParse_combinator_descendant() {
        QueryParser parser = new QueryParser("div p");
        assertEquals(parser.parse(), new StructuralEvaluator.Descendant(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
    }

    @Test
    public void testParse_combinator_child() {
        QueryParser parser = new QueryParser("div > p");
        assertEquals(parser.parse(), new StructuralEvaluator.Child(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
    }

    @Test
    public void testParse_combinator_adjacentSibling() {
        QueryParser parser = new QueryParser("div + p");
        assertEquals(parser.parse(), new StructuralEvaluator.AdjacentSibling(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
    }

    @Test
    public void testParse_combinator_generalSibling() {
        QueryParser parser = new QueryParser("div ~ p");
        assertEquals(parser.parse(), new StructuralEvaluator.GeneralSibling(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
    }

    @Test
    public void testParse_combinator_or() {
        QueryParser parser = new QueryParser("div, p");
        assertEquals(parser.parse(), new Or(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
    }

    @Test
    public void testParse_combinator_and() {
        QueryParser parser = new QueryParser("div > p.class");
        StructuralEvaluator.Descendant descendant = new StructuralEvaluator.Descendant(new Evaluator.Tag("div"), new Evaluator.Tag("p"));
        StructuralEvaluator.Class classEvaluator = new StructuralEvaluator.Class("class");
        assertEquals(parser.parse(), new And(descendant, classEvaluator));
    }

    @Test
    public void testParse_pseudoClass_firstChild() {
        QueryParser parser = new QueryParser(":first-child");
        assertEquals(parser.parse(), new IsFirstChild());
    }

    @Test
    public void testParse_pseudoClass_lastChild() {
        QueryParser parser = new QueryParser(":last-child");
        assertEquals(parser.parse(), new IsLastChild());
    }

    @Test
    public void testParse_pseudoClass_firstOfType() {
        QueryParser parser = new QueryParser(":first-of-type");
        assertEquals(parser.parse(), new IsFirstOfType());
    }

    @Test
    public void testParse_pseudoClass_lastOfType() {
        QueryParser parser = new QueryParser(":last-of-type");
        assertEquals(parser.parse(), new IsLastOfType());
    }

    @Test
    public void testParse_pseudoClass_onlyChild() {
        QueryParser parser = new QueryParser(":only-child");
        assertEquals(parser.parse(), new IsOnlyChild());
    }

    @Test
    public void testParse_pseudoClass_onlyOfType() {
        QueryParser parser = new QueryParser(":only-of-type");
        assertEquals(parser.parse(), new IsOnlyOfType());
    }

    @Test
    public void testParse_pseudoClass_empty() {
        QueryParser parser = new QueryParser(":empty");
        assertEquals(parser.parse(), new IsEmpty());
    }

    @Test
    public void testParse_pseudoClass_root() {
        QueryParser parser = new QueryParser(":root");
        assertEquals(parser.parse(), new IsRoot());
    }

    @Test
    public void testParse_pseudoClass_not() {
        QueryParser parser = new QueryParser(":not(p)");
        assertEquals(parser.parse(), new StructuralEvaluator.Not(new Evaluator.Tag("p")));
    }

    @Test
    public void testParse_pseudoClass_has() {
        QueryParser parser = new QueryParser(":has(p)");
        assertEquals(parser.parse(), new Has(new Evaluator.Tag("p")));
    }

    @Test
    public void testParse_pseudoClass_nthChild() {
        QueryParser parser = new QueryParser(":nth-child(2)");
        assertEquals(parser.parse(), new IsNthChild(1, 2));
    }

    @Test
    public void testParse_pseudoClass_nthLastChild() {
        QueryParser parser = new QueryParser(":nth-last-child(2)");
        assertEquals(parser.parse(), new IsNthLastChild(1, 2));
    }

    @Test
    public void testParse_pseudoClass_nthOfType() {
        QueryParser parser = new QueryParser(":nth-of-type(2)");
        assertEquals(parser.parse(), new IsNthOfType(1, 2));
    }

    @Test
    public void testParse_pseudoClass_nthLastOfType() {
        QueryParser parser = new QueryParser(":nth-last-of-type(2)");
        assertEquals(parser.parse(), new IsNthLastOfType(1, 2));
    }

    @Test
    public void testParse_pseudoClass_lt() {
        QueryParser parser = new QueryParser(":lt(3)");
        assertEquals(parser.parse(), new Evaluator.IndexLessThan(3));
    }

    @Test
    public void testParse_pseudoClass_gt() {
        QueryParser parser = new QueryParser(":gt(3)");
        assertEquals(parser.parse(), new Evaluator.IndexGreaterThan(3));
    }

    @Test
    public void testParse_pseudoClass_eq() {
        QueryParser parser = new QueryParser(":eq(3)");
        assertEquals(parser.parse(), new Evaluator.IndexEquals(3));
    }

    @Test
    public void testParse_pseudoClass_contains() {
        QueryParser parser = new QueryParser(":contains(foo)");
        assertEquals(parser.parse(), new Evaluator.ContainsText("foo"));
    }

    @Test
    public void testParse_pseudoClass_containsOwn() {
        QueryParser parser = new QueryParser(":containsOwn(foo)");
        assertEquals(parser.parse(), new Evaluator.ContainsOwnText("foo"));
    }

    @Test
    public void testParse_pseudoClass_matches() {
        QueryParser parser = new QueryParser(":matches(foo)");
        assertEquals(parser.parse(), new Evaluator.Matches(Pattern.compile("foo")));
    }

    @Test
    public void testParse_pseudoClass_matchesOwn() {
        QueryParser parser = new QueryParser(":matchesOwn(foo)");
        assertEquals(parser.parse(), new Evaluator.MatchesOwn(Pattern.compile("foo")));
    }

    @Test
    public void testParse_pseudoClass_data() {
        QueryParser parser = new QueryParser(":containsData(foo)");
        assertEquals(parser.parse(), new Evaluator.ContainsData("foo"));
    }
}