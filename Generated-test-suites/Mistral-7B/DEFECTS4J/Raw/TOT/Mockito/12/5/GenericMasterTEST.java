import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 // Test with a generic field of List type
 Class clazz = List.class;
 Field field = ReflectionUtils.findField(clazz, "list");
 if (field != null) {
 GenericMaster genericMaster = new GenericMaster();
 Class expectedType = List.class;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }

 // Test with a non-generic field
 clazz = String.class;
 field = ReflectionUtils.findField(clazz, "value");
 if (field != null) {
 GenericMaster genericMaster = new GenericMaster();
 Class expectedType = Object.class;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }

 // Test with null field
 clazz = null;
 field = null;
 GenericMaster genericMaster = new GenericMaster();
 Class expectedType = Object.class;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeWithNullGenericType() {
 GenericMaster genericMaster = new GenericMaster();
 Field field = null;
 Class expectedException = NullPointerException.class;
 Class actualException = null;
 try {
 actualException = genericMaster.getGenericType(field);
 } catch (NullPointerException e) {
 // do nothing
 }
 Assert.assertSame(expectedException, actualException);
 }

 @Test(expected = ClassCastException.class)
 public void testGetGenericTypeWithNonParameterizedType() {
 GenericMaster genericMaster = new GenericMaster();
 Field field = ReflectionUtils.findField(int[].class, "length");
 if (field != null) {
 Field modifiersField = Field.class.getDeclaredField("modifiers");
 modifiersField.setAccessible(true);
 modifiersField.setInt(field, Modifier.FINAL | Modifier.STATIC);
 Type generic = field.getGenericType();
 Class expectedException = ClassCastException.class;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertSame(expectedException, actualType.getClass());
 }
 }
}