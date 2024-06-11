package org.mockito.internal.util.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.GenericTestClass;

public class GenericMasterTest {

 @Test
 public void testGetGenericType_SimpleField() {
 Field field = GenericTestClass.class.getDeclaredField("simpleField");
 GenericMaster genericMaster = new GenericMaster();
 Class expectedType = Integer.class;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_NullField() {
 GenericMaster genericMaster = new GenericMaster();
 Field field = null;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericType_NotGenericField() {
 Field field = GenericTestClass.class.getDeclaredField("notGenericField");
 GenericMaster genericMaster = new GenericMaster();
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals(Object.class, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericType_InvalidField() {
 Field field = new Field("invalidField", Integer.TYPE);
 GenericMaster genericMaster = new GenericMaster();
 genericMaster.getGenericType(field);
 }

 @Test
 public void testGetGenericType_NestedGenericField() {
 Field field = GenericNestedTestClass.class.getDeclaredField("nestedField");
 GenericMaster genericMaster = new GenericMaster();
 Class expectedType = String[].class;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_GenericFieldWithArray() {
 Field field = GenericTestClassArray.class.getDeclaredField("field");
 GenericMaster genericMaster = new GenericMaster();
 Class expectedType = Integer[].class;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }
}

class GenericTestClass {
 private Integer simpleField;
 private String notGenericField;
}

class GenericNestedTestClass {
 private String nestedField;
}

class GenericTestClassArray {
 private Integer[] field;
}