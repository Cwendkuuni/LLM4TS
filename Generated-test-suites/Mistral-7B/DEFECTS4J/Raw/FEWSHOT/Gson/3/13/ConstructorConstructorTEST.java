package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  @Test
  public void testGet_Collection() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }

  @Test
  public void testGet_EnumSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<EnumSet<Foo>> typeToken = new TypeToken<EnumSet<Foo>>() {};
    ObjectConstructor<EnumSet<Foo>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }

  @Test
  public void testGet_LinkedHashMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<LinkedHashMap<String, Object>> typeToken = new TypeToken<LinkedHashMap<String, Object>>() {};
    ObjectConstructor<LinkedHashMap<String, Object>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }

  @Test
  public void testGet_LinkedHashSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<LinkedHashSet<String>> typeToken = new TypeToken<LinkedHashSet<String>>() {};
    ObjectConstructor<LinkedHashSet<String>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }

  @Test
  public void testGet_LinkedList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<LinkedList<String>> typeToken = new TypeToken<LinkedList<String>>() {};
    ObjectConstructor<LinkedList<String>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }

  @Test
  public void testGet_Map() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {};
    ObjectConstructor<Map<String, Object>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }

  @Test
  public void testGet_SortedMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<TreeMap<String, Object>> typeToken = new TypeToken<TreeMap<String, Object>>() {};
    ObjectConstructor<TreeMap<String, Object>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }

  @Test
  public void testGet_SortedSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<TreeSet<String>> typeToken = new TypeToken<TreeSet<String>>() {};
    ObjectConstructor<TreeSet<String>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }

  @Test
  public void testGet_rawType_Collection() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> actual = constructorConstructor.get(typeToken);
    Class<? super ArrayList<String>> rawType = ArrayList.class;
    ObjectConstructor<ArrayList<String>> expected = new ObjectConstructor<ArrayList<String>>() {
      @Override public ArrayList<String> construct() {
        return new ArrayList<String>();
      }
    };
    assertEquals(expected, actual);
  }

  @Test
  public void testGet_rawType_EnumSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<EnumSet<Foo>> typeToken = new TypeToken<EnumSet<Foo>>() {};
    ObjectConstructor<EnumSet<Foo>> actual = constructorConstructor.get(typeToken);
    Class<? super EnumSet<Foo>> rawType = EnumSet.class;
    ObjectConstructor<EnumSet<Foo>> expected = new ObjectConstructor<EnumSet<Foo>>() {
      @Override public EnumSet<Foo> construct() {
        return EnumSet.noneOf(Foo.class);
      }
    };
    assertEquals(expected, actual);
  }

  @Test
  public void testGet_rawType_LinkedHashMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<LinkedHashMap<String, Object>> typeToken = new TypeToken<LinkedHashMap<String, Object>>() {};
    ObjectConstructor<LinkedHashMap<String, Object>> actual = constructorConstructor.get(typeToken);
    Class<? super LinkedHashMap<String, Object>> rawType = LinkedHashMap.class;
    ObjectConstructor<LinkedHashMap<String, Object>> expected = new ObjectConstructor<LinkedHashMap<String, Object>>() {
      @Override public LinkedHashMap<String, Object> construct() {
        return new LinkedHashMap<String, Object>();
      }
    };
    assertEquals(expected, actual);
  }

  @Test
  public void testGet_rawType_LinkedHashSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<LinkedHashSet<String>> typeToken = new TypeToken<LinkedHashSet<String>>() {};
    ObjectConstructor<LinkedHashSet<String>> actual = constructorConstructor.get(typeToken);
    Class<? super LinkedHashSet<String>> rawType = LinkedHashSet.class;
    ObjectConstructor<LinkedHashSet<String>> expected = new ObjectConstructor<LinkedHashSet<String>>() {
      @Override public LinkedHashSet<String> construct() {
        return new LinkedHashSet<String>();
      }
    };
    assertEquals(expected, actual);
  }

  @Test
  public void testGet_rawType_LinkedList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<LinkedList<String>> typeToken = new TypeToken<LinkedList<String>>() {};
    ObjectConstructor<LinkedList<String>> actual = constructorConstructor.get(typeToken);
    Class<? super LinkedList<String>> rawType = LinkedList.class;
    ObjectConstructor<LinkedList<String>> expected = new ObjectConstructor<LinkedList<String>>() {
      @Override public LinkedList<String> construct() {
        return new LinkedList<String>();
      }
    };
    assertEquals(expected, actual);
  }

  @Test
  public void testGet_rawType_Map() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {};
    ObjectConstructor<Map<String, Object>> actual = constructorConstructor.get(typeToken);
    Class<? super Map<String, Object>> rawType = Map.class;
    ObjectConstructor<Map<String, Object>> expected = new ObjectConstructor<Map<String, Object>>() {
      @Override public Map<String, Object> construct() {
        return new HashMap<String, Object>();
      }
    };
    assertEquals(expected, actual);
  }

  @Test
  public void testGet_rawType_SortedMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<TreeMap<String, Object>> typeToken = new TypeToken<TreeMap<String, Object>>() {};
    ObjectConstructor<TreeMap<String, Object>> actual = constructorConstructor.get(typeToken);
    Class<? super TreeMap<String, Object>> rawType = TreeMap.class;
    ObjectConstructor<TreeMap<String, Object>> expected = new ObjectConstructor<TreeMap<String, Object>>() {
      @Override public TreeMap<String, Object> construct() {
        return new TreeMap<String, Object>();
      }
    };
    assertEquals(expected, actual);
  }

  @Test
  public void testGet_rawType_SortedSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<TreeSet<String>> typeToken = new TypeToken<TreeSet<String>>() {};
    ObjectConstructor<TreeSet<String>> actual = constructorConstructor.get(typeToken);
    Class<? super TreeSet<String>> rawType = TreeSet.class;
    ObjectConstructor<TreeSet<String>> expected = new ObjectConstructor<TreeSet<String>>() {
      @Override public TreeSet<String> construct() {
        return new TreeSet<String>();
      }
    };
    assertEquals(expected, actual);
  }

  @Test
  public void testGet_TypeCreator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    ObjectConstructor<String> actual = constructorConstructor.get(typeToken);
    assertNull(actual);
  }

  @Test
  public void testGet_TypeCreator_Class() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Class<String> rawType = String.class;
    ObjectConstructor<String> actual = constructorConstructor.get(rawType);
    assertNull(actual);
  }

  @Test
  public void testNewDefaultConstructor_Class() {
    Class<String> rawType = String.class;
    ObjectConstructor<String> actual = new ConstructorConstructor(null).newDefaultConstructor(rawType);
    assertNotNull(actual);
  }

  @Test(expected = RuntimeException.class)
  public void testNewDefaultConstructor_Class_NoDefaultConstructor() {
    Class<Integer> rawType = Integer.class;
    ObjectConstructor<Integer> actual = new ConstructorConstructor(null).newDefaultConstructor(rawType);
    assertNull(actual);
  }

  @Test(expected = JsonIOException.class)
  public void testNewDefaultImplementationConstructor_InvalidType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = new TypeToken<Object>() {
    }.getType();
    ObjectConstructor<Object> actual = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
    assertNull(actual);
  }

  @Test
  public void testNewDefaultImplementationConstructor_ArrayList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = new TypeToken<ArrayList<Object>>() {}.getType();
    ObjectConstructor<ArrayList<Object>> actual = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
    assertNotNull(actual);
  }

  @Test
  public void testNewDefaultImplementationConstructor_EnumSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = new TypeToken<EnumSet<Foo>>() {}.getType();
    ObjectConstructor<EnumSet<Foo>> actual = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
    assertNotNull(actual);
  }

  @Test
  public void testNewDefaultImplementationConstructor_LinkedHashMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = new TypeToken<LinkedHashMap<String, Object>>() {}.getType();
    ObjectConstructor<LinkedHashMap<String, Object>> actual = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
    assertNotNull(actual);
  }

  @Test
  public void testNewDefaultImplementationConstructor_LinkedHashSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = new TypeToken<LinkedHashSet<String>>() {}.getType();
    ObjectConstructor<LinkedHashSet<String>> actual = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
    assertNotNull(actual);
  }

  @Test
  public void testNewDefaultImplementationConstructor_LinkedList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = new TypeToken<LinkedList<Object>>() {}.getType();
    ObjectConstructor<LinkedList<Object>> actual = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
    assertNotNull(actual);
  }

  @Test
  public void testNewDefaultImplementationConstructor_Map() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = new TypeToken<Map<String, Object>>() {}.getType();
    ObjectConstructor<Map<String, Object>> actual = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
    assertNotNull(actual);
  }

  @Test
  public void testNewDefaultImplementationConstructor_SortedMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = new TypeToken<TreeMap<String, Object>>() {}.getType();
    ObjectConstructor<TreeMap<String, Object>> actual = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
    assertNotNull(actual);
  }

  @Test
  public void testNewDefaultImplementationConstructor_SortedSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = new TypeToken<TreeSet<String>>() {}.getType();
    ObjectConstructor<TreeSet<String>> actual = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
    assertNotNull(actual);
  }

  @Test
  public void testNewUnsafeAllocator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Type type = String[].class;
    ObjectConstructor<String[]> actual = constructorConstructor.newUnsafeAllocator(type, String[].class);
    assertNotNull(actual);
  }
}