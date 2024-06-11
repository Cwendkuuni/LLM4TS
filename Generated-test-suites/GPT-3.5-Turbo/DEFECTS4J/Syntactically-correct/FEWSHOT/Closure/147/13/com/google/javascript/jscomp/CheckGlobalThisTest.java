import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_shouldReturnFalseForConstructorFunction() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        Node parent = new Node();

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForNonConstructorFunctionWithoutThisTypeAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node();
        parent.setType(Token.BLOCK);

        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit_shouldReportGlobalThisWhenNodeIsThisAndParentIsGet() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.THIS);
        Node parent = new Node();
        parent.setType(Token.GETPROP);
        parent.addChildToBack(n);

        checkGlobalThis.visit(t, n, parent);

        assertTrue(compiler.hasReportedErrors());
    }

    @Test
    public void testVisit_shouldNotReportGlobalThisWhenNodeIsThisAndParentIsSet() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.THIS);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        parent.addChildBefore(n, new Node());

        checkGlobalThis.visit(t, n, parent);

        assertFalse(compiler.hasReportedErrors());
    }

    @Test
    public void testGetFunctionJsDocInfo_shouldReturnJsDocInfoForFunctionWithJSDocInfo() {
        Node n = new Node();
        JSDocInfo expected = new JSDocInfo();
        n.setJSDocInfo(expected);

        JSDocInfo actual = CheckGlobalThis.getFunctionJsDocInfo(n);

        assertSame(expected, actual);
    }

    @Test
    public void testGetFunctionJsDocInfo_shouldReturnJsDocInfoForFunctionWithParentWithJSDocInfo() {
        Node n = new Node();
        Node parent = new Node();
        JSDocInfo expected = new JSDocInfo();
        parent.setJSDocInfo(expected);
        n.setParent(parent);

        JSDocInfo actual = CheckGlobalThis.getFunctionJsDocInfo(n);

        assertSame(expected, actual);
    }

    @Test
    public void testGetFunctionJsDocInfo_shouldReturnJsDocInfoForFunctionWithParentVarWithJSDocInfo() {
        Node n = new Node();
        Node parent = new Node();
        Node var = new Node();
        JSDocInfo expected = new JSDocInfo();
        var.setJSDocInfo(expected);
        parent.setType(Token.VAR);
        parent.addChildToBack(var);
        n.setParent(parent);

        JSDocInfo actual = CheckGlobalThis.getFunctionJsDocInfo(n);

        assertSame(expected, actual);
    }

    @Test
    public void testGetFunctionJsDocInfo_shouldReturnNullForFunctionWithoutJSDocInfo() {
        Node n = new Node();
        Node parent = new Node();
        n.setParent(parent);

        JSDocInfo actual = CheckGlobalThis.getFunctionJsDocInfo(n);

        assertNull(actual);
    }

}