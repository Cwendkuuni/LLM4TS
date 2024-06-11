package org.apache.commons.cli;

import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.TypeRestriction;
import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType arrayType = interpreter.typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE);
        ObjectTypeobjType = (ObjectType) interpreter.typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);

        // Test with null type
        assertNull(interpreter.restrictToArrayVisitor.visit(null));

        // Test with ObjectType that is an array
        ObjectType arrayObjType = (ObjectType) arrayType.visit(interpreter.restrictToArrayVisitor);
        assertEquals(arrayType, arrayObjType);

        // Test with ObjectType that is not an array
        ObjectType nonArrayObjType = (ObjectType) objType.visit(interpreter.restrictToArrayVisitor);
        assertNull(nonArrayObjType);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType arrayType = interpreter.typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE);
        ObjectType objType = (ObjectType) interpreter.typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);

        // Test with null type
        assertNull(interpreter.restrictToNotArrayVisitor.visit(null));

        // Test with ArrayType
        assertNull(arrayType.visit(interpreter.restrictToNotArrayVisitor));

        // Test with ObjectType that is an array
        ObjectType arrayObjType = (ObjectType) arrayType.visit(interpreter.restrictToNotArrayVisitor);
        assertNull(arrayObjType);

        // Test with ObjectType that is not an array
        ObjectType nonArrayObjType = (ObjectType) objType.visit(interpreter.restrictToNotArrayVisitor);
        assertEquals(objType, nonArrayObjType);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType noObjectType = interpreter.typeRegistry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
        ObjectType objType = (ObjectType) interpreter.typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);
        FunctionType funcType = (FunctionType) interpreter.typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE);

        // Test with null type
        assertNull(interpreter.restrictToObjectVisitor.visit(null));

        // Test with ObjectType
        ObjectType objectType = (ObjectType) objType.visit(interpreter.restrictToObjectVisitor);
        assertEquals(objType, objectType);

        // Test with FunctionType
        FunctionType functionType = (FunctionType) funcType.visit(interpreter.restrictToObjectVisitor);
        assertEquals(funcType, functionType);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType noObjectType = interpreter.typeRegistry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
        ObjectType objType = (ObjectType) interpreter.typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);
        FunctionType funcType = (FunctionType) interpreter.typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE);

        // Test with null type
        assertNull(interpreter.restrictToNotObjectVisitor.visit(null));

        // Test with ObjectType
        assertNull(objType.visit(interpreter.restrictToNotObjectVisitor));

        // Test with FunctionType
        assertNull(funcType.visit(interpreter.restrictToNotObjectVisitor));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType arrayType = interpreter.typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE);
        JSType objType = interpreter.typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);
        JSType funcType = interpreter.typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE);

        Node condition = new Node(CALL, null, 2);
        Node callee = new Node(GETPROP, null, "goog");
        Node param = new Node(STRING, "isArray");

        // Test with null type for condition
        FlowScope blindScope = new FlowScope();
        FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertNull(scope);

        // Test with null type for condition and false outcome
        scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
        assertNull(scope);

        // Test with ObjectType for condition and true outcome
        JSType paramType = objType;
        scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertEquals(blindScope, scope);

        // Test with ArrayType for condition and true outcome
        paramType = arrayType;
        scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertNotNull(scope);

        // Test with ObjectType for condition and false outcome
        scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
        assertNull(scope);

        // Test with ArrayType for condition and false outcome
        scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
        assertNull(scope);

        // Test with FunctionType for condition and true outcome
        paramType = funcType;
        scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertNull(scope);

        // Test with FunctionType for condition and false outcome
        scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
        assertNull(scope);
    }
}