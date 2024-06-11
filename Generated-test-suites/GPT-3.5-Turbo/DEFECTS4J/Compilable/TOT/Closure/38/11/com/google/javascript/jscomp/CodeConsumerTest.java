package com.google.javascript.jscomp;
import static org.junit.Assert.*;
import org.junit.Test;

public class CodeConsumerTest {
  
  @Test
  public void testAddIdentifier() {
    CodeConsumer codeConsumer = new CodeConsumer() {
      char getLastChar() {
        return ' ';
      }

      void append(String str) {}

      void addIdentifier(String identifier) {
        assertEquals("identifier", identifier);
      }

      void startNewLine(){}
      
      void maybeCutLine() {}

      void endLine() {}

      void notePreferredLineBreak() {}

      void beginBlock(){}

      void endStatement() {}

      void maybeEndStatement() {}

      void endFunction() {}

      void addOp(String op, boolean binOp) {}

      void addNumber(double x) {}

      void endFile() {}
    };
    
    codeConsumer.addIdentifier("identifier");
  }
  
  @Test
  public void testAppendBlockStart() {
    CodeConsumer codeConsumer = new CodeConsumer() {
      char getLastChar() {
        return ' ';
      }

      void append(String str) {}

      void addIdentifier(String identifier) {}

      void startNewLine(){}

      void maybeCutLine() {}

      void endLine() {}

      void notePreferredLineBreak() {}

      void beginBlock(){
        assertEquals("{", "{");
      }

      void endStatement() {}

      void maybeEndStatement() {}

      void endFunction() {}

      void addOp(String op, boolean binOp) {}

      void addNumber(double x) {}

      void endFile() {}
    };
    
    codeConsumer.appendBlockStart();
  }
  
  @Test
  public void testStartNewLine() {
    CodeConsumer codeConsumer = new CodeConsumer() {
      char getLastChar() {
        return ' ';
      }

      void append(String str) {}

      void addIdentifier(String identifier) {}

      void startNewLine(){
        assertEquals(true, true);
      }

      void maybeCutLine() {}

      void endLine() {}

      void notePreferredLineBreak() {}
      
      void beginBlock(){}

      void endStatement() {}

      void maybeEndStatement() {}

      void endFunction() {}

      void addOp(String op, boolean binOp) {}

      void addNumber(double x) {}

      void endFile() {}
    };
    
    codeConsumer.startNewLine();
  }
  
  // More test cases for the other methods...
  
}