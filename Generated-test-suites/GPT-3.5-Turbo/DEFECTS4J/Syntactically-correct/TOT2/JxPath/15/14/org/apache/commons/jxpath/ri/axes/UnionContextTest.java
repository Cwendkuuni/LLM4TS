import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

  @Test
  public void testGetDocumentOrder() {
    EvalContext[] contexts = {new EvalContext(), new EvalContext()};
    UnionContext unionContext = new UnionContext(new EvalContext(), contexts);
    assertEquals(1, unionContext.getDocumentOrder());
  }

  @Test
  public void testSetPosition() {
    EvalContext[] contexts = {new EvalContext(), new EvalContext()};
    UnionContext unionContext = new UnionContext(new EvalContext(), contexts);
    assertTrue(unionContext.setPosition(1));
  }

}