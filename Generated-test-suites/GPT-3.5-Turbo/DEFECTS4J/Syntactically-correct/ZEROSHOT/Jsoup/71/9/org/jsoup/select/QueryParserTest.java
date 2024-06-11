import org.junit.Test;
import org.junit.Assert.*;

import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import static org.junit.Assert.assertEquals;

public class QueryParserTest {

  @Test
  public void testParse() {
    Evaluator eval = QueryParser.parse("input[name~=foo]");
    assertEquals("Evaluator.AttributeWithValueMatching", eval.getClass().getSimpleName());

    eval = QueryParser.parse("input#foo");
    assertEquals("Evaluator.Id", eval.getClass().getSimpleName());
    
    // Add more test cases for other methods in QueryParser class
  }

}