import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testConstructorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> mockInstanceCreator = mock(InstanceCreator.class);
    when(mockInstanceCreator.createInstance(TypeToken.of(String.class))).thenReturn("Test");
    instanceCreators.put(TypeToken.of(String.class), mockInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<String> constructor = constructorConstructor.get(TypeToken.of(String.class));
    assertNotNull(constructor);
  }

  @Test
  public void testGetObjectConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> mockInstanceCreator = mock(InstanceCreator.class);
    when(mockInstanceCreator.createInstance(TypeToken.of(String.class))).thenReturn("Test");
    instanceCreators.put(TypeToken.of(String.class), mockInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<String> constructor = constructorConstructor.get(TypeToken.of(String.class));
    String result = constructor.construct();
    assertNotNull(result);
    assertEquals("Test", result);
  }
}