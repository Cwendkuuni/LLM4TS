package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {
  
  @Test
  public void testEqual() {
    Collection<Integer> lCollection = Arrays.asList(1, 2, 3, 4, 5);
    Collection<Integer> rCollection = Arrays.asList(2, 3, 4);
    
    EvalContext evalContext = new EvalContext();
    evalContext.getContextNodeList().add(lCollection);
    evalContext.getContextNodeList().add(rCollection);
    
    Expression left = new Expression(lCollection);
    Expression right = new Expression(rCollection);
    
    CoreOperationCompare compare = new CoreOperationCompare(left, right);
    
    boolean result = compare.equal(evalContext, left, right);
    assertFalse(result);
  }
  
  @Test
  public void testContains() {
    Iterator iterator = Arrays.asList("a", "b", "c").iterator();
    String value = "b";
    
    CoreOperationCompare compare = new CoreOperationCompare(null, null) {
      @Override
      protected boolean equal(Object l, Object r) {
        return l.equals(r);
      }
    };
    
    boolean result = compare.contains(iterator, value);
    assertTrue(result);
  }
  
  @Test
  public void testContains_notContains() {
    Iterator iterator = Arrays.asList("a", "b", "c").iterator();
    String value = "d";
    
    CoreOperationCompare compare = new CoreOperationCompare(null, null) {
      @Override
      protected boolean equal(Object l, Object r) {
        return l.equals(r);
      }
    };
    
    boolean result = compare.contains(iterator, value);
    assertFalse(result);
  }
  
  @Test
  public void testFindMatch() {
    Iterator lit = Arrays.asList(1, 2, 3, 4, 5).iterator();
    Iterator rit = Arrays.asList(2, 3, 4).iterator();
    
    CoreOperationCompare compare = new CoreOperationCompare(null, null) {
      @Override
      protected boolean contains(Iterator it, Object value) {
        return it.hasNext() && it.next().equals(value);
      }
    };
    
    boolean result = compare.findMatch(lit, rit);
    assertTrue(result);
  }
  
  @Test
  public void testFindMatch_noMatch() {
    Iterator lit = Arrays.asList(1, 2, 3, 4, 5).iterator();
    Iterator rit = Arrays.asList(6, 7, 8).iterator();
    
    CoreOperationCompare compare = new CoreOperationCompare(null, null) {
      @Override
      protected boolean contains(Iterator it, Object value) {
        return it.hasNext() && it.next().equals(value);
      }
    };
    
    boolean result = compare.findMatch(lit, rit);
    assertFalse(result);
  }
  
  @Test
  public void testEqual_Pointer() {
    Pointer lPointer = new Pointer() {
      @Override
      public Object getValue() {
        return "value";
      }
    };
    Pointer rPointer = new Pointer() {
      @Override
      public Object getValue() {
        return "value";
      }
    };
    
    CoreOperationCompare compare = new CoreOperationCompare(null, null) {
      @Override
      protected boolean equal(Object l, Object r) {
        return l.equals(r);
      }
    };
    
    boolean result = compare.equal(lPointer, rPointer);
    assertTrue(result);
  }
  
  @Test
  public void testEqual_notEqual() {
    Pointer lPointer = new Pointer() {
      @Override
      public Object getValue() {
        return "value";
      }
    };
    Pointer rPointer = new Pointer() {
      @Override
      public Object getValue() {
        return "notValue";
      }
    };
    
    CoreOperationCompare compare = new CoreOperationCompare(null, null) {
      @Override
      protected boolean equal(Object l, Object r) {
        return l.equals(r);
      }
    };
    
    boolean result = compare.equal(lPointer, rPointer);
    assertFalse(result);
  }
}