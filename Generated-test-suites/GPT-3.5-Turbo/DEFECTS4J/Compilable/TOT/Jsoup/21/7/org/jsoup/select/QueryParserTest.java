package org.jsoup.select;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse_validQuery_returnsValidEvaluator() {
        // Test if the parse method returns a valid evaluator for a valid query
        String validQuery = "div.test";
        Evaluator evaluator = QueryParser.parse(validQuery);

        assertNotNull(evaluator);
    }

    @Test
    public void testParse_emptyQuery_throwsException() {
        // Test if the parse method throws an exception when an empty query is given
        String emptyQuery = "";
        
        try {
            QueryParser.parse(emptyQuery);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query '', unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void testParse_invalidCombinator_throwsException() {
        // Test if the parse method throws an exception when an invalid combinator is given
        String invalidQuery = "div > p";

        try {
            QueryParser.parse(invalidQuery);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: >", e.getMessage());
        }
    }

    @Test
    public void testParse_validCombinator_returnsValidEvaluator() {
        // Test if the parse method returns a valid evaluator for a valid combinator
        String validQuery = "div + p";
        Evaluator evaluator = QueryParser.parse(validQuery);

        assertNotNull(evaluator);
    }

    @Test
    public void testParse_attributeQuery_returnsValidEvaluator() {
        // Test if the parse method returns a valid evaluator for an attribute query
        String attributeQuery = "[class='test']";
        Evaluator evaluator = QueryParser.parse(attributeQuery);

        assertNotNull(evaluator);
    }

    @Test
    public void testParse_invalidAttributeQuery_throwsException() {
        // Test if the parse method throws an exception when an invalid attribute query is given
        String invalidAttributeQuery = "[class!=]";

        try {
            QueryParser.parse(invalidAttributeQuery);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse attribute query '[class!=]': unexpected token at ']", e.getMessage());
        }
    }

    @Test
    public void testParse_pseudoSelector_returnsValidEvaluator() {
        // Test if the parse method returns a valid evaluator for a pseudo selector
        String pseudoSelector = ":has(div)";
        Evaluator evaluator = QueryParser.parse(pseudoSelector);

        assertNotNull(evaluator);
    }

    @Test
    public void testParse_invalidPseudoSelector_throwsException() {
        // Test if the parse method throws an exception when an invalid pseudo selector is given
        String invalidPseudoSelector = ":invalid";

        try {
            QueryParser.parse(invalidPseudoSelector);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query ':invalid': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void testParse_containsSelector_returnsValidEvaluator() {
        // Test if the parse method returns a valid evaluator for a contains selector
        String containsSelector = ":contains(text)";
        Evaluator evaluator = QueryParser.parse(containsSelector);

        assertNotNull(evaluator);
    }

    @Test
    public void testParse_invalidContainsSelector_throwsException() {
        // Test if the parse method throws an exception when an invalid contains selector is given
        String invalidContainsSelector = ":contains";

        try {
            QueryParser.parse(invalidContainsSelector);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query ':contains': unexpected token at ''", e.getMessage());
        }
    }

}