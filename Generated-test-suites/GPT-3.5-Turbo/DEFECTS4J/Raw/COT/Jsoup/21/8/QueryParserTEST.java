
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import org.junit.Assert;

public class QueryParserTest {
  
  // Test parse method with a basic CSS query
  @Test
  public void testParse() {
    String query = "div > p";
    Evaluator evaluator = QueryParser.parse(query);
    Assert.assertNotNull(evaluator);
  }

  // Test parse method with a CSS query starting with a combinator
  @Test
  public void testParseStartingWithCombinator() {
    String query = "> div";
    Evaluator evaluator = QueryParser.parse(query);
    Assert.assertNotNull(evaluator);
  }

  // Test parse method with a CSS query containing multiple combinators
  @Test
  public void testParseMultipleCombinators() {
    String query = "div > p + span";
    Evaluator evaluator = QueryParser.parse(query);
    Assert.assertNotNull(evaluator);
  }

  // Test parse method with a CSS query containing a comma
  @Test
  public void testParseWithComma() {
    String query = "div, span";
    Evaluator evaluator = QueryParser.parse(query);
    Assert.assertNotNull(evaluator);
  }

  // Test parse method with a CSS query starting with a space
  @Test
  public void testParseStartingWithSpace() {
    String query = " div";
    Evaluator evaluator = QueryParser.parse(query);
    Assert.assertNotNull(evaluator);
  }

  // Test parse method with a CSS query starting with an unknown combinator
  @Test(expected = Selector.SelectorParseException.class)
  public void testParseStartingWithUnknownCombinator() {
    String query = "@ div";
    QueryParser.parse(query);
  }

  // Test parse method with a CSS query ending with an unknown combinator
  @Test(expected = Selector.SelectorParseException.class)
  public void testParseEndingWithUnknownCombinator() {
    String query = "div ?";
    QueryParser.parse(query);
  }

  // Test parse method with a CSS query containing an empty subquery
  @Test(expected = Selector.SelectorParseException.class)
  public void testParseEmptySubQuery() {
    String query = "div > ()";
    QueryParser.parse(query);
  }

  // Test parse method with a CSS query containing an empty attribute key
  @Test(expected = IllegalArgumentException.class)
  public void testParseEmptyAttributeKey() {
    String query = "div[]";
    QueryParser.parse(query);
  }

  // Test parse method with a CSS query containing an attribute key starting with ^
  @Test
  public void testParseAttributeKeyStartingWithCaret() {
    String query = "div[^key]";
    Evaluator evaluator = QueryParser.parse(query);
    Assert.assertNotNull(evaluator);
  }

  // Test parse method with a CSS query containing an attribute key starting with ^
  @Test
  public void testParseAttributeKeyWithNamespace() {
    String query = "div[abc|def]";
    Evaluator evaluator = QueryParser.parse(query);
    Assert.assertNotNull(evaluator);
  }

  // Test parse method with a CSS query ending with an unknown pseudo selector
  @Test(expected = Selector.SelectorParseException.class)
  public void testParseEndingWithUnknownPseudoSelector() {
    String query = "div:unknown";
    QueryParser.parse(query);
  }

}
