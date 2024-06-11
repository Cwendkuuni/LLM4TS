package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class VarCheckTest {

  private VarCheck varCheck;
  private Compiler compiler;

  @Before
  public void setup() {
    compiler = new Compiler(TestSourceFile.builder()
        .setInputFileName("Test.js")
        .setSourceFile("Test.js")
        .setExterns("doodad.js")
        .build());

    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testUndefinedVarError() {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    Node root = Node.newText("// No such variable");
    nodeTraversal.traverse(root);

    varCheck.process(null, root);

    Node undeclaredVarNode = Node.newName("foo");
    root.addChildToBack(undeclaredVarNode);
    nodeTraversal.traverse(root);

    varCheck.process(null, root);

    assertThat(varCheck.varsToDeclareInExterns).containsExactly("foo");

    Set<Diagnostic> diagnostics = compiler.getDiagnostics();
    Diagnostic diagnostic = diagnostics.iterator().next();
    assertThat(diagnostic.getMessage()).isEqualTo("variable foo is undefined");
  }

  @Test
  public void testViolatedModuleDependencyError() {
    // Create a doodad.js file with a declaration for "bar"
    CompilerInput doodadInput = compiler.newInput("doodad.js");
    Node doodadRoot = Node.newText("var bar = 42;");
    doodadInput.setAstRoot(doodadRoot);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    Node root = Node.newText("// Module A accesses bar from module B");
    root.addChildToBack(Node.newName("bar"));

    varCheck.process(null, root);

    Set<Diagnostic> diagnostics = compiler.getDiagnostics();
    Diagnostic diagnostic = diagnostics.iterator().next();
    assertThat(diagnostic.getMessage())
        .isEqualTo("module A cannot reference bar, defined in module doodad.js");
  }

  @Test
  public void testMissingModuleDependencyWarning() {
    // Create a doodad.js file with a declaration for "bar"
    CompilerInput doodadInput = compiler.newInput("doodad.js");
    Node doodadRoot = Node.newText("var bar = 42;");
    doodadInput.setAstRoot(doodadRoot);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    Node root = Node.newText("// Module A should depend on doodad.js");
    root.addChildToBack(Node.newName("bar"));

    varCheck.process(null, root);

    Set<Diagnostic> diagnostics = compiler.getDiagnostics();
    Diagnostic diagnostic = diagnostics.iterator().next();
    assertThat(diagnostic.getMessage())
        .isEqualTo("missing module dependency; module A should depend on doodad.js because it references bar");
  }

  @Test
  public void testStrictModuleDependencyError() {
    // Create a doodad.js file with a declaration for "bar"
    CompilerInput doodadInput = compiler.newInput("doodad.js");
    Node doodadRoot = Node.newText("var bar = 42;");
    doodadInput.setAstRoot(doodadRoot);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    Node root = Node.newText("// Module A has strictModuleDependency set to true");
    root.addChildToBack(Node.newName("bar"));

    VarCheck varCheckStrict = new VarCheck(compiler, true);

    varCheckStrict.process(null, root);

    Set<Diagnostic> diagnostics = compiler.getDiagnostics();
    Diagnostic diagnostic = diagnostics.iterator().next();
    assertThat(diagnostic.getMessage())
        .isEqualTo("module A cannot reference bar, defined in module doodad.js");

    // Make sure the error was not reported twice
    diagnostic = diagnostics.iterator().next();
    assertNull(diagnostic);
  }

  @Test
  public void testDuplicateDeclaration() {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    Node root = Node.newText("// Duplicate declaration of foo");
    root.addChildToBack(Node.newName("foo"));
    root.addChildToBack(Node.newName("foo"));

    varCheck.process(null, root);

    Set<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertNull(diagnostics);
  }

  @Test
  public void testUndefinedExternVarWarning() {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    Node root = Node.newText("// Undefined extern var");
    root.addChildToBack(Node.newName("baz"));

    varCheck.process(null, root);

    Set<Diagnostic> diagnostics = compiler.getDiagnostics();
    Diagnostic diagnostic = diagnostics.iterator().next();
    assertThat(diagnostic.getMessage())
        .isEqualTo("name baz is not undefined in the externs.");
  }

  @Test
  public void testNameReferenceInExternsWarning() {
    CompilerInput externsInput = compiler.newExternInput("{ foo: 1 }");
    Node externsRoot = externsInput.getAstRoot(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    Node root = Node.newText("// Accessing foo in externs has no effect");
    root.addChildToBack(Node.newGetProp(Node.newName("foo")));

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(externsInput, null);

    Set<Diagnostic> diagnostics = compiler.getDiagnostics();
    Diagnostic diagnostic = diagnostics.iterator().next();
    assertThat(diagnostic.getMessage())
        .isEqualTo("accessing name foo in externs has no effect");
  }
}