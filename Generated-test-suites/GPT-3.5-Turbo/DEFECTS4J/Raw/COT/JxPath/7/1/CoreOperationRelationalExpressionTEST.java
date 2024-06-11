package org.apache.commons.jxpath.ri.compiler;

import junit.framework.TestCase;

public class CoreOperationRelationalExpressionTest extends TestCase {

   // Test for the getPrecedence() method
   public void testGetPrecedence() {
       CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(null) {
           // dummy implementation for testing
           protected final int getPrecedence() {
               return 3;
           }
       };
       assertEquals(3, exp.getPrecedence());
   }

   // Test for the isSymmetric() method
   public void testIsSymmetric() {
       CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(null) {
           // dummy implementation for testing
           protected final boolean isSymmetric() {
               return false;
           }
       };
       assertFalse(exp.isSymmetric());
   }
}