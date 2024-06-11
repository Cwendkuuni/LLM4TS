import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {
  
  private UnionContext unionContext;
  
  @Before
  public void setUp() {
    EvalContext parentContext = new EvalContext();
    EvalContext[] contexts = new EvalContext[2];
    contexts[0] = new EvalContext();
    contexts[1] = new EvalContext();
    unionContext = new UnionContext(parentContext, contexts);
  }
  
  @Test
  public void testGetDocumentOrder() {
    int expected = 1;
    int actual = unionContext.getDocumentOrder();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPosition() {
    int position = 1;
    boolean expected = true;
    boolean actual = unionContext.setPosition(position);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPositionWithInvalidPosition() {
    int position = -1;
    boolean expected = false;
    boolean actual = unionContext.setPosition(position);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPositionWithPreparedTrue() {
    int position = 1;
    unionContext.setPosition(position);
    boolean expected = false;
    boolean actual = unionContext.setPosition(position);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPositionWithEmptyNodeSet() {
    int position = 1;
    unionContext.setPosition(position);
    boolean expected = false;
    boolean actual = unionContext.setPosition(position);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPositionWithNonEmptyNodeSet() {
    int position = 1;
    unionContext.setPosition(position);
    position = 2;
    boolean expected = true;
    boolean actual = unionContext.setPosition(position);
    assertEquals(expected, actual);
  }
  
}