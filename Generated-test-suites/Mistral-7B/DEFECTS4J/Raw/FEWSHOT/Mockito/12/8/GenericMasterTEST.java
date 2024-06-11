package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = null; // replace with a valid field
 Class expectedType = String.class;
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_field_is_not_generic() {
 Field field = null; // replace with a valid non-generic field
 Class expectedType = Object.class;
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_field_with_single_type_argument() {
 Field field = null; // replace with a valid field having a single type argument
 Type genericType = new Type[] { new Type[] { new Class[] { Integer.TYPE } } };
 Class expectedType = Integer.class;
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_field_with_multiple_type_arguments() {
 Field field = null; // replace with a valid field having multiple type arguments
 Type genericType = new Type[] { new Type[] { new Class[] { Integer.TYPE }, new Class[] { String.class } } };
 Class[] expectedTypes = { Integer.class, String.class };
 Class[] actualTypes = new GenericMaster().getGenericTypes(field);

 assertArrayEquals(expectedTypes, actualTypes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericType_field_with_invalid_type() {
 Field field = null; // replace with a valid field having an invalid type

 new GenericMaster().getGenericType(field);
 }

 @Test
 public void testGetGenericTypes() {
 Field field = null; // replace with a valid field having multiple type arguments
 Type genericType = new Type[] { new Type[] { new Class[] { Integer.TYPE }, new Class[] { String.class } } };
 Class[] expectedTypes = { Integer.class, String.class };
 Class[] actualTypes = new GenericMaster().getGenericTypes(field);

 assertArrayEquals(expectedTypes, actualTypes);
 }

 @Test
 public void testGetGenericTypes_field_with_single_type_argument() {
 Field field = null; // replace with a valid field having a single type argument
 Type genericType = new Type[] { new Type[] { new Class[] { Integer.TYPE } } };
 Class expectedType = Integer.class;
 Class actualType = new GenericMaster().getGenericTypes(field)[0];

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypes_field_with_no_generic_type() {
 Field field = null; // replace with a valid non-generic field

 assertNull(new GenericMaster().getGenericTypes(field));
 }
}