package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.jstype.FunctionType;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ClosureReverseAbstractInterpreterTest {
    private CodingConvention codingConvention;
    private JSTypeRegistry typeRegistry;
    private ClosureReverseAbstractInterpreter raI;

    @Before
    public void setUp() {
        codingConvention = mock(CodingConvention.class);
        typeRegistry = mock(JSTypeRegistry.class);
        raI = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
        Node condition = createMockNode("goog.isDef(x)");
        JSType xType = createMockType("number");
        FlowScope blindScope = mock(FlowScope.class);

        when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(createMockType("array"));
        when(typeRegistry.getNativeType(OBJECT_TYPE)).thenReturn(createMockType("object"));
        when(typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN)).thenReturn(createMockType("number|string|boolean"));
        when(typeRegistry.getNativeType(NULL_TYPE)).thenReturn(createMockType("null"));
        when(typeRegistry.getNativeType(NULL_VOID)).thenReturn(createMockType("null|void"));
        when(typeRegistry.getNativeType(VOID_TYPE)).thenReturn(createMockType("void"));

        when(raI.restricters.get("isDef")).thenReturn(new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                    return getRestrictedWithoutUndefined(p.type);
                } else {
                    return null;
                }
            }
        });

        JSType result = raI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true).getScopeType();
        assertNotNull(result);
        assertEquals("number", result.getClass().getSimpleName());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
        Node condition = createMockNode("goog.isDef(x)");
        JSType xType = createMockType("number");
        FlowScope blindScope = mock(FlowScope.class);

        when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(createMockType("array"));
        when(typeRegistry.getNativeType(OBJECT_TYPE)).thenReturn(createMockType("object"));
        when(typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN)).thenReturn(createMockType("number|string|boolean"));
        when(typeRegistry.getNativeType(NULL_TYPE)).thenReturn(createMockType("null"));
        when(typeRegistry.getNativeType(NULL_VOID)).thenReturn(createMockType("null|void"));
        when(typeRegistry.getNativeType(VOID_TYPE)).thenReturn(createMockType("void"));

        when(raI.restricters.get("isDef")).thenReturn(new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                    return getRestrictedWithoutUndefined(p.type);
                } else {
                    return null;
                }
            }
        });

        JSType result = raI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false).getScopeType();
        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_isNull_true() {
        Node condition = createMockNode("goog.isNull(x)");
        JSType xType = null;
        FlowScope blindScope = mock(FlowScope.class);

        when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(createMockType("array"));
        when(typeRegistry.getNativeType(OBJECT_TYPE)).thenReturn(createMockType("object"));
        when(typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN)).thenReturn(createMockType("number|string|boolean"));
        when(typeRegistry.getNativeType(NULL_TYPE)).thenReturn(createMockType("null"));
        when(typeRegistry.getNativeType(NULL_VOID)).thenReturn(createMockType("null|void"));
        when(typeRegistry.getNativeType(VOID_TYPE)).thenReturn(createMockType("void"));

        when(raI.restricters.get("isNull")).thenReturn(new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                    return getRestrictedWithoutNull(p.type);
                } else {
                    return null;
                }
            }
        });

        JSType result = raI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true).getScopeType();
        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_isNull_false() {
        Node condition = createMockNode("goog.isNull(x)");
        JSType xType = createMockType("number");
        FlowScope blindScope = mock(FlowScope.class);

        when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(createMockType("array"));
        when(typeRegistry.getNativeType(OBJECT_TYPE)).thenReturn(createMockType("object"));
        when(typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN)).thenReturn(createMockType("number|string|boolean"));
        when(typeRegistry.getNativeType(NULL_TYPE)).thenReturn(createMockType("null"));
        when(typeRegistry.getNativeType(NULL_VOID)).thenReturn(createMockType("null|void"));
        when(typeRegistry.getNativeType(VOID_TYPE)).thenReturn(createMockType("void"));

        when(raI.restricters.get("isNull")).thenReturn(new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                    return getRestrictedWithoutNull(p.type);
                } else {
                    return null;
                }
            }
        });

        JSType result = raI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false).getScopeType();
        assertNotNull(result);
        assertEquals("null|void", result.getClass().getSimpleName());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_true() {
        Node condition = createMockNode("goog.isDefAndNotNull(x)");
        JSType xType = createMockType("number");
        FlowScope blindScope = mock(FlowScope.class);

        when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(createMockType("array"));
        when(typeRegistry.getNativeType(OBJECT_TYPE)).thenReturn(createMockType("object"));
        when(typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN)).thenReturn(createMockType("number|string|boolean"));
        when(typeRegistry.getNativeType(NULL_TYPE)).thenReturn(createMockType("null"));
        when(typeRegistry.getNativeType(NULL_VOID)).thenReturn(createMockType("null|void"));
        when(typeRegistry.getNativeType(VOID_TYPE)).thenReturn(createMockType("void"));

        when(raI.restricters.get("isDefAndNotNull")).thenReturn(new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                    return getRestrictedWithoutUndefined(getRestrictedWithoutNull(p.type));
                } else {
                    return null;
                }
            }
        });

        JSType result = raI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true).getScopeType();
        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_false() {
        Node condition = createMockNode("goog.isDefAndNotNull(x)");
        JSType xType = createMockType("number");
        FlowScope blindScope = mock(FlowScope.class);

        when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(createMockType("array"));
        when(typeRegistry.getNativeType(OBJECT_TYPE)).thenReturn(createMockType("object"));
        when(typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN)).thenReturn(createMockType("number|string|boolean"));
        when(typeRegistry.getNativeType(NULL_TYPE)).thenReturn(createMockType("null"));
        when(typeRegistry.getNativeType(NULL_VOID)).thenReturn(createMockType("null|void"));
        when(typeRegistry.getNativeType(VOID_TYPE)).thenReturn(createMockType("void"));

        when(raI.restricters.get("isDefAndNotNull")).thenReturn(new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                    return getRestrictedWithoutUndefined(getRestrictedWithoutNull(p.type));
                } else {
                    return null;
                }
            }
        });

        JSType result = raI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false).getScopeType();
        assertNotNull(result);
        assertEquals("null|void", result.getClass().getSimpleName());
    }

    private Node createMockNode(String nodeName) {
        Node node = mock(Node.class);
        when(node.isCall()).thenReturn(true);
        when(node.getChildCount()).thenReturn(2);
        when(node.getFirstChild()).thenReturn(createMockNode("goog"));
        when(node.getLastChild()).thenReturn(createMockNode(nodeName));
        return node;
    }

    private JSType createMockType(String typeName) {
        JSType type = mock(JSType.class);
        when(type.getClass().getSimpleName()).thenReturn(typeName);
        return type;
    }

    private JSType getRestrictedWithoutUndefined(JSType type) {
        if (type == null) {
            return typeRegistry.getNativeType(VOID_TYPE);
        }
        return type;
    }

    private JSType getRestrictedWithoutNull(JSType type) {
        if (type == null) {
            return null;
        }
        return type;
    }

    private JSType getRestrictedByTypeOfResult(JSType type, String expectedType, boolean outcome) {
        if (type == null) {
            if (outcome) {
                return typeRegistry.getNativeType(expectedType);
            } else {
                return null;
            }
        }
        if (type.getClass().getSimpleName().equals(expectedType)) {
            return type;
        }
        return null;
    }
}