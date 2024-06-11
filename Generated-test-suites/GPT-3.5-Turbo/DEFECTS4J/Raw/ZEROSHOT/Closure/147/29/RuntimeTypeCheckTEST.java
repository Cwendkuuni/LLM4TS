import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.*;

import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {

    private AbstractCompiler compiler;
    private RuntimeTypeCheck runtimeTypeCheck;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    }

    @Test
    public void testProcess() {
        // Arrange
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);

        // Act
        runtimeTypeCheck.process(externs, root);

        // Assert
        verify(compiler, times(2)).reportCodeChange();
    }

    @Test
    public void testAddMarkers() {
        // Arrange
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.BLOCK);
        Node parent = new Node(Token.SCRIPT);

        // Act
        runtimeTypeCheck.new AddMarkers(compiler).visit(t, n, parent);

        // Assert
        // Not implemented, cannot test
    }

    @Test
    public void testAddChecks() {
        // Arrange
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.BLOCK);
        Node parent = new Node(Token.SCRIPT);

        // Act
        runtimeTypeCheck.new AddChecks(compiler).visit(t, n, parent);

        // Assert
        // Not implemented, cannot test
    }

}