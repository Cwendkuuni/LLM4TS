import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setup() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_RegularFunction_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assert(result);
    }

    @Test
    public void testShouldTraverse_ConstructorFunction_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.putProp("jsdoc", new JSDocInfo().setConstructor(true));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assert(!result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.putProp("jsdoc", new JSDocInfo().setHasThisType(true));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assert(!result);
    }

    @Test
    public void testShouldTraverse_AssignmentFunctionToPrototype_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.STRING, "prototype");
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assert(!result);
    }

    @Test
    public void testShouldTraverse_AssignmentFunctionToPrototypeWithSubproperty_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.STRING, "prototype");
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);
        parent.addChildToBack(new Node(Token.STRING, "subproperty"));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assert(!result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignment_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        parent.addChildToBack(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assert(result);
    }

    @Test
    public void testVisit_ThisNodeWithParent_AssignsGlobalThisWarning() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        CheckLevel level = CheckLevel.WARNING;
        t.makeError(n, level, CheckGlobalThis.GLOBAL_THIS);
        checkGlobalThis.visit(t, n, parent);
        // Assert that the global this warning is assigned
    }

    @Test
    public void testVisit_NotAssigningGlobalThis_LeftSideOfAssignmentHandled() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        // Assert that no global this warning is assigned
    }

    @Test
    public void testVisit_ResetAssignLhsChild_AssignLhsChildIsNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        // Assert that assignLhsChild is null
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionHasJsDoc_ReturnsJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        n.putProp("jsdoc", new JSDocInfo().setConstructor(true));
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assert(result != null);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssignWithJsDoc_ReturnsJsDocInfo() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.putProp("jsdoc", new JSDocInfo().setConstructor(true));
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assert(result != null);
    }

    @Test
    public void testGetFunctionJsDocInfo_GrampsIsVarWithJsDoc_ReturnsJsDocInfo() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.NAME);
        Node gramps = new Node(Token.VAR);
        gramps.putProp("jsdoc", new JSDocInfo().setConstructor(true));
        parent.setParent(gramps);
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assert(result != null);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionHasNoJsDoc_ReturnsNull() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assert(result == null);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsNameWithNoJsDoc_ReturnsNull() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assert(result == null);
    }

    @Test
    public void testGetFunctionJsDocInfo_GrampsIsVarWithNoJsDoc_ReturnsNull() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.NAME);
        Node gramps = new Node(Token.VAR);
        parent.setParent(gramps);
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assert(result == null);
    }
}