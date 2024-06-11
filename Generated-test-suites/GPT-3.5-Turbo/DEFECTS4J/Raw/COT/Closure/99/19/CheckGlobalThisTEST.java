
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler mockCompiler;

    @Before
    public void setUp() {
        mockCompiler = mock(AbstractCompiler.class);
        checkGlobalThis = new CheckGlobalThis(mockCompiler, CheckLevel.WARNING);
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_DoNotTraverse() {
        Node functionNode = createFunctionNodeWithConstructorAnnotation();

        boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, mock(Node.class));

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_DoNotTraverse() {
        Node functionNode = createFunctionNodeWithThisAnnotation();

        boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, mock(Node.class));

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation_DoNotTraverse() {
        Node functionNode = createFunctionNodeWithOverrideAnnotation();

        boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, mock(Node.class));

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionWithInvalidParents_DoNotTraverse() {
        Node functionNode = createFunctionNodeWithInvalidParents();

        boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, mock(Node.class));

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionWithNormalParents_Traverse() {
        Node functionNode = createFunctionNodeWithNormalParents();

        boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, mock(Node.class));

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_LeftSideOfAssignment_Traverse() {
        Node assignNode = createAssignNode();
        Node functionNode = createFunctionNodeWithNormalParents();
        assignNode.getFirstChild().addChildToFront(functionNode);

        boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, assignNode);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignmentToPrototypeProperty_DoNotTraverse() {
        Node assignNode = createAssignNodeToPrototypeProperty();
        Node functionNode = createFunctionNodeWithNormalParents();
        assignNode.getFirstChild().addChildToFront(functionNode);

        boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, assignNode);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignmentToSubPropertyOfPrototype_DoNotTraverse() {
        Node assignNode = createAssignNodeToSubPropertyOfPrototype();
        Node functionNode = createFunctionNodeWithNormalParents();
        assignNode.getFirstChild().addChildToFront(functionNode);

        boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, assignNode);

        assertEquals(false, result);
    }

    @Test
    public void testVisit_ThisOnLeftSideOfAssignment_ReportError() {
        Node thisNode = createThisNode();
        Node assignNode = createAssignNode();
        assignNode.addChildToFront(thisNode);
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        DiagnosticType globalThisType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS",
                "dangerous use of the global 'this' object");

        checkGlobalThis.visit(mockNodeTraversal, thisNode, assignNode);

        verify(mockNodeTraversal).makeError(thisNode, CheckLevel.WARNING, globalThisType);
    }

    @Test
    public void testVisit_ThisOnRightSideOfAssignmentWithPrototypeProperty_DoNotReportError() {
        Node thisNode = createThisNode();
        Node assignNode = createAssignNodeToPrototypeProperty();
        assignNode.addChildToFront(thisNode);
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);

        checkGlobalThis.visit(mockNodeTraversal, thisNode, assignNode);

        verifyZeroInteractions(mockNodeTraversal);
    }

    @Test
    public void testVisit_ThisOnRightSideOfAssignmentWithSubPropertyOfPrototype_DoNotReportError() {
        Node thisNode = createThisNode();
        Node assignNode = createAssignNodeToSubPropertyOfPrototype();
        assignNode.addChildToFront(thisNode);
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);

        checkGlobalThis.visit(mockNodeTraversal, thisNode, assignNode);

        verifyZeroInteractions(mockNodeTraversal);
    }

    private Node createFunctionNodeWithConstructorAnnotation() {
        Node functionNode = Node.newString(Token.FUNCTION, "function");
        functionNode.setJSDocInfo(createJsDocInfoWithConstructorAnnotation());
        return functionNode;
    }

    private Node createFunctionNodeWithThisAnnotation() {
        Node functionNode = Node.newString(Token.FUNCTION, "function");
        functionNode.setJSDocInfo(createJsDocInfoWithThisAnnotation());
        return functionNode;
    }

    private Node createFunctionNodeWithOverrideAnnotation() {
        Node functionNode = Node.newString(Token.FUNCTION, "function");
        functionNode.setJSDocInfo(createJsDocInfoWithOverrideAnnotation());
        return functionNode;
    }

    private Node createFunctionNodeWithInvalidParents() {
        Node functionNode = Node.newString(Token.FUNCTION, "function");
        Node parent = Node.newString(Token.CALL, "parent");
        parent.addChildToFront(functionNode);
        return functionNode;
    }

    private Node createFunctionNodeWithNormalParents() {
        return Node.newString(Token.FUNCTION, "function");
    }

    private Node createAssignNode() {
        return Node.newString(Token.ASSIGN, "=");
    }

    private Node createAssignNodeToPrototypeProperty() {
        Node assignNode = createAssignNode();
        assignNode.getFirstChild().addChildToFront(createPrototypePropertyNode());
        return assignNode;
    }

    private Node createAssignNodeToSubPropertyOfPrototype() {
        Node assignNode = createAssignNode();
        assignNode.getFirstChild().addChildToFront(createSubPropertyOfPrototypeNode());
        return assignNode;
    }

    private Node createThisNode() {
        return Node.newString(Token.THIS, "this");
    }

    private JSDocInfo createJsDocInfoWithConstructorAnnotation() {
        JSDocInfoBuilder builder = new JSDocInfoBuilder(true);
        builder.recordConstructor();
        return builder.build();
    }

    private JSDocInfo createJsDocInfoWithThisAnnotation() {
        JSDocInfoBuilder builder = new JSDocInfoBuilder(true);
        builder.recordThisType();
        return builder.build();
    }

    private JSDocInfo createJsDocInfoWithOverrideAnnotation() {
        JSDocInfoBuilder builder = new JSDocInfoBuilder(true);
        builder.recordOverride();
        return builder.build();
    }

    private Node createPrototypePropertyNode() {
        return Node.newString(Token.NAME, "prototype");
    }

    private Node createSubPropertyOfPrototypeNode() {
        Node prototypePropertyNode = createPrototypePropertyNode();
        Node subPropertyNode = Node.newString(Token.STRING, "subProperty");
        prototypePropertyNode.addChildToFront(subPropertyNode);
        return prototypePropertyNode;
    }
}
