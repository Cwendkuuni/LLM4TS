import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestUtils;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field testField = FieldTestUtils.getField(GenericMaster.class, "testField");
 Class expectedType = String.class;

 GenericMaster genericMaster = new GenericMaster();
 Class actualType = genericMaster.getGenericType(testField);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_NullField() {
 Field testField = null;
 GenericMaster genericMaster = new GenericMaster();

 try {
 genericMaster.getGenericType(testField);
 fail("Should have thrown NPE");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testGetGenericType_NonGenericField() {
 Field testField = FieldTestUtils.getField(GenericMaster.class, "nonGenericField");
 GenericMaster genericMaster = new GenericMaster();

 Class actualType = genericMaster.getGenericType(testField);
 assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericType_DeepNestedGenerics() {
 Class outerClass = Object.class;
 Class innerClass = List.class;
 Field testField = FieldTestUtils.getField(outerClass, "testField");
 Type innerType = new TypeToken<List<String>>() {}.getType();
 ParameterizedType innerParameterizedType = (ParameterizedType) testField.getGenericType();
 Type innerGenericType = innerParameterizedType.getActualTypeArguments()[0];
 Field innerTestField = FieldTestUtils.getField(innerClass, "testField");

 GenericMaster genericMaster = new GenericMaster();
 Class actualType = genericMaster.getGenericType(testField);
 Class expectedType = (Class) innerGenericType;

 assertEquals(expectedType, actualType);
 }
}