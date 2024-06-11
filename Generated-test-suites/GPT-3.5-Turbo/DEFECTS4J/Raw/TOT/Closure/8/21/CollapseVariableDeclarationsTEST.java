import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    private Node rootNode;
    private CompilerPass compilerPass;

    @Before
    public void setUp() throws Exception {
        rootNode = new Node(Token.ROOT);
        compilerPass = new CollapseVariableDeclarations(rootNode);
    }

    @Test
    public void process_noVariableDeclarations_noChange() {
        // Arrange
        final String sourceCode = "function foo() { return 1; }";

        // Act
        Node externs = new Node(Token.EXTERN);
        Node root = parseSourceCode(sourceCode);
        compilerPass.process(externs, root);

        // Assert
        assertEquals(sourceCode, root.toString());
    }

    @Test
    public void process_multipleVariableDeclarations_collapseIntoOne() {
        // Arrange
        final String sourceCode = 
            "var a; " +
            "var b = 1; " +
            "var c = 2;";

        // Act
        Node externs = new Node(Token.EXTERN);
        Node root = parseSourceCode(sourceCode);
        compilerPass.process(externs, root);

        // Assert
        final String expectedCode = "var a, b = 1, c = 2;";
        assertEquals(expectedCode, root.toString());
    }

    @Test
    public void process_withAssignments_collapseIntoOne() {
        // Arrange
        final String sourceCode = 
            "a = true; " +
            "b = true; " +
            "var c = true;";

        // Act
        Node externs = new Node(Token.EXTERN);
        Node root = parseSourceCode(sourceCode);
        compilerPass.process(externs, root);

        // Assert
        final String expectedCode = "var c = b = a = true;";
        assertEquals(expectedCode, root.toString());
    }

    @Test
    public void process_withIfCondition_noCollapseIntoOne() {
        // Arrange
        final String sourceCode = 
            "var a = 1; " +
            "if (true) { " +
                "var b = 2; " +
            "}";

        // Act
        Node externs = new Node(Token.EXTERN);
        Node root = parseSourceCode(sourceCode);
        compilerPass.process(externs, root);

        // Assert
        final String expectedCode = 
            "var a = 1; " +
            "if (true) { " +
                "var b = 2; " +
            "}";
        assertEquals(expectedCode, root.toString());
    }

    private Node parseSourceCode(String sourceCode) {
        Node root = new Node(Token.SCRIPT);
        JsParser parser = new JsParser();

        Node astRoot = parser.parse(sourceCode, null);
        root.addChildrenToBack(astRoot.removeChildren());
        return root;
    }
}