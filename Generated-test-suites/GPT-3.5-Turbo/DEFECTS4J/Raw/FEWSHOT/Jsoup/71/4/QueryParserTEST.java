import org.junit.Test;
import org.jsoup.select.*;

import static org.junit.Assert.*;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);

        query = "#id";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);

        query = ".class";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);

        query = "[attr]";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);

        query = ":contains(text)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.ContainsText);

        query = ":containsOwn(text)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);

        query = ":containsData(data)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.ContainsData);

        query = ":matches(regex)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Matches);

        query = ":matchesOwn(regex)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);

        query = ":not(selector)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);

        query = "div, #id";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
    }
}