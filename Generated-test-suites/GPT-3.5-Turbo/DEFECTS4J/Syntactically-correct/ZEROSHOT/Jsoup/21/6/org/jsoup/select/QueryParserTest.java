import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

  @Test
  public void testParse() {
    // Test case 1: Empty query
    Evaluator result = QueryParser.parse("");
    assertTrue(result instanceof Evaluator.AllElements);

    // Test case 2: Simple tag query
    result = QueryParser.parse("div");
    assertTrue(result instanceof Evaluator.Tag);
    Evaluator.Tag tagEvaluator = (Evaluator.Tag) result;
    assertEquals("div", tagEvaluator.tagName());

    // Test case 3: Tag with id
    result = QueryParser.parse("div#myId");
    assertTrue(result instanceof Evaluator.And);
    Evaluator.And andEvaluator = (Evaluator.And) result;
    assertTrue(andEvaluator.evals().get(0) instanceof Evaluator.Tag);
    tagEvaluator = (Evaluator.Tag) andEvaluator.evals().get(0);
    assertEquals("div", tagEvaluator.tagName());
    assertTrue(andEvaluator.evals().get(1) instanceof Evaluator.Id);
    Evaluator.Id idEvaluator = (Evaluator.Id) andEvaluator.evals().get(1);
    assertEquals("myId", idEvaluator.id());

    // More test cases can be added to test other methods in QueryParser class
    // ...

  }

}