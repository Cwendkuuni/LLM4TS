import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {
  
  private JXPathContext context;
  
  @Before
  public void setUp() {
    context = JXPathContext.newContext(new Object());
  }
  
  @Test
  public void testGetDocumentOrder() {
    EvalContext ctx1 = new EvalContext(null, null);
    EvalContext ctx2 = new EvalContext(null, null);
    EvalContext[] contexts = { ctx1, ctx2 };
    UnionContext unionContext = new UnionContext(context, contexts);
    
    int expected = 1;
    int actual = unionContext.getDocumentOrder();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPosition() {
    EvalContext ctx1 = new EvalContext(null, null);
    EvalContext ctx2 = new EvalContext(null, null);
    EvalContext[] contexts = { ctx1, ctx2 };
    UnionContext unionContext = new UnionContext(context, contexts);
    
    int position = 0;
    boolean expected = true;
    boolean actual = unionContext.setPosition(position);
    
    assertEquals(expected, actual);
  }
}