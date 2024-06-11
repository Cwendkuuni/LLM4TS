import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testValidScopedAliases() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALL);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String filePath, SourcePosition position) {
        // Do nothing
      }
    });

    SourceFile sourceFile = new SourceFile("TestScopedAliases.js", "test\n" +
        "goog.scope(function() {\n" +
        "  var a = goog.a; // alias for goog\n" +
        "  var b = goog.b; // alias for goog\n" +
        "  var c = goog.c; // alias for goog\n" +
        "  var d = goog.dom; // alias for goog.dom\n" +
        "  var e = goog.event; // alias for goog.event\n" +
        "  var f = goog.log; // alias for goog.log\n" +
        "});");

    Node rootNode = compiler.parseTree(sourceFile, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, rootNode, traversal);

    // Check for correct number of alias definitions
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(6, aliasDefinitions.size());

    // Check for correct alias names
    for (int i = 0; i < aliasDefinitions.size(); ++i) {
      Node aliasDefinition = aliasDefinitions.get(i);
      String aliasName = aliasDefinition.getFirstChild().getString();
      assertEquals("a", aliasName);
      aliasName = aliasDefinition.getLastChild().getString();
      assertEquals("goog", aliasName);
    }

    // Check for correct number of scope calls
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    // Check for no errors
    assertTrue(traversal.hasErrors());
    assertEquals(0, traversal.getNumberOfErrors());
  }

  @Test
  public void testImproperUsageOfScope() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALL);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String filePath, SourcePosition position) {
        // Do nothing
      }
    });

    SourceFile sourceFile = new SourceFile("TestScopedAliases.js", "test\n" +
        "goog.scope(function() {\n" +
        "  var a = goog.a; // alias for goog\n" +
        "  var b = function() {}; // function definition\n" +
        "  goog.scope(function() {\n" +
        "    var c = goog.c; // alias for goog\n" +
        "  });\n" +
        "});");

    Node rootNode = compiler.parseTree(sourceFile, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, rootNode, traversal);

    // Check for correct number of alias definitions
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());

    // Check for error due to improper usage of goog.scope
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getNumberOfErrors());
    DiagnosticType errorType = traversal.getDiagnosticAtIndex(0).getType();
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errorType);
  }

  @Test
  public void testScopeHasBadParameters() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALL);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String filePath, SourcePosition position) {
        // Do nothing
      }
    });

    SourceFile sourceFile = new SourceFile("TestScopedAliases.js", "test\n" +
        "goog.scope(function(x) {\n" + // Error: goog.scope should not take any parameters
        "  var a = goog.a; // alias for goog\n" +
        "});");

    Node rootNode = compiler.parseTree(sourceFile, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, rootNode, traversal);

    // Check for correct number of alias definitions
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    // No alias definitions created since traversal is interrupted due to error
    assertTrue(aliasDefinitions.isEmpty());

    // Check for error due to bad parameters
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getNumberOfErrors());
    DiagnosticType errorType = traversal.getDiagnosticAtIndex(0).getType();
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, errorType);
  }

  @Test
  public void testScopeRefersToThis() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALL);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String filePath, SourcePosition position) {
        // Do nothing
      }
    });

    SourceFile sourceFile = new SourceFile("TestScopedAliases.js", "test\n" +
        "goog.scope(function() {\n" +
        "  this.a = goog.a; // Error: goog.scope function body cannot reference 'this'\n" +
        "});");

    Node rootNode = compiler.parseTree(sourceFile, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, rootNode, traversal);

    // Check for no alias definitions
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitions.isEmpty());

    // Check for error due to 'this' reference
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getNumberOfErrors());
    DiagnosticType errorType = traversal.getDiagnosticAtIndex(0).getType();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errorType);
  }

  @Test
  public void testScopeUsesReturn() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALL);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String filePath, SourcePosition position) {
        // Do nothing
      }
    });

    SourceFile sourceFile = new SourceFile("TestScopedAliases.js", "test\n" +
        "goog.scope(function() {\n" +
        "  return goog.a; // Error: goog.scope function body cannot use 'return'\n" +
        "});");

    Node rootNode = compiler.parseTree(sourceFile, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, rootNode, traversal);

    // Check for no alias definitions
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitions.isEmpty());

    // Check for error due to 'return' statement
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getNumberOfErrors());
    DiagnosticType errorType = traversal.getDiagnosticAtIndex(0).getType();
    assertEquals(GOOG_SCOPE_USES_RETURN, errorType);
  }

  @Test
  public void testScopeUsesThrow() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALL);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String filePath, SourcePosition position) {
        // Do nothing
      }
    });

    SourceFile sourceFile = new SourceFile("TestScopedAliases.js", "test\n" +
        "goog.scope(function() {\n" +
        "  throw new Error('Test error'); // Error: goog.scope function body cannot use 'throw'\n" +
        "});");

    Node rootNode = compiler.parseTree(sourceFile, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, rootNode, traversal);

    // Check for no alias definitions
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitions.isEmpty());

    // Check for error due to 'throw' statement
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getNumberOfErrors());
    DiagnosticType errorType = traversal.getDiagnosticAtIndex(0).getType();
    assertEquals(GOOG_SCOPE_USES_THROW, errorType);
  }

  @Test
  public void testAliasRedefined() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALL);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String filePath, SourcePosition position) {
        // Do nothing
      }
    });

    SourceFile sourceFile = new SourceFile("TestScopedAliases.js", "test\n" +
        "goog.scope(function() {\n" +
        "  var a = goog.a; // Alias for goog\n" +
        "  var a = 5; // Error: alias 'a' is reassigned\n" +
        "});");

    Node rootNode = compiler.parseTree(sourceFile, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, rootNode, traversal);

    // Check for correct number of alias definitions
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitions.size());

    // Check for error due to alias redefinition
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getNumberOfErrors());
    DiagnosticType errorType = traversal.getDiagnosticAtIndex(0).getType();
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errorType);
  }

  @Test
  public void testNonAliasLocal() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALL);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String filePath, SourcePosition position) {
        // Do nothing
      }
    });

    SourceFile sourceFile = new SourceFile("TestScopedAliases.js", "test\n" +
        "goog.scope(function() {\n" +
        "  var a = 5; // Local variable 'a' is not an alias\n" +
        "});");

    Node rootNode = compiler.parseTree(sourceFile, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, rootNode, traversal);

    // Check for no alias definitions
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitions.isEmpty());

    // Check for error due to non-alias local variable
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getNumberOfErrors());
    DiagnosticType errorType = traversal.getDiagnosticAtIndex(0).getType();
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, errorType);
  }
}