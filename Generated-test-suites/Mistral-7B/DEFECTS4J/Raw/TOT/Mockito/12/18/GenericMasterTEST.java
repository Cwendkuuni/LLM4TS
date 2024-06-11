import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GenericMasterTest {

 private GenericMaster genericMaster;
 private Field field;

 @Before
 public void setUp() throws NoSuchFieldException {
 genericMaster = new GenericMaster();
 field = getField();
 }

 @Test
 public void testGetGenericType_FieldIsGeneric_ReturnsExpectedType() {
 Class expectedType = String.class;
 Field testField = new FieldTestDouble(String.class, expectedType);

 try {
 Field localField = new Field(this.getClass().getName() + "_testField", testField.getType(), new Field.Modifiers(0));
 Field modifiers = localField.getModifiers();
 localField.setAccessible(true);
 localField.set(this, testField);

 Class actualType = genericMaster.getGenericType(localField);
 assertEquals(expectedType, actualType);
 } catch (IllegalAccessException | NoSuchFieldException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetGenericType_FieldIsNotGeneric_ReturnsObject() {
 Class actualType = genericMaster.getGenericType(field);
 assertNull(actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_NullField() {
 genericMaster.getGenericType(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericType_NonFieldObject() {
 Object nonFieldObject = new Object();
 genericMaster.getGenericType(nonFieldObject);
 }

 private Field getField() throws NoSuchFieldException {
 return new Field("testField", String.class);
 }
}