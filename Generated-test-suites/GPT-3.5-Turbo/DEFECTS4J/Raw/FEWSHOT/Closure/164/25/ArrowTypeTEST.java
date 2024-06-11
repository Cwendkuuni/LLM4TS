package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
    private ArrowType arrowType;
    private JSTypeRegistry registry;
    private Node parameters;
    private JSType returnType;
    private boolean returnTypeInferred;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        parameters = new Node(JSTypeNative.UNKNOWN_TYPE);
        returnType = JSTypeNative.UNKNOWN_TYPE;
        returnTypeInferred = false;
        arrowType = new ArrowType(registry, parameters, returnType, returnTypeInferred);
    }

    @Test
    public void testConstructor() {
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }

    @Test
    public void testIsSubtype() {
        JSType other = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType.isSubtype(other));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType other = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType.hasEqualParameters(other));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType other = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType.isEquivalentTo(other));
    }

    @Test
    public void testHashCode() {
        int hashCode = 0;
        hashCode += returnType.hashCode();
        if (returnTypeInferred) {
            hashCode += 1;
        }
        Node param = parameters.getFirstChild();
        while (param != null) {
            JSType paramType = param.getJSType();
            if (paramType != null) {
                hashCode += paramType.hashCode();
            }
            param = param.getNext();
        }
        assertEquals(hashCode, arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSType that = new ArrowType(registry, parameters, returnType);
        arrowType.getLeastSupertype(that);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSType that = new ArrowType(registry, parameters, returnType);
        arrowType.getGreatestSubtype(that);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSType that = new ArrowType(registry, parameters, returnType);
        arrowType.testForEquality(that);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        Visitor<Boolean> visitor = new Visitor<Boolean>() {
            @Override
            public <D> Boolean caseDeclaration(DeclarationType<D> declType, D decl) {
                return null;
            }

            @Override
            public Boolean caseBoolean(BooleanType type) {
                return null;
            }

            @Override
            public Boolean caseUnion(UnionType type) {
                return null;
            }

            @Override
            public Boolean caseRecord(RecordType type) {
                return null;
            }

            @Override
            public Boolean caseArrow(ArrowType type) {
                return null;
            }

            @Override
            public Boolean caseNull(NullType type) {
                return null;
            }

            @Override
            public Boolean caseTypeVariable(TypeVariable type) {
                return null;
            }

            @Override
            public Boolean caseNativeObjectType(NativeObjectType type) {
                return null;
            }

            @Override
            public Boolean caseNamedType(NamedType type) {
                return null;
            }

            @Override
            public Boolean caseVoid(VoidType type) {
                return null;
            }

            @Override
            public Boolean caseNumber(NumberType type) {
                return null;
            }

            @Override
            public Boolean caseAllType() {
                return null;
            }

            @Override
            public Boolean caseTemplateType(TemplateType templateType) {
                return null;
            }

            @Override
            public Boolean caseString(StringType type) {
                return null;
            }
        };
        arrowType.visit(visitor);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter() {
            @Override
            public void warning(String message, String sourceName, int line, int lineOffset) {
            }

            @Override
            public void error(String message, String sourceName, int line, int lineOffset) {
            }

            @Override
            public void fatal(String message, String sourceName, int line, int lineOffset) {
            }
        };
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = arrowType.resolveInternal(errorReporter, scope);
        assertEquals(arrowType, resolvedType);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        String expected = "ArrowType";
        assertEquals(expected, arrowType.toStringHelper(true));
    }
}