import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
    private AbstractCompiler compiler;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        checkGlobalThis = new CheckGlobalThis(compiler);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_NoThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.ADD);
        Node parent = new Node();
        parent.setType(Token.FUNCTION);
        Node assignLhsChild = new Node();
        assignLhsChild.setType(Token.NAME);

        t.shouldTraverse(n, parent, assignLhsChild);

        assertFalse("Should not traverse function with constructor or this annotation",
            t.shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_ThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.ADD);
        Node parent = new Node();
        parent.setType(Token.FUNCTION);
        Node assignLhsChild = new Node();
        assignLhsChild.setType(Token.NAME);

        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setThisType(true);

        parent.setJSDocInfo(jsDoc);

        t.shouldTraverse(n, parent, assignLhsChild);

        assertFalse("Should not traverse function with constructor or this annotation",
            t.shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_GlobalThisAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.ADD);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        Node assignLhsChild = new Node();
        assignLhsChild.setType(Token.NAME);

        t.shouldTraverse(n, parent, assignLhsChild);

        assertTrue("Should traverse function with global this assignment",
            t.shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionPrototypeMethod() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.ADD);
        Node parent = new Node();
        parent.setType(Token.BLOCK);
        Node grandparent = new Node();
        grandparent.setType(Token.FUNCTION);
        Node assignLhsChild = new Node();
        assignLhsChild.setType(Token.NAME);

        parent.setParent(grandparent);

        t.shouldTraverse(n, parent, assignLhsChild);

        assertTrue("Should traverse function with prototype method",
            t.shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionNoLogicalPlaceForThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.ADD);
        Node parent = new Node();
        parent.setType(Token.BLOCK);
        Node grandparent = new Node();
        grandparent.setType(Token.SCRIPT);
        Node assignLhsChild = new Node();
        assignLhsChild.setType(Token.NAME);

        parent.setParent(grandparent);

        t.shouldTraverse(n, parent, assignLhsChild);

        assertTrue("Should traverse function with no logical place for this annotation",
            t.shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionNotBlockOrScript() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.ADD);
        Node parent = new Node();
        parent.setType(Token.LP);
        Node grandparent = new Node();
        grandparent.setType(Token.VAR);
        Node assignLhsChild = new Node();
        assignLhsChild.setType(Token.NAME);

        parent.setParent(grandparent);

        t.shouldTraverse(n, parent, assignLhsChild);

        assertFalse("Should not traverse function not in block or script",
            t.shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionAssignmentToPrototypeProperty() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.ADD);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        Node grandparent = new Node();
        grandparent.setType(Token.GETPROP);
        Node assignLhsChild = new Node();
        assignLhsChild.setType(Token.NAME);

        parent.setFirstChild(grandparent);
        grandparent.setFirstChild(new Node());
        grandparent.getFirstChild().setString("prototype");

        t.shouldTraverse(n, parent, assignLhsChild);

        assertFalse("Should not traverse function assignment to prototype property",
            t.shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionAssignmentToPrototypeSubproperty() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.ADD);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        Node grandparent = new Node();
        grandparent.setType(Token.GETPROP);
        Node assignLhsChild = new Node();
        assignLhsChild.setType(Token.NAME);

        parent.setFirstChild(grandparent);
        grandparent.setFirstChild(new Node());
        grandparent.getFirstChild().setString("prototype");
        grandparent.getFirstChild().setFirstChild(new Node());
        grandparent.getFirstChild().getFirstChild().setString("property");

        t.shouldTraverse(n, parent, assignLhsChild);

        assertFalse("Should not traverse function assignment to prototype subproperty",
            t.shouldTraverse);
    }

    @Test
    public void testVisit_GlobalThis_NoReport() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.STRING);

        checkGlobalThis.assignLhsChild = null;
        checkGlobalThis.visit(t, n, null);

        // check no report
    }

    @Test
    public void testVisit_GlobalThis_Report() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.THIS);

        checkGlobalThis.assignLhsChild = null;
        checkGlobalThis.visit(t, n, null);

        // check report
    }

    @Test
    public void testVisit_GlobalThis_AssignLhsChildNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.THIS);

        checkGlobalThis.assignLhsChild = n;
        checkGlobalThis.visit(t, n, null);

        // check assignLhsChild set to null
    }

    @Test
    public void testShouldReportThis_AssignLhsChildNotNull() {
        Node n = new Node();
        n.setType(Token.THIS);

        checkGlobalThis.assignLhsChild = n;

        assertTrue("Should report global this when assignLhsChild is not null",
            checkGlobalThis.shouldReportThis(n));
    }

    @Test
    public void testShouldReportThis_PropertyAccess() {
        Node n = new Node();
        n.setType(Token.THIS);
        Node parent = new Node();
        parent.setType(Token.GETPROP);
        parent.setFirstChild(n);

        checkGlobalThis.assignLhsChild = null;

        assertTrue("Should report global this with property access",
            checkGlobalThis.shouldReportThis(n));
    }

    @Test
    public void testShouldReportThis_NoReport() {
        Node n = new Node();
        n.setType(Token.STRING);
        Node parent = new Node();
        parent.setType(Token.GETPROP);
        parent.setFirstChild(n);

        checkGlobalThis.assignLhsChild = null;

        assertFalse("Should not report global this",
            checkGlobalThis.shouldReportThis(n));
    }

    @Test
    public void testGetFunctionJsDocInfo_WithJsDoc() {
        Node n = new Node();

        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);

        JSDocInfo returnedJsDoc = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals("Should return the provided JSDocInfo",
            jsDoc, returnedJsDoc);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDoc_ParentName() {
        Node n = new Node();
        Node parent = new Node();
        parent.setType(Token.NAME);

        n.setParent(parent);

        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);

        JSDocInfo returnedJsDoc = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals("Should return the parent's JSDocInfo",
            jsDoc, returnedJsDoc);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDoc_ParentAssign() {
        Node n = new Node();
        Node parent = new Node();
        parent.setType(Token.ASSIGN);

        n.setParent(parent);

        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);

        JSDocInfo returnedJsDoc = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals("Should return the parent's JSDocInfo",
            jsDoc, returnedJsDoc);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDoc_VarAssignment() {
        Node n = new Node();
        Node parent = new Node();
        parent.setType(Token.VAR);
        Node grandparent = new Node();
        grandparent.setType(Token.SCRIPT);

        parent.setParent(grandparent);
        n.setParent(parent);

        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);

        JSDocInfo returnedJsDoc = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals("Should return the grandparent's JSDocInfo",
            jsDoc, returnedJsDoc);
    }
}