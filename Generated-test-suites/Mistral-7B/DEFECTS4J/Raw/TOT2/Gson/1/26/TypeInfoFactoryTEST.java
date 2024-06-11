import static org.junit.Assert.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class TypeInfoFactoryTest {
  @Test
  public void testTypeInfoForArray() {
    Type intTypeArray = new GenericArrayTypeImpl(Integer.TYPE);
    TypeInfoFactory factory = new TypeInfoFactory();
    TypeInfo expectedTypeInfo = new TypeInfoArray(intTypeArray);
    TypeInfo actualTypeInfo = factory.TypeInfoForArray(intTypeArray);
    assertInstanceOf(TypeInfoArray.class, actualTypeInfo);
    // Add more assertions if necessary
  }
}