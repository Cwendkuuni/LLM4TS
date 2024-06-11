package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for VarCheck class.
 */
public class VarCheckTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private VarCheck varCheck;
  private CompilerInput input;
  private CompilerInput externsInput;
  private Node synthesizedExternsRoot;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = NodeFactory.getInitial();
    varCheck = new VarCheck(compiler);

    input = new CompilerInput("TestInput.js", "TestInput.js", null);
    externsInput = new CompilerInput("TestExterns.js", "TestExterns.js", null);
    synthesizedExternsRoot = nodeFactory.createTree(
        "{SyntheticVarsDecl: {}}");
  }

  @Test
  public void testNewInstance() {
    VarCheck varCheck = new VarCheck(compiler);
    assertNull(varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
  }

  @Test
  public void testNewInstanceSanityCheck() {
    VarCheck varCheck = new VarCheck(compiler, true);
    assertNull(varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
  }

  @Test
  public void testProcessWithEmptyExternsAndRoot() {
    varCheck.process(externsInput, null);
    assertFalse(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testProcessWithExternsAndRoot() {
    Node roots[] = new Node[] { externsInput.getAstRoot(compiler), input.getAstRoot(compiler) };
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(roots), varCheck);

    ImmutableSet<String> expectedVars = ImmutableSet.of("foo", "bar");
    assertSame(expectedVars, varCheck.varsToDeclareInExterns);
  }

  @Test
  public void testVisitUndefinedVar() {
    Node variableNode = nodeFactory.createTree(Token.NAME, "foo");
    Node parent = nodeFactory.createTree(Token.LP, variableNode);
    Node root = nodeFactory.createTree(Token.SCRIPTLET, parent);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    assertFalse(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testVisitDuplicateVar() {
    Node variableNode1 = nodeFactory.createTree(Token.NAME, "foo");
    Node variableNode2 = nodeFactory.createTree(Token.NAME, "foo");
    Node parent = nodeFactory.createTree(Token.VAR, variableNode1);
    Node root = nodeFactory.createTree(Token.SCRIPTLET, parent);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    ImmutableSet<String> expectedVars = ImmutableSet.of();
    assertSame(expectedVars, varCheck.varsToDeclareInExterns);
  }

  @Test
  public void testVisitUndefinedVarInFunction() {
    Node functionNode = nodeFactory.createTree(Token.FUNCTION, null, null);
    Node variableNode = nodeFactory.createTree(Token.NAME, "foo");
    functionNode.addChildToBack(variableNode);
    Node root = nodeFactory.createTree(Token.SCRIPTLET, functionNode);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    ImmutableSet<String> expectedVars = ImmutableSet.of("foo");
    assertSame(expectedVars, varCheck.varsToDeclareInExterns);
  }

  @Test
  public void testVisitVarThatIsAlreadyDeclared() {
    Node variableNode1 = nodeFactory.createTree(Token.NAME, "foo");
    Node variableNode2 = nodeFactory.createTree(Token.NAME, "foo");
    Node parent1 = nodeFactory.createTree(Token.VAR, variableNode1);
    Node parent2 = nodeFactory.createTree(Token.VAR, variableNode2);
    Node root = nodeFactory.createTree(Token.SCRIPTLET, parent1);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    ImmutableSet<String> expectedVars = ImmutableSet.of();
    assertSame(expectedVars, varCheck.varsToDeclareInExterns);
  }

  @Test
  public void testVisitFunctionDeclarationWithEmptyName() {
    Node functionNode = nodeFactory.createTree(Token.FUNCTION, null, null);
    Node root = nodeFactory.createTree(Token.SCRIPTLET, functionNode);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    // A function declaration with an empty name passes Rhino, but is supposed
    // to be a syntax error according to the spec.
    DiagnosticType diagnosticType = varCheck.lastDiagnostic();
    assertNotNull(diagnosticType);
    assertEquals(INVALID_FUNCTION_DECL, diagnosticType);
  }

  @Test
  public void testVisitVarWithModuleDependency() {
    // Dependency module A -> module B
    Node moduleA = nodeFactory.createTree(Token.MODULE_DECL, null, null);
    Node moduleB = nodeFactory.createTree(Token.MODULE_DECL, null, null);
    Node dependency = nodeFactory.createTree(Token.GETPROP, null, moduleB);
    Node variableNode = nodeFactory.createTree(Token.NAME, "foo");
    Node parent = nodeFactory.createTree(Token.VAR, variableNode);
    Node rootA = nodeFactory.createTree(Token.SCRIPTLET, moduleA);
    Node rootB = nodeFactory.createTree(Token.SCRIPTLET, moduleB);

    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph() {
      @Override
      public boolean dependsOn(JSModule module1, JSModule module2) {
        if (module1 == moduleA && module2 == moduleB) {
          return true;
        }
        return false;
      }
    });

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(rootA, rootB), varCheck);

    DiagnosticType diagnosticType = varCheck.lastDiagnostic();
    assertNotNull(diagnosticType);
    assertEquals(VIOLATED_MODULE_DEP_ERROR, diagnosticType);
  }

  @Test
  public void testVisitVarWithMissingModuleDependency() {
    // Dependency module B -> module A
    Node moduleA = nodeFactory.createTree(Token.MODULE_DECL, null, null);
    Node moduleB = nodeFactory.createTree(Token.MODULE_DECL, null, null);
    Node variableNode = nodeFactory.createTree(Token.NAME, "foo");
    Node parent = nodeFactory.createTree(Token.VAR, variableNode);
    Node rootA = nodeFactory.createTree(Token.SCRIPTLET, moduleA);
    Node rootB = nodeFactory.createTree(Token.SCRIPTLET, moduleB);

    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph() {
      @Override
      public boolean dependsOn(JSModule module1, JSModule module2) {
        if (module1 == moduleB && module2 == moduleA) {
          return true;
        }
        return false;
      }
    });

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(rootA, rootB), varCheck);

    DiagnosticType diagnosticType = varCheck.lastDiagnostic();
    assertNotNull(diagnosticType);
    assertEquals(MISSING_MODULE_DEP_ERROR, diagnosticType);
  }

  @Test
  public void testVisitVarWithStrictModuleDependency() {
    // Dependency module B -> module A
    Node moduleA = nodeFactory.createTree(Token.MODULE_DECL, null, null);
    Node moduleB = nodeFactory.createTree(Token.MODULE_DECL, null, null);
    Node variableNode = nodeFactory.createTree(Token.NAME, "foo");
    Node parent = nodeFactory.createTree(Token.VAR, variableNode);
    Node rootA = nodeFactory.createTree(Token.SCRIPTLET, moduleA);
    Node rootB = nodeFactory.createTree(Token.SCRIPTLET, moduleB);

    VarCheck varCheck = new VarCheck(compiler, true);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(rootA, rootB), varCheck);

    DiagnosticType diagnosticType = varCheck.lastDiagnostic();
    assertNotNull(diagnosticType);
    assertEquals(STRICT_MODULE_DEP_ERROR, diagnosticType);
  }

  @Test
  public void testVisitVarWithNameReferenceInExterns() {
    Node variableNode = nodeFactory.createTree(Token.NAME, "foo");
    Node parent = nodeFactory.createTree(Token.VAR, variableNode);
    Node root = nodeFactory.createTree(Token.SCRIPTLET, parent);

    Node externs = nodeFactory.createTree(Token.MODULE_DECL, null, null);
    Node nameReferenceNode = nodeFactory.createTree(Token.NAME, "foo");
    Node nameReferenceParent = nodeFactory.createTree(Token.GETPROP, null, nameReferenceNode);
    externs.addChildToBack(nameReferenceParent);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, new NameRefInExternsCheck());
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getAstRoot(compiler), externs), varCheck);

    DiagnosticType diagnosticType = varCheck.lastDiagnostic();
    assertNotNull(diagnosticType);
    assertEquals(NAME_REFERENCE_IN_EXTERNS_ERROR, diagnosticType);
  }

  @Test
  public void testCreateSynthesizedExternVar() {
    String varName = "foo";
    varCheck.createSynthesizedExternVar(varName);

    Node synthesizedExternsRootCopy = nodeFactory.cloneTree(synthesizedExternsRoot);
    Node expectedSynthesizedExternVar =
        synthesizedExternsRootCopy.getChild(0).getFirstChild();
    Node actualSynthesizedExternVar = varCheck.getSynthesizedExternsRoot().getChild(0)
        .getFirstChild();

    assertEquals(expectedSynthesizedExternVar, actualSynthesizedExternVar);
  }

  @Test
  public void testGetSynthesizedExternsInput() {
    VarCheck varCheck = new VarCheck(compiler);
    CompilerInput actualSynthesizedExternsInput = varCheck.getSynthesizedExternsInput();
    assertEquals(externsInput, actualSynthesizedExternsInput);
  }

  @Test
  public void testGetSynthesizedExternsRoot() {
    VarCheck varCheck = new VarCheck(compiler);
    Node actualSynthesizedExternsRoot = varCheck.getSynthesizedExternsRoot();
    assertEquals(synthesizedExternsRoot, actualSynthesizedExternsRoot);
  }
}