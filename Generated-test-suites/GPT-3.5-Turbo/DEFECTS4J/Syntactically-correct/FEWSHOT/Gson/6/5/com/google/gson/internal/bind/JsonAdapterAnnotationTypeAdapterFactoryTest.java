package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@JsonAdapter(TestAdapter.class)
class TestClass {}

class TestAdapter extends TypeAdapter<TestClass> {
    @Override
    public void write(JsonWriter out, TestClass value) throws IOException {
    }

    @Override
    public TestClass read(JsonReader in) throws IOException {
        return null;
    }
}

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    @Mock
    private Gson gson;
    
    @Mock
    private TypeToken<TestClass> typeToken;
    
    @Mock
    private JsonAdapter annotation;
    
    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreate() {
        when(typeToken.getRawType()).thenReturn(TestClass.class);
        when(annotation.value()).thenReturn(TestAdapter.class);
        
        TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);
        
        assertTrue(adapter instanceof TestAdapter);
    }
    
    @Test
    public void testCreate_withNullAnnotation() {
        when(typeToken.getRawType()).thenReturn(TestClass.class);
        when(annotation.value()).thenReturn(null);
        
        TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);
        
        assertNull(adapter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_withInvalidValue() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(annotation.value()).thenReturn(String.class);
        
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }
    
    @Test
    public void testGetTypeAdapter_withTypeAdapterValue() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(annotation.value()).thenReturn(TestAdapter.class);
        when(constructorConstructor.get(eq(TypeToken.get(TestAdapter.class)))).thenReturn(mock(ObjectConstructor.class));
        
        TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertTrue(adapter instanceof TestAdapter);
    }
    
    @Test
    public void testGetTypeAdapter_withTypeAdapterFactoryValue() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(annotation.value()).thenReturn(TestAdapterFactory.class);
        when(constructorConstructor.get(eq(TypeToken.get(TestAdapterFactory.class)))).thenReturn(mock(ObjectConstructor.class));
        TestAdapterFactory testAdapterFactory = mock(TestAdapterFactory.class);
        when(testAdapterFactory.create(eq(gson), eq(fieldType))).thenReturn(mock(TypeAdapter.class));
        when(constructorConstructor.get(eq(TypeToken.get(TestAdapterFactory.class)))).thenReturn(new TestAdapterFactory());
        
        TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertTrue(adapter instanceof TypeAdapter);
    }
    
    @Test
    public void testGetTypeAdapter_withTypeAdapterFactoryValue_nullSafe() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(annotation.value()).thenReturn(TestAdapterFactory.class);
        when(constructorConstructor.get(eq(TypeToken.get(TestAdapterFactory.class)))).thenReturn(mock(ObjectConstructor.class));
        TestAdapterFactory testAdapterFactory = mock(TestAdapterFactory.class);
        TypeAdapter typeAdapter = mock(TypeAdapter.class);
        when(typeAdapter.nullSafe()).thenReturn(typeAdapter);
        when(testAdapterFactory.create(eq(gson), eq(fieldType))).thenReturn(typeAdapter);
        when(constructorConstructor.get(eq(TypeToken.get(TestAdapterFactory.class)))).thenReturn(new TestAdapterFactory());
        
        TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertEquals(adapter, typeAdapter);
        verify(typeAdapter, times(1)).nullSafe();
    }
}

class TestAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
        return null;
    }
}