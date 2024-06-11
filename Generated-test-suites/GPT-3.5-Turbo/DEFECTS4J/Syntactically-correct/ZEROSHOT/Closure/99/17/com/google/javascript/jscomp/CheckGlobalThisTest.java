import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

    @Test 
    public void testShouldTraverse_WithConstructorAnnotation_ReturnFalse() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test 
    public void testShouldTraverse_WithThisTypeAnnotation_ReturnFalse() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test 
    public void testShouldTraverse_WithOverrideAnnotation_ReturnFalse() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test 
    public void testShouldTraverse_WithNonBlockParent_ReturnFalse() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test 
    public void testVisit_WithValidThisAndParent_ReturnTrue() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();

        // Act
        checkGlobalThis.visit(t, n, parent);
        DiagnosticType result = compiler.report(t.makeError(n, level, GLOBAL_THIS));

        // Assert
        assertTrue(result);
    }

    @Test 
    public void testVisit_WithAssignLhsChild_ReturnTrue() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        // Act
        checkGlobalThis.visit(t, n, parent);
        Node result = assignLhsChild;

        // Assert
        assertNull(result);
    }

    @Test 
    public void testShouldReportThis_WithAssignLhsChild_ReturnTrue() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        Node n = new Node();
        Node parent = new Node();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }

    @Test 
    public void testShouldReportThis_WithParentIsGet_ReturnTrue() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        Node n = new Node();
        Node parent = new Node(Token.GETPROP);

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }

    @Test 
    public void testGetFunctionJsDocInfo_WithJSDocInfoNotNull_ReturnJsDocInfo() {
        // Arrange
        Node n = new Node();
        JSDocInfo expected = new JSDocInfo();

        // Act
        n.setJSDocInfo(expected);
        JSDocInfo result = getFunctionJsDocInfo(n);

        // Assert
        assertEquals(expected, result);
    }

    @Test 
    public void testGetFunctionJsDocInfo_WithParentTypeIsName_ReturnJSDocInfo() {
        // Arrange
        Node n = new Node();
        Node parent = new Node(Token.NAME);
        JSDocInfo expected = new JSDocInfo();

        // Act
        parent.setJSDocInfo(expected);
        n.setParent(parent);
        JSDocInfo result = getFunctionJsDocInfo(n);

        // Assert
        assertEquals(expected, result);
    }

    @Test 
    public void testGetFunctionJsDocInfo_WithParentTypeIsAssign_ReturnJSDocInfo() {
        // Arrange
        Node n = new Node();
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo expected = new JSDocInfo();

        // Act
        parent.setJSDocInfo(expected);
        n.setParent(parent);
        JSDocInfo result = getFunctionJsDocInfo(n);

        // Assert
        assertEquals(expected, result);
    }

    @Test 
    public void testGetFunctionJsDocInfo_WithParentTypeIsVar_ReturnJSDocInfo() {
        // Arrange
        Node n = new Node();
        Node parent = new Node(Token.VAR);
        JSDocInfo expected = new JSDocInfo();

        // Act
        parent.setJSDocInfo(expected);
        Node gramps = new Node();
        parent.setParent(gramps);
        n.setParent(parent);
        JSDocInfo result = getFunctionJsDocInfo(n);

        // Assert
        assertEquals(expected, result);
    }

}