import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingStrategy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private static final Gson gson = new GsonBuilder().create();

 @Test
 public void testCreate_NullTypeToken() {
   try {
     new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {}, new Excluder()).create(null, new TypeToken<Object>() {});
     fail("Should have thrown an IllegalArgumentException.");
   } catch (IllegalArgumentException e) {
     // Expected.
   }
 }

 @Test
 public void testCreate_NonObjectType() {
   try {
     new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {}, new Excluder()).create(gson, new TypeToken<Integer>() {});
     fail("Should have returned null.");
   } catch (NullPointerException e) {
     // Expected.
   }
 }

 @Test
 public void testCreate_ObjectTypeWithExcludedFields() {
   class TestClass {
     @Exclude
     private String excludedField;
     private String includedField;
   }

   ObjectConstructor<TestClass> constructor = new ObjectConstructor<TestClass>() {};
   ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructor, new FieldNamingStrategy() {}, new Excluder());
   TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

   TestClass testClass = new TestClass();
   testClass.includedField = "included";

   String json = "{\"included\": \"included\"}";
   TestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals("included", deserialized.includedField);
   assertNull(deserialized.excludedField);

   json = "{\"excluded\": \"excluded\"}";
   try {
     deserialized = adapter.read(new JsonReader(new StringReader(json)));
     fail("Should have thrown a JsonSyntaxException.");
   } catch (JsonSyntaxException e) {
     // Expected.
   }
 }

 @Test
 public void testCreate_ObjectTypeWithCustomJsonAdapter() {
   class TestClass {
     @JsonAdapter(CustomJsonAdapter.class)
     private String field;
   }

   class CustomJsonAdapter extends TypeAdapter<String> {
     @Override public String read(JsonReader in) throws IOException {
       return in.nextString();
     }

     @Override public void write(JsonWriter out, String value) throws IOException {
       out.value(value);
     }
   }

   ObjectConstructor<TestClass> constructor = new ObjectConstructor<TestClass>() {};
   ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructor, new FieldNamingStrategy() {}, new Excluder());
   TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

   TestClass testClass = new TestClass();
   testClass.field = "value";

   String json = "{\"field\": \"value\"}";
   TestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals("value", deserialized.field);

   json = "{\"otherField\": \"otherValue\"}";
   assertTrue(deserialized.equals(testClass));
 }

 @Test
 public void testCreate_ObjectTypeWithNestedFields() {
   class TestClass {
     private NestedClass nested;

     static class NestedClass {
       private String field;
     }
   }

   ObjectConstructor<TestClass> outerConstructor = new ObjectConstructor<TestClass>() {};
   ObjectConstructor<TestClass.NestedClass> innerConstructor = new ObjectConstructor<TestClass.NestedClass>() {};
   ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(outerConstructor, new FieldNamingStrategy() {}, new Excluder());
   TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

   TestClass testClass = new TestClass();
   TestClass.NestedClass nested = new TestClass.NestedClass();
   nested.field = "nested";
   testClass.nested = nested;

   String json = "{\"nested\": {\"field\": \"nested\"}}";
   TestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals("nested", deserialized.nested.field);
   assertSame(nested, deserialized.nested);
   assertTrue(deserialized.equals(testClass));
 }

 @Test
 public void testCreate_ObjectTypeWithPrimitiveFields() {
   class TestClass {
     private int intField;
     private long longField;
     private boolean booleanField;
     private float floatField;
     private double doubleField;

     @Override
     public String toString() {
       return "TestClass{" +
               "intField=" + intField +
               ", longField=" + longField +
               ", booleanField=" + booleanField +
               ", floatField=" + floatField +
               ", doubleField=" + doubleField +
               '}';
     }
   }

   ObjectConstructor<TestClass> constructor = new ObjectConstructor<TestClass>() {};
   ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructor, new FieldNamingStrategy() {}, new Excluder());
   TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

   TestClass testClass = new TestClass();
   testClass.intField = 42;
   testClass.longField = 1234567890L;
   testClass.booleanField = true;
   testClass.floatField = 3.14159265F;
   testClass.doubleField = 1.23456789E8D;

   String json = "{\"intField\": 42, \"longField\": 1234567890, \"booleanField\": true, \"floatField\": 3.14159265, \"doubleField\": 1.23456789E8}";
   TestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals(testClass, deserialized);
 }

 @Test
 public void testWriteRead_ObjectTypeWithCustomJsonAdapter() {
   class TestClass {
     @JsonAdapter(CustomJsonAdapter.class)
     private String field;
   }

   class CustomJsonAdapter extends TypeAdapter<String> {
     @Override public String read(JsonReader in) throws IOException {
       return in.nextString();
     }

     @Override public void write(JsonWriter out, String value) throws IOException {
       out.value("custom");
     }
   }

   ObjectConstructor<TestClass> constructor = new ObjectConstructor<TestClass>() {};
   ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructor, new FieldNamingStrategy() {}, new Excluder());
   TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

   TestClass testClass = new TestClass();
   testClass.field = "value";

   String json = "{\"field\": \"value\"}";
   TestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals("value", deserialized.field);

   json = "{\"field\": \"custom\"}";
   TestClass roundTripped = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals("custom", roundTripped.field);
 }

 @Test
 public void testWriteRead_ObjectTypeWithNestedFields() {
   class TestClass {
     private NestedClass nested;

     static class NestedClass {
       private String field;
     }
   }

   ObjectConstructor<TestClass> outerConstructor = new ObjectConstructor<TestClass>() {};
   ObjectConstructor<TestClass.NestedClass> innerConstructor = new ObjectConstructor<TestClass.NestedClass>() {};
   ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(outerConstructor, new FieldNamingStrategy() {}, new Excluder());
   TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

   TestClass testClass = new TestClass();
   TestClass.NestedClass nested = new TestClass.NestedClass();
   nested.field = "nested";
   testClass.nested = nested;

   String json = "{\"nested\": {\"field\": \"nested\"}}";
   TestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals("nested", deserialized.nested.field);
   assertSame(nested, deserialized.nested);

   json = "{\"nested\": {\"field\": \"roundTripNested\"}}";
   TestClass roundTripped = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals("roundTripNested", roundTripped.nested.field);
   assertSame(nested, roundTripped.nested);

   json = "{\"nested\": null}";
   deserialized = adapter.read(new JsonReader(new StringReader(json)));
   assertNull(deserialized.nested);
 }

 @Test
 public void testWriteRead_ObjectTypeWithPrimitiveFields() {
   class TestClass {
     private int intField;
     private long longField;
     private boolean booleanField;
     private float floatField;
     private double doubleField;
   }

   ObjectConstructor<TestClass> constructor = new ObjectConstructor<TestClass>() {};
   ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructor, new FieldNamingStrategy() {}, new Excluder());
   TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

   TestClass testClass = new TestClass();
   testClass.intField = 42;
   testClass.longField = 1234567890L;
   testClass.booleanField = true;
   testClass.floatField = 3.14159265F;
   testClass.doubleField = 1.23456789E8D;

   String json = "{\"intField\": 42, \"longField\": 1234567890, \"booleanField\": true, \"floatField\": 3.14159265, \"doubleField\": 1.23456789E8}";
   TestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals(testClass, deserialized);
 }

 @Test
 public void testWriteRead_ObjectTypeWithExcludedFields() {
   class TestClass {
     @Exclude
     private String excludedField;
     private String includedField;
   }

   ObjectConstructor<TestClass> constructor = new ObjectConstructor<TestClass>() {};
   ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructor, new FieldNamingStrategy() {}, new Excluder());
   TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

   TestClass testClass = new TestClass();
   testClass.includedField = "included";

   String json = "{\"included\": \"included\"}";
   TestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
   assertEquals("included", deserialized.includedField);
   assertNull(deserialized.excludedField);

   json = "{\"excluded\": \"excluded\"}";
   try {
     deserialized = adapter.read(new JsonReader(new StringReader(json)));
     fail("Should have thrown a JsonSyntaxException.");
   } catch (JsonSyntaxException e) {
     // Expected.
   }
 }
}