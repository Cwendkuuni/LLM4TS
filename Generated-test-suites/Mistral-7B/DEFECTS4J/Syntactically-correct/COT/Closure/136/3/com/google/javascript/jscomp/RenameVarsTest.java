import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import org.junit.Before;
import org.junit.Test;

public class RenameVarsTest {

  private RenameVars renameVars;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    renameVars = new RenameVars(compiler, "prefix", false, false, false, null, null, null);
  }

  @Test
  public void testProcess() {
    Node node = new Node(); // Assuming a dummy Node for testing
    Node root = new Node(); // Assuming a dummy Node for testing
    renameVars.process(new Node(), root);
  }

  @Test
  public void testGetNewGlobalName() {
    Node globalNameNode = new Node();
    String originalName = "var1";

    renameVars.assignNames(Collections.singletonList(new Assignment(originalName, null)));

    String newName = renameVars.getNewGlobalName(globalNameNode);

    assertEquals("Expected a new name for the global variable",
        renameVars.renameMap.get(originalName), newName);
  }

  @Test
  public void testGetNewLocalName() {
    Node localNameNode = new Node();
    int index = 0;

    renameVars.assignNames(Collections.singletonList(new Assignment(LOCAL_VAR_PREFIX + index, null)));

    String newName = renameVars.getNewLocalName(localNameNode, index);

    assertEquals("Expected a new name for the local variable",
        renameVars.renameMap.get(LOCAL_VAR_PREFIX + index), newName);
  }

  @Test
  public void testGetPseudoName() {
    String originalName = "variableName";

    String newName = renameVars.getPseudoName(originalName);

    assertTrue("Expected pseudo name to have a '$' prefix and a '$' suffix",
        newName.startsWith("$") && newName.endsWith("$$"));
  }

  @Test
  public void testReusePreviouslyUsedVariableMap() {
    // Set up a previously used rename map with test data
    Map<String, String> prevUsedRenameMap = new HashMap<>();
    prevUsedRenameMap.put("test1", "test2");
    prevUsedRenameMap.put("test3", "test4");

    renameVars = new RenameVars(compiler, "prefix", false, false, false, prevUsedRenameMap, null, null);

    String originalName1 = "test1";
    String originalName2 = "test3";

    Assignment assignment1 = new Assignment(originalName1, null);
    Assignment assignment2 = new Assignment(originalName2, null);

    renameVars.assignNames(Arrays.asList(assignment1, assignment2));

    assertEquals("Expected 'test2' for the first variable",
        renameVars.renameMap.get(originalName1), "test2");
    assertEquals("Expected 'test4' for the second variable",
        renameVars.renameMap.get(originalName2), "test4");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOkToRenameVarReturnsFalseForExportedVariable() {
    renameVars = new RenameVars(compiler, "prefix", false, false, false, null, null, null);
    String exportedVariableName = "_exportedVariable";

    assertFalse("Expected okToRenameVar to return false for exported variable",
        okToRenameVar(exportedVariableName, false));
  }

  @Test
  public void testAssignNames() {
    Map<String, Assignment> assignments = new HashMap<>();
    List<Assignment> varsToRename = new ArrayList<>();

    String originalName1 = "var1";
    Assignment assignment1 = new Assignment(originalName1, null);
    assignments.put(originalName1, assignment1);
    varsToRename.add(assignment1);

    String originalName2 = "var2";
    Assignment assignment2 = new Assignment(originalName2, null);
    assignments.put(originalName2, assignment2);
    varsToRename.add(assignment2);

    NameGenerator globalNameGenerator = new NameGenerator(new HashSet<>(), "prefix", null);
    NameGenerator localNameGenerator = new NameGenerator(new HashSet<>(), "", null);

    renameVars.assignNames(varsToRename);

    assertEquals("Expected the first variable to have a shorter name",
        assignments.get(originalName1).newName.length(),
        assignments.get(originalName2).newName.length());
  }
}