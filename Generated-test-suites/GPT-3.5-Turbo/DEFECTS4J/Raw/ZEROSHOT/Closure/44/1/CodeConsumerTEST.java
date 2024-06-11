import org.junit.*;
import static org.junit.Assert.*;

public class CodeConsumerTest {

   @Test
   public void test_startSourceMapping() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void startSourceMapping(Node node) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.startSourceMapping(null);
   }

   @Test
   public void test_endSourceMapping() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endSourceMapping(Node node) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endSourceMapping(null);
   }

   @Test
   public void test_continueProcessing() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         boolean continueProcessing() {
            // TODO Auto-generated method stub
            return false;
         }
      };

      boolean result = codeConsumer.continueProcessing();
      assertFalse(result);
   }

   @Test
   public void test_getLastChar() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         char getLastChar() {
            // TODO Auto-generated method stub
            return 0;
         }
      };

      char result = codeConsumer.getLastChar();
      assertEquals(0, result);
   }

   @Test
   public void test_append() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void append(String str) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.append("test");
   }

   @Test
   public void test_appendBlockStart() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void appendBlockStart() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.appendBlockStart();
   }

   @Test
   public void test_appendBlockEnd() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void appendBlockEnd() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.appendBlockEnd();
   }

   @Test
   public void test_startNewLine() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void startNewLine() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.startNewLine();
   }

   @Test
   public void test_maybeLineBreak() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void maybeLineBreak() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.maybeLineBreak();
   }

   @Test
   public void test_maybeCutLine() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void maybeCutLine() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.maybeCutLine();
   }

   @Test
   public void test_endLine() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endLine() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endLine();
   }

   @Test
   public void test_notePreferredLineBreak() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void notePreferredLineBreak() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.notePreferredLineBreak();
   }

   @Test
   public void test_beginBlock() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void beginBlock() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.beginBlock();
   }

   @Test
   public void test_endBlock() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endBlock() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endBlock();
   }

   @Test
   public void test_endBlockWithBoolean() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endBlock(boolean shouldEndLine) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endBlock(true);
   }

   @Test
   public void test_listSeparator() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void listSeparator() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.listSeparator();
   }

   @Test
   public void test_endStatement() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endStatement() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endStatement();
   }

   @Test
   public void test_endStatementWithBoolean() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endStatement(boolean needSemiColon) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endStatement(true);
   }

   @Test
   public void test_maybeEndStatement() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void maybeEndStatement() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.maybeEndStatement();
   }

   @Test
   public void test_endFunction() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endFunction() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endFunction();
   }

   @Test
   public void test_endFunctionWithBoolean() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endFunction(boolean statementContext) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endFunction(true);
   }

   @Test
   public void test_beginCaseBody() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void beginCaseBody() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.beginCaseBody();
   }

   @Test
   public void test_endCaseBody() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endCaseBody() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endCaseBody();
   }

   @Test
   public void test_add() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void add(String newcode) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.add("test");
   }

   @Test
   public void test_appendOp() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void appendOp(String op, boolean binOp) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.appendOp("+", true);
   }

   @Test
   public void test_addOp() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void addOp(String op, boolean binOp) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.addOp("+", true);
   }

   @Test
   public void test_addNumber() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void addNumber(double x) {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.addNumber(1.0);
   }

   @Test
   public void test_isNegativeZeroTrue() {
      boolean result = CodeConsumer.isNegativeZero(-0.0);
      assertTrue(result);
   }

   @Test
   public void test_isNegativeZeroFalse() {
      boolean result = CodeConsumer.isNegativeZero(0.0);
      assertFalse(result);
   }

   @Test
   public void test_isWordCharTrue() {
      boolean result = CodeConsumer.isWordChar('a');
      assertTrue(result);
   }

   @Test
   public void test_isWordCharFalse() {
      boolean result = CodeConsumer.isWordChar(' ');
      assertFalse(result);
   }

   @Test
   public void test_shouldPreserveExtraBlocks() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         boolean shouldPreserveExtraBlocks() {
            // TODO Auto-generated method stub
            return false;
         }
      };

      boolean result = codeConsumer.shouldPreserveExtraBlocks();
      assertFalse(result);
   }

   @Test
   public void test_breakAfterBlockForTrue() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         boolean breakAfterBlockFor(Node n, boolean statementContext) {
            // TODO Auto-generated method stub
            return true;
         }
      };

      boolean result = codeConsumer.breakAfterBlockFor(null, true);
      assertTrue(result);
   }

   @Test
   public void test_breakAfterBlockForFalse() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         boolean breakAfterBlockFor(Node n, boolean statementContext) {
            // TODO Auto-generated method stub
            return false;
         }
      };

      boolean result = codeConsumer.breakAfterBlockFor(null, false);
      assertFalse(result);
   }

   @Test
   public void test_endFile() {
      CodeConsumer codeConsumer = new CodeConsumer() {

         @Override
         void endFile() {
            // TODO Auto-generated method stub

         }
      };

      codeConsumer.endFile();
   }

}