package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.*;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {
  private static final String JSON_TEST = "{\"foo\": 1, \"bar\": 2, \"baz\": null}";

  @Test
  public void testCreate_Null_TypeToken() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {}, new Excluder() {});
    assertNull(factory.create(null, new TypeToken<Object>() {}));
  }

  @Test
  public void testCreate_NonObjectType() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {}, new Excluder() {});
    assertNull(factory.create(new Gson(), new TypeToken<Integer>() {}));
  }

  @Test
  public void testCreate_ClassWithNoFields() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {}, new Excluder() {});
    TypeAdapter<Object> adapter = factory.create(new Gson(), new TypeToken<Object>() {});
    assertEquals(adapter.getClass(), Object.class);
  }

  @Test
  public void testCreate_ClassWithFields() throws IOException {
    String serializedValue = "{\"foo\":1,\"bar\":2,\"baz\":null}";
    Class<TestClass> type = TestClass.class;

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {}, new Excluder() {});
    TypeAdapter<TestClass> adapter = factory.create(new Gson(), new TypeToken<TestClass>() {});

    TestClass value = adapter.read(new JsonReader(new StringReader(serializedValue)));
    assertEquals(1, value.getFoo());
    assertEquals(2, value.getBar());
    assertNull(value.getBaz());

    JsonElement jsonElement = new JsonParser().parse(new StringReader(serializedValue));
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, value);
    assertEquals(serializedValue, jsonWriter.toString());
  }

  @Test
  public void testCreate_ClassWithJsonAdapter() throws IOException {
    String serializedValue = "{\"foo\":\"value1\",\"bar\":\"value2\"}";
    Class<TestClassWithJsonAdapter> type = TestClassWithJsonAdapter.class;

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {}, new Excluder() {});
    TypeAdapter<TestClassWithJsonAdapter> adapter = factory.create(new Gson(), new TypeToken<TestClassWithJsonAdapter>() {});

    TestClassWithJsonAdapter value = adapter.read(new JsonReader(new StringReader(serializedValue)));
    assertEquals("value1", value.getFoo());
    assertEquals("value2", value.getBar());

    JsonElement jsonElement = new JsonParser().parse(new StringReader(serializedValue));
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, value);
    assertEquals(serializedValue, jsonWriter.toString());
  }

  @Test
  public void testCreate_ClassWithExcluder() throws IOException {
    String serializedValue = "{\"foo\":1,\"bar\":2,\"baz\":null}";
    Class<TestClassWithExcluder> type = TestClassWithExcluder.class;

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {}, new Excluder() {
          @Override public boolean excludeField(Field field, boolean serialize) {
            return Module.class.isAssignableFrom(field.getType());
          }
        });
    TypeAdapter<TestClassWithExcluder> adapter = factory.create(new Gson(), new TypeToken<TestClassWithExcluder>() {});

    TestClassWithExcluder value = adapter.read(new JsonReader(new StringReader(serializedValue)));
    assertNull(value.getModule());

    JsonElement jsonElement = new JsonParser().parse(new StringReader(serializedValue));
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, value);
    assertEquals(serializedValue.replace("\"module\"", "\"value\""), jsonWriter.toString());
  }

  @Test
  public void testCreate_ClassWithMultipleFieldsWithSameName() throws IOException {
    String serializedValue = "{\"foo\":1,\"foo\":2}";
    Class<TestClassWithMultipleFields> type = TestClassWithMultipleFields.class;

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {}, new Excluder() {});
    TypeAdapter<TestClassWithMultipleFields> adapter = factory.create(new Gson(), new TypeToken<TestClassWithMultipleFields>() {});

    TestClassWithMultipleFields value = adapter.read(new JsonReader(new StringReader(serializedValue)));
    assertEquals(1, value.getFoo1());
    assertEquals(2, value.getFoo2());

    JsonElement jsonElement = new JsonParser().parse(new StringReader(serializedValue));
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, value);
    assertEquals(serializedValue, jsonWriter.toString());
  }

  @Test(expected = JsonSyntaxException.class)
  public void testCreate_ClassWithFieldAccessException() throws IOException {
    String serializedValue = "{\"foo\":1,\"bar\":{}}";
    Class<TestClassWithFieldAccessException> type = TestClassWithFieldAccessException.class;

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {}, new Excluder() {});
    TypeAdapter<TestClassWithFieldAccessException> adapter = factory.create(new Gson(), new TypeToken<TestClassWithFieldAccessException>() {});

    TestClassWithFieldAccessException value = adapter.read(new JsonReader(new StringReader(serializedValue)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_ClassWithTwoFieldsNamedSame() throws IOException {
    String serializedValue = "{\"foo\":1,\"foo\":2}";
    Class<TestClassWithTwoFieldsNamedSame> type = TestClassWithTwoFieldsNamedSame.class;

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {}, new Excluder() {});
    factory.create(new Gson(), new TypeToken<TestClassWithTwoFieldsNamedSame>() {});
  }

  private static class TestClass {
    private int foo;
    private int bar;
    private Object baz;

    public int getFoo() {
      return foo;
    }

    public void setFoo(int foo) {
      this.foo = foo;
    }

    public int getBar() {
      return bar;
    }

    public void setBar(int bar) {
      this.bar = bar;
    }

    public Object getBaz() {
      return baz;
    }

    public void setBaz(Object baz) {
      this.baz = baz;
    }
  }

  private static class TestClassWithJsonAdapter {
    private String foo;
    private String bar;

    @JsonAdapter(StringAdapter.class)
    public String getFooJsonAdapter() {
      return foo;
    }

    public void setFooJsonAdapter(String foo) {
      this.foo = foo;
    }

    @JsonAdapter(StringAdapter.class)
    public String getBarJsonAdapter() {
      return bar;
    }

    public void setBarJsonAdapter(String bar) {
      this.bar = bar;
    }
  }

  private static class TestClassWithExcluder {
    private int foo;
    private Module module;
    private int bar;

    public int getFoo() {
      return foo;
    }

    public void setFoo(int foo) {
      this.foo = foo;
    }

    public Module getModule() {
      return module;
    }

    public void setModule(Module module) {
      this.module = module;
    }

    public int getBar() {
      return bar;
    }

    public void setBar(int bar) {
      this.bar = bar;
    }
  }

  private static class TestClassWithMultipleFields {
    private int foo1;
    private int foo2;

    public int getFoo1() {
      return foo1;
    }

    public void setFoo1(int foo1) {
      this.foo1 = foo1;
    }

    public int getFoo2() {
      return foo2;
    }

    public void setFoo2(int foo2) {
      this.foo2 = foo2;
    }
  }

  private static class TestClassWithFieldAccessException {
    private List<String> list;

    public List<String> getList() {
      return Collections.unmodifiableList(list);
    }

    public void setList(List<String> list) {
      this.list = list;
    }
  }

  private static class TestClassWithTwoFieldsNamedSame {
    private int foo;
    private int foo;

    public int getFoo() {
      return foo;
    }

    public void setFoo(int foo) {
      this.foo = foo;
    }

    public int getFoo2() {
      return foo;
    }

    public void setFoo2(int foo) {
      this.foo = foo;
    }
  }

  private static class Module {}

  private static class StringAdapter extends TypeAdapter<String> {
    @Override public String read(JsonReader reader) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override public void write(JsonWriter writer, String value) throws IOException {
      writer.value(value);
    }
  }

  private static class StringWriter extends StringWriter {
    @Override public void write(String s, int offset, int length) throws IOException {
      super.write(s, offset, length);
    }
  }

  private static class StringReader extends CharArrayReader {
    private final String json;

    public StringReader(String json) {
      super(json.toCharArray(), 0, json.length());
      this.json = json;
    }

    @Override public String toString() {
      return json;
    }
  }
}

class TestClass {
  private int foo;
  private int bar;
  private Object baz;

  public int getFoo() {
    return foo;
  }

  public void setFoo(int foo) {
    this.foo = foo;
  }

  public int getBar() {
    return bar;
  }

  public void setBar(int bar) {
    this.bar = bar;
  }

  public Object getBaz() {
    return baz;
  }

  public void setBaz(Object baz) {
    this.baz = baz;
  }

  @Override public String toString() {
    return "TestClass{" +
        "foo=" + foo +
        ", bar=" + bar +
        ", baz=" + baz +
        '}';
  }

  public static class FieldNamingPolicy implements FieldNamingStrategy {
    @Override public String nameForField(Field field) {
      return field.getName();
    }
  }
}

class TestClassWithJsonAdapter {
  private String foo;
  private String bar;

  @JsonAdapter(StringAdapter.class)
  public String getFooJsonAdapter() {
    return foo;
  }

  public void setFooJsonAdapter(String foo) {
    this.foo = foo;
  }

  @JsonAdapter(StringAdapter.class)
  public String getBarJsonAdapter() {
    return bar;
  }

  public void setBarJsonAdapter(String bar) {
    this.bar = bar;
  }
}

class TestClassWithExcluder {
  private int foo;
  private Module module;
  private int bar;

  public int getFoo() {
    return foo;
  }

  public void setFoo(int foo) {
    this.foo = foo;
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public int getBar() {
    return bar;
  }

  public void setBar(int bar) {
    this.bar = bar;
  }

  public static class Excluder {
    public boolean excludeField(Field field, boolean serialize) {
      return false;
    }

    public boolean excludeClass(Class<?> clazz, boolean serialize) {
      return false;
    }
  }
}

class TestClassWithMultipleFields {
  private int foo1;
  private int foo2;

  public int getFoo1() {
    return foo1;
  }

  public void setFoo1(int foo1) {
    this.foo1 = foo1;
  }

  public int getFoo2() {
    return foo2;
  }

  public void setFoo2(int foo2) {
    this.foo2 = foo2;
  }
}

class TestClassWithFieldAccessException {
  private List<String> list;

  public List<String> getList() {
    return Collections.unmodifiableList(list);
  }

  public void setList(List<String> list) {
    this.list = list;
  }
}

class TestClassWithTwoFieldsNamedSame {
  private int foo;
  private int foo;

  public int getFoo() {
    return foo;
  }

  public void setFoo(int foo) {
    this.foo = foo;
  }

  public int getFoo2() {
    return foo;
  }

  public void setFoo2(int foo) {
    this.foo = foo;
  }
}

class Module {}

class StringAdapter extends TypeAdapter<String> {
  @Override public String read(JsonReader reader) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override public void write(JsonWriter writer, String value) throws IOException {
    writer.value(value);
  }
}

class StringWriter extends StringWriter {
  @Override public void write(String s, int offset, int length) throws IOException {
    super.write(s, offset, length);
  }
}

class StringReader extends CharArrayReader {
  private final String json;

  public StringReader(String json) {
    super(json.toCharArray(), 0, json.length());
    this.json = json;
  }

  @Override public String toString() {
    return json;
  }
}