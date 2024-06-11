import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

  @Test
  public void testParse() {
    String query = "div#test";
    Evaluator expected = new Evaluator.Id("test");
    Evaluator result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div.class";
    expected = new Evaluator.Class("class");
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div[attr=value]";
    expected = new Evaluator.AttributeWithValue("attr", "value");
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div[attr^=value]";
    expected = new Evaluator.AttributeWithValueStarting("attr", "value");
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div[attr$=value]";
    expected = new Evaluator.AttributeWithValueEnding("attr", "value");
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div[attr*=value]";
    expected = new Evaluator.AttributeWithValueContaining("attr", "value");
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div[attr~=value]";
    expected = new Evaluator.AttributeWithValueMatching("attr", java.util.regex.Pattern.compile("value"));
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div:lt(5)";
    expected = new Evaluator.IndexLessThan(5);
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div:gt(5)";
    expected = new Evaluator.IndexGreaterThan(5);
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div:eq(5)";
    expected = new Evaluator.IndexEquals(5);
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div:has(span)";
    expected = new StructuralEvaluator.Has(new Evaluator.Tag("span"));
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div:contains(text)";
    expected = new Evaluator.ContainsText("text");
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div:containsOwn(text)";
    expected = new Evaluator.ContainsOwnText("text");
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div:matches(regex)";
    expected = new Evaluator.Matches(java.util.regex.Pattern.compile("regex"));
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div:matchesOwn(regex)";
    expected = new Evaluator.MatchesOwn(java.util.regex.Pattern.compile("regex"));
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());

    query = "div:not(span)";
    expected = new StructuralEvaluator.Not(new Evaluator.Tag("span"));
    result = QueryParser.parse(query);
    assertEquals(expected.getClass(), result.getClass());
  }
}