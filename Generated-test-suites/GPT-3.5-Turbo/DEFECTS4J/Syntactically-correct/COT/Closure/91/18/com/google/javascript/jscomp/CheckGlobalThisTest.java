import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;


public class CheckGlobalThisTest {

    // Test shouldTraverse method

    @Test
    public void testShouldTraverse_functionIsConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionHasThisAnnotation() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionHasOverrideAnnotation() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_noParentNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_parentNodeIsBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_parentNodeIsScript() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_parentNodeIsName() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_parentNodeIsAssign() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_parentNodeIsString() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_parentNodeIsNumber() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_parentNodeIsGetPropWithPrototype() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node lhs = new Node(Token.GETPROP);
        lhs.setString("prototype");
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_parentNodeIsGetPropNestedPrototype() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node lhs = new Node(Token.GETPROP);
        lhs.setString("prototype");
        Node llhs = new Node(Token.GETPROP);
        llhs.setString("prototype");
        lhs.addChildToBack(llhs);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_parentNodeIsGetPropNotPrototype() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node lhs = new Node(Token.GETPROP);
        lhs.setString("property");
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertEquals(true, result);
    }

    // Test visit method

    @Test
    public void testVisit_thisOnLeftSideOfAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        lhs.setString("a");
        parent.addChildToBack(lhs);
        parent.addChildToBack(n);
        checkGlobalThis.visit(t, n, parent);
        // Assert error reported
    }

    @Test
    public void testVisit_thisOnRightSideOfAssignmentNoPrototype() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        lhs.setString("b");
        parent.addChildToBack(lhs);
        parent.addChildToBack(n);
        checkGlobalThis.visit(t, n, parent);
        // Assert no error reported
    }

    @Test
    public void testVisit_thisOnRightSideOfAssignmentNestedPrototype() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.setString("prototype");
        Node llhs = new Node(Token.GETPROP);
        llhs.setString("prototype");
        lhs.addChildToBack(llhs);
        parent.addChildToBack(lhs);
        parent.addChildToBack(n);
        checkGlobalThis.visit(t, n, parent);
        // Assert no error reported
    }

    @Test
    public void testVisit_thisOnRightSideOfAssignmentPrototype() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.setString("prototype");
        parent.addChildToBack(lhs);
        parent.addChildToBack(n);
        checkGlobalThis.visit(t, n, parent);
        // Assert error reported
    }

    @Test
    public void testVisit_thisOnRightSideOfAssignmentNotPrototype() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.setString("property");
        parent.addChildToBack(lhs);
        parent.addChildToBack(n);
        checkGlobalThis.visit(t, n, parent);
        // Assert error reported
    }

}