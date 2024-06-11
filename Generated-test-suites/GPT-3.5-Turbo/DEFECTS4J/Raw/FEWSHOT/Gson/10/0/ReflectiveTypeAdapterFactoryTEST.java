package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ReflectiveTypeAdapterFactoryTest {

    @Mock
    private Gson gson;

    @Mock
    private TypeToken<?> typeToken;

    @Mock
    private Class<?> rawClass;

    @Mock
    private ObjectConstructor<Object> constructor;

    @Mock
    private JsonReader jsonReader;

    @Mock
    private JsonWriter jsonWriter;

    @Mock
    private FieldNamingStrategy fieldNamingStrategy;

    @Mock
    private Excluder excluder;

    @InjectMocks
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    private Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        boundFields = new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>();
        boundFields.put("field1", new ReflectiveTypeAdapterFactory.BoundField("field1", true, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return false;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
            }
        });

        when(gson.getAdapter(typeToken)).thenReturn(null);
        when(fieldNamingStrategy.translateName(any(Field.class))).thenReturn("field1");
        when(typeToken.getRawType()).thenReturn(rawClass);
        when(constructor.construct()).thenReturn(new Object());
        when(rawClass.isInterface()).thenReturn(false);
        when(rawClass.getDeclaredFields()).thenReturn(new Field[]{});
        when(excluder.excludeField(any(Field.class), anyBoolean())).thenReturn(false);
        when(excluder.excludeClass(any(Class.class), anyBoolean())).thenReturn(false);
    }

    @Test
    public void testCreate_PrimitiveRawClass_ReturnsNull() {
        when(rawClass.isAssignableFrom(Object.class)).thenReturn(false);

        TypeAdapter<?> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

        assertNull(typeAdapter);
    }

    @Test
    public void testCreate_NonPrimitiveRawClass_ReturnsAdapter() {
        when(rawClass.isAssignableFrom(Object.class)).thenReturn(true);
        when(constructorConstructor.get(typeToken)).thenReturn(constructor);
        when(constructor.construct()).thenReturn(new Object());
        when(rawClass.getDeclaredFields()).thenReturn(new Field[]{});
        when(gson.getAdapter(any(TypeToken.class))).thenReturn(mock(TypeAdapter.class));
        when($Gson$Types.resolve(any(Type.class), any(Class.class), any(Type.class))).thenReturn(TypeToken.get(Object.class).getType());

        TypeAdapter<?> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

        assertEquals(ReflectiveTypeAdapterFactory.Adapter.class, typeAdapter.getClass());
    }

    @Test
    public void testCreate_NullRawClass_ReturnsNull() {
        when(rawClass.isAssignableFrom(Object.class)).thenReturn(true);
        when(typeToken.getRawType()).thenReturn(null);

        TypeAdapter<?> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

        assertNull(typeAdapter);
    }

    @Test
    public void testRead_NullValue_ReturnsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        Object result = reflectiveTypeAdapterFactory.create(gson, typeToken).read(jsonReader);

        assertNull(result);
    }

    @Test
    public void testRead_NonNullValue_ReturnsObject() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
        when(jsonReader.nextName()).thenReturn("field1");
        when(boundFields.get("field1")).thenReturn(mock(ReflectiveTypeAdapterFactory.BoundField.class));

        Object result = reflectiveTypeAdapterFactory.create(gson, typeToken).read(jsonReader);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testWrite_NullValue_WritesNull() throws IOException {
        reflectiveTypeAdapterFactory.create(gson, typeToken).write(jsonWriter, null);

        verify(jsonWriter, times(1)).nullValue();
    }

    @Test
    public void testWrite_NonNullValue_WritesObject() throws IOException {
        reflectiveTypeAdapterFactory.create(gson, typeToken).write(jsonWriter, new Object());

        verify(jsonWriter, times(1)).beginObject();
        verify(jsonWriter, times(1)).endObject();
    }
}