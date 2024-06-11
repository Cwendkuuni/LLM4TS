import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if method was called
    assertTrue(consumer.sourceMappingsStarted);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Check if method was called twice and source mappings are reset
    assertFalse(consumer.sourceMappingsStarted);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingWhenFalse() {
    CodeConsumer consumer = new StubCodeConsumerWithContinueProcessingFalse();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("test");
    consumer.append(" = 5;");
    assertEquals("test = 5;", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.append(" = 5;");
    assertEquals("test = 5;", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.appendBlockStart();
    consumer.append("function testFunction() {}\n");
    consumer.appendBlockEnd();
    consumer.append("test2 = 5;");
    assertEquals("test{\nfunction testFunction() {}\n}\ntest2 = 5;", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.appendBlockStart();
    consumer.append("function testFunction() {}\n");
    consumer.appendBlockEnd();
    consumer.append("test2 = 5;");
    assertEquals("test{\nfunction testFunction() {}\n}\ntest2 = 5;", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.startNewLine();
    consumer.append("function testFunction() {}\n");
    consumer.append("test2 = 5;");
    assertEquals("test\nfunction testFunction() {}\n test2 = 5;", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeLineBreak();
    consumer.append("function testFunction() {}\n");
    consumer.append(" test2 = 5;");
    assertEquals("test\n function testFunction() {}\n test2 = 5;", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeCutLine();
    consumer.append("function testFunction() {}\n");
    consumer.append(" test2 = 5;");
    assertEquals("test\nfunction testFunction() {}\ntest2 = 5;", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    consumer.append("function testFunction() {}\n");
    consumer.append(" test2 = 5;");
    assertEquals("test\nfunction testFunction() {}\ntest2 = 5;", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.notePreferredLineBreak();
    consumer.append("function testFunction() {}\n");
    consumer.append(" test2 = 5;");
    assertEquals("test\nfunction testFunction() {}\ntest2 = 5;", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.beginBlock();
    consumer.append("function testFunction() {}\n");
    consumer.append(" test2 = 5;");
    consumer.endBlock();
    assertEquals("test{\nfunction testFunction() {}\n}\ntest2 = 5;", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.beginBlock();
    consumer.append("function testFunction() {}\n");
    consumer.append(" test2 = 5;");
    consumer.endBlock();
    assertEquals("test{\nfunction testFunction() {}\n}\ntest2 = 5;", consumer.getCode());
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.beginBlock();
    consumer.append("function testFunction() {}\n");
    consumer.append(" test2 = 5;");
    consumer.endBlock(true);
    assertEquals("test{\nfunction testFunction() {}\n}\ntest2 = 5;\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test1 = 1,\ntest2 = 2,\ntest3 = 3");
    assertEquals("test1 = 1,\ntest2 = 2,\ntest3 = 3", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test = 5;");
    consumer.endStatement();
    consumer.append("test2 = 6;");
    assertEquals("test = 5;\ntest2 = 6;", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test = 5;");
    consumer.endStatement(true);
    consumer.append("test2 = 6;");
    assertEquals("test = 5;\ntest2 = 6;", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test = 5;");
    consumer.maybeEndStatement();
    consumer.append("test2 = 6;");
    assertEquals("test = 5\ntest2 = 6;", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function testFunction() {}\n");
    consumer.endFunction();
    consumer.append("test = 5;");
    assertEquals("function testFunction() {}\ntest = 5;", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function testFunction() {}\n");
    consumer.endFunction(true);
    consumer.append("test = 5;");
    assertEquals("function testFunction() {}\ntest = 5;", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("case testCase:\n");
    consumer.beginCaseBody();
    consumer.append("test = 5;");
    consumer.endCaseBody();
    assertEquals("case testCase:\n{\ntest = 5;\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test = ");
    consumer.add("5");
    assertEquals("test = 5", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test = ");
    consumer.addOp("+", false);
    consumer.add(5);
    assertEquals("test = 5", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test = ");
    consumer.addNumber(5);
    assertEquals("test = 5", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('A'));
    assertTrue(isWordChar('0'));
    assertTrue(isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertEquals(false, consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

class StubCodeConsumer {
  boolean sourceMappingsStarted = false;
  String code = "";

  void append(String s) {
    code += s;
  }

  char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  String getCode() {
    return code;
  }
}

class StubCodeConsumerWithContinueProcessingFalse extends StubCodeConsumer {
  @Override
  boolean continueProcessing() {
    return false;
  }
}