package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Excluder;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testCreateInstance() {
        Gson gson = new Gson();
        ConstructorConstructor constructorConstructor = gson.getFactory().constructorConstructor;
        FieldNamingStrategy fieldNamingPolicy = gson.getFieldNamingPolicy();
        Excluder excluder = gson.getExcluder();

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
        TypeAdapter<Object> adapter = factory.create(gson, new TypeToken<Object>() {});
        assertNotNull(adapter);
    }

    @Test
    public void testExcludeField() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = new Object() {
            private static final long serialVersionUID = 1L;
            @SerializedName("excludeField")
            private String field;
        }.getClass().getDeclaredField("field");

        assertFalse(factory.excludeField(field, true));
        assertTrue(factory.excludeField(field, false));

        field.setAccessible(true);
        field.set(new Object(), "test");

        assertTrue(factory.excludeField(field, true));
        assertFalse(factory.excludeField(field, false));
    }

    @Test
    public void testGetFieldNames() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = new Object() {
            private static final long serialVersionUID = 1L;
            @SerializedName("name")
            private String name;

            @SerializedName("alternateName")
            private String alternateName;
        }.getClass().getDeclaredField("name");

        List<String> fieldNames = factory.getFieldNames(field);
        assertEquals(1, fieldNames.size());
        assertEquals("name", fieldNames.get(0));

        field = new Object() {
            private static final long serialVersionUID = 1L;
            @SerializedName("name")
            private String name;

            @SerializedName({"alternateName1", "alternateName2"})
            private String alternateName;
        }.getClass().getDeclaredField("name");

        fieldNames = factory.getFieldNames(field);
        assertEquals(2, fieldNames.size());
        assertEquals("name", fieldNames.get(0));
        assertEquals("alternateName1", fieldNames.get(1));
        assertEquals("alternateName2", fieldNames.get(2));
    }

    @Test
    public void testCreateTypeAdapter() throws Exception {
        Gson gson = new Gson();
        ConstructorConstructor constructorConstructor = gson.getFactory().constructorConstructor;
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, gson.getFieldNamingPolicy(), gson.getExcluder());

        Class<?super Object> rawType = Object.class;
        ObjectConstructor<Object> constructor = constructorConstructor.get(new TypeToken<Object>() {}.getType());
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory
            .getBoundFields(gson, new TypeToken<Object>() {}.getType(), rawType.getClass());

        JsonAdapter<Object> jsonAdapter = getTypeAdapter(constructorConstructor, gson, rawType, null);
        TypeAdapter<Object> adapter = factory.create(gson, new TypeToken<Object>() {});
        TypeAdapter<Object> expectedAdapter = new TypeAdapter<Object>() {
            @Override public Object read(JsonReader in) throws IOException {
                return adapter.read(in);
            }

            @Override public void write(JsonWriter out, Object value) throws IOException {
                adapter.write(out, value);
            }
        }.nullSafe();

        assertEquals(expectedAdapter, adapter);

        JsonObject json = new JsonObject();
        json.add("name", new JsonPrimitive("test"));
        JsonElement jsonElement = json.get("name");

        Object value = adapter.read(new JsonReader(new StringReader(json.toString()))).getValue();
        assertNotNull(value);
        assertEquals("test", value);

        jsonElement = new JsonPrimitive("test").getName();
        JsonObject jsonToWrite = new JsonObject();
        jsonToWrite.add(jsonElement, new JsonPrimitive("test"));

        StringWriter stringWriter = new StringWriter(new StringBuilder());
        JsonWriter jsonWriter = new JsonWriter(new StringWriter(stringWriter));
        adapter.write(jsonWriter, value);
        jsonWriter.flush();

        assertEquals(json.toString(), stringWriter.toString());
    }

    @Test
    public void testBoundFields() throws Exception {
        Gson gson = new Gson();
        ConstructorConstructor constructorConstructor = gson.getFactory().constructorConstructor;
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, gson.getFieldNamingPolicy(), gson.getExcluder());

        Class<?super Object> rawType = Object.class;
        ObjectConstructor<Object> constructor = constructorConstructor.get(new TypeToken<Object>() {}.getType());
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory
            .getBoundFields(gson, new TypeToken<Object>() {}.getType(), rawType.getClass());

        JsonAdapter<Object> jsonAdapter = getTypeAdapter(constructorConstructor, gson, rawType, null);
        TypeAdapter<Object> adapter = factory.create(gson, new TypeToken<Object>() {});

        assertNotNull(boundFields);
        assertEquals(15, boundFields.size());

        ReflectiveTypeAdapterFactory.BoundField boundField1 = boundFields.get("class");
        assertEquals("class", boundField1.name);
        assertFalse(boundField1.deserialized);
        assertFalse(boundField1.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField2 = boundFields.get("hashCode");
        assertEquals("hashCode", boundField2.name);
        assertTrue(boundField2.deserialized);
        assertTrue(boundField2.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField3 = boundFields.get("toString");
        assertEquals("toString", boundField3.name);
        assertTrue(boundField3.deserialized);
        assertTrue(boundField3.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField4 = boundFields.get("equals");
        assertEquals("equals", boundField4.name);
        assertTrue(boundField4.deserialized);
        assertTrue(boundField4.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField5 = boundFields.get("clone");
        assertEquals("clone", boundField5.name);
        assertFalse(boundField5.deserialized);
        assertFalse(boundField5.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField6 = boundFields.get("finalize");
        assertEquals("finalize", boundField6.name);
        assertFalse(boundField6.deserialized);
        assertFalse(boundField6.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField7 = boundFields.get("serialVersionUID");
        assertEquals("serialVersionUID", boundField7.name);
        assertTrue(boundField7.deserialized);
        assertTrue(boundField7.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField8 = boundFields.get("notify");
        assertEquals("notify", boundField8.name);
        assertFalse(boundField8.deserialized);
        assertFalse(boundField8.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField9 = boundFields.get("notifyAll");
        assertEquals("notifyAll", boundField9.name);
        assertFalse(boundField9.deserialized);
        assertFalse(boundField9.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField10 = boundFields.get("wait");
        assertEquals("wait", boundField10.name);
        assertFalse(boundField10.deserialized);
        assertFalse(boundField10.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField11 = boundFields.get("wait0");
        assertEquals("wait0", boundField11.name);
        assertFalse(boundField11.deserialized);
        assertFalse(boundField11.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField12 = boundFields.get("wait1");
        assertEquals("wait1", boundField12.name);
        assertFalse(boundField12.deserialized);
        assertFalse(boundField12.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField13 = boundFields.get("wait2");
        assertEquals("wait2", boundField13.name);
        assertFalse(boundField13.deserialized);
        assertFalse(boundField13.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField14 = boundFields.get("wait3");
        assertEquals("wait3", boundField14.name);
        assertFalse(boundField14.deserialized);
        assertFalse(boundField14.serialized);

        ReflectiveTypeAdapterFactory.BoundField boundField15 = boundFields.get("wait4");
        assertEquals("wait4", boundField15.name);
        assertFalse(boundField15.deserialized);
        assertFalse(boundField15.serialized);
    }
}