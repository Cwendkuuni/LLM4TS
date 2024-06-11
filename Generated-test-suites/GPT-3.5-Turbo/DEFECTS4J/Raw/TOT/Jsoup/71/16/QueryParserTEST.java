import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Selector;
import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Typical use cases
        Evaluator evaluator1 = QueryParser.parse("div");
        assertEquals("Evaluator should be of type Evaluator.Tag", Evaluator.Tag.class, evaluator1.getClass());
        
        Evaluator evaluator2 = QueryParser.parse("#header");
        assertEquals("Evaluator should be of type Evaluator.Id", Evaluator.Id.class, evaluator2.getClass());
        
        Evaluator evaluator3 = QueryParser.parse("ul > li");
        assertEquals("Evaluator should be of type CombiningEvaluator.And", CombiningEvaluator.And.class, evaluator3.getClass());

        // Edge cases
        Evaluator evaluator4 = QueryParser.parse("div #header");
        assertEquals("Evaluator should be of type CombiningEvaluator.And", CombiningEvaluator.And.class, evaluator4.getClass());

        Evaluator evaluator5 = QueryParser.parse("div[id = 'header']");
        assertEquals("Evaluator should be of type Evaluator.AttributeWithValue", Evaluator.AttributeWithValue.class, evaluator5.getClass());

        Evaluator evaluator6 = QueryParser.parse("div + p");
        assertEquals("Evaluator should be of type CombiningEvaluator.And", CombiningEvaluator.And.class, evaluator6.getClass());
        
        Evaluator evaluator7 = QueryParser.parse("[data-attr]");
        assertEquals("Evaluator should be of type Evaluator.Attribute", Evaluator.Attribute.class, evaluator7.getClass());

        // Error scenarios
        boolean exceptionThrown = false;
        try {
            QueryParser.parse("");
        } catch (Selector.SelectorParseException e) {
            exceptionThrown = true;
        }
        assertTrue("Exception should be thrown for empty query", exceptionThrown);

        exceptionThrown = false;
        try {
            QueryParser.parse("div#header");
        } catch (Selector.SelectorParseException e) {
            exceptionThrown = true;
        }
        assertTrue("Exception should be thrown for invalid query", exceptionThrown);
    }

}