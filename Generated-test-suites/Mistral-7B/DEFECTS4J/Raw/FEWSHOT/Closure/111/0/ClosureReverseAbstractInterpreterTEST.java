package com.google.javascript.jscomp.type;

import static com.google.javascript.junit.RhinoAssert.assertTypesEqual;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.testtype.SimpleTypeUnion;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        CodingConvention convention = new CodingConvention();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType arrayType = typeRegistry.getNativeType(ARRAY_TYPE);
        JSType objectType = typeRegistry.getNativeType(OBJECT_TYPE);
        JSType numberType = typeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE);

        // When isArray returns true
        Node arrayNode = new Node(Node.IS_ARRAY);
        assertTypesEqual(arrayType, interpreter.restrictToArrayVisitor.visit(objectType));
        assertTypesEqual(arrayType, interpreter.restrictToArrayVisitor.visit(arrayType));
        assertNull(interpreter.restrictToArrayVisitor.visit(null));
        assertNull(interpreter.restrictToArrayVisitor.visit(numberType));

        // When isArray returns false
        Node notArrayNode = new Node(Node.IS_NOT_ARRAY);
        assertNull(interpreter.restrictToArrayVisitor.visit(objectType));
        assertNull(interpreter.restrictToArrayVisitor.visit(arrayType));
        assertNull(interpreter.restrictToArrayVisitor.visit(numberType));
        assertNull(interpreter.restrictToArrayVisitor.visit(null));
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        CodingConvention convention = new CodingConvention();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType arrayType = typeRegistry.getNativeType(ARRAY_TYPE);
        JSType objectType = typeRegistry.getNativeType(OBJECT_TYPE);
        JSType numberType = typeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE);

        // When isObject returns true (includes functions, but not null)
        Node isObjectNodeTrueFunction = new Node(Node.IS_OBJECT, new Node(Node.IS_FUNCTION));
        Node isObjectNodeTrueObject = new Node(Node.IS_OBJECT, new Node(Node.IS_OBJECT));
        assertNull(interpreter.restrictToNotObjectVisitor.visit(objectType));
        assertNull(interpreter.restrictToNotObjectVisitor.visit(arrayType));
        assertNull(interpreter.restrictToNotObjectVisitor.visit(numberType));
        assertNull(interpreter.restrictToNotObjectVisitor.visit(isObjectNodeTrueFunction));
        assertNull(interpreter.restrictToNotObjectVisitor.visit(isObjectNodeTrueObject));

        assertTypesEqual(OBJECT_TYPE, interpreter.restrictToObjectVisitor.visit(objectType));
        assertTypesEqual(OBJECT_TYPE, interpreter.restrictToObjectVisitor.visit(isObjectNodeTrueFunction));
        assertTypesEqual(OBJECT_TYPE, interpreter.restrictToObjectVisitor.visit(isObjectNodeTrueObject));

        // When isObject returns false
        Node isObjectNodeFalse = new Node(Node.IS_NOT_OBJECT);
        assertTypesEqual(NULL_TYPE.getGreatestSubtype(NULL_VOID), interpreter.restrictToNotObjectVisitor.visit(null));
        assertNull(interpreter.restrictToNotObjectVisitor.visit(objectType));
        assertNull(interpreter.restrictToNotObjectVisitor.visit(arrayType));
        assertNull(interpreter.restrictToNotObjectVisitor.visit(numberType));
        assertTypesEqual(NULL_TYPE.getGreatestSubtype(NULL_VOID), interpreter.restrictToNotObjectVisitor.visit(isObjectNodeFalse));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        CodingConvention convention = new CodingConvention();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType arrayType = typeRegistry.getNativeType(ARRAY_TYPE);
        JSType objectType = typeRegistry.getNativeType(OBJECT_TYPE);
        JSType numberType = typeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE);
        JSType functionType = typeRegistry.getNativeType(FunctionType.class);

        Node conditionNode1 = new Node(Node.IS_OBJECT, new Node(Node.IS_FUNCTION));
        Node left1 = new Node(Node.GET_PROP, new Node("goog"), new Node(Node.STRING_LITERAL, "isArray"));
        Node right1 = new Node(Node.IS_STRING, new Node(Node.STRING_LITERAL, "foo"));
        Node conditionNode1_1 = new Node(Node.LOGICAL_AND, conditionNode1, right1);

        Node conditionNode2 = new Node(Node.IS_OBJECT, new Node(Node.IS_FUNCTION));
        Node left2 = new Node(Node.GET_PROP, new Node("goog"), new Node(Node.STRING_LITERAL, "isDef"));
        Node right2 = new Node(Node.IS_NULL, new Node(Node.GET_PROP, new Node(Node.IDENTIFIER, "param"), new Node(Node.STRING_LITERAL, "value")));
        Node conditionNode2_1 = new Node(Node.LOGICAL_AND, conditionNode2, right2);

        Node conditionNode3 = new Node(Node.IS_OBJECT, new Node(Node.IS_FUNCTION));
        Node left3 = new Node(Node.GET_PROP, new Node("goog"), new Node(Node.STRING_LITERAL, "isDefAndNotNull"));
        Node right3 = new Node(Node.IS_NULL, new Node(Node.GET_PROP, new Node(Node.IDENTIFIER, "param"), new Node(Node.STRING_LITERAL, "value")));
        Node conditionNode3_1 = new Node(Node.LOGICAL_AND, conditionNode3, right3);

        Node conditionNode4 = new Node(Node.IS_OBJECT, new Node(Node.IS_FUNCTION));
        Node left4 = new Node(Node.GET_PROP, new Node("goog"), new Node(Node.STRING_LITERAL, "isString"));
        Node right4 = new Node(Node.IS_NULL, new Node(Node.GET_PROP, new Node(Node.IDENTIFIER, "param"), new Node(Node.STRING_LITERAL, "value")));
        Node conditionNode4_1 = new Node(Node.LOGICAL_AND, conditionNode4, right4);

        Node conditionNode5 = new Node(Node.IS_OBJECT, new Node(Node.IS_FUNCTION));
        Node left5 = new Node(Node.GET_PROP, new Node("goog"), new Node(Node.STRING_LITERAL, "isBoolean"));
        Node right5 = new Node(Node.IS_NULL, new Node(Node.GET_PROP, new Node(Node.IDENTIFIER, "param"), new Node(Node.STRING_LITERAL, "value")));
        Node conditionNode5_1 = new Node(Node.LOGICAL_AND, conditionNode5, right5);

        Node conditionNode6 = new Node(Node.IS_OBJECT, new Node(Node.IS_FUNCTION));
        Node left6 = new Node(Node.GET_PROP, new Node("goog"), new Node(Node.STRING_LITERAL, "isNumber"));
        Node right6 = new Node(Node.IS_NULL, new Node(Node.GET_PROP, new Node(Node.IDENTIFIER, "param"), new Node(Node.STRING_LITERAL, "value")));
        Node conditionNode6_1 = new Node(Node.LOGICAL_AND, conditionNode6, right6);

        Node conditionNode7 = new Node(Node.IS_OBJECT, new Node(Node.IS_FUNCTION));
        Node left7 = new Node(Node.GET_PROP, new Node("goog"), new Node(Node.STRING_LITERAL, "isArray"));
        Node right7 = new Node(Node.IS_NULL, new Node(Node.IDENTIFIER, "array"));
        Node conditionNode7_1 = new Node(Node.LOGICAL_AND, conditionNode7, right7);

        Node conditionNode8 = new Node(Node.IS_OBJECT, new Node(Node.IS_FUNCTION));
        Node left8 = new Node(Node.GET_PROP, new Node("goog"), new Node(Node.STRING_LITERAL, "isObject"));
        Node right8 = new Node(Node.IS_NULL, new Node(Node.IDENTIFIER, "object"));
        Node conditionNode8_1 = new Node(Node.LOGICAL_AND, conditionNode8, right8);

        FlowScope blindScope = new FlowScope();
        ObjectType paramType = typeRegistry.createType(SimpleTypeUnion.class, "param");

        FlowScope scope1 = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode1_1, blindScope, true);
        assertTypesEqual(ARRAY_TYPE, scope1.getVariableType(Node.IDENTIFIER, "array"));

        FlowScope scope2 = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode2_1, blindScope, false);
        assertNull(scope2.getVariableType(Node.IDENTIFIER, "param"));

        FlowScope scope3 = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode3_1, blindScope, true);
        assertTypesEqual(OBJECT_TYPE, scope3.getVariableType(Node.IDENTIFIER, "param"));

        FlowScope scope4 = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode4_1, blindScope, false);
        assertNull(scope4.getVariableType(Node.IDENTIFIER, "param"));

        FlowScope scope5 = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode5_1, blindScope, true);
        assertTypesEqual(OBJECT_TYPE, scope5.getVariableType(Node.IDENTIFIER, "param"));

        FlowScope scope6 = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode6_1, blindScope, false);
        assertNull(scope6.getVariableType(Node.IDENTIFIER, "param"));

        FlowScope scope7 = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode7_1, blindScope, true);
        assertTypesEqual(ARRAY_TYPE, scope7.getVariableType(Node.IDENTIFIER, "array"));

        FlowScope scope8 = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode8_1, blindScope, false);
        assertNull(scope8.getVariableType(Node.IDENTIFIER, "object"));
    }
}