package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field testField = new Field("testField", String.class, this.getClass());
 Assert.assertEquals(String.class, GenericMaster.getGenericType(testField));
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeNullField() {
 Assert.assertNull(GenericMaster.getGenericType(null));
 }

 @Test
 public void testGetGenericTypeNonParameterizedType() {
 Field testField = new Field("testField", int.class, this.getClass());
 Assert.assertEquals(Object.class, GenericMaster.getGenericType(testField));
 }

 @Test(expected = RuntimeException.class)
 public void testGetGenericTypeMultipleActualTypeArguments() {
 Field testField = Mockito.spy(new Field("testField", new Type[] { List.class, Integer.class }, this.getClass()));
 Assert.assertThrows(RuntimeException.class, () -> GenericMaster.getGenericType(testField));
 }

}