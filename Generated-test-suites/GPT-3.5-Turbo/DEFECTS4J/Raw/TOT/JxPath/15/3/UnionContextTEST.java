import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.*;

public class UnionContextTest {
   private UnionContext unionContext;

   @Before
   public void setUp() {
      EvalContext[] evalContexts = new EvalContext[2];
      evalContexts[0] = new EvalContext();
      evalContexts[1] = new EvalContext();
      unionContext = new UnionContext(null, evalContexts);
   }

   @Test
   public void testGetDocumentOrder_WhenMultipleContexts_Returns1() {
      int documentOrder = unionContext.getDocumentOrder();
      assertEquals(1, documentOrder);
   }

   @Test
   public void testGetDocumentOrder_WhenSingleContext_ReturnsDefault() {
      EvalContext[] evalContexts = new EvalContext[1];
      evalContexts[0] = new EvalContext();
      unionContext = new UnionContext(null, evalContexts);

      int documentOrder = unionContext.getDocumentOrder();
      assertEquals(0, documentOrder);
   }

   @Test
   public void testSetPosition_WithValidPosition_ReturnsTrue() {
      boolean positionSet = unionContext.setPosition(1);
      assertTrue(positionSet);
   }

   @Test
   public void testSetPosition_WithInvalidPosition_ReturnsFalse() {
      boolean positionSet = unionContext.setPosition(-1);
      assertFalse(positionSet);
   }

   @Test
   public void testSetPosition_WithValidPosition_ChecksPreparedFlag() {
      unionContext.setPosition(1);
      assertTrue(unionContext.isPrepared());
   }

   @Test
   public void testSetPosition_WithoutPreparing_ReturnsFalse() {
      unionContext.setPosition(1);
      unionContext.setPrepared(false);

      boolean positionSet = unionContext.setPosition(2);
      assertFalse(positionSet);
   }

   @Test
   public void testSetPosition_WithMultipleContexts_AddsPointersToNodeSet() {
      unionContext.setPosition(1);

      BasicNodeSet nodeSet = (BasicNodeSet) unionContext.getNodeSet();
      ArrayList<NodePointer> pointers = nodeSet.getPointers();
      
      assertEquals(0, pointers.size());

      EvalContext[] evalContexts = new EvalContext[2];
      evalContexts[0] = new EvalContext();
      evalContexts[1] = new EvalContext();
      unionContext = new UnionContext(null, evalContexts);

      unionContext.setPosition(2);

      nodeSet = (BasicNodeSet) unionContext.getNodeSet();
      pointers = nodeSet.getPointers();

      assertEquals(2, pointers.size());
   }

   @Test
   public void testSetPosition_WithDuplicatePointers_DoesNotAddDuplicatesToNodeSet() {
      unionContext.setPosition(1);

      BasicNodeSet nodeSet = (BasicNodeSet) unionContext.getNodeSet();
      ArrayList<NodePointer> pointers = nodeSet.getPointers();
      
      assertEquals(0, pointers.size());

      EvalContext[] evalContexts = new EvalContext[2];
      evalContexts[0] = new EvalContext();
      evalContexts[1] = new EvalContext();
      unionContext = new UnionContext(null, evalContexts);

      unionContext.setPosition(1);

      nodeSet = (BasicNodeSet) unionContext.getNodeSet();
      pointers = nodeSet.getPointers();

      assertEquals(1, pointers.size());
   }
}