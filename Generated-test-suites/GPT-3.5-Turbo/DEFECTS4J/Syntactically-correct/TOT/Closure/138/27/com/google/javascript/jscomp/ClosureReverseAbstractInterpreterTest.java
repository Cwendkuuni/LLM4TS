import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter interpreter;
    private CodingConvention convention;
    private JSTypeRegistry typeRegistry;

    @Before
    public void setup() {
        convention = mock(CodingConvention.class);
        typeRegistry = mock(JSTypeRegistry.class);
        interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_functionCallWithValidStringParam_returnsPreciserScope() {
        // Arrange
        Node condition = createFunctionCallNode("goog.isString", "param");
        FlowScope blindScope = mock(FlowScope.class);
        JSType mockParamType = mock(JSType.class);
        when(typeRegistry.getTypeIfRefinable(eq("param"), eq(blindScope))).thenReturn(mockParamType);
        FunctionType functionType = mock(FunctionType.class);
        when(mockParamType.visit(any())).thenReturn(mockParamType);
        when(convention.getHasTypeOfPredicate(eq("string"), eq(true))).thenReturn(functionType);
        when(mockParamType.restrictByTrueType(eq(functionType))).thenReturn(mockParamType);
        boolean outcome = true;

        // Act
        FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // Assert
        verify(mockParamType).restrictByTrueType(eq(functionType));
        verify(blindScope).createChildFlowScope();
        verify(blindScope).declareNameInScope(eq("param"), eq(mockParamType));
        assertEquals(blindScope, actual);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_functionCallWithInvalidStringParam_returnsBlindScope() {
        // Arrange
        Node condition = createFunctionCallNode("goog.isString", "param");
        FlowScope blindScope = mock(FlowScope.class);
        JSType mockParamType = mock(JSType.class);
        when(typeRegistry.getTypeIfRefinable(eq("param"), eq(blindScope))).thenReturn(mockParamType);
        FunctionType functionType = mock(FunctionType.class);
        when(mockParamType.visit(any())).thenReturn(mockParamType);
        when(convention.getHasTypeOfPredicate(eq("string"), eq(true))).thenReturn(functionType);
        when(mockParamType.restrictByTrueType(eq(functionType))).thenReturn(null);
        boolean outcome = true;

        // Act
        FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // Assert
        verify(mockParamType).restrictByTrueType(eq(functionType));
        verify(blindScope, times(0)).createChildFlowScope();
        verify(blindScope, times(0)).declareNameInScope(eq("param"), eq(mockParamType));
        assertEquals(blindScope, actual);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_functionCallWithValidNumberParam_returnsPreciserScope() {
        // Arrange
        Node condition = createFunctionCallNode("goog.isNumber", "param");
        FlowScope blindScope = mock(FlowScope.class);
        JSType mockParamType = mock(JSType.class);
        when(typeRegistry.getTypeIfRefinable(eq("param"), eq(blindScope))).thenReturn(mockParamType);
        FunctionType functionType = mock(FunctionType.class);
        when(mockParamType.visit(any())).thenReturn(mockParamType);
        when(convention.getHasTypeOfPredicate(eq("number"), eq(true))).thenReturn(functionType);
        when(mockParamType.restrictByTrueType(eq(functionType))).thenReturn(mockParamType);
        boolean outcome = true;

        // Act
        FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // Assert
        verify(mockParamType).restrictByTrueType(eq(functionType));
        verify(blindScope).createChildFlowScope();
        verify(blindScope).declareNameInScope(eq("param"), eq(mockParamType));
        assertEquals(blindScope, actual);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_functionCallWithInvalidNumberParam_returnsBlindScope() {
        // Arrange
        Node condition = createFunctionCallNode("goog.isNumber", "param");
        FlowScope blindScope = mock(FlowScope.class);
        JSType mockParamType = mock(JSType.class);
        when(typeRegistry.getTypeIfRefinable(eq("param"), eq(blindScope))).thenReturn(mockParamType);
        FunctionType functionType = mock(FunctionType.class);
        when(mockParamType.visit(any())).thenReturn(mockParamType);
        when(convention.getHasTypeOfPredicate(eq("number"), eq(true))).thenReturn(functionType);
        when(mockParamType.restrictByTrueType(eq(functionType))).thenReturn(null);
        boolean outcome = true;

        // Act
        FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // Assert
        verify(mockParamType).restrictByTrueType(eq(functionType));
        verify(blindScope, times(0)).createChildFlowScope();
        verify(blindScope, times(0)).declareNameInScope(eq("param"), eq(mockParamType));
        assertEquals(blindScope, actual);
    }

    // ... repeat this pattern for other test cases

    private Node createFunctionCallNode(String functionName, String param) {
        Node callNode = new Node(Token.CALL);
        Node calleeNode = new Node(Token.GETPROP);
        Node functionNode = new Node(Token.NAME, functionName);
        Node paramNode = new Node(Token.STRING, param);
        callNode.addChildToBack(calleeNode);
        callNode.addChildToBack(paramNode);
        calleeNode.addChildToBack(functionNode);
        calleeNode.addChildToBack(new Node(Token.STRING, "someObject"));

        return callNode;
    }
}