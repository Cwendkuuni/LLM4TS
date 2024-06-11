import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.BaseTestCase;
import com.google.javascript.jscomp.test.NodeAssert;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest extends BaseTestCase {

  private AbstractCompiler compiler;
  private CollapseVariableDeclarations collapseVariableDeclarations;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = new Compiler();
    collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
    compiler = null;
    collapseVariableDeclarations = null;
  }

  // Basic test case for process method
  @Test
  public void testProcess() {
    Node root = parseTestSource("testCollapseVariableDeclarations.js");
    collapseVariableDeclarations.process(null, root);
    printTree("processed", root);
  }

  // Test collapsing multiple variable declarations into a single one
  @Test
  public void testCollapseMultipleVariableDeclarations() {
    Node root = parseTestSource("testCollapseMultipleVariableDeclarations.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseMultipleVariableDeclarations.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing assigns
  @Test
  public void testCollapseAssigns() {
    Node root = parseTestSource("testCollapseAssigns.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseAssigns.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing multiple assignments into a single one
  @Test
  public void testCollapseMultipleAssignments() {
    Node root = parseTestSource("testCollapseMultipleAssignments.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseMultipleAssignments.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing variables inside if statements
  @Test
  public void testCollapseVariablesInsideIfStatements() {
    Node root = parseTestSource("testCollapseVariablesInsideIfStatements.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseVariablesInsideIfStatements.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing variables that can be redeclared
  @Test
  public void testCollapseVariablesThatCanBeRedeclared() {
    Node root = parseTestSource("testCollapseVariablesThatCanBeRedeclared.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseVariablesThatCanBeRedeclared.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing variables in different scopes
  @Test
  public void testCollapseVariablesInDifferentScopes() {
    Node root = parseTestSource("testCollapseVariablesInDifferentScopes.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseVariablesInDifferentScopes.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing variables with JSDoc info
  @Test
  public void testCollapseVariablesWithJSDocInfo() {
    Node root = parseTestSource("testCollapseVariablesWithJSDocInfo.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseVariablesWithJSDocInfo.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing variables with types
  @Test
  public void testCollapseVariablesWithTypes() {
    Node root = parseTestSource("testCollapseVariablesWithTypes.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseVariablesWithTypes.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing variables with init expressions
  @Test
  public void testCollapseVariablesWithInitExpressions() {
    Node root = parseTestSource("testCollapseVariablesWithInitExpressions.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseVariablesWithInitExpressions.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing variables with same name in different scopes
  @Test
  public void testCollapseVariablesWithSameNameInDifferentScopes() {
    Node root = parseTestSource("testCollapseVariablesWithSameNameInDifferentScopes.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseVariablesWithSameNameInDifferentScopes.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test collapsing variables with same name and same scope
  @Test
  public void testCollapseVariablesWithSameNameAndSameScope() {
    Node root = parseTestSource("testCollapseVariablesWithSameNameAndSameScope.js");
    collapseVariableDeclarations.process(null, root);
    Node processed = root;
    Node expected = parseTestSource("expectedCollapseVariablesWithSameNameAndSameScope.js");
    NodeAssert.assertTreeEquals(expected, processed);
  }

  // Test handling exception when processing a non-normalized code
  @Test(expected = AssertionError.class)
  public void testProcessNonNormalizedCode() {
    Node root = parseTestSource("testCollapseVariableDeclarationsNonNormalized.js");
    collapseVariableDeclarations.process(null, root);
  }

  // Test handling exception when processing a code with malformed nodes
  @Test(expected = AssertionError.class)
  public void testProcessMalformedCode() {
    Node root = parseTestSource("testCollapseVariableDeclarationsMalformed.js");
    collapseVariableDeclarations.process(null, root);
  }
}