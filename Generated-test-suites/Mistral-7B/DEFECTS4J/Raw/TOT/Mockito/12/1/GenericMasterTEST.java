import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

 private GenericMaster genericMaster;
 private Field fieldUnderTest;

 @Before
 public void setUp() {
 genericMaster = new GenericMaster();

 try {
 fieldUnderTest = FieldTestDouble.class.getDeclaredField("testField");
 fieldUnderTest.setAccessible(true);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericType_FieldWithSingleGenericType() {
 Class expectedType = String.class;
 Class actualType = genericMaster.getGenericType(fieldUnderTest);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_FieldWithoutGenericType() {
 Class expectedType = Object.class;
 Class actualType = genericMaster.getGenericType(new FieldTestDouble().getClass().getDeclaredField("testFieldWithoutGenericType"));

 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericType_NullField() {
 genericMaster.getGenericType(null);
 }

 @Test
 public void testGetGenericType_FieldWithEmptyArrayAsGenericType() {
 Class expectedType = new Class[0].getClass();
 Class actualType = genericMaster.getGenericType(new FieldTestDouble().getClass().getDeclaredField("testFieldWithEmptyArrayAsGenericType"));

 assertEquals(expectedType, actualType);
 }
}