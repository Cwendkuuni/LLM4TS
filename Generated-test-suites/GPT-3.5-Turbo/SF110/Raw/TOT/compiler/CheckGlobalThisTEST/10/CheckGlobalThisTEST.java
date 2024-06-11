import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.testing.GeneratorSupplier;
import com.google.javascript.testing.GeneratorSupplier.OneOfString;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private FakeAbstractCompiler compiler;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        compiler = new FakeAbstractCompiler();
        checkGlobalThis = new CheckGlobalThis(compiler);
    }

    @Test
    public void shouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
        NodeTraversal t = getFakeNodeTraversalForFunctionWithConstructorAnnotation();

        boolean result = checkGlobalThis.shouldTraverse(t, null, null);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
        NodeTraversal t = getFakeNodeTraversalForFunctionWithThisAnnotation();

        boolean result = checkGlobalThis.shouldTraverse(t, null, null);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_FunctionWithoutAnnotationInsideScript_ShouldReturnTrue() {
        NodeTraversal t = getFakeNodeTraversalForFunctionWithoutAnnotationInsideScript();

        boolean result = checkGlobalThis.shouldTraverse(t, null, null);

        assertTrue(result);
    }

    @Test
    public void shouldTraverse_FunctionInsideObjectLiteral_ShouldReturnFalse() {
        NodeTraversal t = getFakeNodeTraversalForFunctionInsideObjectLiteral();

        boolean result = checkGlobalThis.shouldTraverse(t, null, null);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_RightSideOfAssignmentToPrototypeProperty_ShouldReturnFalse() {
        NodeTraversal t = getFakeNodeTraversalForRightSideOfAssignmentToPrototypeProperty();

        boolean result = checkGlobalThis.shouldTraverse(t, null, null);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_RightSideOfNestedAssignmentToPrototypeProperty_ShouldReturnFalse() {
        NodeTraversal t = getFakeNodeTraversalForRightSideOfNestedAssignmentToPrototypeProperty();

        boolean result = checkGlobalThis.shouldTraverse(t, null, null);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_LeftSideOfAssignment_ShouldReturnTrue() {
        NodeTraversal t = getFakeNodeTraversalForLeftSideOfAssignment();

        boolean result = checkGlobalThis.shouldTraverse(t, null, null);

        assertTrue(result);
    }

    @Test
    public void shouldReportThis_AssignLhsChildNotNull_ShouldReturnTrue() {
        Node n = new Node(Token.THIS);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME, "a");

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertTrue(result);
    }

    @Test
    public void shouldReportThis_ParentIsNull_ShouldReturnFalse() {
        Node n = new Node(Token.THIS);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertFalse(result);
    }

    @Test
    public void shouldReportThis_AlsoReportThisWithPropertyAccess_ShouldReturnTrue() {
        FakeNode parent = new FakeNode(Token.GETPROP);

        boolean result = checkGlobalThis.shouldReportThis(parent);

        assertTrue(result);
    }

    @Test
    public void visit_IsThisAndShouldReportThis_ShouldReportError() {
        NodeTraversal t = getFakeNodeTraversalForThisWithWarning();
        Node n = new Node(Token.THIS);

        checkGlobalThis.visit(t, n, null);

        assertEquals(1, compiler.errors.size());
        assertEquals("dangerous use of the global 'this' object", compiler.errors.get(0).description);
    }

    @Test
    public void visit_IsAssignLhsChild_ShouldSetAssignLhsChildToNull() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.NAME, "l");
        checkGlobalThis.assignLhsChild = n;

        checkGlobalThis.visit(t, n, null);

        assertNull(checkGlobalThis.assignLhsChild);
    }

    private NodeTraversal getFakeNodeTraversalForFunctionWithConstructorAnnotation() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        t.setScope(new Scope());
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        n.setJSDocInfo(jsDocInfo);
        return t;
    }

    private NodeTraversal getFakeNodeTraversalForFunctionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        t.setScope(new Scope());
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType(new OneOfString());
        n.setJSDocInfo(jsDocInfo);
        return t;
    }

    private NodeTraversal getFakeNodeTraversalForFunctionWithoutAnnotationInsideScript() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        t.setScope(new Scope());
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);
        return t;
    }

    private NodeTraversal getFakeNodeTraversalForFunctionInsideObjectLiteral() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        t.setScope(new Scope());
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.OBJECTLIT);
        n.setParent(parent);
        return t;
    }

    private NodeTraversal getFakeNodeTraversalForRightSideOfAssignmentToPrototypeProperty() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        t.setScope(new Scope());
        Node n = new Node(Token.NAME, "prototype");
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);
        n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);
        n.setParent(parent);
        return t;
    }

    private NodeTraversal getFakeNodeTraversalForRightSideOfNestedAssignmentToPrototypeProperty() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        t.setScope(new Scope());
        Node n = new Node(Token.NAME, "prototype");
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);
        Node grandparent = new Node(Token.GETPROP);
        grandparent.addChildToFront(parent);
        n = new Node(Token.NAME, "property");
        grandparent.addChildToBack(n);
        parent.setParent(grandparent);
        n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);
        return t;
    }

    private NodeTraversal getFakeNodeTraversalForLeftSideOfAssignment() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        t.setScope(new Scope());
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        n = new Node(Token.THIS);
        parent.addChildToBack(n);
        n.setParent(parent);
        return t;
    }

    private NodeTraversal getFakeNodeTraversalForThisWithWarning() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        t.setScope(new Scope());
        t.setGlobal("this");
        t.makeError(new Node(Token.THIS), CheckGlobalThis.GLOBAL_THIS);
        return t;
    }
}

class FakeAbstractCompiler extends AbstractCompiler {
    List<CompilerError> errors;

     FakeAbstractCompiler() {
        this.errors = new ArrayList<>();
    }

    @Override
    public void report(JSError error) {
        errors.add(new CompilerError(error));
    }
}

class CompilerError {
    String description;

    CompilerError(JSError error) {
        this.description = error.description;
    }
}

class FakeNode extends Node {
    FakeNode(int type) {
        super(type);
    }
}