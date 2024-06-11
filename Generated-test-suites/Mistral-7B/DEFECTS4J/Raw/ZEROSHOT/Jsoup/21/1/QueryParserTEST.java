import static org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @org.junit.Test
    public void testParse() {
        // Test parsing a simple CSS query
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new Evaluator.Tag("div"));

        assertEquals(expectedEvaluators, evaluator.getChildren());

        // Test parsing a query with combinators
        query = "div > p";
        evaluator = QueryParser.parse(query);
        expectedEvaluators.clear();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
        expectedEvaluators.add(new Evaluator.Tag("p"));

        assertEquals(expectedEvaluators, evaluator.getChildren());

        // Test parsing a query with id and class
        query = "#id1.class1.class2";
        evaluator = QueryParser.parse(query);
        expectedEvaluators.clear();
        expectedEvaluators.add(new Evaluator.Id("id1"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new Evaluator.Class("class2"));

        assertEquals(expectedEvaluators, evaluator.getChildren());

        // Test parsing a query with multiple selectors
        query = "div, p, #id2.class3";
        evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluatorsMulti = new ArrayList<>();
        expectedEvaluatorsMulti.add(new Evaluator.Tag("div"));
        expectedEvaluatorsMulti.add(new Evaluator.Tag("p"));
        expectedEvaluatorsMulti.add(new Evaluator.Id("id2"));
        expectedEvaluatorsMulti.add(new Evaluator.Class("class3"));

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(expectedEvaluatorsMulti.subList(0, 2));
        andEvaluator.add(new CombiningEvaluator.Or(expectedEvaluatorsMulti.subList(2, expectedEvaluatorsMulti.size())));

        assertEquals(andEvaluator, evaluator);

        // Test parsing a query with attribute selectors
        query = "input[type='text']";
        evaluator = QueryParser.parse(query);
        expectedEvaluators.clear();
        expectedEvaluators.add(new Evaluator.Tag("input"));
        expectedEvaluators.add(new Evaluator.AttributeWithValue("type", "text"));

        assertEquals(expectedEvaluators, evaluator.getChildren());

        // Test parsing a query with pseudo-classes
        query = "a:link, a:visited";
        evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluatorsPseudo = new ArrayList<>();
        expectedEvaluatorsPseudo.add(new Evaluator.Tag("a"));
        expectedEvaluatorsPseudo.add(new StructuralEvaluator.PseudoClass("link"));

        evaluator = QueryParser.parse(query);
        evaluator = ((CombiningEvaluator.Or) evaluator.getChildren().get(0)).getChildren().get(0);

        assertEquals(expectedEvaluatorsPseudo, evaluator.getChildren());

        expectedEvaluatorsPseudo.clear();
        expectedEvaluatorsPseudo.add(new Evaluator.Tag("a"));
        expectedEvaluatorsPseudo.add(new StructuralEvaluator.PseudoClass("visited"));

        evaluator = ((CombiningEvaluator.Or) evaluator.getChildren().get(1)).getChildren().get(0);

        assertEquals(expectedEvaluatorsPseudo, evaluator.getChildren());

        // Test parsing a query with multiple pseudo-classes
        query = "li:first-child:last-child";
        evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluatorsPseudoMulti = new ArrayList<>();
        expectedEvaluatorsPseudoMulti.add(new Evaluator.Tag("li"));
        expectedEvaluatorsPseudoMulti.add(new StructuralEvaluator.FirstChild());
        expectedEvaluatorsPseudoMulti.add(new StructuralEvaluator.LastChild());

        evaluator = ((CombiningEvaluator.And) evaluator.getChildren().get(0)).getChildren().get(0);

        assertEquals(expectedEvaluatorsPseudoMulti.get(0), evaluator);
        assertEquals(expectedEvaluatorsPseudoMulti.get(1), evaluator.getChildren().get(0));
        assertEquals(expectedEvaluatorsPseudoMulti.get(2), evaluator.getChildren().get(1));
    }

    @org.junit.Test
    public void testParseInvalidQuery() {
        String query = "div div"; // This query is not valid because it creates an infinite loop

        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected a SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.Empty);
    }
}