import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;

class QueryParserTest {
    @Mock
    private TokenQueue mockTq;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testParse() {
        String query = "div.test-class";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Class);
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div.test-class");
        parser.combinator('>');
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof CombiningEvaluator.And);
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("div.test-class");
        String subQuery = parser.consumeSubQuery();
        assertNotNull(subQuery);
        assertTrue(subQuery.isEmpty());
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("div.test-class");
        parser.findElements();
        assertEquals(2, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.Class);
        assertTrue(parser.evals.get(1) instanceof Evaluator.Tag);
    }

    @Test
    public void testFindElementsById() {
        QueryParser parser = new QueryParser("#test-id");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.Id);
    }

    @Test
    public void testFindElementsByClass() {
        QueryParser parser = new QueryParser(".test-class");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.Class);
    }

    @Test
    public void testFindElementsByTag() {
        QueryParser parser = new QueryParser("div");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.Tag);
    }

    @Test
    public void testFindByAttribute() {
        QueryParser parser = new QueryParser("[name=value]");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testFindByAttributeStarting() {
        QueryParser parser = new QueryParser("[name^=value]");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.AttributeWithValueStarting);
    }

    @Test
    public void testFindByAttributeEnding() {
        QueryParser parser = new QueryParser("[name$=value]");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.AttributeWithValueEnding);
    }

    @Test
    public void testFindByAttributeContaining() {
        QueryParser parser = new QueryParser("[name*=value]");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.AttributeWithValueContaining);
    }

    @Test
    public void testFindByAttributeMatching() {
        QueryParser parser = new QueryParser("[name~=value]");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.AttributeWithValueMatching);
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("*");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.AllElements);
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser(":lt(5)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.IndexLessThan);
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser(":gt(5)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.IndexGreaterThan);
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser(":eq(5)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.IndexEquals);
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser(":has(div)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.ContainsText);
    }

    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser(":containsOwn(text)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.ContainsOwnText);
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.Matches);
    }

    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser(":matchesOwn(regex)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof Evaluator.MatchesOwn);
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(div)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
        assertTrue(parser.evals.get(0) instanceof StructuralEvaluator.Not);
    }
}