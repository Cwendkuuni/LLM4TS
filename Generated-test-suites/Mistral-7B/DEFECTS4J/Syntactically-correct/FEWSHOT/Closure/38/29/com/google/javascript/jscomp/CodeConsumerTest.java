package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    double value = -0.0;
    when(consumer.isNegativeZero(value)).thenReturn(true);
    assertTrue(consumer.isNegativeZero(value));
  }

  @Test
  public void testIsNegativeZeroFalse() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    double value = -1.0;
    when(consumer.isNegativeZero(value)).thenReturn(false);
    assertFalse(consumer.isNegativeZero(value));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    char c = '_';
    when(consumer.isWordChar(c)).thenReturn(true);
    assertTrue(consumer.isWordChar(c));
  }

  @Test
  public void testIsWordCharFalse() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    char c = '!';
    when(consumer.isWordChar(c)).thenReturn(false);
    assertFalse(consumer.isWordChar(c));
  }

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }
    };
    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void append(String str) {
        super.append(str);
      }
    };
    consumer.append("test");
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void appendBlockStart() {
        super.appendBlockStart();
      }
    };
    consumer.appendBlockStart();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void appendBlockEnd() {
        super.appendBlockEnd();
      }
    };
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void startNewLine() {
        super.startNewLine();
      }
    };
    consumer.startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void maybeLineBreak() {
        super.maybeLineBreak();
      }
    };
    consumer.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void maybeCutLine() {
        super.maybeCutLine();
      }
    };
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endLine() {
        super.endLine();
      }
    };
    consumer.endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void notePreferredLineBreak() {
        super.notePreferredLineBreak();
      }
    };
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void beginBlock() {
        super.beginBlock();
      }
    };
    consumer.beginBlock();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endBlock(boolean shouldEndLine) {
        super.endBlock(shouldEndLine);
      }
    };
    consumer.endBlock();
    consumer.endBlock(true);
    assertEquals("}", consumer.getLastChar());
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void listSeparator() {
        super.listSeparator();
      }
    };
    consumer.listSeparator();
    assertEquals(",", consumer.getLastChar());
    consumer.maybeLineBreak();
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endStatement() {
        super.endStatement();
      }
    };
    consumer.endStatement();
    consumer.endStatement(true);
    assertEquals(";", consumer.getLastChar());
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endFunction(boolean statementContext) {
        super.endFunction(statementContext);
      }
    };
    consumer.endFunction();
    consumer.endFunction(true);
    assertTrue(consumer.sawFunction);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void beginCaseBody() {
        super.beginCaseBody();
      }
    };
    consumer.beginCaseBody();
    assertEquals(":", consumer.getLastChar());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endCaseBody() {
        super.endCaseBody();
      }
    };
    consumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void add(String newcode) {
        super.add(newcode);
      }
    };
    consumer.add("test");
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void addOp(String op, boolean binOp) {
        super.addOp(op, binOp);
      }
    };
    consumer.addOp("+", false);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void addNumber(double x) {
        super.addNumber(x);
      }
    };
    consumer.addNumber(0.0);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return false;
      }
    };
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node n = mock(Node.class);
    when(consumer.breakAfterBlockFor(n, false)).thenReturn(true);
    assertTrue(consumer.breakAfterBlockFor(n, false));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node n = mock(Node.class);
    when(consumer.breakAfterBlockFor(n, false)).thenReturn(false);
    assertFalse(consumer.breakAfterBlockFor(n, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endFile() {
        super.endFile();
      }
    };
    consumer.endFile();
  }
}