import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

    private MustBeReachingVariableDef.ReachingDef reachingDef;

    @Before
    public void setUp() {
        reachingDef = new MustBeReachingVariableDef.ReachingDef();
    }

    @Test
    public void testGetDef_ExistingVariable_ReturnsNode() {
        // Arrange
        String name = "a";
        Node useNode = new Node(Token.NAME, name);
        reachingDef.putDef(name, useNode);

        // Act
        Node result = reachingDef.getDef(name, useNode);

        // Assert
        assertEquals(useNode, result);
    }

    @Test
    public void testGetDef_NonExistingVariable_ReturnsNull() {
        // Arrange
        String name = "b";
        Node useNode = new Node(Token.NAME, name);

        // Act
        Node result = reachingDef.getDef(name, useNode);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDependsOnOuterScopeVars_VariableDependsOnOuterScope_ReturnsTrue() {
        // Arrange
        String name = "a";
        Node useNode = new Node(Token.NAME, name);
        reachingDef.addDependency(name, "outer");

        // Act
        boolean result = reachingDef.dependsOnOuterScopeVars(name, useNode);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testDependsOnOuterScopeVars_VariableDoesNotDependOnOuterScope_ReturnsFalse() {
        // Arrange
        String name = "a";
        Node useNode = new Node(Token.NAME, name);

        // Act
        boolean result = reachingDef.dependsOnOuterScopeVars(name, useNode);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDependsOnOuterScopeVars_VariableNotInReachingDef_ReturnsFalse() {
        // Arrange
        String name = "a";
        Node useNode = new Node(Token.NAME, name);

        // Act
        boolean result = reachingDef.dependsOnOuterScopeVars(name, useNode);

        // Assert
        assertFalse(result);
    }
}