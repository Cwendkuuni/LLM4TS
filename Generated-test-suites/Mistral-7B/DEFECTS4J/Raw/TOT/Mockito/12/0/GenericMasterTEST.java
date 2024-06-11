import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

 @Test
 public void testGetGenericTypeWithSimpleField() {
 Field field = getClass().getDeclaredField("someField"); // assume someField is a field in this class with a generic type
 Class expectedType = String.class;
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeWithNullField() {
 Field field = null;
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericTypeWithNonGenericField() {
 Field field = getClass().getDeclaredField("someNonGenericField"); // assume someNonGenericField is a non-generic field in this class
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.assertEquals(Object.class, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeWithNullGenericType() {
 Field field = getClass().getDeclaredField("someField"); // assume someField is a field with a generic type in this class
 field.setAccessible(true); // make the field accessible
 Field genericTypeField = field.getType().getDeclaredField("genericType"); // assume "genericType" is a field in the Type class that holds the generic type information
 genericTypeField.setAccessible(true);
 Type genericType = genericTypeField.get(field.getGenericType()); // get the generic type directly to bypass the getGenericType() method in GenericMaster
 genericTypeField.setAccessible(false); // restore accessibility
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.assertEquals(Object.class, actualType); // the test should fail with a NullPointerException as the getGenericType() method should have received a null argument
 }

 @Test
 public void testGetGenericTypeWithNestedGenerics() {
 Class<List<String>> listOfStringsType = List.class.getTypeParameters()[0].getType(); // get the Type object for List<String>
 Field field = getClass().getDeclaredField("someNestedField"); // assume someNestedField is a field with the type List<String> in this class
 Class expectedType = listOfStringsType.getComponentType(); // get the component type of the List<String> type
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }
}