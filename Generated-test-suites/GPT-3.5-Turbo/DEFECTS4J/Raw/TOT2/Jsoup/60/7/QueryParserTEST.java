import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        QueryParser parser = new QueryParser("div#main > p");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithCombinators() {
        QueryParser parser = new QueryParser("div > p, div h1");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithInvalidQuery() {
        QueryParser parser = new QueryParser(".class1 .class2");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithSpecialCharQuery() {
        QueryParser parser = new QueryParser(".class[name=sample]");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithNthChild() {
        QueryParser parser = new QueryParser(":nth-child(3n+1)");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithMatches() {
        QueryParser parser = new QueryParser(":matchesOwn(^regex$)");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithNotSelector() {
        QueryParser parser = new QueryParser(":not(.hidden)");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

}