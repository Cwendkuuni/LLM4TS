package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        Expression left = new ConstantExpr("abc");
        Expression right = new ConstantExpr("abc");
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        assertTrue(operation.equal(context, left, right));
    }
    
    @Test
    public void testNotEqual() {
        Expression left = new ConstantExpr("abc");
        Expression right = new ConstantExpr("def");
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        assertFalse(operation.equal(context, left, right));
    }
    
    @Test
    public void testEqualWithCollection() {
        Expression left = new ConstantExpr(createStringList());
        Expression right = new ConstantExpr("def");
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        assertFalse(operation.equal(context, left, right));
    }
    
    @Test
    public void testEqualWithPointers() {
        EvaluationContext context = new EvaluationContext();
        Pointer pointer1 = new Pointer("abc");
        Pointer pointer2 = new Pointer("abc");
        Pointer pointer3 = new Pointer("def");
        
        Expression left = new ConstantExpr(pointer1);
        Expression right = new ConstantExpr(pointer2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        assertTrue(operation.equal(context, left, right));
        
        right = new ConstantExpr(pointer3);
        assertFalse(operation.equal(context, left, right));
    }
    
    @Test
    public void testEqualWithValueAndPointer() {
        EvaluationContext context = new EvaluationContext();
        Pointer pointer = new Pointer("abc");
        
        Expression left = new ConstantExpr("abc");
        Expression right = new ConstantExpr(pointer);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        assertTrue(operation.equal(context, left, right));
    }
    
    @Test
    public void testFindMatch() {
        Expression left = new ConstantExpr(createStringList());
        Expression right = new ConstantExpr(createStringList("def"));
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        assertTrue(operation.findMatch(context, left, right));
        
        right = new ConstantExpr(createStringList("ghi"));
        assertFalse(operation.findMatch(context, left, right));
    }
    
    @Test
    public void testContains() {
        Expression left = new ConstantExpr(createStringList());
        Expression right = new ConstantExpr("abc");
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        assertTrue(operation.contains(context, left, right));
        
        right = new ConstantExpr("def");
        assertFalse(operation.contains(context, left, right));
    }
    
    @Test
    public void testEqualWithBoolean() {
        Expression left = new ConstantExpr(true);
        Expression right = new ConstantExpr(true);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        assertTrue(operation.equal(context, left, right));
        
        right = new ConstantExpr(false);
        assertFalse(operation.equal(context, left, right));
    }
    
    @Test
    public void testEqualWithNumber() {
        Expression left = new ConstantExpr(123);
        Expression right = new ConstantExpr(123);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        assertTrue(operation.equal(context, left, right));
        
        right = new ConstantExpr(456);
        assertFalse(operation.equal(context, left, right));
    }
    
    @Test
    public void testEqualWithString() {
        Expression left = new ConstantExpr("abc");
        Expression right = new ConstantExpr("abc");
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        assertTrue(operation.equal(context, left, right));
        
        right = new ConstantExpr("def");
        assertFalse(operation.equal(context, left, right));
    }
    
    @Test
    public void testEqualWithNull() {
        Expression left = new ConstantExpr(null);
        Expression right = new ConstantExpr(null);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        assertTrue(operation.equal(context, left, right));
        
        right = new ConstantExpr("abc");
        assertFalse(operation.equal(context, left, right));
    }
    
    private List<String> createStringList(String... values) {
        List<String> list = new ArrayList<>();
        for (String value : values) {
            list.add(value);
        }
        return list;
    }
}