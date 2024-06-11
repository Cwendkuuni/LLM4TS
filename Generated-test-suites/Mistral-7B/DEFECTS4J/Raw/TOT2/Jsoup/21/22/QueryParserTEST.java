import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
   @Test
   public void testParse() {
      String query = "div.class1 > div.class2";
      QueryParser parser = new QueryParser(query);
      Evaluator evaluator = parser.parse();

      // Basic functionality check
      assertNotNull(evaluator);

      // Edge cases and error scenarios
      try {
         String invalidQuery = "div class1 > div.class2"; // missing whitespace
         QueryParser invalidParser = new QueryParser(invalidQuery);
         invalidParser.parse(); // should throw an exception
         fail("Expected Selector.SelectorParseException but got none");
      } catch (Selector.SelectorParseException e) {
         // Expected exception
      }

      String emptyQuery = "";
      QueryParser emptyParser = new QueryParser(emptyQuery);
      Evaluator emptyEvaluator = emptyParser.parse(); // should return an empty evaluator
      assertTrue(emptyEvaluator instanceof Evaluator.AllElements);
   }
}