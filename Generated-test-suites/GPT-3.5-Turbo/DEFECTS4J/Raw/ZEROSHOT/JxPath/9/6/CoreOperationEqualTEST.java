import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

  @Test
  public void testComputeValue() {
    CoreOperationEqual equal = new CoreOperationEqual(null, null);
    EvalContext context = new EvalContext(null);
    
    // Test equal values
    assertEquals(Boolean.TRUE, equal.computeValue(context));
  
    equal = new CoreOperationEqual(null, new Expression());
    context = new EvalContext(null);
    context.setNodeSet(new NodeSet());
    
    // Test first argument null
    assertEquals(Boolean.FALSE, equal.computeValue(context));
  
    equal = new CoreOperationEqual(new Expression(), null);
    
    // Test second argument null
    assertEquals(Boolean.FALSE, equal.computeValue(context));
  
    equal = new CoreOperationEqual(new Expression(), new Expression());
    
    // Test both arguments null
    assertEquals(Boolean.TRUE, equal.computeValue(context));
  
    context.setNodeSet(new NodeSet());
    
    // Test different node sets
    assertEquals(Boolean.FALSE, equal.computeValue(context));
  
    NodeSet nodeSet = new NodeSet();
    nodeSet.add(new Node());
    
    context.setNodeSet(nodeSet);
    
    // Test same node sets
    assertEquals(Boolean.TRUE, equal.computeValue(context));
  }

  @Test
  public void testGetSymbol() {
    CoreOperationEqual equal = new CoreOperationEqual(null, null);
    
    // Test get symbol
    assertEquals("=", equal.getSymbol());
  }
}