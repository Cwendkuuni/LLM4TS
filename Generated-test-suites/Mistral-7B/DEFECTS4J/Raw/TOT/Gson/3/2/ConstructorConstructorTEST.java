import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.google.gson.reflect.TypeToken.get;

public class ConstructorConstructorTest {

  private static final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

  static {
    instanceCreators.put(get(String[].class), (InstanceCreator<String[]>) new StringArrayCreator());
  }

  @Test
  public void testGetInstance_ForStringArray() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<String[]> objectConstructor = constructorConstructor.get(get(String[].class));
    String[] instance = objectConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals(0, instance.length);
  }

  @Test
  public void testGetInstance_ForUnknownType_ReturnsNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(getTypeOf(MyClass.class));
    Assert.assertNull(objectConstructor);
  }

  @Test
  public void testGetInstance_ForPrimitiveType_ReturnsDefaultValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(get(int[].class).getComponentType());
    Integer instance = objectConstructor.construct();
    Assert.assertEquals(0, instance.intValue());
  }

  @Test
  public void testGetInstance_ForCustomClass_WithDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<MyClass> objectConstructor = constructorConstructor.get(get(MyClass.class));
    MyClass instance = objectConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals("Default constructor test", instance.getMessage());
  }

  @Test
  public void testGetInstance_ForCustomClass_WithParameterizedConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<MyOtherClass<String>> objectConstructor = constructorConstructor.get(get(MyOtherClass.class, String.class));
    MyOtherClass<String> instance = objectConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals("Parameterized constructor test", instance.getMessage());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstance_ForCustomClass_WithNoDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<NoDefaultConstructorClass> objectConstructor = constructorConstructor.get(get(NoDefaultConstructorClass.class));
  }

  @Test
  public void testGetInstance_ForEnumSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<MyEnumSet> objectConstructor = constructorConstructor.get(get(MyEnumSet.class));
    MyEnumSet instance = objectConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertTrue(instance.isEmpty());
  }

  @Test
  public void testGetInstance_ForLinkedHashMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<MyLinkedHashMap> objectConstructor = constructorConstructor.get(get(MyLinkedHashMap.class));
    MyLinkedHashMap instance = objectConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals(0, instance.size());
  }

  @Test
  public void testGetInstance_ForTreeMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<MyTreeMap> objectConstructor = constructorConstructor.get(get(MyTreeMap.class));
    MyTreeMap instance = objectConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals(0, instance.size());
  }

  @Test
  public void testGetInstance_ForArrayList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<MyArrayList> objectConstructor = constructorConstructor.get(get(MyArrayList.class));
    MyArrayList instance = objectConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals(0, instance.size());
  }

  @Test
  public void testGetInstance_ForTreeMapWithStringKey() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<MyTreeMapWithStringKey> objectConstructor = constructorConstructor.get(get(MyTreeMapWithStringKey.class));
    MyTreeMapWithStringKey instance = objectConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals(0, instance.size());
  }

  private static Class<?> getTypeOf(Class<?> clazz) {
    return clazz.getComponentType();
  }

  static class MyClass {
    MyClass() {
      this.getMessage() = "Default constructor test";
    }

    String getMessage() {
      return message;
    }

    private String message;
  }

  static class MyOtherClass<T> {
    MyOtherClass(T message) {
      this.message = message;
    }

    String getMessage() {
      return message;
    }

    private T message;
  }

  static class MyEnumSet extends EnumSet<MyEnum> {}

  static class MyLinkedHashMap extends LinkedHashMap<String, Object> {}

  static class MyTreeMap extends TreeMap<String, Object> {}

  static class MyArrayList extends ArrayList<Object> {}

  static class MyTreeMapWithStringKey extends TreeMap<String, Object> {}

  static class StringArrayCreator implements InstanceCreator<String[]> {
    @Override public String[] createInstance(Type type) {
      return new String[0];
    }
  }

  static class NoDefaultConstructorClass {}
}