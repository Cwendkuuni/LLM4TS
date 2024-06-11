import org.jsoup.select.*;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        try {
            QueryParser p = new QueryParser("div > p");
            Evaluator result = p.parse();
            assertNotNull("Result Evaluator is not null", result);
        } catch (Exception e) {
            fail("Exception thrown while parsing query");
        }
    }

    @Test
    public void testParseWithEmptyQuery() {
        try {
            QueryParser p = new QueryParser("");
            Evaluator result = p.parse();
            fail("No exception thrown for empty query");
        } catch (Selector.SelectorParseException e) {
            assertTrue("Exception message contains 'Could not parse query'", e.getMessage().contains("Could not parse query"));
        }
    }

    @Test
    public void testParseWithInvalidCombinator() {
        try {
            QueryParser p = new QueryParser("div $ p");
            Evaluator result = p.parse();
            fail("No exception thrown for invalid combinator");
        } catch (Selector.SelectorParseException e) {
            assertTrue("Exception message contains 'Unknown combinator'", e.getMessage().contains("Unknown combinator"));
        }
    }

    @Test
    public void testParseWithInvalidAttributeQuery() {
        try {
            QueryParser p = new QueryParser("div [class^=]");
            Evaluator result = p.parse();
            fail("No exception thrown for invalid attribute query");
        } catch (Selector.SelectorParseException e) {
            assertTrue("Exception message contains 'Could not parse attribute query'", e.getMessage().contains("Could not parse attribute query"));
        }
    }

    @Test
    public void testParseWithInvalidNthIndex() {
        try {
            QueryParser p = new QueryParser("div:nth-child(a)");
            Evaluator result = p.parse();
            fail("No exception thrown for invalid nth-index");
        } catch (Selector.SelectorParseException e) {
            assertTrue("Exception message contains 'Could not parse nth-index'", e.getMessage().contains("Could not parse nth-index"));
        }
    }

    @Test
    public void testById() {
        QueryParser p = new QueryParser("#id");
        p.byId();
        Evaluator result = p.evals.get(0);
        assertTrue("Evaluator is Evaluator.Id", result instanceof Evaluator.Id);
    }

    @Test
    public void testByClass() {
        QueryParser p = new QueryParser(".classname");
        p.byClass();
        Evaluator result = p.evals.get(0);
        assertTrue("Evaluator is Evaluator.Class", result instanceof Evaluator.Class);
    }

    @Test
    public void testByTag() {
        QueryParser p = new QueryParser("a");
        p.byTag();
        Evaluator result = p.evals.get(0);
        assertTrue("Evaluator is Evaluator.Tag", result instanceof Evaluator.Tag);
    }

    @Test
    public void testByAttribute() {
        QueryParser p = new QueryParser("[key=value]");
        p.byAttribute();
        Evaluator result = p.evals.get(0);
        assertTrue("Evaluator is Evaluator.AttributeWithValue", result instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testAllElements() {
        QueryParser p = new QueryParser("*");
        p.allElements();
        Evaluator result = p.evals.get(0);
        assertTrue("Evaluator is Evaluator.AllElements", result instanceof Evaluator.AllElements);
    }

}