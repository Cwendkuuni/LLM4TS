import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter;
    private CodingConvention codingConvention;
    private JSTypeRegistry typeRegistry;

    @Before
    public void setUp() {
        codingConvention = mock(CodingConvention.class);
        typeRegistry = mock(JSTypeRegistry.class);
        closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        // Create test nodes
        Node condition1 = new Node(Node.CALL);
        Node condition2 = new Node(Node.CALL);
        Node callee = new Node(Node.GETPROP);
        callee.addChildToBack(new Node(Node.NAME, "goog"));
        callee.addChildToBack(new Node(Node.STRING, "isDef"));
        Node param1 = new Node(Node.NAME, "param1");
        Node param2 = new Node(Node.NAME, "param2");
        condition1.addChildToBack(callee);
        condition1.addChildToBack(param1);
        condition2.addChildToBack(callee);
        condition2.addChildToBack(param2);

        // Create test scopes
        FlowScope blindScope = mock(FlowScope.class);
        FlowScope informed = mock(FlowScope.class);

        // Mocked return values
        when(param1.isQualifiedName()).thenReturn(true);
        when(param2.isQualifiedName()).thenReturn(false);
        when(callee.getFirstChild()).thenReturn(new Node(Node.NAME, "goog"));
        when(callee.getLastChild()).thenReturn(new Node(Node.STRING, "isDef"));
        when(typeRegistry.getTypeIfRefinable(param1, blindScope)).thenReturn(mock(JSType.class));
        when(typeRegistry.getTypeIfRefinable(param2, blindScope)).thenReturn(mock(JSType.class));
        when(codingConvention.isVarArgsParameter(param1)).thenReturn(false);
        when(codingConvention.isVarArgsParameter(param2)).thenReturn(true);

        // Perform test
        FlowScope result1 = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope, true);
        FlowScope result2 = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope, false);

        // Verify results
        assertEquals(informed, result1);
        assertEquals(blindScope, result2);
        verify(typeRegistry).getTypeIfRefinable(param1, blindScope);
        verify(typeRegistry).getTypeIfRefinable(param2, blindScope);
        verify(codingConvention).isVarArgsParameter(param1);
        verify(codingConvention).isVarArgsParameter(param2);
        verify(blindScope).createChildFlowScope();
        verify(informed).declareNameInScope(param1, mock(JSType.class));
    }

    @Test
    public void testRestrictParameter() {
        // Create test nodes
        Node parameter = new Node(Node.NAME, "parameter");

        // Create test scopes
        FlowScope blindScope = mock(FlowScope.class);
        FlowScope informed = mock(FlowScope.class);

        // Mocked return values
        JSType type = mock(JSType.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        TypeRestriction typeRestriction = mock(TypeRestriction.class);

        when(restriction.apply(typeRestriction)).thenReturn(mock(JSType.class));

        // Perform test
        FlowScope result = closureReverseAbstractInterpreter.restrictParameter(parameter, type, blindScope, restriction, true);

        // Verify results
        assertEquals(informed, result);
        verify(restriction).apply(typeRestriction);
        verify(informed).declareNameInScope(parameter, mock(JSType.class));
    }

    @Test
    public void testClosureReverseAbstractInterpreter() {
        // Perform test
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

        // Verify results
        assertEquals(codingConvention, closureReverseAbstractInterpreter.getCodingConvention());
        assertEquals(typeRegistry, closureReverseAbstractInterpreter.getTypeRegistry());
    }

    @Test
    public void testRestrictToArrayVisitor() {
        // Create test types
        JSType type = mock(JSType.class);
        JSType arrayType = mock(JSType.class);

        // Mocked return values
        when(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE)).thenReturn(arrayType);
        when(arrayType.isSubtype(type)).thenReturn(true);

        // Perform test
        JSType result = restrictToArrayVisitor.caseObjectType(type);

        // Verify results
        assertEquals(arrayType, result);
        verify(typeRegistry).getNativeType(JSTypeNative.ARRAY_TYPE);
        verify(arrayType).isSubtype(type);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        // Create test types
        JSType type = mock(JSType.class);
        JSType arrayType = mock(JSType.class);

        // Mocked return values
        when(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE)).thenReturn(arrayType);
        when(type.isSubtype(arrayType)).thenReturn(true);

        // Perform test
        JSType result = restrictToNotArrayVisitor.caseObjectType(type);

        // Verify results
        assertNull(result);
        verify(typeRegistry).getNativeType(JSTypeNative.ARRAY_TYPE);
        verify(type).isSubtype(arrayType);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        // Create test types
        JSType type = mock(JSType.class);
        ObjectType objectType = mock(ObjectType.class);
        FunctionType functionType = mock(FunctionType.class);
        JSType noObjectType = mock(JSType.class);

        // Mocked return values
        when(typeRegistry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(noObjectType);

        // Perform test
        JSType result1 = restrictToObjectVisitor.caseTopType(type);
        JSType result2 = restrictToObjectVisitor.caseObjectType(objectType);
        JSType result3 = restrictToObjectVisitor.caseFunctionType(functionType);

        // Verify results
        assertEquals(noObjectType, result1);
        assertEquals(objectType, result2);
        assertEquals(functionType, result3);
        verify(typeRegistry).getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        // Create test types
        JSType type = mock(JSType.class);
        ObjectType objectType = mock(ObjectType.class);
        FunctionType functionType = mock(FunctionType.class);
        JSType numberStringBoolean = mock(JSType.class);
        JSType nullVoid = mock(JSType.class);

        // Mocked return values
        when(typeRegistry.createUnionType(numberStringBoolean, nullVoid)).thenReturn(mock(JSType.class));
        when(objectType.isSubtype(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE))).thenReturn(true);
        when(functionType.isSubtype(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE))).thenReturn(true);

        // Perform test
        JSType result1 = restrictToNotObjectVisitor.caseAllType();
        JSType result2 = restrictToNotObjectVisitor.caseObjectType(objectType);
        JSType result3 = restrictToNotObjectVisitor.caseFunctionType(functionType);

        // Verify results
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        verify(typeRegistry).createUnionType(numberStringBoolean, nullVoid);
        verify(objectType).isSubtype(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE));
        verify(functionType).isSubtype(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE));
    }
}