package org.jsoup.select;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
  
  @Test
  public void testParse() {
    String query = "div";
    Evaluator evaluator = QueryParser.parse(query);
    assertNotNull(evaluator);
    
    query = "#myId";
    evaluator = QueryParser.parse(query);
    assertNotNull(evaluator);
    
    query = ".myClass";
    evaluator = QueryParser.parse(query);
    assertNotNull(evaluator);
    
    query = "[name=value]";
    evaluator = QueryParser.parse(query);
    assertNotNull(evaluator);
    
    query = ":contains(text)";
    evaluator = QueryParser.parse(query);
    assertNotNull(evaluator);
    
    query = ":has(element)";
    evaluator = QueryParser.parse(query);
    assertNotNull(evaluator);
    
    query = ":matches(regex)";
    evaluator = QueryParser.parse(query);
    assertNotNull(evaluator);
    
    query = ":empty";
    evaluator = QueryParser.parse(query);
    assertNotNull(evaluator);
  }
  
  @Test(expected = Selector.SelectorParseException.class)
  public void testParseInvalidQuery() {
    String query = "[name";
    QueryParser.parse(query);
  }
}