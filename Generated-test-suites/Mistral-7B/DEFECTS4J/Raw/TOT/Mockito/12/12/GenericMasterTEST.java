import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

 // Test case for getGenericType method with a generic field
 @Test
 public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
 GenericMaster genericMaster = new GenericMaster();
 Class<String> clazz = String.class;
 Field field = clazz.getDeclaredField("LIST");
 field.setAccessible(true); // make private field accessible

 Class expectedType = String.class;
 Class actualType = genericMaster.getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 // Test case for getGenericType method with a non-generic field
 @Test
 public void testGetGenericTypeWithNonGenericField() throws NoSuchFieldException {
 GenericMaster genericMaster = new GenericMaster();
 Class<String> clazz = String.class;
 Field field = clazz.getDeclaredField("VALUE");
 field.setAccessible(true); // make private field accessible

 Object expectedType = Object.class;
 Class actualType = genericMaster.getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 // Test case for getGenericType method with null field
 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeWithNullField() throws NoSuchFieldException {
 GenericMaster genericMaster = new GenericMaster();
 Field nullField = null;

 genericMaster.getGenericType(nullField);
 }

 // Test case for getGenericType method with an illegal access field
 @Test(expected = IllegalAccessException.class)
 public void testGetGenericTypeWithIllegalAccessField() throws NoSuchFieldException {
 GenericMaster genericMaster = new GenericMaster();
 Class<String> clazz = String.class;
 Field field = clazz.getDeclaredField("PRIVATE_FIELD"); // private field
 field.setAccessible(false);

 genericMaster.getGenericType(field);
 }
}