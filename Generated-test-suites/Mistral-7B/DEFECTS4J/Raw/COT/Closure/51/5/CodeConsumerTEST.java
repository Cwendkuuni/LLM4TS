import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    consumer = (CodeConsumer) mock(CodeConsumer.class);
  }

  // Basic test cases for all methods

  @Test
  public void testStartSourceMapping() {
    consumer.startSourceMapping(createNode(""));
    verify(consumer).startSourceMapping((Node) anyObject());
  }

  @Test
  public void testEndSourceMapping() {
    consumer.endSourceMapping(createNode(""));
    verify(consumer).endSourceMapping((Node) anyObject());
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
    verify(consumer).continueProcessing();
  }

  @Test
  public void testGetLastChar() {
    when(consumer.getLastChar()).thenReturn('a');
    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("test");
    verify(consumer).append((String) eq("test"));
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    verify(consumer).append("{");
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    verify(consumer).append("}");
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    verify(consumer).startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    verify(consumer).maybeCutLine();
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
    verify(consumer).maybeCutLine();
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    verify(consumer).endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
    verify(consumer).notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    verify(consumer).append("{");
    verify(consumer).endLine();
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
    verify(consumer).append("}");
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    consumer.endBlock(true);
    verify(consumer).append("}");
    verify(consumer).endLine();
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    verify(consumer).add(",");
    verify(consumer).maybeLineBreak();
  }

  @Test
  public void testEndStatement() {
    consumer.endStatement();
    verify(consumer).endStatement(false);
  }

  @Test
  public void testEndStatementWithNeedSemiColon() {
    consumer.endStatement(true);
    verify(consumer).endStatement(true);
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.maybeEndStatement();
    verify(consumer).maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    consumer.endFunction();
    verify(consumer).endFunction(false);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.endFunction(true);
    verify(consumer).endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginCaseBody();
    verify(consumer).append(":");
  }

  @Test
  public void testEndCaseBody() {
    consumer.endCaseBody();
    verifyNoMoreInteractions(consumer);
  }

  @Test
  public void testAdd() {
    Node node = createNode("identifier");
    consumer.addIdentifier("test");
    consumer.add(node.toString());
    verify(consumer).add((String) eq("testidentifier"));
  }

  @Test
  public void testAddOp() {
    consumer.addOp("+", false);
    consumer.addOp("++", false);
    consumer.addOp("=", true);
    consumer.addOp("!=", true);
    consumer.addOp("&&", true);
    consumer.addOp("||", true);
    verify(consumer).append("+");
    verify(consumer).append("++");
    verify(consumer).append("=");
    verify(consumer).append("!=");
    verify(consumer).append("&&");
    verify(consumer).append("||");

    consumer.appendOp("+", true);
    consumer.appendOp("++", true);
    consumer.appendOp("=", false);
    consumer.appendOp("!=", false);
    consumer.appendOp("&&", false);
    consumer.appendOp("||", false);
    verify(consumer).append("+");
    verify(consumer).append("++");
    verify(consumer).append("=");
    verify(consumer).append("!=");
    verify(consumer).append("&&");
    verify(consumer).append("||");
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(1.2);
    consumer.addNumber(-1.2);
    consumer.addNumber(1234567890123.456);
    verify(consumer).append("1.2");
    verify(consumer).append("-1.2");
    verify(consumer).append("1234567890123.456");
  }

  // Edge cases and exception handling tests

  // check if method isAbstract is called when trying to invoke abstract method
  @Test(expected = IllegalStateException.class)
  public void testInvokeAbstractMethod() {
    CodeConsumer concreteConsumer = new ConcreteCodeConsumer();
    concreteConsumer.append("abc");
    concreteConsumer.endStatement();
    concreteConsumer.statementNeedsEnded = true;
    consumer = (CodeConsumer) proxy(CodeConsumer.class);
    doReturn(concreteConsumer).when(consumer).getProxy();
    consumer.endBlock();
  }

  @Test
  public void testAppendWhenStatementNeedsEndedIsTrue() {
    when(consumer.getLastChar()).thenReturn(';');
    consumer.append("test");
    consumer.statementNeedsEnded = true;
    consumer.append(";");
    verify(consumer).append("test;");
  }

  @Test
  public void testAppendWhenStatementStartedIsTrue() {
    consumer.statementStarted = true;
    consumer.append("test");
    verify(consumer).append("test");
  }

  @Test
  public void testStartNewLineWhenLastCharacterIsNotALineTerminator() {
    consumer.append("test");
    consumer.maybeLineBreak();
    verify(consumer).maybeCutLine();
    verify(consumer).endLine();
  }

  @Test
  public void testMaybeCutLineWhenLastCharacterIsALineTerminator() {
    consumer.append("test\n");
    consumer.maybeCutLine();
    verify(consumer).maybeCutLine();
  }

  @Test
  public void testEndLineWhenLastCharacterIsALineTerminator() {
    consumer.append("test\n");
    consumer.endLine();
    verify(consumer).endLine();
  }

  @Test
  public void testEndLineWhenLastCharacterIsNotALineTerminator() {
    consumer.append("test");
    consumer.endLine();
    verify(consumer).append("\n");
    verify(consumer).endLine();
  }

  @Test
  public void testBeginBlockWhenStatementNeedsEndedIsTrue() {
    consumer.statementNeedsEnded = true;
    consumer.beginBlock();
    verify(consumer).append(";");
    verify(consumer).maybeLineBreak();
    verify(consumer).append("{");
    verify(consumer).endLine();
  }

  @Test
  public void testBeginBlockWhenStatementNeedsEndedIsFalse() {
    consumer.statementNeedsEnded = false;
    consumer.beginBlock();
    verify(consumer).append("{");
    verify(consumer).endLine();
  }

  @Test
  public void testEndBlockWhenStatementContextIsFalse() {
    consumer.endBlock();
    verify(consumer).append("}");

    Node node = createNode("Statement");
    consumer.statementNeedsEnded = false;
    consumer.beginBlock();
    consumer.append("if (true) {");
    consumer.add(node);
    consumer.endBlock();
    verify(consumer).append("}");
  }

  @Test
  public void testEndBlockWhenStatementContextIsTrue() {
    consumer.endBlock(true);
    verify(consumer).append("}");
    verify(consumer).endLine();
  }

  @Test
  public void testListSeparatorWhenLastCharacterIsNotALineTerminator() {
    consumer.append("test");
    consumer.listSeparator();
    verify(consumer).append(",");
    verify(consumer).maybeLineBreak();
  }

  @Test
  public void testListSeparatorWhenLastCharacterIsALineTerminator() {
    consumer.append("test\n");
    consumer.listSeparator();
    verify(consumer).append(",");
    verify(consumer).maybeLineBreak();
  }

  @Test
  public void testMaybeEndStatementWhenStatementNeedsEndedIsTrue() {
    consumer.statementNeedsEnded = true;
    consumer.maybeEndStatement();
    verify(consumer).append(";");
    verify(consumer).maybeLineBreak();
    verify(consumer).endLine();
  }

  @Test
  public void testMaybeEndStatementWhenStatementNeedsEndedIsFalse() {
    consumer.statementNeedsEnded = false;
    consumer.maybeEndStatement();
    verify(consumer).maybeEndStatement();
  }

  @Test
  public void testEndFunctionWhenStatementContextIsFalse() {
    consumer.endFunction();
    verify(consumer).append("}");
    verify(consumer).endLine();
  }

  @Test
  public void testEndFunctionWhenStatementContextIsTrue() {
    consumer.endFunction(true);
    verify(consumer).append("}");
    verify(consumer).endLine();
  }

  // Helper methods

  private Node createNode(String type) {
    return new Node() {
      @Override
      public NodeType getType() {
        return NodeType.fromString(type);
      }
    };
  }
}

class ConcreteCodeConsumer extends CodeConsumer {
  @Override
  void append(String str) {
    super.append(str);
    getProxy().getLastChar();
  }
}