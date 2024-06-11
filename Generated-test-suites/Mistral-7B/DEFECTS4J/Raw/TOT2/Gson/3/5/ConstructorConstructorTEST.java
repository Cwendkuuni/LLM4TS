package com.example.junit4.constructorconstructor;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.InstanceCreator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  @Test
  public void testConstructorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    assertNotNull(constructorConstructor);
  }

  @Test
  public void testGetMethodWithValidTypeToken() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    InstanceCreator<String> stringCreator = new Gson().getDelegateAdapter(String.class);
    instanceCreators.put(String.class, stringCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(typeToken);
    String result = stringConstructor.construct();
    assertNotNull(result);
    assertEquals("", result);
  }

  @Test(expected = NullPointerException.class)
  public void testGetMethodWithNullTypeToken() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<String> typeToken = null;
    constructorConstructor.get(typeToken);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetMethodWithUnknownType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Type unknownType = Object.class;
    constructorConstructor.get(new TypeToken<>(unknownType) {});
  }

  @Test(expected = RuntimeException.class)
  public void testGetMethodWithTypeWithoutDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<TypeAdapter<Map<String, String>>> typeToken = new TypeToken<TypeAdapter<Map<String, String>>>(){};
    constructorConstructor.get(typeToken);
  }

  @Test(expected = RuntimeException.class)
  public void testGetMethodWithUnregisteredType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Exception> typeToken = new TypeToken<Exception>() {};
    constructorConstructor.get(typeToken);
  }
}

//