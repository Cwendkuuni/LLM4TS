
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class QueryParserTest {

    private QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("");
    }

    @Test
    public void testParse() {
        Evaluator evaluator = queryParser.parse("");
        assertEquals(new Evaluator.AllElements(), evaluator);
    }

    @Test
    public void testCombinator() {
        queryParser.combinator('>');
        assertEquals(new CombiningEvaluator.And(new Evaluator.Tag("")), queryParser.evals.get(0));
    }

    @Test
    public void testConsumeSubQuery() {
        String subQuery = queryParser.consumeSubQuery();
        assertEquals("", subQuery);
    }

    @Test
    public void testFindElements() {
        queryParser.findElements();
        assertEquals(0, queryParser.evals.size());
    }

    @Test
    public void testById() {
        queryParser.byId();
        assertEquals(new Evaluator.Id(""), queryParser.evals.get(0));
    }

    @Test
    public void testByClass() {
        queryParser.byClass();
        assertEquals(new Evaluator.Class(""), queryParser.evals.get(0));
    }

    @Test
    public void testByTag() {
        queryParser.byTag();
        assertEquals(new Evaluator.Tag(""), queryParser.evals.get(0));
    }

    @Test
    public void testByAttribute() {
        queryParser.byAttribute();
        assertEquals(new Evaluator.Attribute(""), queryParser.evals.get(0));
    }

    @Test
    public void testAllElements() {
        queryParser.allElements();
        assertEquals(new Evaluator.AllElements(), queryParser.evals.get(0));
    }

    @Test
    public void testIndexLessThan() {
        queryParser.indexLessThan();
        assertEquals(new Evaluator.IndexLessThan(0), queryParser.evals.get(0));
    }

    @Test
    public void testIndexGreaterThan() {
        queryParser.indexGreaterThan();
        assertEquals(new Evaluator.IndexGreaterThan(0), queryParser.evals.get(0));
    }

    @Test
    public void testIndexEquals() {
        queryParser.indexEquals();
        assertEquals(new Evaluator.IndexEquals(0), queryParser.evals.get(0));
    }

    @Test
    public void testHas() {
        queryParser.has();
        assertEquals(new StructuralEvaluator.Has(new Evaluator.AllElements()), queryParser.evals.get(0));
    }

    @Test
    public void testContains() {
        queryParser.contains(true);
        assertEquals(new Evaluator.ContainsOwnText(""), queryParser.evals.get(0));
    }

    @Test
    public void testMatches() {
        queryParser.matches(true);
        assertEquals(new Evaluator.MatchesOwn(Pattern.compile("")), queryParser.evals.get(0));
    }

    @Test
    public void testNot() {
        queryParser.not();
        assertEquals(new StructuralEvaluator.Not(new Evaluator.AllElements()), queryParser.evals.get(0));
    }
}
