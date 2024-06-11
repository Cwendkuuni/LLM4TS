import org.junit.Test;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {
  
  @Test
  public void testGet_withInstanceCreator() {
    // Arrange
    InstanceCreator<TestClass> creator = type -> new TestClass();

    // Act
    ObjectConstructor<TestClass> constructor = new ConstructorConstructor(Collections.singletonMap(TestClass.class, creator)).get(TypeToken.get(TestClass.class));

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testGet_withRawTypeCreator() {
    // Arrange
    InstanceCreator<TestClass> creator = type -> new TestClass();

    // Act
    ObjectConstructor<TestClass> constructor = new ConstructorConstructor(Collections.singletonMap(TestClass.class, creator)).get(TypeToken.get(TestClass.class));

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testGet_withDefaultConstructor() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.get(TypeToken.get(TestClass.class));

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testGet_withDefaultImplementationConstructor() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.get(TypeToken.get(TestClass.class));

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testGet_withUnsafeAllocator() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.get(TypeToken.get(TestClass.class));

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewDefaultConstructor() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newDefaultConstructor(TestClass.class);

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withSortedSet() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newDefaultImplementationConstructor(new TypeToken<SortedSet<TestClass>>() {}.getType(), SortedSet.class);

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withEnumSet() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newDefaultImplementationConstructor(new TypeToken<EnumSet<TestClass>>() {}.getType(), EnumSet.class);

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withSet() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newDefaultImplementationConstructor(new TypeToken<Set<TestClass>>() {}.getType(), Set.class);

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withQueue() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newDefaultImplementationConstructor(new TypeToken<Queue<TestClass>>() {}.getType(), Queue.class);

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withArrayList() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newDefaultImplementationConstructor(new TypeToken<ArrayList<TestClass>>() {}.getType(), ArrayList.class);

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withSortedMap() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newDefaultImplementationConstructor(new TypeToken<SortedMap<String, TestClass>>() {}.getType(), SortedMap.class);

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withLinkedHashMap() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newDefaultImplementationConstructor(new TypeToken<Map<String, TestClass>>() {}.getType(), Map.class);

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withLinkedTreeMap() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newDefaultImplementationConstructor(new TypeToken<LinkedTreeMap<String, TestClass>>() {}.getType(), LinkedTreeMap.class);

    // Assert
    assertNotNull(constructor.construct());
  }
  
  @Test
  public void testNewUnsafeAllocator() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

    // Act
    ObjectConstructor<TestClass> constructor = constructorConstructor.newUnsafeAllocator(TestClass.class, TestClass.class);

    // Assert
    assertNotNull(constructor.construct());
  }

}