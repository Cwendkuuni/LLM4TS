package com.example.junit;

import static org.junit.Assert.*;
import com.google.gson.internal.*;
import org.junit.*;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;

  @Before
  public void setUp() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }

  @Test
  public void testGetMethod() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(stringTypeToken);
    String result = objectConstructor.construct();
    assertEquals("", result);
  }

  @Test(expected = NullPointerException.class)
  public void testGetMethodWithNullTypeToken() {
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(null);
  }

  @Test(expected = RuntimeException.class)
  public void testGetMethodWithUnknownType() {
    TypeToken<UnknownType> unknownTypeToken = new TypeToken<UnknownType>() {};
    ObjectConstructor<UnknownType> objectConstructor = constructorConstructor.get(unknownTypeToken);
  }

  @Test
  public void testGetMethodWithEmptyInstanceCreatorMap() {
    Map<Type, InstanceCreator<?>> emptyInstanceCreators = new LinkedHashMap<>();
    ConstructorConstructor emptyConstructorConstructor = new ConstructorConstructor(emptyInstanceCreators);
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> objectConstructor = emptyConstructorConstructor.get(stringTypeToken);
  }

  @Test(expected = IllegalAccessException.class)
  public void testGetMethodWithIllegalAccessException() {
    Class<?> privateClass = AccessController.doPrivileged((PrivilegedAction<Class<?>>) () -> {
      return new PrivateClass();
    });
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<PrivateClass> privateClassTypeToken = new TypeToken<PrivateClass>() {};
    ObjectConstructor<PrivateClass> objectConstructor = constructorConstructor.get(privateClassTypeToken);
  }

  @Test(expected = InstantiationException.class)
  public void testGetMethodWithInstantiationException() {
    Class<?> privateClass = AccessController.doPrivileged((PrivilegedAction<Class<?>>) () -> {
      return new PrivateClassWithNoPublicConstructor();
    });
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<PrivateClassWithNoPublicConstructor> privateClassTypeToken = new TypeToken<PrivateClassWithNoPublicConstructor>() {};
    ObjectConstructor<PrivateClassWithNoPublicConstructor> objectConstructor = constructorConstructor.get(privateClassTypeToken);
  }

  @Test(expected = NoSuchMethodException.class)
  public void testGetMethodWithNoSuchMethodException() {
    Class<?> privateClass = AccessController.doPrivileged((PrivilegedAction<Class<?>>) () -> {
      return new PrivateClassWithNoDefaultConstructor();
    });
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<PrivateClassWithNoDefaultConstructor> privateClassTypeToken = new TypeToken<PrivateClassWithNoDefaultConstructor>() {};
    ObjectConstructor<PrivateClassWithNoDefaultConstructor> objectConstructor = constructorConstructor.get(privateClassTypeToken);
  }

  @Test(expected = InvocationTargetException.class)
  public void testGetMethodWithInvocationTargetException() {
    Class<?> privateClass = AccessController.doPrivileged((PrivilegedAction<Class<?>>) () -> {
      return new PrivateClassWithThrowingConstructor();
    });
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<PrivateClassWithThrowingConstructor> privateClassTypeToken = new TypeToken<PrivateClassWithThrowingConstructor>() {};
    ObjectConstructor<PrivateClassWithThrowingConstructor> objectConstructor = constructorConstructor.get(privateClassTypeToken);
  }

  @Test(expected = JsonIOException.class)
  public void testGetMethodWithJsonIOException() {
    Class<?> privateClass = AccessController.doPrivileged((PrivilegedAction<Class<?>>) () -> {
      return new PrivateClassWithJsonIOException();
    });
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<PrivateClassWithJsonIOException> privateClassTypeToken = new TypeToken<PrivateClassWithJsonIOException>() {};
    ObjectConstructor<PrivateClassWithJsonIOException> objectConstructor = constructorConstructor.get(privateClassTypeToken);
  }

  @Test
  public void testGetMethodWithRuntimeException() {
    Class<?> privateClass = AccessController.doPrivileged((PrivilegedAction<Class<?>>) () -> {
      return new PrivateClassWithRuntimeException();
    });
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<PrivateClassWithRuntimeException> privateClassTypeToken = new TypeToken<PrivateClassWithRuntimeException>() {};
    ObjectConstructor<PrivateClassWithRuntimeException> objectConstructor = constructorConstructor.get(privateClassTypeToken);
  }
}