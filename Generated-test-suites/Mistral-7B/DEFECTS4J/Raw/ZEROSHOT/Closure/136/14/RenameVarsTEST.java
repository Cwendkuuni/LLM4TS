import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.Assignment.*;
import static com.google.javascript.jscomp.RenameVars.ProcessVars.ORDER_OF_OCCURRENCE_COMPARATOR;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.Token;

import java.util.*;

public class RenameVarsTest extends CompilerPassTestBase {

  private static final String RENAME_VARS_CLASS = RenameVars.class.getName();
  private static final String PREFIX = "test_";

  @Override
  protected CompilerPass getPass() {
    return new RenameVars(getCompiler(), PREFIX, false, false, false, null,
        null, Sets.newHashSet());
  }

  @Test
  public void testSimplePass() throws Exception {
    String input = "function test() { var a = 1; var b = 2; }";

    Node externs = parseAndTraverseToRoot(input);
    Node root = externs;

    getPass().process(externs, root);

    List<Assignment> assignments = Lists.newArrayList(
        getAssignment("a"), getAssignment("b"));

    // Check that assignments were made in the correct order
    for (Assignment a : assignments) {
      assertEquals(a.orderOfOccurrence, a.newName.length());
    }

    // Check the output
    String output = getSourceFromDebugLog(1);
    assertEquals(input, output);
  }

  @Test
  public void testLocalVariableRenaming() throws Exception {
    String input = "function test() { var a = 1; function x() {} var a = 2; }";

    Node externs = parseAndTraverseToRoot(input);
    Node root = externs;

    getPass().process(externs, root);

    List<Assignment> assignments = Lists.newArrayList(
        getAssignment("a"), getAssignment("x"));

    // Check that assignments were made in the correct order
    for (Assignment a : assignments) {
      assertEquals(a.orderOfOccurrence, a.newName.length());
    }

    // Check the output
    String output = getSourceFromDebugLog(1);
    assertEquals(input, output);
  }

  @Test
  public void testGlobalVariableRenaming() throws Exception {
    String input = "function test() { var a = 1; } var b = 2;";

    Node externs = parseAndTraverseToRoot(input);
    Node root = externs;

    getPass().process(externs, root);

    List<Assignment> assignments = Lists.newArrayList(
        getAssignment("a"), getAssignment("b"));

    // Check that assignments were made in the correct order
    for (Assignment a : assignments) {
      assertEquals(a.orderOfOccurrence, a.newName.length());
    }

    // Check the output
    String output = getSourceFromDebugLog(1);
    assertEquals(input, output);
  }

  @Test
  public void testGlobalVariableRenamingWithDuplicates() throws Exception {
    String input = "function test() { var a = 1; var a = 2; }";

    Node externs = parseAndTraverseToRoot(input);
    Node root = externs;

    getPass().process(externs, root);

    List<Assignment> assignments = Lists.newArrayList(
        getAssignment("a"), getAssignment("a"));

    // Check that assignments were made in the correct order
    for (Assignment a : assignments) {
      assertEquals(a.orderOfOccurrence, a.newName.length());
    }

    // Check the output
    String output = getSourceFromDebugLog(1);
    assertEquals(input, output);
  }

  @Test
  public void testGlobalVariableRenamingWithExterns() throws Exception {
    String input = "function test() { var a = 1; } var GOOG_LOG = 2;";

    Node externs = parseAndTraverseToRoot(input);
    Node root = externs;

    getPass().process(externs, root);

    List<Assignment> assignments = Lists.newArrayList(
        getAssignment("a"), getAssignment("GOOG_LOG"));

    // Check that assignments were made in the correct order
    for (Assignment a : assignments) {
      assertEquals(a.orderOfOccurrence, a.newName.length());
    }

    // Check the output
    String output = getSourceFromDebugLog(1);
    assertEquals(input, output);
  }

  @Test
  public void testLocalVariableRenamingWithDuplicates() throws Exception {
    String input = "function test() { var a = 1; function x() {} var a = 2; }";

    Node externs = parseAndTraverseToRoot(input);
    Node root = externs;

    getPass().process(externs, root);

    List<Assignment> assignments = Lists.newArrayList(
        getAssignment("a"), getAssignment("a"));

    // Check that assignments were made in the correct order
    for (Assignment a : assignments) {
      assertEquals(a.orderOfOccurrence, a.newName.length());
    }

    // Check the output
    String output = getSourceFromDebugLog(1);
    assertEquals(input, output);
  }

  private Assignment getAssignment(String name) {
    return Arrays.stream(getPass().assignments.values().toArray(Assignment[]
            .class))
        .filter(a -> a.oldName.equals(name))
        .findFirst()
        .orElseThrow(() -> new AssertionError("Assignment not found for " + name));
  }
}