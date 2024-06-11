import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

  @Test
  public void testGetDocumentOrder() {
    EvalContext parentContext = new EvalContext();
    EvalContext context1 = new EvalContext();
    EvalContext context2 = new EvalContext();
    EvalContext[] contexts = {context1, context2};
    UnionContext union = new UnionContext(parentContext, contexts);

    // Test when number of contexts is greater than 1
    assertEquals(1, union.getDocumentOrder());

    // Test when number of contexts is equal to 1
    EvalContext[] singleContext = {context1};
    UnionContext singleUnion = new UnionContext(parentContext, singleContext);
    assertEquals(0, singleUnion.getDocumentOrder());
  }

  @Test
  public void testSetPosition() {
    EvalContext parentContext = new EvalContext();
    EvalContext context1 = new EvalContext();
    EvalContext context2 = new EvalContext();
    EvalContext[] contexts = {context1, context2};
    UnionContext union = new UnionContext(parentContext, contexts);

    // Test when prepared flag is false
    union.setPosition(1);
    assertTrue(union.isPrepared());

    // Test when prepared flag is true
    union.setPosition(2);
    assertTrue(union.isPrepared());
  }
}