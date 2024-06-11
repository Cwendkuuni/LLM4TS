import org.jsoup.select.*;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.testClass > p#paragraph[id=1]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_InvalidQuery() {
        String query = "div.testClass p#paragraph[id=1]";
        try {
            Evaluator result = QueryParser.parse(query);
            fail("Expected Selector.SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_MultipleAttributes() {
        String query = "div.testClass[id=1][name=example]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_CommaCombinator() {
        String query = "div.testClass, p#paragraph";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_ChildCombinator() {
        String query = "div.testClass > p#paragraph";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_SpaceCombinator() {
        String query = "div.testClass p#paragraph";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_AdditionalCombinators() {
        String query = "div.testClass + p#paragraph";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);

        query = "div.testClass ~ p#paragraph";
        result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_MultipleCombos() {
        String query = "div.testClass > p#paragraph, div.exampleClass + p#paragraph";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_MultipleSelectors() {
        String query = "div.testClass, p#paragraph, span.exampleClass";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_ContainsSelector() {
        String query = "div.testClass :contains(\"example\")";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_AttributeSelector() {
        String query = "div.testClass[id=1]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_MatchesSelector() {
        String query = "div.testClass :matches(\\d+)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }
    
    @Test
    public void testParse_NthChildSelector() {
        String query = "div.testClass :nth-child(2)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }
}
