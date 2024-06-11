package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.CompilerTestingBase;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.ExpressionVisitor;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest extends CompilerTestingBase {

 private ExpressionFactory factory = new ExpressionFactory();

 @Test
 public void testCompareEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createNumber(1),
 factory.createNumber(1)
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertTrue(compare.compute(ctx));
 }

 @Test
 public void testCompareUnequal() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createNumber(1),
 factory.createNumber(2)
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertFalse(compare.compute(ctx));
 }

 @Test
 public void testCompareEqualString() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createString("test"),
 factory.createString("test")
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertTrue(compare.compute(ctx));
 }

 @Test
 public void testCompareUnequalString() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createString("test"),
 factory.createString("test2")
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertFalse(compare.compute(ctx));
 }

 @Test
 public void testCompareEqualCollection() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createCollection(new Object[] { 1, 2 }),
 factory.createCollection(new Object[] { 1, 2 })
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertTrue(compare.compute(ctx));
 }

 @Test
 public void testCompareUnequalCollection() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createCollection(new Object[] { 1, 2 }),
 factory.createCollection(new Object[] { 1, 3 })
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertFalse(compare.compute(ctx));
 }

 @Test
 public void testCompareEqualIterator() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createIterator(new Object[] { 1, 2 }),
 factory.createIterator(new Object[] { 1, 2 })
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertTrue(compare.compute(ctx));
 }

 @Test
 public void testCompareUnequalIterator() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createIterator(new Object[] { 1, 2 }),
 factory.createIterator(new Object[] { 1, 3 })
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertFalse(compare.compute(ctx));
 }

 @Test
 public void testCompareEqualPointer() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createPointer(new Object() {
 @Override
 public Object getValue() {
 return 1;
 }
 }),
 factory.createPointer(new Object() {
 @Override
 public Object getValue() {
 return 1;
 }
 })
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertTrue(compare.compute(ctx));
 }

 @Test
 public void testCompareUnequalPointer() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createPointer(new Object() {
 @Override
 public Object getValue() {
 return 1;
 }
 }),
 factory.createPointer(new Object() {
 @Override
 public Object getValue() {
 return 2;
 }
 })
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertFalse(compare.compute(ctx));
 }

 @Test
 public void testCompareEqualNullPointer() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createPointer(null),
 factory.createPointer(null)
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertTrue(compare.compute(ctx));
 }

 @Test
 public void testCompareUnequalNullPointer() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createPointer(null),
 factory.createPointer(new Object() {
 @Override
 public Object getValue() {
 return 1;
 }
 })
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertFalse(compare.compute(ctx));
 }

 @Test
 public void testCompareEqualBoolean() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createBoolean(true),
 factory.createBoolean(true)
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertTrue(compare.compute(ctx));
 }

 @Test
 public void testCompareUnequalBoolean() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createBoolean(true),
 factory.createBoolean(false)
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertFalse(compare.compute(ctx));
 }

 @Test
 public void testCompareEqualNullBoolean() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createBoolean(null),
 factory.createBoolean(null)
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertTrue(compare.compute(ctx));
 }

 @Test
 public void testCompareUnequalNullBoolean() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createBoolean(null),
 factory.createBoolean(true)
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertFalse(compare.compute(ctx));
 }

 @Test
 public void testCompareComplex() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createCollection(new Object[] {
 factory.createIterator(new Object[] { 1, 2 }),
 factory.createIterator(new Object[] { 1, 3 })
 }),
 factory.createCollection(new Object[] {
 factory.createIterator(new Object[] { 1, 1, 2 }),
 factory.createIterator(new Object[] { 1, 1, 3 })
 })
 });
 EvalContext ctx = new EvalContext();
 ctx.setCursor(createEmptyInfoSet());

 assertFalse(compare.compute(ctx));
 }

 @Test
 public void testCompareVisitor() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Expression[] {
 factory.createNumber(1),
 factory.createNumber(1)
 });
 ExpressionVisitor visitor = new ExpressionVisitor() {
 @Override
 public void visitCoreOperationCompare(CoreOperationCompare so) {
 assertEquals(2, so.getPrecedence());
 assertTrue(so.isSymmetric());
 }
 };
 compare.accept(visitor);
 }

}