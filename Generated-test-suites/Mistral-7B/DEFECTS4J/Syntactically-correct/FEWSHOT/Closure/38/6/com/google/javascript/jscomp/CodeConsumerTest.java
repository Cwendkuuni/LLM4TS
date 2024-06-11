package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

 @Test
 public void testShouldPreserveExtraBlocks() {
 CodeConsumer consumer = new TestCodeConsumer();
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockForFalseStatementContext() {
 CodeConsumer consumer = new TestCodeConsumer();
 Node node = Node.newBlock("if (true) { }");
 boolean result = consumer.breakAfterBlockFor(node, false);
 assertFalse(result);
 }

 @Test
 public void testBreakAfterBlockForTrueStatementContext() {
 CodeConsumer consumer = new TestCodeConsumer();
 Node node = Node.newBlock("if (true) { }");
 boolean result = consumer.breakAfterBlockFor(node, true);
 assertTrue(result);
 }

 @Test
 public void testGetLastChar() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("abc");
 assertEquals('c', consumer.getLastChar());
 }

 @Test
 public void testAddIdentifier() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.addIdentifier("foo");
 assertEquals("foo", consumer.getCode());
 }

 @Test
 public void testAppend() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("abc");
 assertEquals("abc", consumer.getCode());
 }

 @Test
 public void testAddBlockStart() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.appendBlockStart();
 assertEquals("{\n", consumer.getCode());
 }

 @Test
 public void testAddBlockEnd() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.appendBlockEnd();
 assertEquals("}\n", consumer.getCode());
 }

 @Test
 public void testStartNewLine() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.startNewLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testMaybeLineBreak() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("ab");
 consumer.maybeLineBreak();
 assertEquals("ab\n", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("ab");
 consumer.maybeCutLine();
 assertEquals("a\nb", consumer.getCode());
 }

 @Test
 public void testEndLine() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("ab");
 consumer.endLine();
 assertEquals("ab\n", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.notePreferredLineBreak();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testBeginBlock() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.beginBlock();
 assertEquals("{\n", consumer.getCode());
 }

 @Test
 public void testEndBlock() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("ab");
 consumer.beginBlock();
 consumer.endBlock();
 assertEquals("ab\n}\n", consumer.getCode());
 }

 @Test
 public void testListSeparator() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.listSeparator();
 assertEquals(",\n", consumer.getCode());
 }

 @Test
 public void testEndStatement() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("ab");
 consumer.endStatement();
 assertEquals("ab;\n", consumer.getCode());
 }

 @Test
 public void testEndStatementWithSemiColon() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("ab");
 consumer.endStatement(true);
 assertEquals("ab;\n", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("ab");
 consumer.maybeEndStatement();
 assertEquals("ab\n", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatementWithSemiColon() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("ab");
 consumer.maybeEndStatement(true);
 assertEquals("ab;\n", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("function foo() { }");
 consumer.endFunction();
 assertEquals("function foo() { }\n", consumer.getCode());
 }

 @Test
 public void testEndFunctionWithStatementContext() {
 CodeConsumer consumer = new TestCodeConsumer();consumer.append("function foo() { return 42; }");
 consumer.endFunction(true);
 assertEquals("function foo() {\n  return 42;\n}\n", consumer.getCode());
 }

 @Test
 public void testBeginCaseBody() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.beginCaseBody();
 assertEquals(":\n", consumer.getCode());
 }

 @Test
 public void testEndCaseBody() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("case 1:");
 consumer.beginCaseBody();
 consumer.append("return 42;");
 consumer.endCaseBody();
 assertEquals("case 1:\n{\n  return 42;\n}\n", consumer.getCode());
 }

 @Test
 public void testAdd() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.add("abc");
 assertEquals("abc", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.append("a + b");
 consumer.addOp("+", true);
 assertEquals("a + b\n+", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.addNumber(42.0);
 assertEquals("42.0\n", consumer.getCode());
 }

 @Test
 public void testAddNumberWithNegativeZero() {
 CodeConsumer consumer = new TestCodeConsumer();
 consumer.addNumber(-0.0);
 assertEquals("-0.0\n", consumer.getCode());
 }

 @Test
 public void testIsNegativeZero() {
 CodeConsumer consumer = new TestCodeConsumer();
 assertTrue(consumer.isNegativeZero(-0.0));
 assertTrue(consumer.isNegativeZero(Double.NEGATIVE_INFINITY));
 assertFalse(consumer.isNegativeZero(0.0));
 assertFalse(consumer.isNegativeZero(Double.POSITIVE_INFINITY));
 }

 @Test
 public void testIsWordChar() {
 CodeConsumer consumer = new TestCodeConsumer();
 assertTrue(consumer.isWordChar('_'));
 assertTrue(consumer.isWordChar('$'));
 assertTrue(consumer.isWordChar('A'));
 assertTrue(consumer.isWordChar('1'));
 assertTrue(consumer.isWordChar('z'));
 assertFalse(consumer.isWordChar('('));
 assertFalse(consumer.isWordChar(')'));
 assertFalse(consumer.isWordChar('?'));
 assertFalse(consumer.isWordChar('!'));
 }
}

class TestCodeConsumer extends CodeConsumer {
 @Override
 char getLastChar() {
 return super.getCode().charAt(super.getCode().length() - 1);
 }

 @Override
 void endLine() {
 super.endLine();
 getCode().append("\n");
 }

 @Override
 String getCode() {
 String code = super.getCode();
 return new String(code.toCharArray());
 }
}