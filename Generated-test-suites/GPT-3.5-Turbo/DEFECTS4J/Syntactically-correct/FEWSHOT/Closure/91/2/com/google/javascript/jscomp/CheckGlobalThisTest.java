import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private AbstractCompiler compiler;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    }

    @Test
    public void testShouldTraverse_functionConstructorWithThisAnnotation_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionInterfaceWithThisTypeAnnotation_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionWithOverrideAnnotation_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordOverride();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_nonGlobalThis_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_globalThis_shouldReturnTrue() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testVisit_globalThis_shouldReportError() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        compiler.disableWarnings();
        compiler.enableWarnings();
        checkGlobalThis.visit(t, n, parent);
        assertEquals(true, compiler.hasErrors());
    }
}