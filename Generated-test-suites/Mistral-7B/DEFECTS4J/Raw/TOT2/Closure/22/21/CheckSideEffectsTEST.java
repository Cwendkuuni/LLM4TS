import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createCallExpression;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createIfStatement;
import static com.google.javascript.rhino.NodeFactory.createExpressionStatement;
import static com.google.javascript.rhino.NodeFactory.createIdentifierExpression;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createIntegerLiteral;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createAddExpression;
import static com.google.javascript.rhino.NodeFactory.createNameExpression;
import static com.google.javascript.rhino.NodeFactory.createCall;
import static com.google.javascript.rhino.NodeFactory.createVoidStatement;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.CheckSideEffects.*;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);

    // Basic functionality test
    String code = "function test() { var s = 'this string is continued on the next line but you forgot the +'; }";
    Node externs = NodeFactory.compile(code);
    Node root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Edge case: empty block
    code = "function test() {}";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Edge case: empty statement
    code = "function test() { }";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Edge case: comment
    code = "function test() { // this comment should not trigger a warning }";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Edge case: string concatenation
    code = "function test() { var s = 'this string is '; s += 'continued on the next line but you forgot the +'; }";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Edge case: void statement
    code = "function test() { ; }";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Edge case: function call
    code = "function test() { foo(); }";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Exception handling: empty catch block
    code = "try { x = 1 / 0; } catch (e) {}";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Exception handling: non-empty catch block
    code = "try { x = 1 / 0; } catch (e) { x = 42; }";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Exception handling: finally block
    code = "try { x = 1 / 0; } finally { x = 42; }";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Exception handling: throw statement in a try block
    code = "try { throw new Error('test'); } catch (e) { x = 42; }";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Exception handling: throw statement outside of a try block
    code = "throw new Error('test');";
    externs = NodeFactory.compile(code);
    root = NodeFactory.parse(code);
    checkSideEffects.process(externs, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
}