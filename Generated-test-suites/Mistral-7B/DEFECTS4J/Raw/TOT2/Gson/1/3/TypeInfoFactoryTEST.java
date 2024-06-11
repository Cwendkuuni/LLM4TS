import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoFactory;
import java.lang.reflect.Type;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = mock(Type.class);
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertInstanceOf(TypeInfoArray.class, typeInfoArray);
  }

  @Test
  public void testTypeInfoForArray_NullType() {
    assertNull(TypeInfoFactory.getTypeInfoForArray(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArray_NonArrayType() {
    Type type = mock(Type.class);
    Preconditions.checkArgument(TypeInfoFactory.isArray(type));
    TypeInfoFactory.getTypeInfoForArray(type);
  }

  @Test(expected = RuntimeException.class)
  public void testTypeInfoForArray_ReflectiveOperationsFailure() {
    Type type = mock(Type.class);
    when(TypeUtils.isArray(type)).thenThrow(new RuntimeException("Reflection failure"));
    TypeInfoFactory.getTypeInfoForArray(type);
  }
}