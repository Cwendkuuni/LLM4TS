import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = mock(Type.class);
    when(type.getClass()).thenReturn(int[].class);

    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(typeInfoArray);
  }
}