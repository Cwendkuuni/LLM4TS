package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.jstype.visitor.RestrictByFalseTypeOfResultVisitor;
import com.google.javascript.rhino.jstype.visitor.RestrictByTrueTypeOfResultVisitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    private CodingConvention convention;
    private JSTypeRegistry typeRegistry;
    private ClosureReverseAbstractInterpreter interpreter;

    @Before
    public void setup() {
        this.convention = mock(CodingConvention.class);
        this.typeRegistry = mock(JSTypeRegistry.class);
        this.interpreter = new ClosureReverseAbstractInterpreter(this.convention, this.typeRegistry);
    }

    @Test
    public void testRestrictToArrayVisitor() {
        Node nodeArray = mock(Node.class);
        ObjectType objectTypeArray = mock(ObjectType.class);
        when(nodeArray.getType()).thenReturn(objectTypeArray);

        JSType jstypeArray = this.interpreter.getTypeIfRefinable(nodeArray, null);
        assertNotNull(jstypeArray);
        assertNotNull(jstypeArray.isSubtype(this.interpreter.getNativeType(ARRAY_TYPE)));

        Visitor<JSType> visitor = this.interpreter.restrictToArrayVisitor;
        JSType jstype = objectTypeArray.visit(visitor);
        assertNotNull(jstype);
        assertNotNull(jstype.isSubtype(this.interpreter.getNativeType(ARRAY_TYPE)));
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        Node nodeObject = mock(Node.class);
        ObjectType objectType = mock(ObjectType.class);
        when(nodeObject.getType()).thenReturn(objectType);

        JSType jstypeObject = this.interpreter.getTypeIfRefinable(nodeObject, null);
        assertNotNull(jstypeObject);
        assertNull(jstypeObject.isSubtype(this.interpreter.getNativeType(ARRAY_TYPE)));

        Visitor<JSType> visitor = this.interpreter.restrictToNotArrayVisitor;
        JSType jstype = objectType.visit(visitor);
        assertNull(jstype);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        Node nodeObject = mock(Node.class);
        ObjectType objectType = mock(ObjectType.class);
        FunctionType functionType = mock(FunctionType.class);
        when(nodeObject.getType()).thenReturn(objectType);

        JSType jstypeObject = this.interpreter.getTypeIfRefinable(nodeObject, null);
        assertNotNull(jstypeObject);

        Visitor<JSType> visitor = this.interpreter.restrictToObjectVisitor;
        JSType jstype = objectType.visit(visitor);
        assertNotNull(jstype);
        assertNull(jstype.isSubtype(this.interpreter.getNativeType(ARRAY_TYPE)));

        when(nodeObject.getType()).thenReturn(functionType);
        jstype = objectType.visit(visitor);
        assertNotNull(jstype);
        assertNull(jstype.isSubtype(this.interpreter.getNativeType(ARRAY_TYPE)));
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        Node nodeAll = mock(Node.class);
        JSType allType = this.interpreter.getNativeType(JSTypeNative.ALL_TYPE);
        when(nodeAll.getType()).thenReturn(allType);

        Visitor<JSType> visitor = this.interpreter.restrictToNotObjectVisitor;
        JSType jstype = allType.visit(visitor);
        assertNotNull(jstype);
        assertNull(jstype.isSubtype(this.interpreter.getNativeType(ObjectType.class)));

        Node nodeFunction = mock(Node.class);
        FunctionType functionType = mock(FunctionType.class);
        when(nodeFunction.getType()).thenReturn(functionType);

        jstype = functionType.visit(visitor);
        assertNull(jstype);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node nodeCall = mock(Node.class);
        Node nodeGetProp = mock(Node.class);
        Node nodeString = mock(Node.class);

        Node left = mock(Node.class);
        Node right = mock(Node.class);

        when(nodeCall.getFirstChild()).thenReturn(left);
        when(nodeCall.getLastChild()).thenReturn(nodeString);
        when(left.isName()).thenReturn(true);
        when(left.getString()).thenReturn("goog");
        when(nodeString.isString()).thenReturn(true);
        when(nodeString.getString()).thenReturn("isArray");

        JSType jstypeString = this.interpreter.getTypeIfRefinable(nodeString, null);
        assertNotNull(jstypeString);
        assertNull(jstypeString.isSubtype(this.interpreter.getNativeType(ARRAY_TYPE)));

        Function<TypeRestriction, JSType> restricter = this.interpreter.restricters.get("isArray");
        assertNotNull(restricter);

        TypeRestriction typeRestriction = new TypeRestriction(null, true);
        JSType jstype = restricter.apply(typeRestriction);

        assertNotNull(jstype);
        assertNotNull(jstype.isSubtype(this.interpreter.getNativeType(ARRAY_TYPE)));

        when(left.getString()).thenReturn("isDef");
        jstype = restricter.apply(typeRestriction);
        assertNull(jstype);
    }
}