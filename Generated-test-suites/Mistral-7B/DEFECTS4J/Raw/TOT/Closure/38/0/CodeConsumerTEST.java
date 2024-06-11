import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMappingAndEndSourceMapping() {
    CodeConsumer codeConsumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    when(codeConsumer.continueProcessing()).thenReturn(true);

    codeConsumer.startSourceMapping(node);
    codeConsumer.endSourceMapping(node);

    // Verify that startSourceMapping and endSourceMapping were called
    // correctly
    assertEquals(1, codeConsumer.startSourceMappingCallCount());
    assertEquals(1, codeConsumer.endSourceMappingCallCount());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    // Test that continueProcessing returns true by default
    assertTrue(codeConsumer.continueProcessing());

    // Test that continueProcessing can be overridden to return false
    codeConsumer = new TestCodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };

    assertFalse(codeConsumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer codeConsumer = new TestCodeConsumer();
    String code = "public class TestClass {\n";
    String expectedLastChar = '\n';

    codeConsumer.append(code);

    assertEquals(expectedLastChar, codeConsumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer codeConsumer = new TestCodeConsumer();
    String code = "public class TestClass {\n" +
                  "  void addIdentifier(String identifier) {\n" +
                  "    add(identifier);\n" +
                  "  }\n" +
                  "}\n";

    codeConsumer.addIdentifier("TestIdentifier");

    StringBuilder expectedCode = new StringBuilder();
    expectedCode.append("public class TestClass {\n")
                 .append("  void addIdentifier(String identifier) {\n")
                 .append("    add(TestIdentifier);\n")
                 .append("  }\n")
                 .append("}\n");

    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.appendBlockStart();

    assertEquals("{\n", codeConsumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer codeConsumer = new TestCodeConsumer();
    codeConsumer.appendBlockStart();
    codeConsumer.appendBlockEnd();

    assertEquals("{\n}\n", codeConsumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("public class TestClass {\n");
    codeConsumer.startNewLine();
    codeConsumer.append("  void testMethod() {\n");

    assertEquals("public class TestClass {\n\n  void testMethod() {\n", codeConsumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("public class TestClass {\n");
    codeConsumer.maybeLineBreak();

    assertEquals("public class TestClass {\n\n", codeConsumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("public class TestClass {\n");
    codeConsumer.maybeCutLine();

    assertEquals("public class TestClass {\n", codeConsumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("public class TestClass {\n");
    codeConsumer.endLine();

    assertEquals("public class TestClass {\n}\n", codeConsumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("public class TestClass {\n");
    codeConsumer.notePreferredLineBreak();

    assertEquals("public class TestClass {\n", codeConsumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.beginBlock();

    assertEquals("{\n", codeConsumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer codeConsumer = new TestCodeConsumer();
    codeConsumer.append("public class TestClass {\n");
    codeConsumer.beginBlock();
    codeConsumer.append("  void testMethod() {\n");
    codeConsumer.endBlock();

    assertEquals("public class TestClass {\n  void testMethod() {\n}\n", codeConsumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.add("first");
    codeConsumer.listSeparator();
    codeConsumer.add("second");

    assertEquals("first,\nsecond\n", codeConsumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("public class TestClass {\n");
    codeConsumer.append("  void testMethod() {\n");
    codeConsumer.endStatement();

    assertEquals("public class TestClass {\n  void testMethod() {\n}\n", codeConsumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("function TestFunction() {\n");
    codeConsumer.endFunction();

    assertEquals("function TestFunction() {\n}\n", codeConsumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("switch (x) {\n  case 1:\n");
    codeConsumer.beginCaseBody();

    assertEquals("switch (x) {\n  case 1:\n    // empty body\n}", codeConsumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("switch (x) {\n  case 1:\n    // body\n  }\n");
    codeConsumer.endCaseBody();

    assertEquals("switch (x) {\n  case 1:\n    // body\n  }\n", codeConsumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.addNumber(1.0);
    codeConsumer.addNumber(2.0);

    assertEquals("1.0\n2.0\n", codeConsumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    assertTrue(codeConsumer.isNegativeZero(0.0));
    assertFalse(codeConsumer.isNegativeZero(1.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    assertTrue(codeConsumer.isWordChar('_'));
    assertTrue(codeConsumer.isWordChar('$'));
    assertTrue(codeConsumer.isWordChar('A'));
    assertTrue(codeConsumer.isWordChar('0'));
    assertFalse(codeConsumer.isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(node.getParent()).thenReturn(null);

    assertTrue(codeConsumer.shouldPreserveExtraBlocks());

    codeConsumer = new TestCodeConsumer() {
      @Override
      public boolean shouldPreserveExtraBlocks() {
        return false;
      }
    };

    assertFalse(codeConsumer.shouldPreserveExtraBlocks());

    codeConsumer = new TestCodeConsumer();
    when(node.getParent()).thenReturn(mock(CodeConsumer.class));

    assertTrue(codeConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(node.getParent()).thenReturn(null);

    // Test that a line break is not added after a block when it is not a statement context
    assertFalse(codeConsumer.breakAfterBlockFor(node, false));

    // Test that a line break is added after a block when it is a statement context
    assertTrue(codeConsumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer codeConsumer = new TestCodeConsumer();

    codeConsumer.append("public class TestClass {\n");
    codeConsumer.endFile();

    assertEquals("public class TestClass {\n}\n", codeConsumer.getCode());
  }

  private int startSourceMappingCallCount() {
    return ((TestCodeConsumer) this).startSourceMappingCallCount;
  }

  private int endSourceMappingCallCount() {
    return ((TestCodeConsumer) this).endSourceMappingCallCount;
  }

  private int startStatementCallCount() {
    return ((TestCodeConsumer) this).startStatementCallCount;
  }

  private int endStatementCallCount() {
    return ((TestCodeConsumer) this).endStatementCallCount;
  }

  private int appendCallCount() {
    return ((TestCodeConsumer) this).appendCallCount;
  }

  private StringBuilder getCode() {
    return ((TestCodeConsumer) this).code;
  }

  private class TestCodeConsumer extends CodeConsumer {
    int startSourceMappingCallCount = 0;
    int endSourceMappingCallCount = 0;
    int startStatementCallCount = 0;
    int endStatementCallCount = 0;
    int appendCallCount = 0;
    StringBuilder code = new StringBuilder();

    @Override
    void startSourceMapping(Node node) {
      startSourceMappingCallCount++;
    }

    @Override
    void endSourceMapping(Node node) {
      endSourceMappingCallCount++;
    }

    @Override
    void startStatement() {
      startStatementCallCount++;
    }

    @Override
    void endStatement() {
      endStatementCallCount++;
    }

    @Override
    void append(String str) {
      code.append(str);
      appendCallCount++;
    }
  }
}