import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessNode;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * RenameLabelsTest is a JUnit test class that tests the functionality of the RenameLabels class.
 */
public class RenameLabelsTest {

  @org.junit.Test
  public void testRenameLabels() {
    // Create test compiler and abstract compiler.
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler();

    // Create the RenameLabels object to be tested.
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Create a test Node for the root of the program.
    Node root = NodeFactory.createProgram();

    // Set up the test Node with some test code.
    Node testCode = NodeFactory.createLabel("a");
    Node block = NodeFactory.createBlock(testCode);
    root.appendChild(block);

    // Create a test CompilerPass for the test.
    ProcessNode processNode = new ProcessNode(compiler, root);

    // Run the RenameLabels CompilerPass on the test Node.
    renameLabels.process(null, root);
    processNode.process();

    // Check that the label "a" was renamed.
    Node renamedLabel = root.lookupField("a");
    assertNotNull(renamedLabel);
    assertEquals(renameLabels.nameSupplier.get(), renamedLabel.getString());

    // Test with a nested label.
    testCode = NodeFactory.createLabel("b");
    Node nestedBlock = NodeFactory.createBlock(testCode);
    block.appendChild(nestedBlock);

    processNode = new ProcessNode(compiler, root);

    renameLabels.process(null, root);
    processNode.process();

    // Check that the label "b" was renamed.
    Node nestedRenamedLabel = root.lookupField("b");
    assertNotNull(nestedRenamedLabel);
    assertEquals(renameLabels.nameSupplier.get() + "_1", nestedRenamedLabel.getString());

    // Test with a label referenced in a break or continue statement.
    testCode = NodeFactory.createLabel("c");
    Node labelNode = NodeFactory.createLabel("c");
    Node breakStatement = NodeFactory.createBreak(labelNode);
    Node breakNode = NodeFactory.createBlock(breakStatement);
    block.appendChild(breakNode);

    processNode = new ProcessNode(compiler, root);

    renameLabels.process(null, root);
    processNode.process();

    // Check that the label "c" was renamed.
    Node renamedBreakLabel = root.lookupField("c");
    assertNotNull(renamedBreakLabel);
    assertEquals(renameLabels.nameSupplier.get() + "_2", renamedBreakLabel.getString());

    // Test with a label that is not referenced.
    testCode = NodeFactory.createLabel("d");
    block.appendChild(testCode);

    processNode = new ProcessNode(compiler, root);

    renameLabels.process(null, root);
    processNode.process();

    // Check that the label "d" was removed.
    Node dLabel = root.lookupField("d");
    assertNull(dLabel);

    // Test with a duplicate label name.
    testCode = NodeFactory.createLabel("a");
    block.appendChild(testCode);

    processNode = new ProcessNode(compiler, root);

    renameLabels.process(null, root);
    processNode.process();

    // Check that the second instance of the label "a" was renamed to "a_1".
    Node duplicateLabel = root.lookupField("a");
    assertNotNull(duplicateLabel);
    assertEquals("a_1", duplicateLabel.getString());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testExceptionWhenLabelNotAString() {
    // Create test compiler and abstract compiler.
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler();

    // Create the RenameLabels object to be tested.
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Create a test Node for the root of the program.
    Node root = NodeFactory.createProgram();

    // Set up the test Node with some test code.
    Node testCode = NodeFactory.createNumber(1);
    Node labelNode = NodeFactory.createLabel(testCode);

    // Create a test CompilerPass for the test.
    ProcessNode processNode = new ProcessNode(compiler, root);

    try {
      // Run the RenameLabels CompilerPass on the test Node.
      renameLabels.process(null, root);
      processNode.process();
      fail("Expected an AssertionError when the label was not a String.");
    } catch (AssertionError expected) {
      // Expected exception.
    }
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testExceptionWhenLabelInUseOutsideOfScope() {
    // Create test compiler and abstract compiler.
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler();

    // Create the RenameLabels object to be tested.
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Create a test Node for the root of the program.
    Node root = NodeFactory.createProgram();

    // Set up the test Node with some test code.
    Node testCode = NodeFactory.createLabel("a");
    Node block = NodeFactory.createBlock(testCode);
    Node outerBlock = NodeFactory.createBlock();
    outerBlock.appendChild(block);
    root.appendChild(outerBlock);

    // Create a test CompilerPass for the test.
    ProcessNode processNode = new ProcessNode(compiler, root);

    try {
      // Run the RenameLabels CompilerPass on the test Node.
      renameLabels.process(null, root);
      processNode.process();

      // Check that the label "a" was not renamed.
      Node expectedLabel = root.lookupField("a");
      assertNotNull(expectedLabel);
      assertEquals("a", expectedLabel.getString());

      // Check that the label "a" was not removed from the outer block.
      Node outerBlockLabel = outerBlock.lookupField("a");
      assertNotNull(outerBlockLabel);
    } catch (AssertionError expected) {
      // Expected exception.
    }
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testExceptionWhenLabelInFunctionDeclaration() {
    // Create test compiler and abstract compiler.
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler();

    // Create the RenameLabels object to be tested.
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Create a test Node for the root of the program.
    Node root = NodeFactory.createProgram();

    // Set up the test Node with some test code.
    Node functionDeclaration = NodeFactory.createFunction("test", null);
    Node labelNode = NodeFactory.createLabel("a");
    Node labelStatement = NodeFactory.createStatement(labelNode);
    Node functionBody = NodeFactory.createBlock();
    functionDeclaration.setLastChild(functionBody);
    root.appendChild(functionDeclaration);

    // Create a test CompilerPass for the test.
    ProcessNode processNode = new ProcessNode(compiler, root);

    try {
      // Run the RenameLabels CompilerPass on the test Node.
      renameLabels.process(null, root);
      processNode.process();

      // Check that the label "a" was not renamed.
      Node expectedLabel = functionDeclaration.lookupField("a");
      assertNotNull(expectedLabel);
      assertEquals("a", expectedLabel.getString());
    } catch (AssertionError expected) {
      // Expected exception.
    }
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testExceptionWhenLabelInStatementOutsideOfScope() {
    // Create test compiler and abstract compiler.
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler();

    // Create the RenameLabels object to be tested.
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Create a test Node for the root of the program.
    Node root = NodeFactory.createProgram();

    // Set up the test Node with some test code.
    Node testCode = NodeFactory.createLabel("a");
    Node outerBlock = NodeFactory.createBlock();
    Node block = NodeFactory.createBlock();
    Node statement = NodeFactory.createStatement(testCode);
    block.appendChild(statement);
    outerBlock.appendChild(block);
    root.appendChild(outerBlock);

    // Create a test CompilerPass for the test.
    ProcessNode processNode = new ProcessNode(compiler, root);

    try {
      // Run the RenameLabels CompilerPass on the test Node.
      renameLabels.process(null, root);
      processNode.process();

      // Check that the label "a" was not renamed.
      Node expectedLabel = root.lookupField("a");
      assertNotNull(expectedLabel);
      assertEquals("a", expectedLabel.getString());
    } catch (AssertionError expected) {
      // Expected exception.
    }
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testExceptionWhenLabelInStatementWithinScopeButNotReferenced() {
    // Create test compiler and abstract compiler.
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler();

    // Create the RenameLabels object to be tested.
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Create a test Node for the root of the program.
    Node root = NodeFactory.createProgram();

    // Set up the test Node with some test code.
    Node testCode = NodeFactory.createLabel("a");
    Node outerBlock = NodeFactory.createBlock();
    Node block = NodeFactory.createBlock();
    Node statement = NodeFactory.createStatement(testCode);
    block.appendChild(statement);
    outerBlock.appendChild(block);
    root.appendChild(outerBlock);

    // Create a test CompilerPass for the test.
    ProcessNode processNode = new ProcessNode(compiler, root);

    try {
      // Run the RenameLabels CompilerPass on the test Node.
      renameLabels.process(null, root);
      processNode.process();

      // Check that the label "a" was removed.
      Node expectedLabel = root.lookupField("a");
      assertNull(expectedLabel);
    } catch (AssertionError expected) {
      // Expected exception.
    }
  }

  @org.junit.Test
  public void testRemoveUnusedLabels() {
    // Create test compiler and abstract compiler.
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler();

    // Create the RenameLabels object to be tested with removeUnused = true.
    RenameLabels renameLabels = new RenameLabels(abstractCompiler, new DefaultNameSupplier(), true);

    // Create a test Node for the root of the program.
    Node root = NodeFactory.createProgram();

    // Set up the test Node with some test code.
    Node testCode = NodeFactory.createLabel("a");
    Node block = NodeFactory.createBlock(testCode);
    root.appendChild(block);

    // Create a test CompilerPass for the test.
    ProcessNode processNode = new ProcessNode(compiler, root);

    // Run the RenameLabels CompilerPass on the test Node.
    renameLabels.process(null, root);
    processNode.process();

    // Check that the label "a" was removed because it was not used.
    Node label = root.lookupField("a");
    assertNull(label);
  }

  @org.junit.Test
  public void testLabelRenamingInNestedScope() {
    // Create test compiler and abstract compiler.
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler();

    // Create the RenameLabels object to be tested.
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Create a test Node for the root of the program.
    Node root = NodeFactory.createProgram();

    // Set up the test Node with some test code.
    Node testCode = NodeFactory.createLabel("a");
    Node outerBlock = NodeFactory.createBlock();
    Node innerBlock = NodeFactory.createBlock();
    Node innerLabel = NodeFactory.createLabel("a");
    Node innerStatement = NodeFactory.createStatement(innerLabel);
    innerBlock.appendChild(innerStatement);
    outerBlock.appendChild(innerBlock);
    outerBlock.appendChild(testCode);
    root.appendChild(outerBlock);

    // Create a test CompilerPass for the test.
    ProcessNode processNode = new ProcessNode(compiler, root);

    // Run the RenameLabels CompilerPass on the test Node.
    renameLabels.process(null, root);
    processNode.process();

    // Check that the labels "a" in the outer and inner scopes were renamed to "a_1" and "a_2" respectively.
    Node outerLabel = root.lookupField("a");
    assertNotNull(outerLabel);
    assertEquals("a_1", outerLabel.getString());
    Node innerLabel2 = outerBlock.lookupField("a_2");
    assertNotNull(innerLabel2);
    assertEquals("a_2", innerLabel2.getString());
  }

  @org.junit.Test
  public void testLabelRenamingInNestedFunction() {
    // Create test compiler and abstract compiler.
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler();

    // Create the RenameLabels object to be tested.
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Create a test Node for the root of the program.
    Node root = NodeFactory.createProgram();

    // Set up the test Node with some test code.
    Node functionDeclaration = NodeFactory.createFunction("test", null);
    Node label = NodeFactory.createLabel("a");
    Node testBlock = NodeFactory.createBlock();
    functionDeclaration.setLastChild(testBlock);
    Node innerFunctionDeclaration = NodeFactory.createFunction("innerTest", null);
    Node innerLabel = NodeFactory.createLabel("a");
    Node innerBlock = NodeFactory.createBlock();
    innerFunctionDeclaration.setLastChild(innerBlock);
    testBlock.appendChild(innerFunctionDeclaration);
    testBlock.appendChild(label);
    root.appendChild(functionDeclaration);

    // Create a test CompilerPass for the test.
    ProcessNode processNode = new ProcessNode(compiler, root);

    // Run the RenameLabels CompilerPass on the test Node.
    renameLabels.process(null, root);
    processNode.process();

    // Check that the labels "a" in the outer and inner functions were renamed to "a_1" and "a_2" respectively.
    Node outerLabel = functionDeclaration.lookupField("a");
    assertNotNull(outerLabel);
    assertEquals("a_1", outerLabel.getString());
    Node innerLabel2 = innerFunctionDeclaration.lookupField("a_2");
    assertNotNull(innerLabel2);
    assertEquals("a_2", innerLabel2.getString());
  }
}