import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.JsonIOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.UnsafeAllocator;

public class ConstructorConstructorTest {
  
  private ConstructorConstructor constructorConstructor;
  
  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(null);
  }
  
  @Test
  public void get_TypeTokenWithInstanceCreator_ReturnsInstanceCreatorConstructor() {
    // Arrange
    TypeToken<TypeWithInstanceCreator> typeToken = new TypeToken<TypeWithInstanceCreator>() {};
    InstanceCreator<TypeWithInstanceCreator> instanceCreator = new InstanceCreator<TypeWithInstanceCreator>() {
        public TypeWithInstanceCreator createInstance(Type type) {
            return new TypeWithInstanceCreator();
        }
    };
    
    // Act
    ObjectConstructor<TypeWithInstanceCreator> constructor = constructorConstructor.get(typeToken);
    
    // Assert
    assertNotNull(constructor);
    assertTrue(constructor.construct() instanceof TypeWithInstanceCreator);
  }
  
  @Test
  public void get_TypeTokenWithRawTypeInstanceCreator_ReturnsInstanceCreatorConstructor() {
    // Arrange
    TypeToken<RawTypeWithInstanceCreator> typeToken = new TypeToken<RawTypeWithInstanceCreator>() {};
    InstanceCreator<RawTypeWithInstanceCreator> instanceCreator = new InstanceCreator<RawTypeWithInstanceCreator>() {
        public RawTypeWithInstanceCreator createInstance(Type type) {
            return new RawTypeWithInstanceCreator();
        }
    };
    
    // Act
    ObjectConstructor<RawTypeWithInstanceCreator> constructor = constructorConstructor.get(typeToken);
    
    // Assert
    assertNotNull(constructor);
    assertTrue(constructor.construct() instanceof RawTypeWithInstanceCreator);
  }
  
  @Test
  public void get_DefaultConstructor_ReturnsDefaultConstructor() {
    // Arrange
    TypeToken<TypeWithDefaultConstructor> typeToken = new TypeToken<TypeWithDefaultConstructor>() {};
    
    // Act
    ObjectConstructor<TypeWithDefaultConstructor> constructor = constructorConstructor.get(typeToken);
    
    // Assert
    assertNotNull(constructor);
    assertTrue(constructor.construct() instanceof TypeWithDefaultConstructor);
  }
  
  @Test
  public void get_DefaultImplementationConstructor_ReturnsDefaultImplementationConstructor() {
    // Arrange
    TypeToken<TypeWithDefaultImplementation> typeToken = new TypeToken<TypeWithDefaultImplementation>() {};
    
    // Act
    ObjectConstructor<TypeWithDefaultImplementation> constructor = constructorConstructor.get(typeToken);
    
    // Assert
    assertNotNull(constructor);
    assertTrue(constructor.construct() instanceof TypeWithDefaultImplementation);
  }
  
  @Test
  public void get_UnsafeAllocatorConstructor_ReturnsUnsafeAllocatorConstructor() {
    // Arrange
    TypeToken<TypeWithUnsafeAllocator> typeToken = new TypeToken<TypeWithUnsafeAllocator>() {};
    
    // Act
    ObjectConstructor<TypeWithUnsafeAllocator> constructor = constructorConstructor.get(typeToken);
    
    // Assert
    assertNotNull(constructor);
    assertTrue(constructor.construct() instanceof TypeWithUnsafeAllocator);
  }
  
  private static class TypeWithInstanceCreator {}
  
  private static class RawTypeWithInstanceCreator {}
  
  private static class TypeWithDefaultConstructor {
    public TypeWithDefaultConstructor() {}
  }
  
  private static class TypeWithDefaultImplementation {}
  
  private static class TypeWithUnsafeAllocator {}
}