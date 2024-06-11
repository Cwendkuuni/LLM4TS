package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

public class CoreOperationRelationalExpressionTest {

 private static final Node ITERATOR_NODE = new NodeTest(10);

 @SuppressWarnings("unchecked")
 private static NodeIterator getIterator(Object obj) {
 return (NodeIterator) Iterators.iterate(Iterators.emptyIterator(), new Iterate() {
 @Override
 public Node next() {
 return ITERATOR_NODE;
 }
 });
 }

 @SuppressWarnings("unchecked")
 private static Iterator<Node> getIteratorFromCollection(Collection<Node> nodes) {
 return nodes.iterator();
 }

 @SuppressWarnings("unchecked")
 private static Collection<Node> getCollectionFromIterator(Iterator<Node> iter) {
 return new HashSet<Node>(iter);
 }

 @SuppressWarnings({ "unchecked", "rawtypes" })
 @org.junit.Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstant(10), new CoreConstant(5));
 assertTrue(expr.evaluateCompare(1));

 Node left = new NodeTest(5);
 Node right = new NodeTest(10);
 EvalContext context = new EvalContext(new Path("."), new NodeTest(1));
 NodeIterator leftIterator = getIterator(left);
 NodeIterator rightIterator = getIterator(right);
 assertFalse(expr.compute(context, leftIterator, rightIterator));

 leftIterator = getIterator(left);
 rightIterator = getIterator(right);
 assertTrue(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(10);
 right = new NodeTest(5);
 leftIterator = getIterator(left);
 rightIterator = getIterator(right);
 assertFalse(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(10.1);
 right = new NodeTest(5.1);
 EvalContext contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertFalse(expr.compute(contextDouble, left, right));

 left = new NodeTest(10.1);
 right = new NodeTest(10.2);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertFalse(expr.compute(contextDouble, left, right));

 left = new NodeTest(10.2);
 right = new NodeTest(10.1);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertFalse(expr.compute(contextDouble, left, right));

 left = new NodeTest(10.2);
 right = new NodeTest(10.2);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));

 left = new NodeTest(Double.MIN_VALUE);
 right = new NodeTest(Double.MIN_VALUE);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));
}

 @SuppressWarnings({ "unchecked", "rawtypes" })
 @org.junit.Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstant(10), new CoreConstant(5));
 assertFalse(expr.evaluateCompare(0));

 Node left = new NodeTest(5);
 Node right = new NodeTest(10);
 EvalContext context = new EvalContext(new Path("."), new NodeTest(1));
 NodeIterator leftIterator = getIterator(left);
 NodeIterator rightIterator = getIterator(right);
 assertTrue(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(10);
 right = new NodeTest(5);
 leftIterator = getIterator(left);
 rightIterator = getIterator(right);
 assertTrue(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(10.1);
 right = new NodeTest(5.1);
 EvalContext contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));

 left = new NodeTest(10.1);
 right = new NodeTest(10.2);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));

 left = new NodeTest(10.2);
 right = new NodeTest(10.1);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));

 left = new NodeTest(10.2);
 right = new NodeTest(10.2);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));

 left = new NodeTest(Double.MIN_VALUE);
 right = new NodeTest(Double.MIN_VALUE);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));
}

 @SuppressWarnings({ "unchecked", "rawtypes" })
 @org.junit.Test
 public void testLessThan() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstant(5), new CoreConstant(10));
 assertFalse(expr.evaluateCompare(-1));

 Node left = new NodeTest(5);
 Node right = new NodeTest(10);
 EvalContext context = new EvalContext(new Path("."), new NodeTest(1));
 NodeIterator leftIterator = getIterator(left);
 NodeIterator rightIterator = getIterator(right);
 assertFalse(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(10);
 right = new NodeTest(5);
 leftIterator = getIterator(left);
 rightIterator = getIterator(right);
 assertFalse(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(5.1);
 right = new NodeTest(10.1);
 EvalContext contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertFalse(expr.compute(contextDouble, left, right));

 left = new NodeTest(5.1);
 right = new NodeTest(10.2);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertFalse(expr.compute(contextDouble, left, right));

 left = new NodeTest(5.2);
 right = new NodeTest(10.1);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertFalse(expr.compute(contextDouble, left, right));

 left = new NodeTest(5.2);
 right = new NodeTest(10.2);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertFalse(expr.compute(contextDouble, left, right));

 left = new NodeTest(Double.MIN_VALUE);
 right = new NodeTest(Double.MIN_VALUE);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertFalse(expr.compute(contextDouble, left, right));
}

 @SuppressWarnings({ "unchecked", "rawtypes" })
 @org.junit.Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstant(5), new CoreConstant(10));
 assertTrue(expr.evaluateCompare(0));

 Node left = new NodeTest(5);
 Node right = new NodeTest(10);
 EvalContext context = new EvalContext(new Path("."), new NodeTest(1));
 NodeIterator leftIterator = getIterator(left);
 NodeIterator rightIterator = getIterator(right);
 assertFalse(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(10);
 right = new NodeTest(5);
 leftIterator = getIterator(left);
 rightIterator = getIterator(right);
 assertFalse(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(5.1);
 right = new NodeTest(10.1);
 EvalContext contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));

 left = new NodeTest(5.1);
 right = new NodeTest(10.2);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));

 left = new NodeTest(5.2);
 right = new NodeTest(10.1);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));

 left = new NodeTest(5.2);
 right = new NodeTest(10.2);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));

 left = new NodeTest(Double.MIN_VALUE);
 right = new NodeTest(Double.MIN_VALUE);
 contextDouble = new EvalContext(new Path("."), new NodeTest(1));
 assertTrue(expr.compute(contextDouble, left, right));
}

 @SuppressWarnings({ "unchecked", "rawtypes" })
 @org.junit.Test
 public void testIterators() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstant(5), new CoreConstant(10));
 assertFalse(expr.evaluateCompare(-1));

 Node left = new NodeTest(5);
 Node right = new NodeTest(10);
 EvalContext context = new EvalContext(new Path("."), new NodeTest(1));
 NodeIterator leftIterator = getIterator(left);
 NodeIterator rightIterator = getIterator(right);
 assertFalse(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(5);
 right = new NodeTest(10);
 EvalContext context1 = new EvalContext(new Path("."), new NodeTest(1));
 Iterator<Node> leftCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(left); }});
 Iterator<Node> rightCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(right); }});
 assertFalse(expr.compute(context1, leftCollection, rightCollection));

 leftCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(left); add(new NodeTest(6)); }});
 rightCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(right); add(new NodeTest(7)); }});
 assertFalse(expr.compute(context1, leftCollection, rightCollection));

 leftCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(left); add(new NodeTest(6)); add(new NodeTest(7)); }});
 rightCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(right); add(new NodeTest(8)); }});
 assertFalse(expr.compute(context1, leftCollection, rightCollection));

 leftCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(left); add(new NodeTest(6)); add(new NodeTest(7)); add(new NodeTest(8)); }});
 rightCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(right); add(new NodeTest(8)); add(new NodeTest(9)); }});
 assertFalse(expr.compute(context1, leftCollection, rightCollection));

 leftCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(left); add(new NodeTest(6)); add(new NodeTest(7)); add(new NodeTest(8)); add(new NodeTest(9)); }});
 rightCollection = getIteratorFromCollection(new HashSet<Node>() {{ add(right); add(new NodeTest(8)); add(new NodeTest(9)); add(new NodeTest(10)); }});
 assertTrue(expr.compute(context1, leftCollection, rightCollection));
}

 @SuppressWarnings({ "unchecked", "rawtypes" })
 @org.junit.Test
 public void testInitialContext() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstant(5), new CoreConstant(10));
 assertFalse(expr.evaluateCompare(-1));

 Node left = new NodeTest(5);
 Node right = new NodeTest(10);
 EvalContext context = new EvalContext(new Path("."), new NodeTest(1));
 NodeIterator leftIterator = getIterator(left);
 NodeIterator rightIterator = getIterator(right);
 assertFalse(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(5);
 right = new NodeTest(10);
 Context contextInitial = new InitialContext();
 assertFalse(expr.compute(contextInitial, left, right));

 left = new NodeTest(5);
 right = new NodeTest(10);
 assertFalse(expr.compute(context, left, right));

 left = new NodeTest(5);
 right = new NodeTest(10);
 contextInitial.setNode(left);
 assertFalse(expr.compute(contextInitial, right));

 left = new NodeTest(5);
 right = new NodeTest(10);
 assertFalse(expr.compute(contextInitial, left, right));

 left = new NodeTest(10);
 right = new NodeTest(5);
 leftIterator = getIterator(left);
 rightIterator = getIterator(right);
 assertFalse(expr.compute(context, leftIterator, rightIterator));

 left = new NodeTest(10);
 right = new NodeTest(5);
 contextInitial.setNode(left);
 assertFalse(expr.compute(contextInitial, right));

 left = new NodeTest(10);
 right = new NodeTest(5);
 assertFalse(expr.compute(context, left, right));

 left = new NodeTest(10);
 right = new NodeTest(5);
 contextInitial.setNode(left);
 assertFalse(expr.compute(contextInitial, right));
}

}