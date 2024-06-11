import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.select.*;

public class QueryParserTest {
  
  @Test
  public void testParse_CorrectInput_ReturnsEvaluator() {
    String query = "div.exampleClass";
    Evaluator expected = new Evaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("exampleClass"));
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_InvalidInput_ThrowsSelectorParseException() {
    String query = "..exampleClass";
    try {
      QueryParser.parse(query);
      fail("Expected SelectorParseException to be thrown.");
    } catch (Selector.SelectorParseException ex) {
      assertEquals("Could not parse query '..exampleClass': unexpected token at ''", ex.getMessage());
    }
  }
  
  @Test
  public void testParse_CommaSeparatedQuery_ReturnsOrEvaluator() {
    String query = "div.exampleClass, div.exampleId";
    Evaluator expected = new Evaluator.Or(new Evaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("exampleClass")), new Evaluator.And(new Evaluator.Tag("div"), new Evaluator.Id("exampleId")));
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_ComplexQuery_ReturnsEvaluator() {
    String query = "div.exampleClass > span.exampleSpan + p.exampleParagraph";
    Evaluator expected = new Evaluator.And(new Evaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("exampleClass")), new Evaluator.And(new Evaluator.ImmediateParent(new Evaluator.Tag("span"), new Evaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("exampleClass")))), new Evaluator.And(new Evaluator.ImmediatePreviousSibling(new Evaluator.Tag("p"), new Evaluator.And(new Evaluator.Tag("span"), new Evaluator.Class("exampleSpan")))));
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_EmptyQuery_ReturnsAllElementsEvaluator() {
    String query = "";
    Evaluator expected = new Evaluator.AllElements();
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithMultipleCombinators_ReturnsEvaluator() {
    String query = "div.exampleClass + span ~ p.exampleParagraph";
    Evaluator expected = new Evaluator.And(new Evaluator.ImmediatePreviousSibling(
      new Evaluator.Tag("p"),
      new Evaluator.And(
        new Evaluator.Tag("span"),
        new Evaluator.Class("exampleSpan")
      )
    ));
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithAttribute_ReturnsEvaluator() {
    String query = "div[attribute='value']";
    Evaluator expected = new Evaluator.AttributeWithValue("attribute", "value");
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithAttributeStarting_ReturnsEvaluator() {
    String query = "div[attribute^='value']";
    Evaluator expected = new Evaluator.AttributeWithValueStarting("attribute", "value");
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithAttributeEnding_ReturnsEvaluator() {
    String query = "div[attribute$='value']";
    Evaluator expected = new Evaluator.AttributeWithValueEnding("attribute", "value");
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithAttributeContaining_ReturnsEvaluator() {
    String query = "div[attribute*='value']";
    Evaluator expected = new Evaluator.AttributeWithValueContaining("attribute", "value");
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithAttributeMatching_ReturnsEvaluator() {
    String query = "div[attribute~='value']";
    Evaluator expected = new Evaluator.AttributeWithValueMatching("attribute", Pattern.compile("value"));
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithPseudoSelector_ReturnsEvaluator() {
    String query = "div:has(span)";
    Evaluator expected = new Evaluator.Has(new Evaluator.Tag("span"));
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithContainsSelector_ReturnsEvaluator() {
    String query = "div:contains(text)";
    Evaluator expected = new Evaluator.ContainsText("text");
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithContainsOwnSelector_ReturnsEvaluator() {
    String query = "div:containsOwn(text)";
    Evaluator expected = new Evaluator.ContainsOwnText("text");
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithMatchesRegex_ReturnsEvaluator() {
    String query = "div:matches(regex)";
    Evaluator expected = new Evaluator.Matches(Pattern.compile("regex"));
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithMatchesOwnRegex_ReturnsEvaluator() {
    String query = "div:matchesOwn(regex)";
    Evaluator expected = new Evaluator.MatchesOwn(Pattern.compile("regex"));
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParse_QueryWithNotSelector_ReturnsEvaluator() {
    String query = "div:not(span)";
    Evaluator expected = new Evaluator.Not(new Evaluator.Tag("span"));
    Evaluator actual = QueryParser.parse(query);
    assertEquals(expected, actual);
  }

}