package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {
  
  @Test
  public void testEqual() {
    Expression arg1 = new MyExpression();
    Expression arg2 = new MyExpression();
    CoreOperationCompare operationCompare = new CoreOperationCompare(arg1, arg2);

    EvalContext context = new EvalContext();
    assertTrue(operationCompare.equal(context, arg1, arg2));
    
    arg2 = new MyDifferentExpression();
    assertFalse(operationCompare.equal(context, arg1, arg2));
  }
  
  @Test
  public void testContains() {
    Expression arg1 = new MyExpression();
    Expression arg2 = new MyExpression();
    CoreOperationCompare operationCompare = new CoreOperationCompare(arg1, arg2);

    EvalContext context = new EvalContext();
    List<String> collection = new ArrayList<>();
    collection.add("element1");
    collection.add("element2");
    Iterator<String> iterator = collection.iterator();
    assertTrue(operationCompare.contains(iterator, "element1"));
    
    assertFalse(operationCompare.contains(iterator, "element3"));
  }
  
  @Test
  public void testFindMatch() {
    Expression arg1 = new MyExpression();
    Expression arg2 = new MyExpression();
    CoreOperationCompare operationCompare = new CoreOperationCompare(arg1, arg2);

    EvalContext context = new EvalContext();
    List<String> collection1 = new ArrayList<>();
    collection1.add("element1");
    collection1.add("element2");
    List<String> collection2 = new ArrayList<>();
    collection2.add("element2");
    collection2.add("element3");
    Iterator<String> iterator1 = collection1.iterator();
    Iterator<String> iterator2 = collection2.iterator();
    assertTrue(operationCompare.findMatch(iterator1, iterator2));
    
    collection2 = new ArrayList<>();
    collection2.add("element3");
    collection2.add("element4");
    iterator2 = collection2.iterator();
    assertFalse(operationCompare.findMatch(iterator1, iterator2));
  }
  
  @Test
  public void testEqualPointers() {
    Expression arg1 = new MyExpression();
    Expression arg2 = new MyExpression();
    CoreOperationCompare operationCompare = new CoreOperationCompare(arg1, arg2);

    Object pointer1 = new Pointer("pointer1");
    Object pointer2 = new Pointer("pointer1");
    assertTrue(operationCompare.equal(pointer1, pointer2));
    
    pointer2 = new Pointer("pointer2");
    assertFalse(operationCompare.equal(pointer1, pointer2));
  }
  
  @Test
  public void testEqualBoolean() {
    Expression arg1 = new MyExpression();
    Expression arg2 = new MyExpression();
    CoreOperationCompare operationCompare = new CoreOperationCompare(arg1, arg2);

    assertTrue(operationCompare.equal(true, true));
    assertTrue(operationCompare.equal(false, false));
    assertFalse(operationCompare.equal(true, false));
    assertFalse(operationCompare.equal(false, true));
  }
  
  @Test
  public void testEqualNumber() {
    Expression arg1 = new MyExpression();
    Expression arg2 = new MyExpression();
    CoreOperationCompare operationCompare = new CoreOperationCompare(arg1, arg2);

    assertTrue(operationCompare.equal(1, 1));
    assertTrue(operationCompare.equal(1.5, 1.5));
    assertTrue(operationCompare.equal(1L, 1L));
    assertFalse(operationCompare.equal(1, 2));
    assertFalse(operationCompare.equal(1.5, 2.5));
    assertFalse(operationCompare.equal(1L, 2L));
  }
  
  @Test
  public void testEqualString() {
    Expression arg1 = new MyExpression();
    Expression arg2 = new MyExpression();
    CoreOperationCompare operationCompare = new CoreOperationCompare(arg1, arg2);

    assertTrue(operationCompare.equal("test", "test"));
    assertFalse(operationCompare.equal("test", "not test"));
  }
  
  @Test
  public void testEqualNull() {
    Expression arg1 = new MyExpression();
    Expression arg2 = new MyExpression();
    CoreOperationCompare operationCompare = new CoreOperationCompare(arg1, arg2);

    assertFalse(operationCompare.equal(null, "test"));
    assertFalse(operationCompare.equal("test", null));
    assertTrue(operationCompare.equal(null, null));
  }
  
  private class MyExpression extends Expression {
    @Override
    public Object compute(EvalContext context) {
      return null;
    }
  }
  
  private class MyDifferentExpression extends Expression {
    @Override
    public Object compute(EvalContext context) {
      return null;
    }
  }
}