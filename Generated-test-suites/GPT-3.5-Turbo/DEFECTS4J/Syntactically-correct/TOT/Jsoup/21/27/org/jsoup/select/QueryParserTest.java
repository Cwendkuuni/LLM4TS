import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    // Test case for private constructor
    @Test
    public void testPrivateConstructor() throws Exception {
        // Trying to create an instance of QueryParser using reflection should throw an exception
        try {
            Constructor<QueryParser> constructor = QueryParser.class.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            QueryParser queryParser = constructor.newInstance("test");
            fail("Expected Exception");
        } catch (Exception e) {
            assertEquals("java.lang.IllegalAccessException", e.toString());
        }
    }

    // Test case for static method parse()
    @Test
    public void parseTest() {
        Evaluator evaluator = QueryParser.parse("#id.test[pattern^=abc].foo");
        assertEquals("Evaluator.Id, Evaluator.Class, Evaluator.AttributeStarting, Evaluator.Tag", evaluator.getClass().getName());
    }

    // Test case for private method combinator()
    @Test
    public void combinatorTest() {
        QueryParser queryParser = new QueryParser("div");
        queryParser.combinator(' ');
        assertEquals("Evaluator.Tag, Evaluator.And, StructuralEvaluator.Parent", queryParser.evals.getClass().getName());
    }

    // Test case for private method consumeSubQuery()
    @Test
    public void consumeSubQueryTest() {
        QueryParser queryParser = new QueryParser("[attr1=val1][attr2=val2]");
        String subQuery = queryParser.consumeSubQuery();
        assertEquals("[attr1=val1][attr2=val2]", subQuery);
    }

    // Test case for private method findElements()
    @Test
    public void findElementsTest() {
        QueryParser queryParser = new QueryParser("#id");
        queryParser.findElements();
        assertEquals("Evaluator.Id", queryParser.evals.getClass().getName());
    }

    // Test case for private method byId()
    @Test
    public void byIdTest() {
        QueryParser queryParser = new QueryParser("#id");
        queryParser.byId();
        assertEquals("Evaluator.Id", queryParser.evals.getClass().getName());
    }

    // Test case for private method byClass()
    @Test
    public void byClassTest() {
        QueryParser queryParser = new QueryParser(".class");
        queryParser.byClass();
        assertEquals("Evaluator.Class", queryParser.evals.getClass().getName());
    }

    // Test case for private method byTag()
    @Test
    public void byTagTest() {
        QueryParser queryParser = new QueryParser("div");
        queryParser.byTag();
        assertEquals("Evaluator.Tag", queryParser.evals.getClass().getName());
    }

    // Test case for private method byAttribute()
    @Test
    public void byAttributeTest() {
        QueryParser queryParser = new QueryParser("[attr=val]");
        queryParser.byAttribute();
        assertEquals("Evaluator.AttributeWithValue", evals.getClass().getName());
    }

    // Test case for private method allElements()
    @Test
    public void allElementsTest() {
        QueryParser queryParser = new QueryParser("*");
        queryParser.allElements();
        assertEquals("Evaluator.AllElements", queryParser.evals.getClass().getName());
    }

    // Test case for private method indexLessThan()
    @Test
    public void indexLessThanTest() {
        QueryParser queryParser = new QueryParser(":lt(3)");
        queryParser.indexLessThan();
        assertEquals("Evaluator.IndexLessThan", queryParser.evals.getClass().getName());
    }

    // Test case for private method indexGreaterThan()
    @Test
    public void indexGreaterThanTest() {
        QueryParser queryParser = new QueryParser(":gt(2)");
        queryParser.indexGreaterThan();
        assertEquals("Evaluator.IndexGreaterThan", queryParser.evals.getClass().getName());
    }

    // Test case for private method indexEquals()
    @Test
    public void indexEqualsTest() {
        QueryParser queryParser = new QueryParser(":eq(4)");
        queryParser.indexEquals();
        assertEquals("Evaluator.IndexEquals", queryParser.evals.getClass().getName());
    }

    // Test case for private method has()
    @Test
    public void hasTest() {
        QueryParser queryParser = new QueryParser(":has(div)");
        queryParser.has();
        assertEquals("StructuralEvaluator.Has", queryParser.evals.getClass().getName());
    }

    // Test case for private method contains()
    @Test
    public void containsTest() {
        QueryParser queryParser = new QueryParser(":contains(text)");
        queryParser.contains(false);
        assertEquals("Evaluator.ContainsText", queryParser.evals.getClass().getName());
    }

    // Test case for private method containsOwn()
    @Test
    public void containsOwnTest() {
        QueryParser queryParser = new QueryParser(":containsOwn(text)");
        queryParser.contains(true);
        assertEquals("Evaluator.ContainsOwnText", queryParser.evals.getClass().getName());
    }

    // Test case for private method matches()
    @Test
    public void matchesTest() {
        QueryParser queryParser = new QueryParser(":matches(regex)");
        queryParser.matches(false);
        assertEquals("Evaluator.Matches", queryParser.evals.getClass().getName());
    }

    // Test case for private method matchesOwn()
    @Test
    public void matchesOwnTest() {
        QueryParser queryParser = new QueryParser(":matchesOwn(regex)");
        queryParser.matches(true);
        assertEquals("Evaluator.MatchesOwn", queryParser.evals.getClass().getName());
    }

    // Test case for private method not()
    @Test
    public void notTest() {
        QueryParser queryParser = new QueryParser(":not(div)");
        queryParser.not();
        assertEquals("StructuralEvaluator.Not", queryParser.evals.getClass().getName());
    }

}