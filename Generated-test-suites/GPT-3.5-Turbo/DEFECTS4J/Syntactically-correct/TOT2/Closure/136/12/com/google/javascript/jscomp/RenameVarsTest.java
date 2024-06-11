import static org.junit.Assert.*;
import org.junit.Test;

public class RenameVarsTest {

    @Test
    public void testProcessVars_WhenLocalVariable() {
        RenameVars renameVars = new RenameVars();
        // Create test input node
        Node testNode = new Node(Token.NAME, "testVar");
        int expectedCount = 1;

        ProcessVars processVars = renameVars.new ProcessVars(false);
        processVars.incCount("testVar", null);

        assertEquals(expectedCount, processVars.assignments.get("testVar").count);
    }

    @Test
    public void testProcessVars_WhenGlobalVariable() {
        RenameVars renameVars = new RenameVars();
        // Create test input node
        Node testNode = new Node(Token.NAME, "globalVar");
        int expectedCount = 1;

        ProcessVars processVars = renameVars.new ProcessVars(false);
        processVars.incCount("globalVar", null);

        assertEquals(expectedCount, processVars.assignments.get("globalVar").count);
    }

    @Test
    public void testReusePreviouslyUsedVariableMap() {
        RenameVars renameVars = new RenameVars();
        VariableMap prevUsedMap = new VariableMap();
        prevUsedMap.addMapping("oldVar", "newVar");
        Set<String> expectedReservedNames = new HashSet<>();
        expectedReservedNames.add("newVar");

        renameVars.reservedNames.add("oldVar");
        renameVars.reusePreviouslyUsedVariableMap();

        assertEquals(expectedReservedNames, renameVars.reservedNames);
        assertEquals("newVar", renameVars.renameMap.get("oldVar"));
    }

    @Test
    public void testAssignNames() {
        RenameVars renameVars = new RenameVars();
        Assignment a1 = renameVars.new Assignment("var1", null);
        Assignment a2 = renameVars.new Assignment("var2", null);
        SortedSet<Assignment> varsToRename = new TreeSet<>();
        varsToRename.add(a1);
        varsToRename.add(a2);

        renameVars.reservedNames.add("newVar1");
        renameVars.reservedNames.add("newVar2");

        renameVars.assignNames(varsToRename);

        assertEquals("newVar1", a1.newName);
        assertEquals("newVar2", a2.newName);
    }

    @Test
    public void testGetVariableMap() {
        RenameVars renameVars = new RenameVars();
        renameVars.renameMap.put("oldVar", "newVar");

        VariableMap variableMap = renameVars.getVariableMap();

        assertEquals("newVar", variableMap.lookupNewName("oldVar"));
    }
}