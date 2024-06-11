import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.VariableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

    private CompilerPass renameVars;
    private VariableMap prevUsedRenameMap;

    @Before
    public void setUp() {
        String prefix = "test_";
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        char[] reservedCharacters = null;
        Set<String> reservedNames = new HashSet<>();
        renameVars = new RenameVars(new Compiler(), prefix, localRenamingOnly,
                preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
                reservedCharacters, reservedNames);
        prevUsedRenameMap = Collections.emptyMap();
    }

    /**
     * Test method for the constructor.
     */
    @Test
    public void testRenameVarsConstructor() {
        assertNotNull(renameVars);
    }

    /**
     * Test method for the process method.
     */
    @Test
    public void testProcess() {
        // Given: A Node object
        Node root = createTestNode();

        // When: The process method is called
        renameVars.process(null, root);

        // Then: The Node object should be renamed
        Node renamedNode = getNameNodeWithName("test_a", root);
        assertNotNull(renamedNode);
        assertEquals("test_a", renamedNode.getString());
    }

    /**
     * Test method for the getVariableMap method.
     */
    @Test
    public void testGetVariableMap() {
        // Given: A RenameVars object
        // When: The getVariableMap method is called
        VariableMap variableMap = renameVars.getVariableMap();

        // Then: The variable map should not be empty
        assertFalse(variableMap.isEmpty());
    }

    /**
     * Test method for the okToRenameVar method.
     */
    @Test
    public void testOkToRenameVar() {
        // Given: A RenameVars object and a local variable name
        String localVariableName = "test_local";
        assertTrue(renameVars.okToRenameVar(localVariableName, true));

        // Given: A RenameVars object and a global variable name
        String globalVariableName = "test_global";
        assertFalse(renameVars.okToRenameVar(globalVariableName, false));

        // Given: A RenameVars object and an anonymous function name
        String anonymousFunctionName = "test_anonymous";
        if (renameVars.preserveAnonymousFunctionNames) {
            assertTrue(renameVars.okToRenameVar(anonymousFunctionName, false));
        } else {
            assertFalse(renameVars.okToRenameVar(anonymousFunctionName, false));
        }
    }

    /**
     * Test method for the reusePreviouslyUsedVariableMap method.
     */
    @Test
    public void testReusePreviouslyUsedVariableMap() {
        // Given: A RenameVars object and a previously used variable map
        VariableMap previouslyUsedRenameMap = new VariableMap();
        previouslyUsedRenameMap.put("test_old", "test_new");
        renameVars = new RenameVars(new Compiler(), null, false, false, false,
                previouslyUsedRenameMap, null, null);

        // When: The reusePreviouslyUsedVariableMap method is called
        renameVars.reusePreviouslyUsedVariableMap();

        // Then: The reservedNames set should contain the previously used name
        assertTrue(renameVars.reservedNames.contains("test_new"));
    }

    /**
     * Test method for the assignNames method with local variables.
     */
    @Test
    public void testAssignNamesLocalVariables() {
        // Given: A RenameVars object with local renaming only enabled
        renameVars = new RenameVars(new Compiler(), "test_", true, false, false,
                prevUsedRenameMap, null, null);

        // Given: A list of local variables
        List<String> localVariables = new ArrayList<>();
        localVariables.add("test_a");
        localVariables.add("test_b");
        localVariables.add("test_c");

        // When: The assignNames method is called
        renameVars.assignNames(localVariables);

        // Then: The first local variable should be assigned a new name
        String firstLocalVariableNewName = renameVars.renameMap.get("test_a");
        assertNotNull(firstLocalVariableNewName);
        assertEquals("L 0", firstLocalVariableNewName);

        // Then: The second local variable should be assigned a new name
        String secondLocalVariableNewName = renameVars.renameMap.get("test_b");
        assertNotNull(secondLocalVariableNewName);
        assertEquals("L 1", secondLocalVariableNewName);

        // Then: The third local variable should be assigned a new name
        String thirdLocalVariableNewName = renameVars.renameMap.get("test_c");
        assertNotNull(thirdLocalVariableNewName);
        assertEquals("L 2", thirdLocalVariableNewName);
    }

    /**
     * Test method for the assignNames method with global variables.
     */
    @Test
    public void testAssignNamesGlobalVariables() {
        // Given: A RenameVars object with global renaming only enabled
        renameVars = new RenameVars(new Compiler(), "test_", false, false, false,
                prevUsedRenameMap, null, null);

        // Given: A list of global variables
        List<String> globalVariables = new ArrayList<>();
        globalVariables.add("test_a");
        globalVariables.add("test_b");
        globalVariables.add("test_c");

        // When: The assignNames method is called
        renameVars.assignNames(globalVariables);

        // Then: The first global variable should be assigned a new name
        String firstGlobalVariableNewName = renameVars.renameMap.get("test_a");
        assertNotNull(firstGlobalVariableNewName);
        assertNotEquals("test_a", firstGlobalVariableNewName);

        // Then: The second global variable should be assigned a new name
        String secondGlobalVariableNewName = renameVars.renameMap.get("test_b");
        assertNotNull(secondGlobalVariableNewName);
        assertNotEquals("test_b", secondGlobalVariableNewName);

        // Then: The third global variable should be assigned a new name
        String thirdGlobalVariableNewName = renameVars.renameMap.get("test_c");
        assertNotNull(thirdGlobalVariableNewName);
        assertNotEquals("test_c", thirdGlobalVariableNewName);
    }

    /**
     * Test method for the assignNames method with conflicting variable names.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAssignNamesConflictingVariableNames() {
        // Given: A RenameVars object
        renameVars = new RenameVars(new Compiler(), null, false, false, false,
                prevUsedRenameMap, null, null);

        // Given: A list of variables with conflicting names
        List<String> conflictingVariables = new ArrayList<>();
        conflictingVariables.add("test_a");
        conflictingVariables.add("test_a");

        // When: The assignNames method is called
        renameVars.assignNames(conflictingVariables);

        // Then: An IllegalArgumentException should be thrown
        renameVars.assignNames(conflictingVariables);
    }

    /**
     * Test method for the getNewGlobalName method.
     */
    @Test
    public void testGetNewGlobalName() {
        // Given: A RenameVars object
        renameVars = new RenameVars(new Compiler(), null, false, false, false,
                prevUsedRenameMap, null, null);

        // Given: A global variable name
        String globalVariableName = "test_a";

        // When: The getNewGlobalName method is called
        String newGlobalVariableName = renameVars.getNewGlobalName(createTestNode(globalVariableName));

        // Then: A new name should be returned
        assertNotNull(newGlobalVariableName);
        assertNotEquals(globalVariableName, newGlobalVariableName);
    }

    /**
     * Test method for the getNewLocalName method.
     */
    @Test
    public void testGetNewLocalName() {
        // Given: A RenameVars object
        renameVars = new RenameVars(new Compiler(), null, false, false, false,
                prevUsedRenameMap, null, null);

        // Given: A local variable name and index
        String localVariableName = "test_a";
        int localVariableIndex = 0;

        // When: The getNewLocalName method is called
        String newLocalVariableName = renameVars.getNewLocalName(localVariableName, localVariableIndex);

        // Then: A new name should be returned
        assertNotNull(newLocalVariableName);
        assertNotEquals(localVariableName, newLocalVariableName);
    }

    /**
     * Test method for the reusePreviouslyUsedVariableMap method with conflicting variable names.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReusePreviouslyUsedVariableMapConflictingVariableNames() {
        // Given: A RenameVars object and a previously used variable map with conflicting names
        VariableMap previouslyUsedRenameMap = new VariableMap();
        previouslyUsedRenameMap.put("test_a", "test_new_a");
        previouslyUsedRenameMap.put("test_a", "test_new_b");
        renameVars = new RenameVars(new Compiler(), null, false, false, false,
                previouslyUsedRenameMap, null, null);

        // When: The reusePreviouslyUsedVariableMap method is called
        renameVars.reusePreviouslyUsedVariableMap();

        // Then: An IllegalArgumentException should be thrown
        renameVars.reusePreviouslyUsedVariableMap();
    }

    /**
     * Helper method to create a Node object with a given name.
     */
    private Node createTestNode(String name) {
        Node node = new Node();
        node.setType(Token.NAME);
        node.setString(name);
        return node;
    }

    /**
     * Helper method to get a Node object with a given name from a given Node object.
     */
    private Node getNameNodeWithName(String name, Node node) {
        for (Node childNode = node.getFirstChild(); childNode != null;
                childNode = childNode.getNextSibling()) {
            if (childNode.getType() == Token.NAME &&
                    childNode.getString().equals(name)) {
                return childNode;
            }
        }
        return null;
    }
}