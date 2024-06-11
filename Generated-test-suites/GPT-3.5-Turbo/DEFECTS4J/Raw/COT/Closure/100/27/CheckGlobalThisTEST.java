
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;
    private CheckGlobalThis checker;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node functionNode = NodeUtil.createFunction("function test() { }");
        JSDocInfo constructorAnnotation = new JSDocInfo();
        constructorAnnotation.setConstructor(true);
        functionNode.setJSDocInfo(constructorAnnotation);

        boolean result = checker.shouldTraverse(t, functionNode, null);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node functionNode = NodeUtil.createFunction("function test() { }");
        JSDocInfo thisAnnotation = new JSDocInfo();
        thisAnnotation.recordThisType();
        functionNode.setJSDocInfo(thisAnnotation);

        boolean result = checker.shouldTraverse(t, functionNode, null);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithNoAnnotations() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node functionNode = NodeUtil.createFunction("function test() { }");

        boolean result = checker.shouldTraverse(t, functionNode, null);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInAssignment_LhsIsThis() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node functionNode = NodeUtil.createFunction("function test() { }");
        Node assignmentNode = NodeUtil.createAssignment("test = function () {}");
        assignmentNode.getFirstChild().addChildToFront(functionNode);

        boolean result = checker.shouldTraverse(t, functionNode, assignmentNode);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInAssignment_LhsIsNotThis() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node functionNode = NodeUtil.createFunction("function test() { }");
        Node assignmentNode = NodeUtil.createAssignment("x = function () {}");
        assignmentNode.getFirstChild().addChildToFront(functionNode);

        boolean result = checker.shouldTraverse(t, functionNode, assignmentNode);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignmentToPrototypeProperty() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node functionNode = NodeUtil.createFunction("function test() { }");
        Node assignmentNode = NodeUtil.createAssignment("namespace.prototype.property = value");
        assignmentNode.getLastChild().addChildToFront(functionNode);

        boolean result = checker.shouldTraverse(t, functionNode, assignmentNode);

        assertFalse(result);
    }

    @Test
    public void testVisit_NodeIsThis_ShouldReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node thisNode = NodeUtil.createThis();
        Node parent = NodeUtil.createAssignment("test = this");

        checker.visit(t, thisNode, parent);
        DiagnosticType reportedType = compiler.getLastError().getType();

        assertEquals(CheckGlobalThis.GLOBAL_THIS, reportedType);
    }

    @Test
    public void testVisit_NodeIsThis_ShouldNotReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node thisNode = NodeUtil.createThis();
        Node parent = NodeUtil.createAssignment("x = this");

        checker.visit(t, thisNode, parent);

        assertNull(compiler.getLastError());
    }

    @Test
    public void testVisit_NodeIsAssignLhsChild_ShouldReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node functionNode = NodeUtil.createFunction("function test() { }");
        Node parent = NodeUtil.createAssignment("test = function () {}");
        parent.getFirstChild().addChildToFront(functionNode);
        checker.shouldTraverse(t, functionNode, parent);
        Node thisNode = NodeUtil.createThis();

        checker.visit(t, thisNode, parent);

        DiagnosticType reportedType = compiler.getLastError().getType();
        assertEquals(CheckGlobalThis.GLOBAL_THIS, reportedType);
    }

    @Test
    public void testVisit_NodeIsAssignLhsChild_ShouldNotReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node functionNode = NodeUtil.createFunction("function test() { }");
        Node parent = NodeUtil.createAssignment("x = function () {}");
        parent.getFirstChild().addChildToFront(functionNode);
        checker.shouldTraverse(t, functionNode, parent);
        Node thisNode = NodeUtil.createThis();

        checker.visit(t, thisNode, parent);

        assertNull(compiler.getLastError());
    }

}
