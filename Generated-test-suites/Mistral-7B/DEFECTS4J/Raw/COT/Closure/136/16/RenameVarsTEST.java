import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.VariableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {
  @org.junit.Test
  public void testRenameVars() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    String prefix = "myPrefix";
    boolean localRenamingOnly = true;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    char[] reservedCharacters = null;
    Set<String> reservedNames = new HashSet<>();
    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    // When
    Node externs = null; // not used in this test
    Node root = createTestNode();
    renameVars.process(externs, root);

    // Then
    assertEquals("size of globalNameNodes should be 2", 2,
        renameVars.globalNameNodes.size());
    assertEquals("size of localNameNodes should be 6", 6,
        renameVars.localNameNodes.size());
    assertEquals("size of localTempNames should be 6", 6,
        renameVars.localTempNames.size());
    assertEquals("size of assignments should be 10", 10,
        renameVars.assignments.size());

    // Test getNewGlobalName method
    Node globalNameNode1 = renameVars.globalNameNodes.get(0);
    String newGlobalName1 = renameVars.getNewGlobalName(globalNameNode1);
    assertEquals("newGlobalName1 should not be null", newGlobalName1, "a");

    Node globalNameNode2 = renameVars.globalNameNodes.get(1);
    String newGlobalName2 = renameVars.getNewGlobalName(globalNameNode2);
    assertEquals("newGlobalName2 should not be null", newGlobalName2, "b");

    // Test getNewLocalName method
    Node localNameNode1 = renameVars.localNameNodes.get(0);
    String newLocalName1 = renameVars.getNewLocalName(localNameNode1, 0);
    assertEquals("newLocalName1 should not be null", newLocalName1, "L0");

    Node localNameNode2 = renameVars.localNameNodes.get(1);
    String newLocalName2 = renameVars.getNewLocalName(localNameNameNode2, 1);
    assertEquals("newLocalName2 should not be null", newLocalName2, "L1");

    // Test reusePreviouslyUsedVariableMap method
    RenameVars renameVars2 = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, renameVars,
        reservedCharacters, reservedNames);
    assertEquals("size of renameMap in renameVars2 should be the same as in renameVars1",
        renameVars.getVariableMap().size(),
        renameVars2.getVariableMap().size());

    // Test okToRenameVar method
    assertFalse("okToRenameVar should return false for exported local variable",
        renameVars.okToRenameVar("exportedLocal", true));
    assertTrue("okToRenameVar should return true for unused local variable",
        renameVars.okToRenameVar("unusedLocal", true));
    assertTrue("okToRenameVar should return true for unused global variable",
        renameVars.okToRenameVar("unusedGlobal", false));
    assertFalse("okToRenameVar should return false for used global variable",
        renameVars.okToRenameVar("usedGlobal", false));

    // Test assignNames method (edge case: no variables to assign names to)
    List<Assignment> assignments = new ArrayList<>();
    RenameVars renameVars3 = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);
    renameVars3.assignments = assignments;
    renameVars3.process(externs, root);
    assertTrue("assignNames method should not change the size of assignments",
        assignments.size() == 0);

    // Test assignNames method (edge case: all variables have the same length name)
    String name1 = "shortName";
    String name2 = "shortName";
    String name3 = "shortName";
    Assignment assignment1 = new Assignment(name1, new CompilerInput("test.js", 1, 1));
    Assignment assignment2 = new Assignment(name2, new CompilerInput("test.js", 1, 2));
    Assignment assignment3 = new Assignment(name3, new CompilerInput("test.js", 1, 3));
    List<Assignment> assignmentsToTest = new ArrayList<>();
    assignmentsToTest.add(assignment1);
    assignmentsToTest.add(assignment2);
    assignmentsToTest.add(assignment3);
    RenameVars renameVars4 = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);
    renameVars4.assignments = assignmentsToTest;
    renameVars4.process(externs, root);
    // Test the order of assignments based on the order of occurrence in the source
    int i = 0;
    for (Assignment assignment : renameVars4.assignments) {
      String expectedName = Integer.toString(i++);
      String actualName = renameVars4.getVariableMap().lookupName(assignment.oldName);
      assertEquals("actual name should match expected name", expectedName, actualName);
    }

    // Test assignNames method (exception handling: name generators throw NameExhaustedException)
    NameGenerator nameGenerator = new NameGenerator(reservedNames, prefix,
        reservedCharacters);
    NameGenerator nameGeneratorSpy = new NameGeneratorSpy(nameGenerator);
    RenameVars renameVars5 = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames) {
      @Override
      NameGenerator makeNameGenerator() {
        return nameGeneratorSpy;
      }
    };
    renameVars5.assignments = assignmentsToTest;
    renameVars5.process(externs, root);
    // Test that NameExhaustedException is thrown and handled correctly
    assertTrue("NameExhaustedException should be thrown and handled",
        nameGeneratorSpy.nameExhaustedCalled);
  }

  private Node createTestNode() {
    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.EMPTY);
    Node a = new Node(Token.VAR, new Node("a"));
    Node b = new Node(Token.VAR, new Node("b"));
    Node c = new Node(Token.FUNCTION, new Node("function c(x) {}"), null);
    Node functionDeclaration = new Node(Token.FUNCTION, new Node("function d() {}"), null);
    Node d = new Node(Token.VAR, new Node("d"));
    Node e = new Node(Token.VAR, new Node("e"));
    Node f = new Node(Token.VAR, new Node("f"));
    Node g = new Node(Token.VAR, new Node("g"));
    Node h = new Node(Token.VAR, new Node("h"));
    Node i = new Node(Token.VAR, new Node("i"));
    Node j = new Node(Token.VAR, new Node("j"));
    Node k = new Node(Token.VAR, new Node("k"));

    root.addChild(externs);
    root.addChild(a);
    root.addChild(b);
    root.addChild(c);
    root.addChild(functionDeclaration);
    functionDeclaration.addChild(d);
    functionDeclaration.addChild(e);
    functionDeclaration.addChild(f);
    functionDeclaration.addChild(g);
    functionDeclaration.addChild(h);
    functionDeclaration.addChild(i);
    functionDeclaration.addChild(j);
    functionDeclaration.addChild(k);
    c.addChild(new Node(Token.VAR, new Node("x")));
    c.addChild(a);
    c.addChild(b);
    root.addChild(root); // add the root node to itself for testing purposes
    return root;
  }

  static class Assignment {
    final String oldName;
    final CompilerInput input;
    final String newName;
    final int count;

    Assignment(String oldName, CompilerInput input) {
      this.oldName = oldName;
      this.input = input;
      this.newName = null;
      this.count = 0;
    }

    Assignment(String oldName, String newName, CompilerInput input) {
      this.oldName = oldName;
      this.input = input;
      this.newName = newName;
      this.count = 0;
    }

    void setNewName(String newName) {
      this.newName = newName;
    }
  }

  static class NameGeneratorSpy extends NameGenerator {
    boolean nameExhaustedCalled = false;

    NameGeneratorSpy(NameGenerator nameGenerator) {
      super(nameGenerator.getReservedNames(), nameGenerator.getPrefix(),
          nameGenerator.getReservedCharacters());
    }

    @Override
    String generateNextName() throws NameExhaustedException {
      nameExhaustedCalled = true;
      throw new NameExhaustedException();
    }
  }
}