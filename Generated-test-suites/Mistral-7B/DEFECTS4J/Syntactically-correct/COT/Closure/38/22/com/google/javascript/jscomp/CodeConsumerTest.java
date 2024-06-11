package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new TestCodeConsumer();
 }

 @Test
 public void testStartSourceMapping() {
 consumer.startSourceMapping(null);
 }

 @Test
 public void testEndSourceMapping() {
 consumer.endSourceMapping(null);
 }

 @Test
 public void testContinueProcessing() {
 assertTrue(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 consumer.append("abc");
 assertEquals('c', consumer.getLastChar());
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("identifier");
 assertEquals("identifier", consumer.getCode());
 }

 @Test
 public void testAppend() {
 consumer.append("hello");
 assertEquals("hello", consumer.getCode());
 }

 @Test
 public void testAppendBlockStart() {
 consumer.appendBlockStart();
 assertEquals("{\n", consumer.getCode());
 }

 @Test
 public void testAppendBlockEnd() {
 consumer.appendBlockEnd();
 assertEquals("}\n", consumer.getCode());
 }

 @Test
 public void testStartNewLine() {
 consumer.startNewLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testMaybeLineBreak() {
 consumer.append("abc");
 consumer.maybeLineBreak();
 consumer.append("def");
 assertEquals("abc\ndef", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.append("abc");
 consumer.maybeCutLine();
 assertEquals("abc\n", consumer.getCode());
 }

 @Test
 public void testEndLine() {
 consumer.append("abc");
 consumer.endLine();
 assertEquals("abc\n", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.append("abc");
 consumer.notePreferredLineBreak();
 assertEquals("abc\n", consumer.getCode());
 }

 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 assertEquals("{\n", consumer.getCode());
 }

 @Test
 public void testEndBlock() {
 consumer.append("abc");
 consumer.endBlock();
 assertEquals("abc}\n", consumer.getCode());
 }

 @Test
 public void testEndBlockWithNewline() {
 consumer.append("abc");
 consumer.endBlock(true);
 assertEquals("abc}\n", consumer.getCode());
 }

 @Test
 public void testListSeparator() {
 consumer.append("abc");
 consumer.listSeparator();
 consumer.append("def");
 assertEquals("abc,\ndef", consumer.getCode());
 }

 @Test
 public void testEndStatement() {
 consumer.append("if (true) {\n}");
 consumer.endStatement();
 consumer.append("else {\n}");
 consumer.endStatement();
 assertEquals("if (true) {\n}\nelse {\n}", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.append("if (true) {\n}");
 consumer.maybeEndStatement();
 consumer.append("else {\n}");
 consumer.maybeEndStatement();
 assertEquals("if (true) {\n}\nelse {\n}", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.append("function foo() {\n}\n");
 consumer.endFunction();
 consumer.append("function bar() {\n}");
 consumer.endFunction();
 assertEquals("function foo() {\n}\nfunction bar() {\n}\n", consumer.getCode());
 }

 @Test
 public void testBeginCaseBody() {
 consumer.append("case 1:\n");
 consumer.beginCaseBody();
 consumer.append("// do something\n");
 consumer.endCaseBody();
 assertEquals("case 1:\n// do something\n", consumer.getCode());
 }

 @Test
 public void testAdd() {
 consumer.append("if (x < 0) {\n}");
 consumer.add("else {\n}");
 consumer.append("// something\n");
 assertEquals("if (x < 0) {\n}\nelse {\n// something\n}", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.append("if (x < y + 5) {\n}");
 consumer.addOp("+", false);
 consumer.append("else {\n}");
 consumer.addOp("!=", false);
 consumer.append("z\n");
 assertEquals("if (x < y + 5) {\n}\nelse {\nz !=\n}", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.append("x = 1 + ");
 consumer.addNumber(1.2);
 consumer.append(";\n");
 assertEquals("x = 1 + 1.2;\n", consumer.getCode());
 }

 @Test
 public void testIsNegativeZero() {
 consumer.append("x = -0;\n");
 consumer.addNumber(-0.0);
 consumer.append(";\n");
 assertEquals("x = -0;\n// 0.0\n", consumer.getCode());
 }

 @Test
 public void testIsWordChar() {
 assertTrue(CodeConsumer.isWordChar('_'));
 assertTrue(CodeConsumer.isWordChar('$'));
 assertTrue(CodeConsumer.isWordChar('a'));
 assertTrue(CodeConsumer.isWordChar('1'));
 assertFalse(CodeConsumer.isWordChar(' '));
 }

 @Test(expected = AssertionError.class)
 public void testException() {
 consumer.append("function foo() {\n}");
 consumer.append("function foo() {\n}");
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 Node node = Node.newNode(Node.BLOCK, null, null, null, null);
 assertTrue(consumer.breakAfterBlockFor(node, false));
 }

 @Test
 public void testEndFile() {
 consumer.endFile();
 assertEquals("", consumer.getCode());
 }
}

class TestCodeConsumer extends CodeConsumer {
 @Override
 char getLastChar() {
 return super.getLastChar();
 }

 @Override
 String getCode() {
 String code = super.getCode();
 return code.substring(0, code.length() - 1);
 }
}