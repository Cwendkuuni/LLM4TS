import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinimizeExitPointsTest {

    @Test
    void testProcessWithEmptyAST_SHOULD_NOT_throwException() {
        // Arrange
        CompilerOptions options = new CompilerOptions();
        Compiler compiler = new Compiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

        // Act & Assert
        assertDoesNotThrow(() -> minimizeExitPoints.process(
                new Node(Token.EMPTY, new SourceFile("", "")),
                new Node(Token.EMPTY, new SourceFile("", ""))));
    }

    @Test
    void testProcessWithInvalidAST_SHOULD_throwException() {
        // Arrange
        CompilerOptions options = new CompilerOptions();
        Compiler compiler = new Compiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

        // Act & Assert
        assertThrows(JSError.class, () -> minimizeExitPoints.process(
                new Node(Token.INVALID, new SourceFile("", "")),
                new Node(Token.INVALID, new SourceFile("", ""))));
    }

    @Test
    void testProcessWithValidAST_AND_hasExitPoints_SHOULD_reduceExitPoints() {
        // Arrange
        CompilerOptions options = new CompilerOptions();
        Compiler compiler = new Compiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

        // Act
        Node externs = new Node(Token.SCRIPT, new SourceFile("", "externs"));
        Node root = new Node(Token.SCRIPT, new SourceFile("", "code"));
        minimizeExitPoints.process(externs, root);

        // Assert
        assertEquals(0, getCountOfExitPoints(root));
    }

    private int getCountOfExitPoints(Node node) {
        int count = 0;
        for (Node child : node.children()) {
            if (isExitPoint(child)) {
                count++;
            }
            count += getCountOfExitPoints(child);
        }
        return count;
    }

    private boolean isExitPoint(Node node) {
        int type = node.getType();
        return type == Token.RETURN || type == Token.BREAK || type == Token.CONTINUE;
    }
}