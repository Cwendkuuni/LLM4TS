package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;

public class ReflectiveTypeAdapterFactoryTest {

    private final Gson gson = new Gson();
    private final ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    private final FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategy() {
        @Override
        public String translateName(Field field) {
            return field.getName();
        }
    };
    private final Excluder excluder = new Excluder();

    private final ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);

    @Test
    public void testExcludeField() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        boolean serialize = true;
        boolean deserialize = true;

        boolean result = reflectiveTypeAdapterFactory.excludeField(field, serialize, deserialize);

        assertEquals(false, result);
    }

    @Test
    public void testExcludeField_nullField() {
        boolean serialize = true;
        boolean deserialize = true;

        boolean result = reflectiveTypeAdapterFactory.excludeField(null, serialize, deserialize);

        assertEquals(true, result);
    }

    @Test
    public void testCreate_primitiveType() {
        TypeToken<Integer> typeToken = TypeToken.get(Integer.TYPE);

        TypeAdapter<Integer> result = reflectiveTypeAdapterFactory.create(gson, typeToken);

        assertNull(result);
    }

    @Test
    public void testGetBoundFields_noFields() {
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};

        Class<?> raw = typeToken.getRawType();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);

        assertEquals(0, result.size());
    }

    @Test
    public void testAdapter_read_nullValue() throws IOException {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = (ReflectiveTypeAdapterFactory.Adapter<Object>) reflectiveTypeAdapterFactory.create(gson, typeToken);

        JsonReader in = new JsonReader(new StringReader("null"));
        Object result = adapter.read(in);

        assertNull(result);
    }

    @Test
    public void testAdapter_write_nullValue() throws IOException {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = (ReflectiveTypeAdapterFactory.Adapter<Object>) reflectiveTypeAdapterFactory.create(gson, typeToken);

        JsonWriter out = new JsonWriter(new StringWriter());
        adapter.write(out, null);
        out.flush();

        assertEquals("null", out.toString());
    }
}