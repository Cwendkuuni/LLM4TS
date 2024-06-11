package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div.test");
        Assert.assertTrue(evaluator instanceof Evaluator.And);
    }

    @Test
    public void testParse_combinator() {
        Evaluator evaluator = QueryParser.parse("div.test > span");
        Assert.assertTrue(evaluator instanceof Evaluator.And);
        Assert.assertTrue(((Evaluator.And) evaluator).evals.get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(((Evaluator.And) evaluator).evals.get(1) instanceof StructuralEvaluator.ImmediateParent);
    }

    @Test
    public void testParse_multipleCombinators() {
        Evaluator evaluator = QueryParser.parse("div.test > span + em");
        Assert.assertTrue(evaluator instanceof Evaluator.And);

        Evaluator.And and = (Evaluator.And) evaluator;
        Assert.assertTrue(and.evals.get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(and.evals.get(1) instanceof StructuralEvaluator.ImmediateParent);
        Assert.assertTrue(and.evals.get(2) instanceof StructuralEvaluator.ImmediatePreviousSibling);
        Assert.assertTrue(and.evals.get(3) instanceof Evaluator.Tag);
    }

    @Test
    public void testParse_multipleSelectors() {
        Evaluator evaluator = QueryParser.parse("div.test, span.example");
        Assert.assertTrue(evaluator instanceof Evaluator.Or);

        Evaluator.Or or = (Evaluator.Or) evaluator;
        Assert.assertEquals(2, or.evals.size());
        Assert.assertTrue(or.evals.get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(or.evals.get(1) instanceof Evaluator.Tag);
    }

    @Test
    public void testParse_attributeSelectors() {
        Evaluator evaluator = QueryParser.parse("div[name=value]");
        Assert.assertTrue(evaluator instanceof Evaluator.And);

        Evaluator.And and = (Evaluator.And) evaluator;
        Assert.assertTrue(and.evals.get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(and.evals.get(1) instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testParse_pseudoSelectors() {
        Evaluator evaluator = QueryParser.parse("div:first-child");
        Assert.assertTrue(evaluator instanceof Evaluator.And);

        Evaluator.And and = (Evaluator.And) evaluator;
        Assert.assertTrue(and.evals.get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(and.evals.get(1) instanceof StructuralEvaluator.FirstChild);
    }
}