import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private CompilerInput input;
  private Node root;

  @Before
  public void setup() {
    compiler = Mockito.mock(AbstractCompiler.class);
    input = new CompilerInput("test.js", null, null, null, null);
    root = NodeFactory.createRoot();
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testProcess_Externs_NoErrors() {
    // Given
    Node externs = NodeFactory.createBlock(new Node(TOKEN_BLOCK, null));
    varCheck.process(externs, root);

    // Then
    // No errors should be reported
    Mockito.verify(compiler, never()).report(ArgumentMatchers.any(), ArgumentMatchers.isInstanceOf(DiagnosticType.class));
  }

  @Test
  public void testProcess_Externs_UndefinedVarError() {
    // Given
    String varName = "testVar";
    Node nameNode = NodeFactory.createString(TOKEN_NAME, varName);
    Node varNode = NodeFactory.createVar(TOKEN_VAR, nameNode);
    Node externs = NodeFactory.createBlock(new Node(TOKEN_BLOCK, null));
    externs.addChildToBack(varNode);

    varCheck = new VarCheck(compiler);

    // When
    varCheck.process(externs, root);

    // Then
    Mockito.verify(compiler).report(ArgumentMatchers.any(), ArgumentMatchers.isInstanceOf(UNDEFINED_VAR_ERROR));
  }

  @Test
  public void testVisit_UndefinedVar_Error() {
    String varName = "testVar";
    Node varNode = NodeFactory.createName(TOKEN_NAME, varName);
    Node parent = NodeFactory.createBlock(new Node(TOKEN_BLOCK, null));

    // Given
    VarCheck varCheck = new VarCheck(compiler, true);
    NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);
    Mockito.when(nodeTraversal.getInput()).thenReturn(input);
    Mockito.when(nodeTraversal.getScope()).thenReturn(new Scope());

    // When
    varCheck.visit(nodeTraversal, null, parent);
    varCheck.visit(nodeTraversal, varNode, parent);

    // Then
    Mockito.verify(nodeTraversal).report(varNode, UNDEFINED_VAR_ERROR, varName);
  }

  @Test
  public void testVisit_UndefinedVar_Warning() {
    String varName = "testVar";
    Node varNode = NodeFactory.createName(TOKEN_NAME, varName);
    Node parent = NodeFactory.createBlock(new Node(TOKEN_BLOCK, null));

    // Given
    VarCheck varCheck = new VarCheck(compiler, false);
    NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);
    Mockito.when(nodeTraversal.getInput()).thenReturn(input);
    Mockito.when(nodeTraversal.getScope()).thenReturn(new Scope());

    // When
    varCheck.visit(nodeTraversal, null, parent);
    varCheck.visit(nodeTraversal, varNode, parent);

    // Then
    Mockito.verify(nodeTraversal, never()).report(varNode, UNDEFINED_VAR_ERROR, varName);
    Mockito.verify(nodeTraversal).report(varNode, UNDEFINED_EXTERN_VAR_ERROR, varName);
  }

  @Test
  public void testVisit_FunctionDeclarationWithEmptyName() {
    String functionName = "";
    Node functionNode = NodeFactory.createBlock(
        new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null, Token.NAME,
            NodeFactory.createString(TOKEN_NAME, functionName), null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null));
    Node parent = NodeFactory.createBlock(new Node(TOKEN_BLOCK, null));

    // Given
    VarCheck varCheck = new VarCheck(compiler);

    // When
    varCheck.visit(null, null, parent);
    varCheck.visit(null, functionNode, parent);

    // Then
    Mockito.verify(compiler, never()).report(ArgumentMatchers.any(), ArgumentMatchers.isInstanceOf(INVALID_FUNCTION_DECL));
  }

  @Test(expected = IllegalStateException.class)
  public void testVisit_DuplicateVarDeclaration() {
    String varName = "testVar";
    Node firstVarNode = NodeFactory.createName(TOKEN_NAME, varName);
    Node secondVarNode = NodeFactory.createName(TOKEN_NAME, varName);
    Node parent = NodeFactory.createBlock(new Node(TOKEN_BLOCK, null));

    // Given
    VarCheck varCheck = new VarCheck(compiler);

    // When
    varCheck.visit(null, null, parent);
    varCheck.visit(null, firstVarNode, parent);
    varCheck.visit(null, secondVarNode, parent);

    // Then
    // An IllegalStateException should be thrown
  }

  // Add more test cases if needed to test edge cases and exception handling scenarios
}