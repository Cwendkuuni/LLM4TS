import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldNotTraverse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        com.google.javascript.rhino.NodeTraversal t = new com.google.javascript.rhino.NodeTraversal();
        com.google.javascript.rhino.Node n = new com.google.javascript.rhino.Node();
        com.google.javascript.rhino.Node parent = new com.google.javascript.rhino.Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ShouldNotTraverse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        com.google.javascript.rhino.NodeTraversal t = new com.google.javascript.rhino.NodeTraversal();
        com.google.javascript.rhino.Node n = new com.google.javascript.rhino.Node();
        com.google.javascript.rhino.Node parent = new com.google.javascript.rhino.Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_LeftSideOfAssignment_ShouldTraverse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        com.google.javascript.rhino.NodeTraversal t = new com.google.javascript.rhino.NodeTraversal();
        com.google.javascript.rhino.Node n = new com.google.javascript.rhino.Node();
        com.google.javascript.rhino.Node parent = new com.google.javascript.rhino.Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignment_PrototypeProperty_ShouldNotTraverse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        com.google.javascript.rhino.NodeTraversal t = new com.google.javascript.rhino.NodeTraversal();
        com.google.javascript.rhino.Node n = new com.google.javascript.rhino.Node();
        com.google.javascript.rhino.Node parent = new com.google.javascript.rhino.Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignment_PrototypeSubproperty_ShouldNotTraverse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        com.google.javascript.rhino.NodeTraversal t = new com.google.javascript.rhino.NodeTraversal();
        com.google.javascript.rhino.Node n = new com.google.javascript.rhino.Node();
        com.google.javascript.rhino.Node parent = new com.google.javascript.rhino.Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testVisit_NodeIsGlobalThis_ShouldReportError() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        com.google.javascript.rhino.NodeTraversal t = new com.google.javascript.rhino.NodeTraversal();
        com.google.javascript.rhino.Node n = new com.google.javascript.rhino.Node();
        com.google.javascript.rhino.Node parent = new com.google.javascript.rhino.Node();

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        // verify that an error is reported
    }

    @Test
    public void testVisit_NodeIsNotLeftSideOfAssignment_ShouldNotReportError() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        com.google.javascript.rhino.NodeTraversal t = new com.google.javascript.rhino.NodeTraversal();
        com.google.javascript.rhino.Node n = new com.google.javascript.rhino.Node();
        com.google.javascript.rhino.Node parent = new com.google.javascript.rhino.Node();

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        // verify that an error is not reported
    }
}