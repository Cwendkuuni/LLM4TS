import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createFunctionCall;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createReturnStatement;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createThisValue;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createUnaryOperator;

import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  Compiler compiler;
  Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    rootNode = createScript("function f() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('unreachable');" +
        "}");
  }

  @Test
  public void testUnreachableCodeElimination() {
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    traverse(compiler, rootNode, eliminator);
    Node expectedResult = createBlock(createReturnStatement(createThisValue()));
    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @Test
  public void testNoOpStatementsAreRemoved() {
    rootNode = createScript("function f() {" +
        "  a.b.MyClass.prototype.propertyName;" +
        "  return;" +
        "}");
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    traverse(compiler, rootNode, eliminator);
    Node expectedResult = createBlock(createReturnStatement(createThisValue()));
    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @Test
  public void testNestedFunctionCalls() {
    rootNode = createScript("function outer() {" +
        "  function inner() {" +
        "    if (true) {" +
        "      return;" +
        "    }" +
        "    alert('inner unreachable');" +
        "  }" +
        "  inner();" +
        "}");
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    traverse(compiler, rootNode, eliminator);
    Node expectedResult = createBlock(createReturnStatement(createThisValue()));
    assertEquals(expectedResult.toString(), rootNode.toString());
  }
}