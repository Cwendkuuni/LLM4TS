package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
  
  @Test
  public void testConstructor() {
    EvalContext parentContext = new EvalContext();
    EvalContext[] contexts = {new EvalContext(), new EvalContext()};
    UnionContext union = new UnionContext(parentContext, contexts);
    
    assertSame(parentContext, union.getParentContext());
    assertEquals(BasicNodeSet.class, union.getNodeSet().getClass());
    assertArrayEquals(contexts, union.getContexts());
  }
  
  @Test
  public void testGetDocumentOrder() {
    EvalContext parentContext = new EvalContext();
    UnionContext union = new UnionContext(parentContext, new EvalContext[]{});
    
    assertEquals(0, union.getDocumentOrder());
    
    union = new UnionContext(parentContext, new EvalContext[]{new EvalContext(), new EvalContext()});
    assertEquals(1, union.getDocumentOrder());
  }
  
  @Test
  public void testSetPosition() {
    UnionContext union = new UnionContext(new EvalContext(), new EvalContext[]{});
    
    assertTrue(union.setPosition(1));
    assertFalse(union.setPosition(0));
  }
}