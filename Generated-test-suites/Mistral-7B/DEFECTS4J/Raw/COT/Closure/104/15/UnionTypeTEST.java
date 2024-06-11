package com.google.javascript.rhino.jstype;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNotNull;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertSame;
 import static org.junit.Assert.assertTrue;

 import java.util.Arrays;
 import java.util.HashSet;
 import java.util.Set;

 import org.junit.Test;

 import com.google.common.collect.ImmutableSet;
 import com.google.javascript.rhino.ErrorReporter;
 import com.google.javascript.rhino.JSType;
 import com.google.javascript.rhino.JSTypeRegistry;
 import com.google.javascript.rhino.JSTypeRegistryTest;
 import com.google.javascript.rhino.StaticScope;
 import com.google.javascript.rhino.jstype.UnionType;

 public class UnionTypeTest {
   private static JSTypeRegistry registry;
   static {
     registry = new JSTypeRegistry();
   }

   @Test
   public void testUnionTypeCreation() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     assertEquals(ImmutableSet.of(registry.getIntType(), registry.getStringType()), unionType.getAlternates());
   }

   @Test
   public void testFindPropertyType() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getStringType());
       add(registry.getIntType());
     }};

     UnionType unionType = new UnionType(registry, alternates);

     String propName = "length";
     JSType expectedType = registry.getIntType();
     JSType result = unionType.findPropertyType(propName);
     assertSame(expectedType, result);
   }

   @Test
   public void testCanAssignTo() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
     }};

     UnionType unionType = new UnionType(registry, alternates);

     JSType otherType = registry.getStringType();
     assertTrue(unionType.canAssignTo(otherType));
   }

   @Test
   public void testToString() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getStringType());
       add(registry.getIntType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     assertEquals("(String|number)", unionType.toString());
   }

   @Test
   public void testIsSubtype() {
     Set<JSType> alternates1 = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
     }};

     UnionType unionType1 = new UnionType(registry, alternates1);

     Set<JSType> alternates2 = new HashSet<JSType>() {{
       add(registry.getStringType());
     }};

     UnionType unionType2 = new UnionType(registry, alternates2);

     JSType intType = registry.getIntType();
     assertTrue(unionType1.isSubtype(intType));
     assertTrue(unionType2.isSubtype(intType));
     assertTrue(unionType1.isSubtype(unionType2));
     assertFalse(intType.isSubtype(unionType1));
     assertFalse(intType.isSubtype(unionType2));
   }

   @Test(expected = AssertionError.class)
   public void testSelfAssignment() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getStringType());
       add(registry.getIntType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     unionType = unionType;
   }

   @Test(expected = AssertionError.class)
   public void testNestedUnionTypes() {
     Set<JSType> alternates1 = new HashSet<JSType>() {{
       add(new UnionType(registry, new HashSet<JSType>() {{
                 add(registry.getIntType());
                 add(registry.getStringType());
               }}));
       add(registry.getStringType());
     }};

     UnionType unionType = new UnionType(registry, alternates1);
   }

   @Test
   public void testEqualsAndHashCode() {
     Set<JSType> alternates1 = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
     }};

     Set<JSType> alternates2 = new HashSet<JSType>() {{
       add(registry.getStringType());
       add(registry.getIntType());
     }};

     UnionType unionType1 = new UnionType(registry, alternates1);
     UnionType unionType2 = new UnionType(registry, alternates2);

     assertFalse(unionType1.equals(unionType2));
     assertFalse(unionType1.hashCode() == unionType2.hashCode());

     UnionType unionType3 = new UnionType(registry, alternates1);
     assertTrue(unionType1.equals(unionType3));
     assertTrue(unionType1.hashCode() == unionType3.hashCode());
   }

   @Test
   public void testGetLeastSupertype() {
     Set<JSType> alternates1 = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
     }};

     UnionType unionType1 = new UnionType(registry, alternates1);

     Set<JSType> alternates2 = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
       add(registry.getBooleanType());
     }};

     UnionType unionType2 = new UnionType(registry, alternates2);

     JSType leastSupertype = unionType1.getLeastSupertype(unionType2);
     assertEquals(registry.getObjectType(), leastSupertype);
   }

   @Test
   public void testMeet() {
     Set<JSType> alternates1 = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
     }};

     UnionType unionType1 = new UnionType(registry, alternates1);

     Set<JSType> alternates2 = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getBooleanType());
     }};

     UnionType unionType2 = new UnionType(registry, alternates2);

     UnionType result = unionType1.meet(unionType2);
     assertEquals(registry.getIntType(), result);
   }

   @Test
   public void testIsObject() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
       add(registry.getObjectType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     assertTrue(unionType.isObject());
   }

   @Test
   public void testIsUnionType() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     assertTrue(unionType.isUnionType());
   }

   @Test
   public void testIsNullable() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
       add(registry.getNullType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     assertTrue(unionType.isNullable());
   }

   @Test
   public void testIsUnknownType() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
       add(registry.getUnknownType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     assertTrue(unionType.isUnknownType());
   }

   @Test
   public void testGetRestrictedUnion() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
       add(registry.getBooleanType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     JSType restricted = unionType.getRestrictedUnion(registry.getIntType());
     assertEquals(registry.getStringType(), restricted);
   }

   @Test
   public void testGetRestrictedTypeGivenToBooleanOutcome() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
       add(registry.getBooleanType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     TypePair result = unionType.getTypesUnderEquality(registry.getBooleanType());
     assertEquals(registry.getIntType(), result.typeA);
     assertNull(result.typeB);
   }

   @Test
   public void testGetPossibleToBooleanOutcomes() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
       add(registry.getBooleanType());
     }};

     UnionType unionType = new UnionType(registry, alternates);
     BooleanLiteralSet literals = unionType.getPossibleToBooleanOutcomes();
     assertEquals(BooleanLiteralSet.BOTH, literals);
   }

   @Test
   public void testEqualsAndHashCodeWithEmptyAlternates() {
     UnionType emptyUnionType = new UnionType(registry, ImmutableSet.of());
     UnionType otherUnionType = new UnionType(registry, new HashSet<JSType>());

     assertTrue(emptyUnionType.equals(otherUnionType));
     assertTrue(emptyUnionType.hashCode() == otherUnionType.hashCode());
   }

   @Test
   public void testEqualsAndHashCodeWithSameAlternatesInDifferentOrder() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
     }};

     UnionType unionType1 = new UnionType(registry, alternates);
     UnionType unionType2 = new UnionType(registry, Arrays.asList(alternates.toArray(new JSType[0])).subList(1, alternates.size()));

     assertFalse(unionType1.equals(unionType2));
     assertFalse(unionType1.hashCode() == unionType2.hashCode());
   }

   @Test
   public void testEqualsAndHashCodeWithDifferentAlternates() {
     Set<JSType> alternates1 = new HashSet<JSType>() {{
       add(registry.getIntType());
       add(registry.getStringType());
     }};

     UnionType unionType1 = new UnionType(registry, alternates1);

     Set<JSType> alternates2 = new HashSet<JSType>() {{
       add(registry.getStringType());
       add(registry.getIntType());
     }};

     UnionType unionType2 = new UnionType(registry, alternates2);

     assertFalse(unionType1.equals(unionType2));
     assertFalse(unionType1.hashCode() == unionType2.hashCode());
   }

   @Test
   public void testResolveInternal() {
     Set<JSType> alternates = new HashSet<JSType>() {{
       add(registry.getStringType());
       add(registry.getIntType());
     }};

     UnionType unionType = new UnionType(registry, alternates);

     ErrorReporter errorReporter = new ErrorReporter() {};
     StaticScope staticScope = new StaticScope(registry, errorReporter);

     unionType.resolveInternal(errorReporter, staticScope);
   }
 }