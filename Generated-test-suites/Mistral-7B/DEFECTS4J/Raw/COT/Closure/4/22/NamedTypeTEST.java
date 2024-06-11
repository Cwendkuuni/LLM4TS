package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.types.JSType;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.StaticSlot;
import com.google.javascript.rhino.types.UnknownType;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NamedTypeTest {
  @Mock private ErrorReporter errorReporter;
  @Mock private JSTypeRegistry registry;
  @Mock private StaticScope<JSType> enclosing;
  @InjectMocks private NamedType namedType;

  private static final String REFERENCE = "java.lang.String";

  @Before
  public void setUp() {
    Mockito.when(registry.getType(REFERENCE)).thenReturn(String.class);
  }

  @Test
  public void testCreateNamedType() {
    JSType type = namedType.createNamedType(registry, REFERENCE, "test", 1, 1);
    assertEquals(REFERENCE, type.getReferenceName());
    assertEquals(NamedType.class, type.getClass());
  }

  @Test
  public void testIsNamedType() {
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testIsResolved() {
    assertFalse(namedType.isResolved());
  }

  @Test
  public void testResolveViaRegistry() {
    namedType.resolve(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertEquals(String.class, namedType.getReferencedType());
  }

  @Test(expected = AssertionError.class)
  public void testResolveViaRegistry_error() {
    Mockito.when(registry.getType(REFERENCE)).thenReturn(null);
    namedType.resolve(errorReporter, enclosing);
  }

  @Test
  public void testResolveViaProperties() {
    StaticSlot<JSType> slot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosing.getSlot(Mockito.anyString())).thenReturn(slot);

    Mockito.when(slot.getType()).thenReturn(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));
    namedType.resolve(errorReporter, enclosing);
    assertSame(UnknownType.class, namedType.getReferencedType());

    Mockito.when(slot.getType()).thenReturn(registry.getType("java.lang.Object"));
    namedType.resolve(errorReporter, enclosing);
    assertEquals(ObjectType.class, namedType.getReferencedType().getClass());
  }

  @Test(expected = AssertionError.class)
  public void testResolveViaProperties_error() {
    StaticSlot<JSType> slot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosing.getSlot(Mockito.anyString())).thenReturn(slot);

    Mockito.when(slot.getType()).thenReturn(null);
    namedType.resolve(errorReporter, enclosing);
  }

  @Test
  public void testFinishPropertyContinuations() {
    List<PropertyContinuation> propertyContinuations = Lists.newArrayList();
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "testProperty", String.class, false, null);
    propertyContinuations.add(propertyContinuation);
    namedType.propertyContinuations = propertyContinuations;

    ObjectType referencedObjType = Mockito.mock(ObjectType.class);
    Mockito.when(referencedObjType.isUnknownType()).thenReturn(false);
    namedType.setReferencedObjTypeInternal(referencedObjType);

    namedType.finishPropertyContinuations();
    assertSame(propertyContinuation.getType(),
        namedType.getPropertyType("testProperty"));
    assertNull(namedType.propertyContinuations);
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = Mockito.mock(Predicate.class);
    Mockito.when(validator.apply(Mockito.any(JSType.class))).thenReturn(true);

    namedType.setValidator(validator);
    Mockito.verify(validator).apply(namedType.getReferencedType());
  }
}