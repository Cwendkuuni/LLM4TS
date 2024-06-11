import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

   @Test
   public void testComputeValue() {
      CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);

      // Typical use case
      EvalContext context1 = new EvalContext();
      context1.setSingleNodePointer(new InitialContext());
      Object result1 = expr.computeValue(context1);
      assertTrue(result1 instanceof Boolean);

      // Edge case: empty context
      EvalContext context2 = new EvalContext();
      context2.setSingleNodePointer(new InitialContext());
      Object result2 = expr.computeValue(context2);
      assertTrue(result2 instanceof Boolean);

      // Error scenario: null context
      EvalContext context3 = null;
      try {
         Object result3 = expr.computeValue(context3);
         fail("Expected NullPointerException");
      } catch (NullPointerException e) {
         // expected
      }
   }

   @Test
   public void testGetPrecedence() {
      CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);

      // Typical use case: precedence 3
      int result1 = expr.getPrecedence();
      assertEquals(3, result1);

      // Edge case: precedence 0
      int result2 = expr.getPrecedence();
      assertEquals(0, result2);
   }

   @Test
   public void testIsSymmetric() {
      CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);

      // Typical use case: not symmetric
      boolean result1 = expr.isSymmetric();
      assertFalse(result1);

      // Edge case: symmetric
      boolean result2 = expr.isSymmetric();
      assertTrue(result2);
   }

   @Test
   public void testEvaluateCompare() {
      CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);

      // Typical use case
      boolean result1 = expr.evaluateCompare(1);
      assertTrue(result1);

      // Edge case: compare = 0
      boolean result2 = expr.evaluateCompare(0);
      assertFalse(result2);

      // Error scenario: compare < 0
      boolean result3 = expr.evaluateCompare(-1);
      assertTrue(result3);
   }

   @Test
   public void testCompute() {
      CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);

      // Typical use case: both left and right are not Iterators
      Object left1 = "left";
      Object right1 = "right";
      boolean result1 = expr.compute(left1, right1);
      assertFalse(result1);

      // Edge case: left is Iterator and right is Iterator
      Object left2 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count < 10) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return null;
         }

         public void remove() {
            // do nothing
         }
      };
      Object right2 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count < 5) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return null;
         }

         public void remove() {
            // do nothing
         }
      };
      boolean result2 = expr.compute(left2, right2);
      assertFalse(result2);

      // Error scenario: left is Iterator and right is not Iterator
      Object left3 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count < 10) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return null;
         }

         public void remove() {
            // do nothing
         }
      };
      Object right3 = "right";
      boolean result3 = expr.compute(left3, right3);
      assertFalse(result3);
   }

   @Test
   public void testReduce() {
      CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);

      // Typical use case: o is not SelfContext and o is not Collection
      Object o1 = "object";
      Object result1 = expr.reduce(o1);
      assertEquals(result1, o1);

      // Edge case: o is SelfContext and o is Collection
      Object o2 = new SelfContext();
      Object result2 = expr.reduce(o2);
      assertEquals(result2, o2);

      // Error scenario: o is null
      Object o3 = null;
      Object result3 = expr.reduce(o3);
      assertNull(result3);
   }

   @Test
   public void testContainsMatch() {
      CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);

      // Typical use case: it has next element and contains match
      Iterator it1 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count == 0) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return "element";
         }

         public void remove() {
            // do nothing
         }
      };
      Object value1 = "element";
      boolean result1 = expr.containsMatch(it1, value1);
      assertTrue(result1);

      // Edge case: it does not have next element
      Iterator it2 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            return false;
         }

         public Object next() {
            return null;
         }

         public void remove() {
            // do nothing
         }
      };
      Object value2 = "element";
      boolean result2 = expr.containsMatch(it2, value2);
      assertFalse(result2);

      // Error scenario: it has next element and does not contain match
      Iterator it3 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count == 0) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return "element";
         }

         public void remove() {
            // do nothing
         }
      };
      Object value3 = "different";
      boolean result3 = expr.containsMatch(it3, value3);
      assertFalse(result1);
   }

   @Test
   public void testFindMatch() {
      CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);

      // Typical use case: lit has next and contains match
      Iterator lit1 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count < 5) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return "element";
         }

         public void remove() {
            // do nothing
         }
      };
      Iterator rit1 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count == 0) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return "element";
         }

         public void remove() {
            // do nothing
         }
      };
      boolean result1 = expr.findMatch(lit1, rit1);
      assertTrue(result1);

      // Edge case: rit does not have next
      Iterator lit2 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count < 5) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return "element";
         }

         public void remove() {
            // do nothing
         }
      };
      Iterator rit2 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            return false;
         }

         public Object next() {
            return null;
         }

         public void remove() {
            // do nothing
         }
      };
      boolean result2 = expr.findMatch(lit2, rit2);
      assertFalse(result2);

      // Error scenario: lit has next and does not contain match
      Iterator lit3 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count < 5) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return "element";
         }

         public void remove() {
            // do nothing
         }
      };
      Iterator rit3 = new Iterator() {
         int count = 0;

         public boolean hasNext() {
            if (count == 0) {
               count++;
               return true;
            } else {
               return false;
            }
         }

         public Object next() {
            return "different";
         }

         public void remove() {
            // do nothing
         }
      };
      boolean result3 = expr.containsMatch(lit3, rit3);
      assertFalse(result1);
   }
}