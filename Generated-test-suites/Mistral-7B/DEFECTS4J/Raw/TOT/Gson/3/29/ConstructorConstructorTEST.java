package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TypeToken;

import org.junit.Test;

public class ConstructorConstructorTest {

 private static final Map<Type, InstanceCreator<?>> EMPTY_INSTANCE_CREATORS = Collections.emptyMap();

 @Test
 public void testGet_TypeToken_ReturnsObjectConstructor() {
   ConstructorConstructor constructorConstructor = new ConstructorConstructor(EMPTY_INSTANCE_CREATORS);
   TypeToken<String> stringTypeToken = new TypeToken<String>() {};
   ObjectConstructor<String> constructor = constructorConstructor.get(stringTypeToken);
   assertNotNull(constructor);
   String constructedObject = constructor.construct();
   assertEquals("", constructedObject);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_NullTypeToken_ThrowsIllegalArgumentException() {
   ConstructorConstructor constructorConstructor = new ConstructorConstructor(EMPTY_INSTANCE_CREATORS);
   ObjectConstructor<String> constructor = constructorConstructor.get(null);
 }

 @Test
 public void testGet_TypeToken_OfPrimitiveType_ReturnsDefaultConstructor() {
   ConstructorConstructor constructorConstructor = new ConstructorConstructor(EMPTY_INSTANCE_CREATORS);
   TypeToken<Boolean> booleanTypeToken = new TypeToken<Boolean>() {};
   ObjectConstructor<Boolean> constructor = constructorConstructor.get(booleanTypeToken);
   assertNotNull(constructor);
   Boolean constructedObject = constructor.construct();
   assertEquals(false, constructedObject);
 }

 @Test
 public void testGet_TypeToken_OfEnum_ReturnsDefaultImplementationConstructor() {
   ConstructorConstructor constructorConstructor = new ConstructorConstructor(EMPTY_INSTANCE_CREATORS);
   TypeToken<java.lang.Enum.StringEnum> enumTypeToken = new TypeToken<java.lang.Enum.StringEnum>() {};
   ObjectConstructor<java.lang.Enum.StringEnum> constructor = constructorConstructor.get(enumTypeToken);
   assertNotNull(constructor);
   java.lang.Enum.StringEnum constructedObject = constructor.construct();
   assertNotNull(constructedObject);
 }

 @Test
 public void testGet_TypeToken_OfCollection_ReturnsDefaultImplementationConstructor() {
   ConstructorConstructor constructorConstructor = new ConstructorConstructor(EMPTY_INSTANCE_CREATORS);
   TypeToken<ArrayList<String>> listTypeToken = new TypeToken<ArrayList<String>>() {};
   ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(listTypeToken);
   assertNotNull(constructor);
   ArrayList<String> constructedObject = constructor.construct();
   assertNotNull(constructedObject);
   assertEquals(0, constructedObject.size());
 }

 @Test
 public void testGet_TypeToken_OfMap_ReturnsDefaultImplementationConstructor() {
   ConstructorConstructor constructorConstructor = new ConstructorConstructor(EMPTY_INSTANCE_CREATORS);
   TypeToken<Map<String, Integer>> mapTypeToken = new TypeToken<Map<String, Integer>>() {};
   ObjectConstructor<Map<String, Integer>> constructor = constructorConstructor.get(mapTypeToken);
   assertNotNull(constructor);
   Map<String, Integer> constructedObject = constructor.construct();
   assertNotNull(constructedObject);
   assertEquals(0, constructedObject.size());
 }

 @Test
 public void testGet_TypeToken_OfCustomClass_ReturnsInstanceCreator() {
   ConstructorConstructor constructorConstructor = new ConstructorConstructor(
       new InstanceCreatorMap());
   TypeToken<MyCustomClass> customClassTypeToken = new TypeToken<MyCustomClass>() {};
   ObjectConstructor<MyCustomClass> constructor = constructorConstructor.get(customClassTypeToken);
   assertNotNull(constructor);
   MyCustomClass constructedObject = constructor.construct();
   assertEquals("Default", constructedObject.getDefaultValue());
 }

 private static class MyCustomClass {
   private String defaultValue;

   public MyCustomClass() {
     this.defaultValue = "Default";
   }

   public String getDefaultValue() {
     return defaultValue;
   }
 }

 private static final Map<Type, InstanceCreator<?>> instanceCreators = new InstanceCreatorMap();

 static class InstanceCreatorMap {
   static {
     instanceCreators.put(MyCustomClass.class, new InstanceCreator<MyCustomClass>() {
       @Override public MyCustomClass createInstance(Type type) {
         return new MyCustomClass();
       }
     });
   }
 }

}