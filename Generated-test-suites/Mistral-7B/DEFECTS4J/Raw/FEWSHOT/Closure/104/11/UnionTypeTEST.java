package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.UnionType;
import com.google.javascript.rhino.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Set;

public class UnionTypeTest {
    private ErrorReporter errorReporter;
    private JSTypeRegistry registry;
    private StaticScope<JSType> scope;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        registry = mock(JSTypeRegistry.class);
        errorReporter = mock(ErrorReporter.class);
        scope = new StaticScope<>(registry, errorReporter);
    }

    @Test
    public void testGetAlternates() {
        Set<JSType> alternates = ImmutableSet.of(mock(JSType.class), mock(JSType.class));
        UnionType unionType = new UnionType(registry, alternates);
        assertEquals(alternates, unionType.getAlternates());
    }

    @Test
    public void testForgiveUnknownNames() {
        JSType typeA = mock(JSType.class);
        JSType typeB = mock(JSType.class);
        when(typeA.getClass()).thenReturn(UnionType.class);
        when(typeB.getClass()).thenReturn(UnionType.class);
        Set<JSType> alternates = ImmutableSet.of(typeA, typeB);
        UnionType unionType = new UnionType(registry, alternates);

        unionType.forgiveUnknownNames();

        verify(typeA).forgiveUnknownNames();
        verify(typeB).forgiveUnknownNames();
    }

    @Test
    public void testMatchesNumberContext() {
        Set<JSType> alternates = ImmutableSet.of(
            new IntType(registry),
            new NumberType(registry)
        );
        UnionType unionType = new UnionType(registry, alternates);
        assertTrue(unionType.matchesNumberContext());
    }

    @Test
    public void testMatchesStringContext() {
        Set<JSType> alternates = ImmutableSet.of(
                new StringType(registry),
                new NumberType(registry)
        );
        UnionType unionType = new UnionType(registry, alternates);
        assertTrue(unionType.matchesStringContext());
    }

    @Test
    public void testMatchesObjectContext() {
        Set<JSType> alternates = ImmutableSet.of(
                new ObjectType(registry),
                new NullType(registry)
        );
        UnionType unionType = new UnionType(registry, alternates);
        assertTrue(unionType.matchesObjectContext());
    }

    @Test
    public void testFindPropertyType() {
        JSType stringType = new StringType(registry);
        JSType booleanType = new BooleanType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

        String propertyName = "length";
        JSType propertyType = unionType.findPropertyType(propertyName);

        assertEquals(stringType, propertyType);
    }

    @Test
    public void testCanAssignTo() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
        JSType that = new NumberType(registry);
        assertTrue(unionType.canAssignTo(that));
    }

    @Test
    public void testCanBeCalled() {
        JSType functionType = new FunctionType(registry);
        UnionType unionType = new UnionType(registry, ImmutableSet.of(functionType));
        assertTrue(unionType.canBeCalled());
    }

    @Test
    public void testRestrictByNotNullOrUndefined() {
        JSType nullType = new NullType(registry);
        Set<JSType> alternates = ImmutableSet.of(new IntType(registry), nullType);
        UnionType unionType = new UnionType(registry, alternates);
        UnionType restricted = unionType.restrictByNotNullOrUndefined();
        assertNotNull(restricted);
        assertTrue(restricted.isNullType());
    }

    @Test
    public void testTestForEquality() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

        JSType that = new StringType(registry);
        TernaryValue result = unionType.testForEquality(that);

        assertTrue(result.isTrue());

        that = new NumberType(registry);
        result = unionType.testForEquality(that);

        assertTrue(result.isFalse());
    }

    @Test
    public void testIsNullable() {
        JSType nullType = new NullType(registry);
        Set<JSType> alternates = ImmutableSet.of(new IntType(registry), nullType);
        UnionType unionType = new UnionType(registry, alternates);

        assertTrue(unionType.isNullable());
    }

    @Test
    public void testIsUnknownType() {
        Set<JSType> alternates = ImmutableSet.of(new IntType(registry), new VoidType(registry));
        UnionType unionType = new UnionType(registry, alternates);

        assertFalse(unionType.isUnknownType());
    }

    @Test
    public void testGetLeastSupertype() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);
        JSType objectType = new ObjectType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
        JSType that = new ObjectType(registry);

        JSType result = unionType.getLeastSupertype(that);

        assertEquals(objectType, result);
    }

    @Test
    public void testMeet() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);
        JSType intType = new IntType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
        UnionType thatType = new UnionType(registry, ImmutableSet.of(intType));

        UnionType result = unionType.meet(thatType);

        assertEquals(intType, result);
    }

    @Test
    public void testEquals() {
        JSType stringTypeA = new StringType(registry);
        JSType stringTypeB = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionTypeA = new UnionType(registry, ImmutableSet.of(stringTypeA, numberType));
        UnionType unionTypeB = new UnionType(registry, ImmutableSet.of(stringTypeB, numberType));

        assertTrue(unionTypeA.equals(unionTypeB));
    }

    @Test
    public void testHashCode() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

        int hashCode = unionType.hashCode();

        UnionType sameUnionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
        int sameHashCode = sameUnionType.hashCode();

        UnionType differentUnionType = new UnionType(registry, ImmutableSet.of(new StringType(registry), numberType));
        int differentHashCode = differentUnionType.hashCode();

        assertEquals(hashCode, sameHashCode);
        assertNotEquals(hashCode, differentHashCode);
    }

    @Test
    public void testIsUnionType() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

        assertTrue(unionType.isUnionType());
    }

    @Test
    public void testIsObject() {
        JSType stringType = new StringType(registry);
        JSType booleanType = new BooleanType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

        assertTrue(unionType.isObject());
    }

    @Test
    public void testContains() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

        assertTrue(unionType.contains(stringType));
        assertTrue(unionType.contains(numberType));
        assertFalse(unionType.contains(new IntType(registry)));
    }

    @Test
    public void testGetRestrictedUnion() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
        JSType that = new StringType(registry);

        UnionType result = unionType.getRestrictedUnion(that);

        assertEquals(stringType, result);
    }

    @Test
    public void testToString() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

        String result = unionType.toString();

        assertEquals("(String|Number)", result);
    }

    @Test
    public void testIsSubtype() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);
        JSType intType = new IntType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

        assertTrue(unionType.isSubtype(intType));
        assertTrue(intType.isSubtype(unionType));
        assertFalse(unionType.isSubtype(new VoidType(registry)));
    }

    @Test
    public void testGetRestrictedTypeGivenToBooleanOutcome() {
        JSType booleanTypeA = new BooleanType(registry);
        JSType booleanTypeB = new BooleanType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(booleanTypeA, booleanTypeB));

        BooleanLiteralSet result = unionType.getPossibleToBooleanOutcomes();

        assertEquals(BooleanLiteralSet.BOTH, result);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        JSType stringType = new StringType(registry);
        JSType booleanType = new BooleanType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

        BooleanLiteralSet result = unionType.getPossibleToBooleanOutcomes();

        assertEquals(BooleanLiteralSet.FALSE, result);
    }

    @Test
    public void testGetTypesUnderEquality() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
        JSType that = new StringType(registry);

        TypePair result = unionType.getTypesUnderEquality(that);

        assertEquals(new UnionType(registry, ImmutableSet.of(stringType)), result.typeA);
        assertEquals(new UnionType(registry, ImmutableSet.of(that)), result.typeB);
    }

    @Test
    public void testGetTypesUnderInequality() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
        JSType that = new StringType(registry);

        TypePair result = unionType.getTypesUnderInequality(that);

        assertEquals(new UnionType(registry, ImmutableSet.of(numberType)), result.typeA);
        assertEquals(new UnionType(registry, ImmutableSet.of(stringType)), result.typeB);
    }

    @Test
    public void testGetTypesUnderShallowInequality() {
        JSType stringType = new StringType(registry);
        JSType numberType = new NumberType(registry);

        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
        JSType that = new StringType(registry);

        TypePair result = unionType.getTypesUnderShallowInequality(that);

        assertEquals(new UnionType(registry, ImmutableSet.of(numberType)), result.typeA);
        assertEquals(new UnionType(registry, ImmutableSet.of(stringType)), result.typeB);
    }

    @Test
    public void testVisit() {
        Visitor<Object> visitor = mock(Visitor.class);

        JSType stringType = new StringType(registry);
        UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType));

        unionType.visit(visitor);

        verify(visitor).caseUnionType(unionType);
    }

    @Test
    public void testResolveInternal() {
        JSType stringType = mock(JSType.class);
        JSType numberType = mock(JSType.class);
        Set<JSType> alternates = ImmutableSet.of(stringType, numberType);
        UnionType unionType = spy(new UnionType(registry, alternates));

        when(registry.getNativeType(JSTypeNative.NO_TYPE)).thenReturn(null);

        unionType.resolveInternal(errorReporter, scope);

        verify(stringType).resolveInternal(errorReporter, scope);
        verify(numberType).resolveInternal(errorReporter, scope);
    }
}