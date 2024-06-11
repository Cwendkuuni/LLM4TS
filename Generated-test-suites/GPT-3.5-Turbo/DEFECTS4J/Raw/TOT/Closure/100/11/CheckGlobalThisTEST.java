import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CheckGlobalThisTest {

    private AbstractCompiler mockCompiler;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        mockCompiler = mock(AbstractCompiler.class);
        checkGlobalThis = new CheckGlobalThis(mockCompiler, CheckLevel.WARNING);
    }

    @Test
    public void shouldTraverse_shouldReturnTrueForFunctionWithNoJSDocInfo() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node mockNode = mock(Node.class);

        boolean result = checkGlobalThis.shouldTraverse(mockNodeTraversal, mockNode, null);

        assertEquals(true, result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForConstructorFunctionWithJSDocInfo() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node mockNode = mock(Node.class);
        JSDocInfo mockJSDocInfo = mock(JSDocInfo.class);

        mockNode.setType(Token.FUNCTION);
        mockNode.setJSDocInfo(mockJSDocInfo);
        mockJSDocInfo.setConstructor(true);

        boolean result = checkGlobalThis.shouldTraverse(mockNodeTraversal, mockNode, null);

        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForFunctionWithThisTypeJSDocInfo() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node mockNode = mock(Node.class);
        JSDocInfo mockJSDocInfo = mock(JSDocInfo.class);

        mockNode.setType(Token.FUNCTION);
        mockNode.setJSDocInfo(mockJSDocInfo);
        mockJSDocInfo.setThisType(mock(Node.class));

        boolean result = checkGlobalThis.shouldTraverse(mockNodeTraversal, mockNode, null);

        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForAssignmentToPrototypeProperty() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node mockParent = mock(Node.class);
        Node mockNode = mock(Node.class);
        Node mockLhs = mock(Node.class);
        Node mockRhs = mock(Node.class);

        mockParent.setType(Token.ASSIGN);
        mockParent.setFirstChild(mockLhs);
        mockLhs.setType(Token.GETPROP);
        mockLhs.getLastChild().setString("prototype");

        boolean result = checkGlobalThis.shouldTraverse(mockNodeTraversal, mockNode, mockParent);

        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForAssignmentToSubpropertyOfPrototype() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node mockParent = mock(Node.class);
        Node mockNode = mock(Node.class);
        Node mockLhs = mock(Node.class);
        Node mockRhs = mock(Node.class);

        mockParent.setType(Token.ASSIGN);
        mockParent.setFirstChild(mockLhs);
        mockLhs.setType(Token.GETPROP);
        mockLhs.getLastChild().setString("prototype.property");

        boolean result = checkGlobalThis.shouldTraverse(mockNodeTraversal, mockNode, mockParent);

        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_shouldReturnTrueForLeftSideOfAssignment() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node mockParent = mock(Node.class);
        Node mockNode = mock(Node.class);
        Node mockLhs = mock(Node.class);
        Node mockRhs = mock(Node.class);

        mockParent.setType(Token.ASSIGN);
        mockParent.setFirstChild(mockLhs);

        boolean result = checkGlobalThis.shouldTraverse(mockNodeTraversal, mockNode, mockParent);

        assertEquals(true, result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForRightSideOfAssignmentToPrototypeProperty() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node mockParent = mock(Node.class);
        Node mockNode = mock(Node.class);
        Node mockLhs = mock(Node.class);
        Node mockRhs = mock(Node.class);

        mockParent.setType(Token.ASSIGN);
        mockParent.setFirstChild(mockLhs);
        mockParent.setSecondChild(mockRhs);
        mockLhs.setType(Token.GETPROP);
        mockLhs.getLastChild().setString("prototype");

        boolean result = checkGlobalThis.shouldTraverse(mockNodeTraversal, mockNode, mockParent);

        assertEquals(false, result);
    }

    @Test
    public void visit_shouldReportGlobalThisWhenEncountered() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node mockNode = mock(Node.class);
        Node mockParent = mock(Node.class);

        mockNode.setType(Token.THIS);

        checkGlobalThis.visit(mockNodeTraversal, mockNode, mockParent);

        verify(mockCompiler, times(1)).report(mockNodeTraversal.makeError(mockNode, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
    }

    @Test
    public void visit_shouldResetAssignLhsChildWhenNodeIsAssignLhsChild() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node mockNode = mock(Node.class);
        Node mockParent = mock(Node.class);

        mockNodeTraversal.makeError(mockNode, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS);

        checkGlobalThis.visit(mockNodeTraversal, mockNode, mockParent);

        assertEquals(null, checkGlobalThis.assignLhsChild);
    }
    
    @Test
    public void getFunctionJsDocInfo_shouldReturnJsDocInfoFromNode() {
        Node mockNode = mock(Node.class);
        JSDocInfo mockJSDocInfo = mock(JSDocInfo.class);
        
        mockNode.setJSDocInfo(mockJSDocInfo);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(mockNode);
        
        assertEquals(mockJSDocInfo, result);
    }
    
    @Test
    public void getFunctionJsDocInfo_shouldReturnJsDocInfoFromParent() {
        Node mockNode = mock(Node.class);
        Node mockParent = mock(Node.class);
        JSDocInfo mockJSDocInfo = mock(JSDocInfo.class);
        
        mockNode.setParent(mockParent);
        mockParent.setJSDocInfo(mockJSDocInfo);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(mockNode);
        
        assertEquals(mockJSDocInfo, result);
    }
    
    @Test
    public void getFunctionJsDocInfo_shouldReturnJsDocInfoFromGramps() {
        Node mockNode = mock(Node.class);
        Node mockParent = mock(Node.class);
        Node mockGramps = mock(Node.class);
        JSDocInfo mockJSDocInfo = mock(JSDocInfo.class);
        
        mockNode.setParent(mockParent);
        mockParent.setParent(mockGramps);
        mockGramps.setType(Token.VAR);
        mockGramps.setJSDocInfo(mockJSDocInfo);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(mockNode);
        
        assertEquals(mockJSDocInfo, result);
    }
}