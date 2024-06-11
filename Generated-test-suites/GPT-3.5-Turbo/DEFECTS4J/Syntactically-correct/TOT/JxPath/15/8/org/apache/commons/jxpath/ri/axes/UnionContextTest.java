import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {

  private EvalContext context1;
  private EvalContext context2;
  private EvalContext context3;
  private UnionContext unionContext;

  @Before
  public void setUp() {
    context1 = new EvalContext();
    context2 = new EvalContext();
    context3 = new EvalContext();
    unionContext = new UnionContext(null, new EvalContext[] { context1, context2, context3 });
  }

  @Test
  public void testGetDocumentOrder_WithMultipleContexts_Returns1() {
    int expectedOrder = 1;
    int actualOrder = unionContext.getDocumentOrder();
    assertEquals(expectedOrder, actualOrder);
  }

  @Test
  public void testGetPosition_WhenNotPrepared_ReturnsFalse() {
    boolean expectedPosition = false;
    boolean actualPosition = unionContext.setPosition(1);
    assertEquals(expectedPosition, actualPosition);
  }

  @Test
  public void testGetPosition_WhenPrepared_ReturnsTrue() {
    context1.nextSet();
    context1.nextNode();
    context1.setCurrentNodePointer(new NodePointer());
    boolean expectedPosition = true;
    boolean actualPosition = unionContext.setPosition(1);
    assertEquals(expectedPosition, actualPosition);
  }

  @Test
  public void testGetPosition_WithInvalidPosition_ReturnsFalse() {
    boolean expectedPosition = false;
    boolean actualPosition = unionContext.setPosition(-1);
    assertEquals(expectedPosition, actualPosition);
  }

  @Test
  public void testGetPosition_WithValidPosition_ReturnsTrue() {
    context1.nextSet();
    context1.nextNode();
    context1.setCurrentNodePointer(new NodePointer());
    boolean expectedPosition = true;
    boolean actualPosition = unionContext.setPosition(0);
    assertEquals(expectedPosition, actualPosition);
  }

  @Test
  public void testGetPosition_WithMultipleContexts_ReturnsFalseForInvalidPositions() {
    context1.nextSet();
    context1.nextNode();
    context1.setCurrentNodePointer(new NodePointer());
    context2.nextSet();
    context2.nextNode();
    context2.setCurrentNodePointer(new NodePointer());
    context3.nextSet();
    context3.nextNode();
    context3.setCurrentNodePointer(new NodePointer());
    
    boolean firstExpectedPosition = false;
    boolean secondExpectedPosition = false;
    boolean thirdExpectedPosition = false;
  
    boolean firstActualPosition = unionContext.setPosition(4);
    boolean secondActualPosition = unionContext.setPosition(7);
    boolean thirdActualPosition = unionContext.setPosition(-2);

    assertEquals(firstExpectedPosition, firstActualPosition);
    assertEquals(secondExpectedPosition, secondActualPosition);
    assertEquals(thirdExpectedPosition, thirdActualPosition);
  }
  
}