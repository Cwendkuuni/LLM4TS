package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  
  @Test
  public void testStatementNeedsEnded() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {}
    };
    assertFalse(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);
    
    consumer.endStatement();
    assertTrue(consumer.statementNeedsEnded);
    
    consumer.maybeEndStatement();
    consumer.maybeEndStatement();
    consumer.maybeEndStatement();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {
        assertEquals("{", str);
      }
    };
    
    consumer.appendBlockStart();
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {
        assertEquals("}", str);
      }
    };
    
    consumer.appendBlockEnd();
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {}
      @Override
      void startNewLine() {
        assertTrue(true);
      }
    };
    
    consumer.startNewLine();
  }

  @Test
  public void testMaybeLinBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {}
      @Override
      void maybeCutLine() {
        assertTrue(true);
      } 
    };
    
    consumer.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {}
    };
    
    consumer.maybeCutLine();
  }
  
  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {}
    };
    
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {
        assertEquals(";", str);
      } 
    };
    
    consumer.endStatement();
    assertEquals(false, consumer.statementNeedsEnded);
    consumer.endStatement(true);
    assertEquals(false, consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {
        assertEquals(";", str);
      }
    };
    
    consumer.maybeEndStatement();
    assertEquals(false, consumer.statementNeedsEnded);
    consumer.maybeEndStatement();
    assertEquals(true, consumer.statementNeedsEnded);  
  }
  
  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {}
    };
    
    assertTrue(consumer.breakAfterBlockFor(null, true));
    assertFalse(consumer.breakAfterBlockFor(null, false));
  }
  
  @Test
  public void testEndFile() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {return 'a';}
      @Override
      void append(String str) {}
    };
    
    consumer.endFile();
  }
  
}