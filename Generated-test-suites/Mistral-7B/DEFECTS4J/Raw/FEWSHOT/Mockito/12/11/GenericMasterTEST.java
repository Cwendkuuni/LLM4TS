package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Class clazz = String.class;

 // Test for a non-generic field
 Field field1 = clazz.getDeclaredField("value");
 assertEquals(String.class, GenericMaster.getGenericType(field1));

 // Test for a generic field of type List
 Class listClazz = ArrayList.class;
 Field field2 = listClazz.getDeclaredField("elementType");
 Type genericType = field2.getGenericType();
 ParameterizedType parameterizedType = (ParameterizedType) genericType;
 Type actualType = parameterizedType.getActualTypeArguments()[0];
 assertEquals(String.class, ((Class<?>) actualType).getClass());
 assertEquals(String.class, GenericMaster.getGenericType(field2));

 // Test for a generic field of type Map
 Class mapClazz = HashMap.class;
 Field field3 = mapClazz.getDeclaredField("entrySet");
 genericType = field3.getGenericType();
 parameterizedType = (ParameterizedType) genericType;
 Type keyType = parameterizedType.getActualTypeArguments()[0];
 Type valueType = parameterizedType.getActualTypeArguments()[1];
 assertEquals(String.class, ((Class<?>) keyType).getClass());
 assertEquals(Integer.class, ((Class<?>) valueType).getClass());
 assertEquals(Map.Entry.class, GenericMaster.getGenericType(field3));

 // Test for a generic field with nested generics
 Class collectionClazz = ArrayList.class;
 Type listType = new TypeToken<List<Map<String, Integer>>>() {}.getType();
 Field field4 = collectionClazz.getDeclaredField("type");
 genericType = field4.getGenericType();
 parameterizedType = (ParameterizedType) genericType;
 Type innerType = parameterizedType.getActualTypeArguments()[0];
 assertEquals(Map.class, ((Class<?>) innerType).getClass());
 Type mapType = new TypeToken<Map<?, ?>>() {}.getType();
 Type mapParameterizedType = ((ParameterizedType) mapType.getComponentType()).getActualTypeArguments()[0];
 assertEquals(String.class, ((Class<?>) mapParameterizedType.getComponentType()).getClass());
 assertEquals(Integer.class, ((Class<?>) mapParameterizedType.getRawType().getComponentType()).getClass());
 assertEquals(Map.class, GenericMaster.getGenericType(field4));
 }
}