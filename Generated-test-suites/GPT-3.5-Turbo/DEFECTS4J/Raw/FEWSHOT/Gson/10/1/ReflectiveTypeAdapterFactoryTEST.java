
package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
    private ConstructorConstructor constructorConstructor;
    private FieldNamingStrategy fieldNamingPolicy;
    private Excluder excluder;
    
    @Before
    public void setup() {
        constructorConstructor = mock(ConstructorConstructor.class);
        fieldNamingPolicy = mock(FieldNamingStrategy.class);
        excluder = mock(Excluder.class);
        
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    }

    @Test
    public void test_excludeField() {
        Field field = null; // provide a field to test
        boolean serialize = false;
        
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, serialize);
        
        // assert result
    }

    @Test
    public void test_excludeField_withFieldNull() {
        Field field = null;
        boolean serialize = false;
        
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, serialize);
        
        // assert result
    }

    @Test
    public void test_create_withRawTypeNotAssignableToObject() {
        Gson gson = new GsonBuilder().create();
        TypeToken<Object> type = TypeToken.get(Object.class);
        
        TypeAdapter<Object> result = reflectiveTypeAdapterFactory.create(gson, type);
        
        assertEquals(null, result);
    }

    @Test
	public void test_create_withRawTypeAssignableToObject() {
		Gson gson = new GsonBuilder().create();
		TypeToken<Map<String, String>> type = new TypeToken<Map<String, String>>() {};
		
		TypeAdapter<Map<String, String>> result = reflectiveTypeAdapterFactory.create(gson, type);
		
		// assert result
    }
    
    @Test
	public void test_createBoundField() {
		Gson gson = new GsonBuilder().create();
		Field field = null; // provide a field to test
		String name = "field1";
		TypeToken<?> fieldType = TypeToken.get(String.class);
		boolean serialize = true;
		boolean deserialize = true;
		
		ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);
		
		// assert result
    } 

    @Test
	public void test_getBoundFields() {
		Gson gson = new GsonBuilder().create();
		TypeToken<?> type = new TypeToken<Object>() {};
		Class<?> raw = Object.class;
		
		Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, type, raw);
		
		// assert result
    }
    
    @Test
	public void test_Adapter_read() throws IOException {
		JsonReader in = mock(JsonReader.class);
		TypeToken<Object> type = new TypeToken<Object>() {};
		
		Object result = reflectiveTypeAdapterFactory.new Adapter<Object>(constructorConstructor, new LinkedHashMap<>()).read(in);
		
		// assert result
    }

    @Test
	public void test_Adapter_write() throws IOException {
		JsonWriter out = mock(JsonWriter.class);
		Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
		Object value = new Object();
		
		reflectiveTypeAdapterFactory.new Adapter<Object>(constructorConstructor, boundFields).write(out, value);
		
		// assert result
    }
}
