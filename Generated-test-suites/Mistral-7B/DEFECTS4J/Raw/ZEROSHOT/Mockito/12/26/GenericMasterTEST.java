package org.junit.test;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() throws NoSuchFieldException {
 // Test with a generic field
 Field field = new FieldTestDouble<>()[0].getClass().getDeclaredField("genericField");
 field.setAccessible(true);
 Class expectedType = String.class;
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);

 // Test with a non-generic field
 field = new FieldTestDouble()[0].getClass().getDeclaredField("nonGenericField");
 field.setAccessible(true);
 Class actualType2 = new GenericMaster().getGenericType(field);
 assertNull(actualType2);

 // Test with null field
 assertNull(new GenericMaster().getGenericType(null));
 }
}

class FieldTestDouble {
 private double genericField;
 private int nonGenericField;

 // getter and setter for tests
 // ...
}