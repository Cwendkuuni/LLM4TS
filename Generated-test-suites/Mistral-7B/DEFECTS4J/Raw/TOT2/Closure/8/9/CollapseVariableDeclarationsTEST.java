import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CollapseVariableDeclarationsTest {
  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private NodeFactory nodeFactory;
  private Scope scope;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    passManager = mock(CompilerPassManager.class);
    nodeFactory = mock(NodeFactory.class);
    scope = mock(Scope.class);

    when(compiler.getPassManager()).thenReturn(passManager);
    when(passManager.addPass(CollapseVariableDeclarations.class.getName()))
        .thenReturn(compiler);
    when(nodeFactory.createNode(Token.VAR)).thenReturn(Node.createVar(null));
  }

  @Test
  public void testCollapseSingleVariableDeclaration() {
    Node root = Node.createProgram(nodeFactory);
    Node variableDeclaration = Node.createVar(nodeFactory, "a");
    root.addChildToBack(variableDeclaration);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    ArgumentCaptor<CompilerPass> capturedPass = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(eq(CollapseVariableDeclarations.class.getName()), capturedPass.capture());
    CompilerPass passedPass = capturedPass.getValue();

    assertSame(collapseVariableDeclarations, passedPass);
  }

  @Test
  public void testCollapseVariableDeclarations() {
    Node root = Node.createProgram(nodeFactory);
    Node variableDeclaration1 = Node.createVar(nodeFactory, "a");
    Node variableDeclaration2 = Node.createVar(nodeFactory, "b");
    root.addChildToBack(variableDeclaration1);
    root.addChildToBack(variableDeclaration2);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    ArgumentCaptor<CompilerPass> capturedPass = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(eq(CollapseVariableDeclarations.class.getName()), capturedPass.capture());
    CompilerPass passedPass = capturedPass.getValue();

    assertSame(collapseVariableDeclarations, passedPass);

    Node collapsedVariableDeclaration = root.getLastChild();
    assertNotNull(collapsedVariableDeclaration);
    assertSame(variableDeclaration1, collapsedVariableDeclaration.getFirstChild());
    assertSame(variableDeclaration2, collapsedVariableDeclaration.getLastChild());
  }

  @Test
  public void testCollapseVariableAssignments() {
    Node root = Node.createProgram(nodeFactory);
    Node variableDeclaration = Node.createVar(nodeFactory, "a");
    root.addChildToBack(variableDeclaration);

    Node assignment1 = Node.createExprAssign(nodeFactory, variableDeclaration, Node.createNumber(1));
    root.addChildToBack(assignment1);

    Node assignment2 = Node.createExprAssign(nodeFactory, variableDeclaration, Node.createNumber(2));
    root.addChildToBack(assignment2);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    ArgumentCaptor<CompilerPass> capturedPass = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(eq(CollapseVariableDeclarations.class.getName()), capturedPass.capture());
    CompilerPass passedPass = capturedPass.getValue();

    assertSame(collapseVariableDeclarations, passedPass);

    Node collapsedAssignment = root.getLastChild();
    assertNotNull(collapsedAssignment);
    assertSame(assignment1, collapsedAssignment.getFirstChild());
    assertSame(assignment2, collapsedAssignment.getLastChild());
  }

  @Test
  public void testCollapseVariableDeclarationsWithParentIfStatement() {
    Node root = Node.createProgram(nodeFactory);
    Node ifStatement = Node.createIf(nodeFactory);
    root.addChildToBack(ifStatement);

    Node variableDeclaration1 = Node.createVar(nodeFactory, "a");
    Node variableDeclaration2 = Node.createVar(nodeFactory, "b");

    ifStatement.addChildToBack(variableDeclaration1);
    ifStatement.addChildToBack(variableDeclaration2);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    ArgumentCaptor<CompilerPass> capturedPass = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(eq(CollapseVariableDeclarations.class.getName()), capturedPass.capture());
    CompilerPass passedPass = capturedPass.getValue();

    assertSame(collapseVariableDeclarations, passedPass);

    assertNull(ifStatement.getLastChild());
  }

  @Test
  public void testCollapseVariableDeclarationsWithParentWhileStatement() {
    Node root = Node.createProgram(nodeFactory);
    Node whileStatement = Node.createWhile(nodeFactory);
    root.addChildToBack(whileStatement);

    Node variableDeclaration1 = Node.createVar(nodeFactory, "a");
    Node variableDeclaration2 = Node.createVar(nodeFactory, "b");

    whileStatement.addChildToBack(variableDeclaration1);
    whileStatement.addChildToBack(variableDeclaration2);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    ArgumentCaptor<CompilerPass> capturedPass = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(eq(CollapseVariableDeclarations.class.getName()), capturedPass.capture());
    CompilerPass passedPass = capturedPass.getValue();

    assertSame(collapseVariableDeclarations, passedPass);

    assertNull(whileStatement.getLastChild());
  }

  @Test
  public void testCollapseVariableDeclarationsWithParentForStatement() {
    Node root = Node.createProgram(nodeFactory);
    Node forStatement = Node.createFor(nodeFactory);
    root.addChildToBack(forStatement);

    Node variableDeclaration1 = Node.createVar(nodeFactory, "a");
    Node variableDeclaration2 = Node.createVar(nodeFactory, "b");

    forStatement.addChildToBack(variableDeclaration1);
    forStatement.addChildToBack(variableDeclaration2);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    ArgumentCaptor<CompilerPass> capturedPass = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(eq(CollapseVariableDeclarations.class.getName()), capturedPass.capture());
    CompilerPass passedPass = capturedPass.getValue();

    assertSame(collapseVariableDeclarations, passedPass);

    assertNull(forStatement.getLastChild());
  }

  @Test
  public void testCollapseVariableDeclarationsWithParentSwitchStatement() {
    Node root = Node.createProgram(nodeFactory);
    Node switchStatement = Node.createSwitch(nodeFactory);
    root.addChildToBack(switchStatement);

    Node variableDeclaration1 = Node.createVar(nodeFactory, "a");
    Node variableDeclaration2 = Node.createVar(nodeFactory, "b");

    switchStatement.addChildToBack(variableDeclaration1);
    switchStatement.addChildToBack(variableDeclaration2);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    ArgumentCaptor<CompilerPass> capturedPass = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(eq(CollapseVariableDeclarations.class.getName()), capturedPass.capture());
    CompilerPass passedPass = capturedPass.getValue();

    assertSame(collapseVariableDeclarations, passedPass);

    assertNull(switchStatement.getLastChild());
  }

  @Test
  public void testCollapseVariableDeclarationsWithParentDoWhileStatement() {
    Node root = Node.createProgram(nodeFactory);
    Node doWhileStatement = Node.createDoWhile(nodeFactory);
    root.addChildToBack(doWhileStatement);

    Node variableDeclaration1 = Node.createVar(nodeFactory, "a");
    Node variableDeclaration2 = Node.createVar(nodeFactory, "b");

    doWhileStatement.addChildToBack(variableDeclaration1);
    doWhileStatement.addChildToBack(variableDeclaration2);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    ArgumentCaptor<CompilerPass> capturedPass = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(eq(CollapseVariableDeclarations.class.getName()), capturedPass.capture());
    CompilerPass passedPass = capturedPass.getValue();

    assertSame(collapseVariableDeclarations, passedPass);

    assertNull(doWhileStatement.getLastChild());
  }

  @Test
  public void testCollapseVariableDeclarationsWithParentForInStatement() {
    Node root = Node.createProgram(nodeFactory);
    Node forInStatement = Node.createForIn(nodeFactory);
    root.addChildToBack(forInStatement);

    Node variableDeclaration1 = Node.createVar(nodeFactory, "a");
    Node variableDeclaration2 = Node.createVar(nodeFactory, "b");

    forInStatement.addChildToBack(variableDeclaration1);
    forInStatement.addChildToBack(variableDeclaration2);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    ArgumentCaptor<CompilerPass> capturedPass = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(eq(CollapseVariableDeclarations.class.getName()), capturedPass.capture());
    CompilerPass passedPass = capturedPass.getValue();

    assertSame(collapseVariableDeclarations, passedPass);

    assertNull(forInStatement.getLastChild());
  }
}