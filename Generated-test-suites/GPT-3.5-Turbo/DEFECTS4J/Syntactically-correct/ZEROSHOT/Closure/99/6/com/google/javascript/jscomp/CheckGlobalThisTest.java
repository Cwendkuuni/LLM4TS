import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckLevel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compiler;

  @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    }

  @Test
    public void testShouldTraverse_functionWithoutJsDoc() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testShouldTraverse_functionWithConstructorJsDoc() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo(true));
        Node parent = new Node(Token.SCRIPT);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testShouldTraverse_functionWithThisJsDoc() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo().recordThisType());
        Node parent = new Node(Token.SCRIPT);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testShouldTraverse_functionWithOverrideJsDoc() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo().recordOverride());
        Node parent = new Node(Token.SCRIPT);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testShouldTraverse_functionWithNonGlobalParent() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testShouldTraverse_functionWithNonTraversableParent() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testShouldTraverse_nodeWithAssignParentAndLhsChild() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        parent.addChildToBack(lhs);
        parent.addChildToBack(new Node(Token.NUMBER));
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        lhs.addChildToBack(new Node(Token.NAME, "property"));
        checkGlobalThis.shouldTraverse(t, lhs, parent);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testShouldTraverse_nodeWithAssignParentAndRhsChild() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        parent.addChildToBack(lhs);
        parent.addChildToBack(n);
        lhs.setString("a");
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testShouldTraverse_nodeWithoutAssignParentButWithPrototypeLhs() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NOT);
        Node lhs = new Node(Token.GETPROP);
        parent.addChildToBack(lhs);
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        lhs.addChildToBack(new Node(Token.NAME, "property"));
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testShouldTraverse_nodeWithoutAssignParentButWithSubpropertyLhs() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NOT);
        Node lhs = new Node(Token.GETPROP);
        parent.addChildToBack(lhs);
        lhs.addChildToBack(new Node(Token.NAME, "some"));
        lhs.addChildToBack(new Node(Token.GETPROP));
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

  @Test
    public void testVisit_nodeIsThisAndNotReported() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        compiler.reported = false;
        checkGlobalThis.visit(t, n, parent);
        assertFalse(compiler.reported);
    }

  @Test
    public void testVisit_nodeIsThisAndReported() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        compiler.reported = false;
        checkGlobalThis.visit(t, n, parent);
        assertTrue(compiler.reported);
    }

  @Test
    public void testVisit_nodeIsAssignLhsChild() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.NAME, "a");
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.THIS);
        parent.addChildToBack(lhs);
        parent.addChildToBack(new Node(Token.NUMBER));
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        lhs.addChildToBack(new Node(Token.NAME, "property"));
        checkGlobalThis.assignLhsChild = lhs;
        compiler.reported = false;
        checkGlobalThis.visit(t, n, parent);
        assertTrue(checkGlobalThis.assignLhsChild == null);
        assertTrue(compiler.reported);
    }

  @Test
    public void testGetFunctionJsDocInfo_jsDocInFunctionNode() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    }

  @Test
    public void testGetFunctionJsDocInfo_jsDocInParentNodeWithNameChild() {
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.NAME);
        Node grandParent = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        grandParent.addChildToBack(parent);
        n.addChildToBack(grandParent);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    }

  @Test
    public void testGetFunctionJsDocInfo_jsDocInParentNodeWithAssignChild() {
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.ASSIGN);
        Node grandParent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        grandParent.addChildToBack(parent);
        n.addChildToBack(grandParent);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    }

    @Test
    public void testGetFunctionJsDocInfo_jsDocInVarNode() {
        Node n = new Node(Token.VAR);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    }

    @Test
    public void testGetFunctionJsDocInfo_noJsDoc() {
        Node n = new Node(Token.FUNCTION);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    }
}