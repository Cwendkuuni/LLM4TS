import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
  
  @Test
  public void testParse() {
    // Test case 1
    String query1 = "div#id";
    Evaluator result1 = QueryParser.parse(query1);
    assertNotNull(result1);
    
    // Test case 2
    String query2 = "div.class";
    Evaluator result2 = QueryParser.parse(query2);
    assertNotNull(result2);
    
    // Test case 3
    String query3 = "div[attr=value]";
    Evaluator result3 = QueryParser.parse(query3);
    assertNotNull(result3);
    
    // Test case 4
    String query4 = "div > span";
    Evaluator result4 = QueryParser.parse(query4);
    assertNotNull(result4);
    
    // Test case 5
    String query5 = "div + span";
    Evaluator result5 = QueryParser.parse(query5);
    assertNotNull(result5);
    
    // Test case 6
    String query6 = "div ~ span";
    Evaluator result6 = QueryParser.parse(query6);
    assertNotNull(result6);
    
    // Test case 7
    String query7 = "div, span";
    Evaluator result7 = QueryParser.parse(query7);
    assertNotNull(result7);
  }
}