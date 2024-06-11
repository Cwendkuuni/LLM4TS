package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.TypeToken;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceOfArrayList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreator.Factory() {
      @Override public InstanceCreator<?> create(Type type) {
        if (ArrayList.class.isAssignableFrom(type)) {
          return new InstanceCreator<ArrayList>() {
            @Override public ArrayList<?> createInstance(Type type) {
              return new ArrayList<>();
            }
          };
        }
        return null;
      }
    });

    TypeToken<ArrayList<Integer>> intArrayListTypeToken = new TypeToken<ArrayList<Integer>>() {};
    ObjectConstructor<ArrayList<Integer>> objConstructor = constructorConstructor.get(intArrayListTypeToken);
    assertNotNull(objConstructor);
    ArrayList<Integer> list = objConstructor.construct();
    assertNotNull(list);
    assertEquals(0, list.size());
  }

  @Test
  public void testGetInstanceOfLinkedHashMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreator.Factory() {
      @Override public InstanceCreator<?> create(Type type) {
        if (Map.class.isAssignableFrom(type)) {
          if (LinkedHashMap.class.isAssignableFrom(type)) {
            return new InstanceCreator<LinkedHashMap<Object, Object>>() {
              @Override public LinkedHashMap<Object, Object> createInstance(Type type) {
                return new LinkedHashMap<Object, Object>();
              }
            };
          }
          return null;
        }
        return null;
      }
    });

    TypeToken<LinkedHashMap<String, Object>> mapTypeToken = new TypeToken<LinkedHashMap<String, Object>>() {};
    ObjectConstructor<LinkedHashMap<String, Object>> objConstructor = constructorConstructor.get(mapTypeToken);
    assertNotNull(objConstructor);
    LinkedHashMap<String, Object> map = objConstructor.construct();
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
  public void testGetInstanceOfTreeSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreator.Factory() {
      @Override public InstanceCreator<?> create(Type type) {
        if (Set.class.isAssignableFrom(type)) {
          if (TreeSet.class.isAssignableFrom(type)) {
            return new InstanceCreator<TreeSet<Object>>() {
              @Override public TreeSet<Object> createInstance(Type type) {
                return new TreeSet<>();
              }
            };
          }
          return null;
        }
        return null;
      }
    });

    TypeToken<TreeSet<Integer>> setTypeToken = new TypeToken<TreeSet<Integer>>() {};
    ObjectConstructor<TreeSet<Integer>> objConstructor = constructorConstructor.get(setTypeToken);
    assertNotNull(objConstructor);
    TreeSet<Integer> set = objConstructor.construct();
    assertNotNull(set);
    assertEquals(0, set.size());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfUnknownType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreator.Factory() {
      @Override public InstanceCreator<?> create(Type type) {
        return null;
      }
    });

    TypeToken<UnknownType> unknownTypeToken = new TypeToken<UnknownType>() {};
    ObjectConstructor<UnknownType> objConstructor = constructorConstructor.get(unknownTypeToken);
    assertNull(objConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfNoDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreator.Factory() {
      @Override public InstanceCreator<?> create(Type type) {
        return null;
      }
    });

    Type type = Object.class;
    ObjectConstructor<Object> objConstructor = constructorConstructor.get(new TypeToken<Object>() {}.getType());
    assertNull(objConstructor);
  }

  @Test
  public void testGetInstanceOfRawType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreator.Factory() {
      @Override public InstanceCreator<?> create(Type type) {
        if (Collection.class.isAssignableFrom(type)) {
          return new InstanceCreator<Collection>() {
            @SuppressWarnings("unchecked")
            @Override public Collection<?> createInstance(Type type) {
              return new ArrayList<>();
            }
          };
        }
        return null;
      }
    });

    Type rawType = Collection.class;
    ObjectConstructor<Collection> objConstructor = constructorConstructor.get(new TypeToken<Collection>() {}.getType());
    assertNotNull(objConstructor);
    Collection<Object> collection = objConstructor.construct();
    assertNotNull(collection);
    assertEquals(0, collection.size());
  }
}