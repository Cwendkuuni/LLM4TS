package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class1.class2 > p#id1 > ul > li:eq(0)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div.class1.class2 > p#id1 > ul > li:eq(0):not(div)"; // missing combinator before :not
    }

    @Test
    public void testCombineElements() {
        String query = "div.class1 > p#id1 > ul > li:eq(0)";
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Class("class1"));
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.Id("id1"));
        evaluators.add(new Evaluator.Index("0"));

        Evaluator rootEvaluator = new CombiningEvaluator.And(evaluators);
        Evaluator parsedEvaluator = QueryParser.parse(query);

        assertEquals(rootEvaluator, parsedEvaluator);
    }

    @Test
    public void testCombineCombinators() {
        String query = "div > p + ul > li:eq(0)";
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Tag("p")));
        evaluators.add(new Evaluator.Tag("ul"));
        evaluators.add(new Evaluator.Tag("li"));
        evaluators.add(new Evaluator.Index("0"));

        Evaluator rootEvaluator = new CombiningEvaluator.And(evaluators);
        Evaluator parsedEvaluator = QueryParser.parse(query);

        assertEquals(rootEvaluator, parsedEvaluator);
    }

    @Test
    public void testCombineAttributes() {
        String query = "div[class='class1' and id='id1']";
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.AttributeWithValue("class", "class1"));
        evaluators.add(new Evaluator.AttributeWithValue("id", "id1"));

        Evaluator rootEvaluator = new StructuralEvaluator.And(evaluators);
        Evaluator parsedEvaluator = QueryParser.parse(query);

        assertEquals(rootEvaluator, parsedEvaluator);
    }

    @Test
    public void testCombinePseudoSelectors() {
        String query = "div:not(.class1) > p";
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new StructuralEvaluator.Not(new Evaluator.Class("class1")));
        evaluators.add(new Evaluator.Tag("p"));

        Evaluator rootEvaluator = new CombiningEvaluator.And(evaluators);
        Evaluator parsedEvaluator = QueryParser.parse(query);

        assertEquals(rootEvaluator, parsedEvaluator);
    }
}